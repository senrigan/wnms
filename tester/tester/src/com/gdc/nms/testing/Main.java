package com.gdc.nms.testing;

import jade.core.AID;

import com.gdc.nms.model.Device;
import com.gdc.nms.model.DeviceResource;
import com.gdc.nms.model.Interface;
import com.gdc.nms.model.SnmpVersion;
import com.gdc.nms.testing.exception.IllegalLocalAddress;
import com.gdc.nms.testing.exception.TimeoutException;
import com.gdc.nms.testing.response.DeviceInterfacesResponse;
import com.gdc.nms.testing.response.DeviceResourcesResponse;

public class Main {
    public static void main(String[] args) {
        if (args.length == 0) {
            return;
        } else if ("--client".equals(args[0])) {
            try {
                Client client = new Client();
                client.start();

                Device d = new Device("172.28.28.76");
                d.setManaged(true);
                d.setReachable(true);
                d.setSnmpable(true);
                d.setSnmpUnreachable(false);
                d.setSysObjectID("1.3.6.1.4.1.9.1.619");
                d.setSnmpVersion(SnmpVersion.V2);
                d.setSnmpPublicCommunity("a1k0st008RW");

//                AID receiver = client.getServerByHostname("arcom.nms.com.mx");
                AID receiver = client.getServerByHostname("190.144.233.49");


                try {
                    DeviceResourcesResponse resources = client.getDeviceResources(d, receiver, 50000);
                    if (resources == null) {
                        System.out.println("Some error ocurrs.");
                    } else {
                        for (DeviceResource r : resources.getDeviceResources()) {
                            System.out.println(r.getName());
                        }
                    }

                    System.out.println("Ifaces: ");
                    DeviceInterfacesResponse interfaces = client.getInterfaces(d, receiver, 50000);

                    if (interfaces == null) {
                        System.out.println("Some error ocurrs.");
                    } else {
                        for (Interface i : interfaces.getInterfaces()) {
                            System.out.println(i.getIfIndex());
                        }
                    }

                } catch (TimeoutException e) {
                    System.out.println("TimeOut Elapsed");
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } catch (IllegalLocalAddress e) {
                e.printStackTrace();
            }
        } else if ("--server".equals(args[0])) {
            Server server = new Server(args[1], new String[] { args[2] }, args[3], Integer.parseInt(args[4]));
            server.start();
        }
    }
}
