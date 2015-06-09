package com.gdc.nms.server.driver.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import jade.core.AID;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.gdc.nms.model.Device;
import com.gdc.nms.server.drivers.snmp.DriverManager;
import com.gdc.nms.server.testutil.SillyDevice;
import com.gdc.nms.testing.Client;
import com.gdc.nms.testing.exception.IllegalLocalAddress;
import com.gdc.nms.testing.response.DeviceDriverResponse;
import com.gdc.nms.testing.response.DeviceInterfacesResponse;
import com.gdc.nms.testing.response.DeviceResourcesResponse;


import connection.credential.Server;

@RunWith(Parameterized.class)
public class TestConsult {
	private static final long TIMEOUT=50000;
	public  ArrayList<Device> devices;
	private static Client client;
	private static AID receiver ;
	private SillyDevice silly;
	
	
	public TestConsult (SillyDevice silly){
		this.silly=silly;
	}
	
	@Parameterized.Parameters
	public static Collection<SillyDevice>data2() throws IOException{
		Map<String, ArrayList<SillyDevice>> deviceMap = new HashMap<>();
		RunTest tester =new RunTest();
		deviceMap=tester.runTestMode();
		ArrayList<SillyDevice> sillyDevices=new ArrayList<SillyDevice>();
	    for(String key:deviceMap.keySet()){
	    	ArrayList<SillyDevice> devices=deviceMap.get(key);
	    	
	    	for(SillyDevice device:devices){
	    		SillyDevice silly=new SillyDevice();
	    		silly.setDevice(device.getDevice());
	    		silly.setDriverName(key);
	    		System.out.println("silly objet"+silly);
	    		sillyDevices.add(silly);
	    	}
	    	
	    }
	    System.out.println(""+sillyDevices);
	    
	    SillyDevice[] de = new SillyDevice[sillyDevices.size()];
	    Collection<SillyDevice> datos=new ArrayList<SillyDevice>();
	    sillyDevices.toArray(de);
	    
	    System.out.println("arryas"+Arrays.toString(de));
	    for(int i=0;i<de.length;i++){
	    	
	    	datos.add(de[i]);
	    }
		
		return datos;
	}
	@BeforeClass
	public  static void setUp(){
		
		System.out.println("se esta ejecutando before");
		try {
			client = new Client();
			client.start();
			// TODO 
			//initConexionServerToData();
		} catch (IllegalLocalAddress e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
         
       
	}
	
	
	public Client getClient(){
		return this.client;
	}
	
	public ArrayList<Device> getDevices(){
		return this.devices;
	}
	
//	@Parameters
//	public static Collection<Device>data(){
//		initConexionServerToData();
//		Collection<Device> c=devices;
//		return c;
//	}
//	
//	public void initConexionServerToData(){
//		 ConectorServer cont=new ConectorServer();
// 		 List<Server> server=cont.getServerList();
// 		 MysqlData mysql=new MysqlData();
//		if(!mysql.isServerFilesDevicesCreated(server.get(1))){
//			mysql.createFilesAndFolders(server.get(1));
//		}
//		devices=mysql.getDevicesofDriver(server.get(1), "Cisco");
//		
//		//receiver = client.getServerByHostname(server.get(1).getSshCredential().getHostname());
//	}
	private long getTimeOut(Device device){
		return device.getSnmpTimeout()*2;
	}
	//@Test(expected = NullPointerException.class)
	@Test
	public void testDriverName() throws Exception{
		Device device =silly.getDevice();
		System.out.println("nbame"+getDriverNameClass(device));
//		try{
			DeviceDriverResponse driver=client.getDeviceDriver(device, receiver,getTimeOut(device));
			System.out.println("device name"+driver.getClassName());

			assertEquals(getDriverNameClass(device),driver.getClassName());
//		}catch(Exception ex){
//			
//			System.out.println("ocurrio un error con device driver");
//
//			ex.printStackTrace();
//		}
		
		
	}
	
	public String getDriverNameClass(Device device){
		return DriverManager.getInstance().getDriver(device).getClass().getName();
//		return com.gdc.nms.server.drivers.snmp.DriverManager.getInstance().getDriver(device).getClass().getName();
	}
	
	//@Test(expected = NullPointerException.class)
	@Test
	public void testResource() throws Exception{
		Device device=silly.getDevice();;
//		try {
			///si esta pendejada regresa nulo ya valio verga
			DeviceResourcesResponse driver=client.getDeviceResources(device, receiver, TIMEOUT);
			
			System.out.println("resourcers List"+driver.getDeviceResources().size());
			//si el metodo de esta pendejada regresa nulo fue lo que regreso el device y no valio verga
			
			assertNotNull("los recursos devolvio nulo",driver.getDeviceResources());
			assertTrue("la lista esta vacia",(!driver.getDeviceResources().isEmpty()));
//		} catch (Exception e) {
//			System.out.println("ocurrio un error con device resources");
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
	
	@Test
	public void TestInterface() throws Exception{
		Device device=silly.getDevice();
		DeviceInterfacesResponse interf=client.getInterfaces(device, receiver, TIMEOUT);
		assertNotNull(interf.getInterfaces());
		assertFalse("no existen interfaces",interf.getInterfaces().isEmpty());
		
	}
//	
//	@AfterClass
//	public static void a(){
//		client.stop();
//	}
	
	
	
	
	
	
//	public static void main(String[] args) {
//		ConectorServer cont=new ConectorServer();
//		List<connection.credential.Server> server=cont.getServerList();
//		MysqlData mysql=new MysqlData();
//		mysql.createFilesAndFolders(server.get(1));
//		HashMap<String, ArrayList<Device>> mapDevice=mysql.getFolderDevice();
//		ArrayList<Device> devices=mapDevice.get("1.3.6.1.4.1.9");
//		
//		System.out.println("devices "+devices.size());
//		
//	}
}
