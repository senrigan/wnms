package com.gdc.nms.testing;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import jade.core.AID;

import com.gdc.nms.model.Device;
import com.gdc.nms.model.DeviceResource;
import com.gdc.nms.model.Interface;
import com.gdc.nms.model.SnmpVersion;
import com.gdc.nms.test.util.connection.ConectorServer;
import com.gdc.nms.test.util.connection.MysqlData;
import com.gdc.nms.testing.exception.IllegalLocalAddress;
import com.gdc.nms.testing.exception.TimeoutException;
import com.gdc.nms.testing.response.DeviceDriverResponse;
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

//                Device d = new Device("172.28.28.76");
//                d.setManaged(true);
//                d.setReachable(true);
//                d.setSnmpable(true);
//                d.setSnmpUnreachable(false);
//                d.setSysObjectID("1.3.6.1.4.1.9.1.619");
//                d.setSnmpVersion(SnmpVersion.V2);
//                d.setSnmpPublicCommunity("a1k0st008RW");
//
                ConectorServer cont=new ConectorServer();
        		List<connection.credential.Server> server=cont.getServerList();
        		MysqlData mysql=new MysqlData();
        		if(mysql.isServerFilesDevicesCreated(server.get(1))){
        			
        		}else{
        			mysql.createFilesAndFolders(server.get(1));
        		}
        		
//        		HashMap<String, ArrayList<Device>> mapDevice=mysql.getFolderDevice();
//        		ArrayList<Device> devices=mapDevice.get("1.3.6.1.4.1.9");
//                AID receiver = client.getServerByHostname(server.get(1).getSshCredential().getHostname());
//                Device d=devices.get(3);
        		ArrayList<Device> devices=mysql.getDevicesofDriver(server.get(1), "Cisco");
        		Device d=devices.get(3);
        		AID receiver = client.getServerByHostname(server.get(1).getSshCredential().getHostname());
                System.out.println("consultando"+d.getSysObjectID());
                System.out.println("device"+d);
                System.out.println("reciver"+receiver);
                try {
					DeviceDriverResponse driver=client.getDeviceDriver(d, receiver, 50000);
					System.out.println("driver"+driver.getClassName()+"driver database"+com.gdc.nms.server.drivers.snmp.DriverManager.getInstance().getDriver(d).getClass());
				} catch (Exception e1) {
					System.out.println("no se pudo obtehner el driver");
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
//                try {
                    DeviceResourcesResponse resources;
					try {
						resources = client.getDeviceResources(d, receiver, 50000);
						 if (resources == null) {
		                        System.out.println("Some error ocurrs. resource");
		                    } else {
		                    	System.out.println("Resource");
		                        for (DeviceResource r : resources.getDeviceResources()) {
		                            System.out.println(r.getName());
		                        }
		                    }
					} catch (Exception e) {
						// TODO Auto-generated catch block
						System.out.println("ocurrio error en resorce");
						e.printStackTrace();
					}
					
					
					
                   

                    System.out.println("Ifaces: ");
                    DeviceInterfacesResponse interfaces;
					try {
						interfaces = client.getInterfaces(d, receiver, 50000);
						  if (interfaces == null) {
		                        System.out.println("Some error ocurrs. interface");
		                    } else {
		                        for (Interface i : interfaces.getInterfaces()) {
		                            System.out.println(i.getIfIndex());
		                        }
		                    }

					} catch (Exception e) {
						// TODO Auto-generated catch block
						System.out.println("ocurrio un error en interfaces");
						e.printStackTrace();
					}

                  
//                } catch (TimeoutException e) {
//                    System.out.println("TimeOut Elapsed");
//                } catch (Exception e) {
//                	System.out.println("ocurrion un error");
//                    e.printStackTrace();
//                }

            } catch (IllegalLocalAddress e) {
                e.printStackTrace();
            }
        } else if ("--server".equals(args[0])) {
            Server server = new Server(args[1], new String[] { args[2] }, args[3], Integer.parseInt(args[4]));
            server.start();
        }
    }
}
