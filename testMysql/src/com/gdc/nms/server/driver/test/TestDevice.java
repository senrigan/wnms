package com.gdc.nms.server.driver.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import jade.core.AID;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;

import org.apache.log4j.Logger;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

import com.gdc.nms.model.Device;
import com.gdc.nms.server.drivers.snmp.DriverManager;
import com.gdc.nms.server.testutil.SillyDevice;
import com.gdc.nms.server.testutil.TestManager;
import com.gdc.nms.testing.Client;
import com.gdc.nms.testing.exception.IllegalLocalAddress;
import com.gdc.nms.testing.response.DeviceDriverResponse;
import com.gdc.nms.testing.response.DeviceInterfacesResponse;
import com.gdc.nms.testing.response.DeviceResourcesResponse;


//@RunWith(Parameterized.class)
public class TestDevice {
	private static final long TIMEOUT=50000;
	private static   Logger log;
	private static Client client;
	private static AID receiver ;
	//TODO crear logs para los devices ya sean warging inform etc... y en ese archivi imprimir ip sysos oid y que driver se esta utilizando
	//responder si los response devolvieron nulo o si los metodos de los respondos regresan nulo
	
	private static SillyDevice sillyDevice;
	private static Device device;
	@BeforeClass
	public static  void setUp(){
//		log=Logger.getLogger(TestDevice.class);
		sillyDevice=RunTest.device;
		device=sillyDevice.getDevice();
		System.out.println("se esta ejecutando before");
	
			TestManager mng=TestManager.getIntance();
			client=mng.getClient();
			receiver=mng.getReciver();
			System.out.println("en el test"+client+" reciver"+receiver);
//			client = new Client();
//			
//			client.start();
//			receiver = client.getServerByHostname(sillyDevice.getServerHost());
			// TODO 
			//initConexionServerToData();
		
//		
//       
	}
//	public TestDevice(Device device){
//		this.device=device;
//	}
//	
	
//	@Parameters
//	public static Collection<Device[]> getData(){
//		TestConsult consult=new TestConsult();
//		//consult.setUp();
//		consult.initConexionServerToData();
//		ArrayList<Device> devices=consult.getDevices();
//		//System.out.println("devices"+devices.size());
//		final Collection <Device[]> data=new ArrayList<Device[]>();
//		for(Device device:devices){
//			data.add(new Device[]{device});
//		}
//		return data;
//	}
	
	
	
	private long getTimeOut(){
		return this.device.getSnmpTimeout()*2;
	}
//	@Test(expected = NullPointerException.class)

	@Ignore
	@Test
	public void TestInterface() throws Exception{
		//Device device=.getDevice();
		DeviceInterfacesResponse interf=client.getInterfaces(device, receiver, TIMEOUT);
		assertNotNull(interf.getInterfaces());
		assertFalse("no existen interfaces",interf.getInterfaces().isEmpty());
		
	}
	
	@Test
	public void testResource() throws Exception{
		//Device device=silly.getDevice();;
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
	public void testDriverName() throws Exception{
		//Device device =silly.getDevice();
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
	
	private long getTimeOut(Device device){
		return device.getSnmpTimeout()*2;
	}
	
}