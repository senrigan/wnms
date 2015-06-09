package com.gdc.nms.server.driver.test;

import static org.junit.Assert.*;

import org.junit.Test;

import com.gdc.nms.model.Device;
import com.gdc.nms.server.drivers.snmp.Driver;
import com.gdc.nms.server.drivers.snmp.DriverManager;

public class SnmpCiscoDriverTest {
	private Device device;
	private Driver deviceDriver;
	public SnmpCiscoDriverTest(Device device){
		this.device=device;
		loadDriver();
	}
	@Test
	public void haveIpSlas(){
		assertNotNull(device.getIpSlas());
		
	}
	
	
	@Test
	public void HaveMeasureLantency(){
		assertTrue(deviceDriver.measureLatency());
		deviceDriver.
	}
	
	@Test
	public void havehostName(){
		assertNotNull(deviceDriver.getHostname());
	}
	public void loadDriver() {
		this.deviceDriver=DriverManager.getInstance().getDriver(device);
	}
	

}
