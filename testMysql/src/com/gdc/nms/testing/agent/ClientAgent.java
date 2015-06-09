package com.gdc.nms.testing.agent;

import jade.core.AID;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import com.gdc.nms.model.Device;
import com.gdc.nms.testing.behaviour.MessageReceiverBehaviour;
import com.gdc.nms.testing.exception.TimeoutException;
import com.gdc.nms.testing.response.DeviceDriverResponse;
import com.gdc.nms.testing.response.DeviceInterfacesResponse;
import com.gdc.nms.testing.response.DeviceResourcesResponse;
import com.gdc.nms.testing.response.ErrorResponse;
import com.gdc.nms.testing.util.AgentUtil;

public class ClientAgent extends Agent {

    private Map<String, AID> availableServers;
    private CountDownLatch latch;

    public ClientAgent(CountDownLatch latch) {
        availableServers = new HashMap<String, AID>();
        this.latch = latch;
    }

    @Override
    protected void setup() {

    }

    public void discovery(final int messageLimit, final long duration, final TimeUnit unit) {
        if (availableServers.size() == 0) {
            addBehaviour(new MessageReceiverBehaviour(this, MessageTemplate.MatchPerformative(ACLMessage.INFORM),
                messageLimit, duration, unit) {
                @Override
                public void handleMessage(ACLMessage message) {
                    AID sender = message.getSender();
                    String host = message.getContent();
                    System.out.println("host"+host+"sender"+sender);
                    availableServers.put(host, sender);
                }

                @Override
                public int onEnd() {
                    latch.countDown();
                    return 0;
                }
            });
        }
    }

    private Serializable sendTask(Device device, AID receiver, long timeout, String taskname) {
        if (device == null || receiver == null) {
            throw new NullPointerException();
        }

        String conversationId = Long.toString(System.currentTimeMillis());

        ACLMessage message = new ACLMessage(AgentUtil.TASK_PERFORMATIVE);
        message.addUserDefinedParameter(AgentUtil.USER_PARAMETER_TASK, taskname);

        message.setConversationId(conversationId);

        message.addReceiver(receiver);

        AID sender = getAID();
        for (String url : getAMS().getAddressesArray()) {
            sender.addAddresses(url);
        }

        try {
            message.setContentObject(device);
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        send(message);

        MessageTemplate mt = MessageTemplate.and(MessageTemplate.MatchSender(receiver),
            MessageTemplate.MatchConversationId(conversationId));

        ACLMessage received = blockingReceive(mt, timeout);
        if (received == null) {
            throw new TimeoutException();
        } else {
            try {
                Serializable object = received.getContentObject();
                return object;
            } catch (UnreadableException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    public DeviceDriverResponse getDeviceDriver(Device device, AID receiver, long timeout) throws Exception {
        Serializable response = sendTask(device, receiver, timeout, AgentUtil.GET_DEVICE_DRIVER_TASK);

        if (response instanceof Exception) {
            throw (Exception) response;
        } else if (response instanceof DeviceDriverResponse) {
            return (DeviceDriverResponse) response;
        } else if (response instanceof ErrorResponse) {
            throw new Exception(((ErrorResponse) response).getMessage());
        }

        return null;
    }

    public DeviceResourcesResponse getDeviceResources(Device device, AID receiver, long timeout) throws Exception {
        Serializable response = sendTask(device, receiver, timeout, AgentUtil.GET_DEVICE_RESOURCES_TASK);

        if (response instanceof Exception) {
            throw (Exception) response;
        } else if (response instanceof DeviceResourcesResponse) {
            return (DeviceResourcesResponse) response;
        } else if (response instanceof ErrorResponse) {
            throw new Exception(((ErrorResponse) response).getMessage());
        }

        return null;
    }

    public DeviceInterfacesResponse getInterfaces(Device device, AID receiver, long timeout) throws Exception {

        Serializable response = sendTask(device, receiver, timeout, AgentUtil.GET_DEVICE_INTERFACES);

        if (response instanceof Exception) {
            throw (Exception) response;
        } else if (response instanceof DeviceInterfacesResponse) {
            return (DeviceInterfacesResponse) response;
        } else if (response instanceof ErrorResponse) {
            throw new Exception(((ErrorResponse) response).getMessage());
        }

        return null;
    }

    public AID getServerByHostname(String hostname) {
    	System.out.println("servidores dispobnibles"+availableServers.keySet());
        return availableServers.get(hostname);
    }

    @Override
    protected void takeDown() {
        availableServers.clear();
    }
}
