package com.gdc.nms.server.testutil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import com.gdc.nms.model.Device;


import com.gdc.nms.test.util.connection.ConectorServer;
import com.gdc.nms.test.util.connection.MysqlData;

import connection.credential.Server;

public class LoaderDriver {
	
	private HashMap<String, ArrayList<SillyDevice>> devicesToTest;
	private List<Server> servers;
	private String ipDeviceFileName="mainIp.txt";
	
	public LoaderDriver(){
		loadServerProperties();

	}

	/**
	 * this method load all devices of drivernames in the test configuration file with n number of devices per driver especefic in the 
	 * field of maximumPerDevice If this number is -1 try all numbers n that is in the database
	 */
	public  HashMap<String , ArrayList<SillyDevice>> getDriversWithNames(List<String> driverNames,Long maximumPerDevice){
		devicesToTest=new  HashMap<String , ArrayList<SillyDevice>>();
		for(Server server :servers){
			ArrayList<Device>  devices=new ArrayList<Device>(); 
			MysqlData mysql=new MysqlData();
			for(String driver:driverNames){
				System.out.println("consultando driver"+driver);
				devices=mysql.getDevicesofDriver(server, driver);
				System.out.println("deviecs"+devices);
				if(maximumPerDevice>0){
					ArrayList<SillyDevice> dev=new ArrayList<SillyDevice>();
					for(int i=0;i<maximumPerDevice;i++){
						System.out.println("devices num"+devices.size());
						System.out.println(maximumPerDevice);
						if(devices.size()>i){
							SillyDevice silly=new SillyDevice();
							silly.setDevice(devices.get(i));
							silly.setDriverName(driver);
							silly.setServerHost(server.getSshCredential().getHostname());
							dev.add(silly);
						}else{
							break;
						}
						
						
					}
					devicesToTest.put(driver,dev);
				}else if(maximumPerDevice==-1){
					ArrayList<SillyDevice> sillys=devicesToSillys(devices,driver,server);
				
					devicesToTest.put("driver",sillys);
				}
			}
		}
		return devicesToTest;
		
	}
	
	
	private ArrayList<SillyDevice> devicesToSillys(ArrayList<Device> devices,String driver,Server string){
		ArrayList <SillyDevice> sillys=new ArrayList<SillyDevice>();
		for(Device device:devices){
			SillyDevice silly=new SillyDevice();
			silly.setDevice(device);
			silly.setDriverName(driver);
			silly.setServerHost(string.getSshCredential().getHostname());
			sillys.add(silly);
		}
		return sillys;
	}
	
	/**
	 * this method load all devices of drivernames in the test configuration file with n number of devices per driver especefic in the 
	 * field of maximumPerDevice If this number is -1 try all numbers n that is in the database
	 *
	 * @param maximumPerDevice
	 * @return
	 */
	
	public HashMap<String , ArrayList<SillyDevice>> getDriversWithoutNames(Long maximumPerDevice){
		devicesToTest=new  HashMap<String , ArrayList<SillyDevice>>();
		MysqlData mysql=new MysqlData();
		for(Server server:servers){
			File[] fileDevices=mysql.getFilesDevice(server);
			System.out.println("probando archivos"+Arrays.toString(fileDevices));
			
			for(File fileDevice:fileDevices){
				System.out.println("nombre del archivo"+fileDevice.getName());
				ArrayList<SillyDevice> sillys=new ArrayList<SillyDevice>();
				if(!fileDevice.getName().equals(ipDeviceFileName)){
					ArrayList<Device> devices=mysql.getDevicesofDriver(server, fileDevice.getName());
					System.out.println("devices num per driver"+devices.size());
					if(maximumPerDevice==-1){
						if(devicesToTest.containsKey(fileDevice.getName())){
							sillys=devicesToSillys(devices,fileDevice.getName(),server);
							devicesToTest.get(fileDevice).addAll(sillys);
						}else{
							sillys=devicesToSillys(devices,fileDevice.getName(),server);

							devicesToTest.put(fileDevice.getName(), sillys);
						}
						
					}else{
						ArrayList<Device> deviceMaximum;
						if(devices.size()>=maximumPerDevice){
//							deviceMaximum=(ArrayList<Device>) devices.subList(0,maximumPerDevice.intValue());
							deviceMaximum=getNDevices(maximumPerDevice.intValue(),devices);
							
						}else{
							deviceMaximum=getNDevices(devices.size(),devices);

//							deviceMaximum=(ArrayList<Device>) devices.subList(0,devices.size());
						}
						if(devicesToTest.containsKey(fileDevice.getName())){
							sillys=devicesToSillys(deviceMaximum,fileDevice.getName(),server);
							devicesToTest.get(fileDevice).addAll(sillys);
						}else{
							sillys=devicesToSillys(deviceMaximum,fileDevice.getName(),server);
							devicesToTest.put(fileDevice.getName(), sillys);
						}
						
						
					}
				}
				
			
			}
		}
		return devicesToTest;
	}
	
	private ArrayList<Device> getNDevices(long maximumPerDevices,ArrayList<Device> devices){
		ArrayList<Device> nDevices=new ArrayList<Device>();
		for(int i=0;i<maximumPerDevices;i++){
			nDevices.add(devices.get(i));
		}
		return nDevices;
		
	}
	public HashMap<String , ArrayList<SillyDevice>> getEspecifcAndGeneralDriver(List<String> driverNames,Long maximumPerDevice,ArrayList<DeviceEspecific> ips){
		//TODO implementar el metodo para ller los dispositovos ip
		 
		 devicesToTest=getDriversWithNames(driverNames, maximumPerDevice);
		 devicesToTest.putAll(getDeviceByIp(ips));
		 return devicesToTest;
	}
	
	public HashMap<String , ArrayList<SillyDevice>> getEspecifcAndGeneralDriver(Long maximumPerDevice,ArrayList<DeviceEspecific> ips){
		//TODO implementar el metodo para ller los dispositovos ip
		 
		 devicesToTest=getDriversWithoutNames(maximumPerDevice);
		 devicesToTest.putAll(getDeviceByIp(ips));
		 return devicesToTest;
	}
	
	/**
	 * consulta los servidores segun la ip dadas y el servidor por el cual se conectan arroja erro si no es enconytrada la ip
	 * @param ipsInfo
	 * @return
	 */
	public HashMap<String , ArrayList<SillyDevice>> getDeviceByIp(ArrayList<DeviceEspecific> ipsInfo){
		MysqlData mysql=new MysqlData();
		HashMap<String,ArrayList<SillyDevice>> dataIp=new HashMap<String,ArrayList<SillyDevice>>();
		for(DeviceEspecific ipInfo:ipsInfo){
			ConectorServer conector=new ConectorServer();
			Server serv=conector.getServerObject(ipInfo.getHostname());
			System.out.println("server"+serv);
			if(serv!=null){
				
				try {
					ArrayList<Device> devicesIp=new ArrayList<Device>();

					Device device=mysql.getIPDevice(ipInfo.getIp(),serv);
					System.out.println("devices "+device.getSysObjectID());
					devicesIp.add(device);
					
					dataIp.put(ipInfo.getIp(),devicesToSillys(devicesIp,ipInfo.getClassTest(),serv));
					System.out.println("data"+dataIp);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else{
				throw new IllegalArgumentException("el nombre del host esta mal");
			}
			
			

		}
		//TODO implementar este metodo
		return dataIp;
	}
	
	private void  loadServerProperties(){
		ConectorServer serverProperties=new ConectorServer();
		this.servers=serverProperties.getServerList();
	}

}
