package com.gdc.nms.testing;

import jade.core.ProfileImpl;
import jade.util.leap.Properties;

import com.gdc.nms.testing.agent.ServerAgent;
import com.gdc.nms.testing.interfaces.Executable;
import com.gdc.nms.testing.util.AgentUtil;
import com.gdc.nms.testing.util.AgentsManager;

public class Server implements Executable {

    private AgentsManager manager;
    private ServerAgent agent;
    private Properties propsJade;
    private String hostaddress;
    private boolean running;

    /**
     * Create a Server instance
     * 
     * @param hostaddress
     *            SSH Address used
     * @param mtps
     *            All the addresses for use in JADE (MTPS)
     * @param clientAddress
     *            IP address or hostname of client
     * @param serverID
     *            Number of server
     */
    public Server(String hostaddress, String[] mtps, String clientAddress, int serverID) {
        if (hostaddress == null || mtps == null || clientAddress == null) {
            throw new NullPointerException();
        }
        agent = new ServerAgent();
        this.hostaddress = hostaddress;
        running = false;
        manager = AgentsManager.getInstance();
        propsJade = new Properties();
        propsJade.put(ProfileImpl.MAIN, "true");
        // propsJade.put(ProfileImpl.GUI, "true");
        propsJade.put(ProfileImpl.PLATFORM_ID, AgentUtil.getServerPlatform(serverID));
        propsJade.put(ProfileImpl.MTPS, AgentUtil.getMTP(mtps));
        agent.setClientAddress(clientAddress);
        agent.setSSHHost(hostaddress);
    }

    @Override
    public void start() {
        if (!isRunning()) {
            try {
                manager.start(new ProfileImpl(propsJade));
                manager.addAgent(AgentUtil.getServerAgentName(hostaddress), agent);
                running = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void stop() {
        if (isRunning()) {
            manager.stop();
            running = false;
        }
    }

    @Override
    public boolean isRunning() {
        return running;
    }

}
