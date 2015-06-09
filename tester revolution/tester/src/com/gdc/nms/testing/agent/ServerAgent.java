package com.gdc.nms.testing.agent;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import jade.util.leap.Iterator;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.gdc.nms.model.Device;
import com.gdc.nms.testing.response.ErrorResponse;
import com.gdc.nms.testing.server.task.GetDeviceInterfacesTask;
import com.gdc.nms.testing.server.task.GetDeviceResourcesTask;
import com.gdc.nms.testing.server.task.GetDriverClassTask;
import com.gdc.nms.testing.server.task.Task;
import com.gdc.nms.testing.util.AgentUtil;

public class ServerAgent extends Agent {

    private String sshHost;
    private AID client;
    private ExecutorService workPool;

    public ServerAgent() {
        client = null;
        workPool = Executors.newSingleThreadExecutor();
    }

    public void setClientAddress(String address) {
        if (client == null) {
            client = new AID(AgentUtil.getClientAgentName(), AID.ISGUID);
            client.addAddresses(AgentUtil.createMTPAddress(address));
        }
    }

    public void setSSHHost(String sshHost) {
        this.sshHost = sshHost;
    }

    @Override
    protected void setup() {
        // Sent alive message
        addBehaviour(new OneShotBehaviour(this) {
            @Override
            public void action() {
                ACLMessage message = getACLMessage(ACLMessage.INFORM);
                message.setContent(sshHost);
                send(message);
            }
        });
        // Receive all work
        addBehaviour(new CyclicBehaviour(this) {
            @Override
            public void action() {
                try {
                    MessageTemplate template = MessageTemplate.MatchPerformative(AgentUtil.TASK_PERFORMATIVE);

                    ACLMessage message = receive();
                    if (message == null) {
                        System.out.println("Waiting for message");
                        block();
                    } else {
                        System.out.println("Message received");
                        ACLMessage reply = message.createReply();
                        AID sender = myAgent.getAID();
                        for (String address : myAgent.getAMS().getAddressesArray()) {
                            sender.addAddresses(address);
                        }

                        String task = message.getUserDefinedParameter(AgentUtil.USER_PARAMETER_TASK);

                        if (AgentUtil.GET_DEVICE_DRIVER_TASK.equals(task)) {
                            Device device;
                            try {
                                device = (Device) message.getContentObject();
                                addTask(new GetDriverClassTask(device), reply);
                            } catch (UnreadableException e) {
                                e.printStackTrace();
                            }
                        } else if (AgentUtil.GET_DEVICE_RESOURCES_TASK.equals(task)) {
                            Device device;
                            try {
                                device = (Device) message.getContentObject();
                                addTask(new GetDeviceResourcesTask(device), reply);
                            } catch (UnreadableException e) {
                                e.printStackTrace();
                            }
                        } else if (AgentUtil.GET_DEVICE_INTERFACES.equals(task)) {
                            Device device;
                            try {
                                device = (Device) message.getContentObject();
                                addTask(new GetDeviceInterfacesTask(device), reply);
                            } catch (UnreadableException e) {
                                e.printStackTrace();
                            }
                        } else {
                            try {
                                reply.setContentObject(new ErrorResponse(task + " is not valid."));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            send(reply);
                        }

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void takeDown() {
        workPool.shutdown();
    }

    public void addTask(Task task, ACLMessage reply) {
        if (task == null) {
            return;
        }
        task.setAgent(this);
        task.setACLMessage(reply);
        workPool.execute(task);
    }

    private ACLMessage getACLMessage(int perf) {
        ACLMessage message = new ACLMessage(perf);
        AID server = getAID();
        Iterator addresses = getAMS().getAllAddresses();
        while (addresses.hasNext()) {
            String address = (String) addresses.next();
            server.addAddresses(address);
        }

        message.addReceiver(client);
        message.setSender(server);

        return message;
    }
}
