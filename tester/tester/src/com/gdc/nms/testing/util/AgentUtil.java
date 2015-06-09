package com.gdc.nms.testing.util;

public class AgentUtil {

    public static final int TASK_PERFORMATIVE = 28;
    public static final String USER_PARAMETER_TASK = "TASK";

    // Tasks
    public static final String GET_DEVICE_DRIVER_TASK = "GetDeviceDriver";
    public static final String GET_DEVICE_RESOURCES_TASK = "GetDeviceResourcesTask";
    public static final String GET_DEVICE_INTERFACES = "GetDeviceInterfaces";

    private static final String MTP_ADDRESS = "http://%s:7778/acc";
    private static final String MTP_FORMAT = "jade.mtp.http.MessageTransportProtocol(%s)";

    // Client Utils
    public static final String CLIENTAGENT_NAME = "Client";
    public static final String CLIENT_PLATFORM_ID = "ClientPlatform";

    // Server(s) Utils
    // (%s) = Server name
    public static final String SERVERAGENT_NAME = "Server-%s";
    // (%s) = Identifier;
    public static final String SERVER_PLATFORM_ID = "Server%sPlatform";

    public static String getServerAgentName(String address) {
        return String.format(SERVERAGENT_NAME, address);
    }

    public static String createMTPAddress(String address) {
        return String.format(MTP_ADDRESS, address);
    }

    public static String getClientAgentName() {
        return CLIENTAGENT_NAME + "@" + CLIENT_PLATFORM_ID;
    }

    public static String getServerPlatform(int serverID) {
        return String.format(SERVER_PLATFORM_ID, Integer.toString(serverID));
    }

    public static String getMTP(String address) {
        return getMTP(new String[] { address });
    }

    public static String getMTP(String[] addresses) {
        if (addresses == null) {
            throw new NullPointerException();
        }
        StringBuffer buf = new StringBuffer();
        for (String address : addresses) {
            if (address != null && !address.isEmpty()) {
                buf.append(String.format(MTP_FORMAT, createMTPAddress(address)));
                buf.append(';');
            }
        }
        buf.setLength(buf.length() - 1);
        return buf.toString();
    }

    /**
     * Avoid objects creation
     */
    private AgentUtil() {
    }
}
