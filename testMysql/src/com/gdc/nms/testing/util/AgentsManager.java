package com.gdc.nms.testing.util;

import jade.core.Agent;
import jade.core.Profile;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.ControllerException;
import jade.wrapper.StaleProxyException;

public class AgentsManager {

    private static final AgentsManager INSTANCE = new AgentsManager();

    private Runtime rtInstance;
    private AgentContainer container;

    private AgentsManager() {
        rtInstance = Runtime.instance();
    }

    public static AgentsManager getInstance() {
        return INSTANCE;
    }

    public void start(Profile p) throws Exception {
        container = rtInstance.createMainContainer(p);

        if (container == null) {
            throw new Exception("JADE is already running.");
        }

    }

    public void addAgent(String name, Agent agent) throws StaleProxyException {
        container.acceptNewAgent(name, agent).start();
    }

    public void killAgent(String name) throws ControllerException {
        if (name == null || name.isEmpty() || name.equalsIgnoreCase("ams") || name.equalsIgnoreCase("df")) {
            return;
        }
        AgentController agent = container.getAgent(name);
        agent.kill();
    }

    public void stop() {
        try {
            container.getPlatformController().kill();
        } catch (ControllerException e) {
            e.printStackTrace();
        }
        rtInstance.shutDown();
    }

}
