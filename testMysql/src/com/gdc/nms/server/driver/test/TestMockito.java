package com.gdc.nms.server.driver.test;

import jade.core.AID;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import static org.junit.Assume.assumeTrue;













import com.gdc.nms.model.Device;
import com.gdc.nms.server.drivers.snmp.DriverManager;
import com.gdc.nms.server.testutil.ConditionalIgnoreRule.ConditionalIgnore;
import com.gdc.nms.server.testutil.ConditionalIgnoreRule.IgnoreCondition;
import com.gdc.nms.server.testutil.SillyDevice;
import com.gdc.nms.server.testutil.TestManager;
import com.gdc.nms.testing.Client;
import com.gdc.nms.testing.exception.IllegalLocalAddress;
import com.gdc.nms.testing.response.DeviceDriverResponse;
import com.googlecode.junit.ext.JunitExtRunner;
import com.googlecode.junit.ext.RunIf;
import com.googlecode.junit.ext.checkers.Checker;

import static org.junit.Assert.assertEquals;
//import static org.mockito.Mock;
import static org.mockito.Mockito.*;
@RunWith(Parameterized.class)
//@RunWith(JunitExtRunner.class)

public class TestMockito {
	//private String name;
	private SillyDevice dev;
	public static Client client;
//	@Test
//	public void test(){
//		TestConsult mockedTest=mock(TestConsult.class);
//		mockedTest.initConexionServerToData();
//		ArrayList<Device> devices= TestConsult.devices;
////		System.out.println("devices "+devices.size());
////		System.out.println("name"+mockedTest.getDriverNameClass(devices.get(3)));
////		when(mockedTest.getDriverNameClass(devices.get(3))).thenReturn("ya valio mockito");
////		assertEquals("ya valio mockito",mockedTest.getDriverNameClass(devices.get(2)));
//		
//		verify(mockedTest)
//		.getDriverNameClass(devices.get(3));
//		
//	}
	
	
	public TestMockito(SillyDevice dev){
		this.dev=dev;
		///this.name=nombre;
	}
//	@Parameters
//	public static Collection<Device[]> data(){
////		ArrayList<String> nombres=new ArrayList<String>();
////		nombres.add("pablo");
////		nombres.add("juan");
////		nombres.add("luis");
////		nombres.add("miguel");
////		Object[]nom=nombres.toArray();
////		return (Collection<Object[]>) Arrays.asList(nom);
//	    
//	    Map<String, ArrayList<Device>> m = new HashMap<>();
//	    Set<Entry<String,ArrayList<Device>>> entrySet = m.entrySet();
//	    
//	    ArrayList<Device> d = new ArrayList<>();
//	    
//	    for(Entry<String,ArrayList<Device>> entry : entrySet){
//	    	d.addAll(entry.getValue());
//	    }
//	    
//	    Device[] de = new Device[d.size()+1];
//	    
//	    d.toArray(de);
//	    
//	    datos.add(de);
//	    return datos;
////		List<T> col=new ArrayList<String>(nombres);
////		return col;
//	}
	@BeforeClass
	public static  void setUp(){
//		log=Logger.getLogger(TestDevice.class);
		try {
		
			client = new Client();
			client.start();
		} catch (IllegalLocalAddress e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
//			TestManager mng=TestManager.getIntance();
//			client=mng.getClient();
			//receiver=mng.getReciver();
			//System.out.println("en el test"+client+" reciver"+receiver);
//			client = new Client();
//			
//			client.start();
//			receiver = client.getServerByHostname(sillyDevice.getServerHost());
			// TODO 
			//initConexionServerToData();
		
//		
//       
	}
	@Test
	public void testDriverName() throws Exception{
		//Device device =silly.getDevice();
		AID receiver=client.getServerByHostname(dev.getServerHost());
		System.out.println("aid"+receiver);
		System.out.println("nbame"+getDriverNameClass(dev.getDevice()));
//		try{
		System.out.println("dev"+dev.getDevice()+"server host"+dev.getServerHost());
			DeviceDriverResponse driver=client.getDeviceDriver(dev.getDevice(), receiver,getTimeOut(dev.getDevice()));
			System.out.println("device name"+driver.getClassName());

			assertEquals(getDriverNameClass(dev.getDevice()),driver.getClassName());
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
	
	@Parameterized.Parameters
	public static Collection<SillyDevice>data2() throws IOException{
		Map<String, ArrayList<SillyDevice>> deviceMap = new HashMap<>();
		RunTest tester =new RunTest();
		deviceMap=tester.runTestMode();
		ArrayList<SillyDevice> sillyDevices=new ArrayList<SillyDevice>();
	    for(String key:deviceMap.keySet()){
	    	ArrayList<SillyDevice> devices=deviceMap.get(key);
	    	sillyDevices.addAll(devices);
//	    	for(SillyDevice device:devices){
//	    		SillyDevice silly=new SillyDevice();
//	    		silly.setDevice(device.getDevice());
//	    		silly.setDriverName(key);
//	    		System.out.println("silly objet"+silly);
//	    		sillyDevices.add(silly);
//	    	}
	    	
	    }
	    System.out.println(""+sillyDevices);
	    
	    SillyDevice[] de = new SillyDevice[sillyDevices.size()];
	    Collection<SillyDevice> datos=new ArrayList<SillyDevice>();
	    //sillyDevices.toArray(de);
	    sillyDevices.toArray(de);
	    System.out.println("arryas"+Arrays.toString(de));
	    for(int i=0;i<de.length;i++){
	    	
	    	datos.add(de[i]);
	    }
		
		return datos;
	}
	@Test
	public void testName(){
		assumeTrue(false);
		System.out.println("nombre del driver"+dev.getDriverName());
		assertEquals("10.161.76.66",dev.getDriverName());
	}
	
	@Test
	//@ConditionalIgnore(condition=com.gdc.nms.server.driver.test.DriverTestType.class)

	@RunIf(DriverTestType.class)
	public void testNameSecoundDriver(){
		//DriverTestType type=new DriverTestType();
		//assumeTrue(type.satisfy());
		assertEquals("172.28.2.189",dev.getDriverName());
	}
////	class DriverTestType implements IgnoreCondition{
//	class DriverTestType implements Checker{
//
//		@Override
//		public boolean satisfy() {
//			System.out.println("es igual"+dev.getDriverName().equals("172.28.2.189"));
//			return dev.getDriverName().equals("172.28.2.189");
//			// TODO Auto-generated method stub
//		}
//
////		@Override
////		public boolean isSatisfied() {
////			System.out.println("es igual"+dev.equals("172.28.2.189"));
////			return dev.equals("172.28.2.189");
////			// TODO Auto-generated method stub
////		}
//	}
	
	
	
//	public static void main(String[] args) {
//		TestConsult mockedTest=mock(TestConsult.class);
//		TestConsult.initConexionServerToData();
//		ArrayList<Device> devices= TestConsult.devices;
//		System.out.println("devices "+devices.size());
//		System.out.println("name"+mockedTest.getDriverNameClass(devices.get(500)));
//		when(mockedTest.getDriverNameClass(devices.get(3))).thenReturn("ya valio mockito");
//		assertEquals("ya valio mockito",mockedTest.getDriverNameClass(devices.get(3)));
//	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
//		Map<String, ArrayList<Device>> m = new HashMap<>();
//		ArrayList<Device> lista = new ArrayList<Device>();
//		
//		lista.add(new Device("1960"));
//		lista.add(new Device("1961"));
//		lista.add(new Device("1962"));
//		lista.add(new Device("1963"));
//		lista.add(new Device("1964"));
//		lista.add(new Device("1965"));
//		lista.add(new Device("1966"));
//		
//		m.put("A", lista);
//	    Set<Entry<String,ArrayList<Device>>> entrySet = m.entrySet();
//		m.put("B", lista);
//	    
//	    ArrayList<SillyDevice> d = new ArrayList<>();
//	    System.out.println("mapo sieze"+m.size());
//	    System.out.println("entry set"+Arrays.toString(m.entrySet().toArray()));
//	    System.out.println("value size"+m.entrySet().size());
////	    for(Entry<String,ArrayList<Device>> entry : entrySet){
////	    	d.addAll(entry.getValue());
////	    }
//	    
//	    
//	    for(SillyDevice device : de){
//	    	System.out.println("silly"+ device.getDevice()+""+device.getDriverName());
//	    }
//	    
		try {
			TestMockito.data2();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	    
//	    for(){
//	    	
//	    }
	    
	}
}
