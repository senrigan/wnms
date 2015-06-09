package com.gdc.nms.server.driver.test.especific;

import static org.junit.Assert.assertEquals;

import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;

import com.gdc.nms.model.Device;
import com.gdc.nms.server.drivers.snmp.DriverManager;
import com.gdc.nms.testing.response.DeviceDriverResponse;
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestGenericDevice {
	
	public void testDriverName() throws Exception{
		Device device =devices.get(3);
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
			
//		
		
		
	}
	
	@Test(expected = NullPointerException.class)
	public void testDriverName() throws Exception{
		Device device =devices.get(3);
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
	
}
