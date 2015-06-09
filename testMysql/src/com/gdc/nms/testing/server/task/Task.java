package com.gdc.nms.testing.server.task;

import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

import java.io.IOException;
import java.io.Serializable;

import com.gdc.nms.testing.agent.ServerAgent;

public abstract class Task implements Runnable {

    protected ServerAgent agent;
    private ACLMessage reply;

    public final ServerAgent getAgent() {
        return agent;
    }

    public final void setAgent(ServerAgent agent) {
        this.agent = agent;
    }

    public final void setACLMessage(ACLMessage message) {
        this.reply = message;
    }

    public final void sendMessage(Serializable content) {
        if (content == null) {
            throw new NullPointerException();
        }
        if (agent == null || reply == null) {
            return;
        }

        try {
            reply.setContentObject(content);
        } catch (IOException e) {
        }

        agent.addBehaviour(new OneShotBehaviour(agent) {
            @Override
            public void action() {
                agent.send(reply);
            }
        });

    }

}
