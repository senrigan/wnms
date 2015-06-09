package com.gdc.nms.testing.behaviour;

import jade.core.Agent;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.util.concurrent.TimeUnit;

public abstract class MessageReceiverBehaviour extends SimpleBehaviour {

    private MessageTemplate messageTemplate;
    private int messageLimit;
    private long timeOut;
    private long start;

    public MessageReceiverBehaviour(Agent a, MessageTemplate messageTemplate, long duration, TimeUnit unit) {
        this(a, messageTemplate, 0, duration, unit);
    }

    public MessageReceiverBehaviour(Agent a, MessageTemplate messageTemplate, int messageLimit) {
        this(a, messageTemplate, messageLimit, 0, TimeUnit.MILLISECONDS);
    }

    public MessageReceiverBehaviour(Agent a, MessageTemplate messageTemplate, int messageLimit, long duration,
            TimeUnit unit) {
        super(a);
        if (messageTemplate == null) {
            throw new NullPointerException();
        }
        this.messageTemplate = messageTemplate;
        this.messageLimit = (messageLimit <= 0) ? 0 : messageLimit + 1;
        this.timeOut = unit.toMillis(duration);
    }

    private boolean messagesLimitExceeded() {
        if (messageLimit <= 0) {
            return false;
        }

        return messageLimit == 1;
    }

    private boolean timeoutLimitExceeded() {
        if (timeOut <= 0) {
            return false;
        }

        return start < System.currentTimeMillis();
    }

    @Override
    public void onStart() {
        start = System.currentTimeMillis() + timeOut;
    }

    @Override
    public final void action() {
        ACLMessage receive = myAgent.receive(messageTemplate);
        if (receive == null) {
            block(1000);
        } else {
            messageLimit--;
            handleMessage(receive);
        }
    }

    @Override
    public final boolean done() {
        return messagesLimitExceeded() || timeoutLimitExceeded();
    }

    public abstract void handleMessage(ACLMessage message);

}
