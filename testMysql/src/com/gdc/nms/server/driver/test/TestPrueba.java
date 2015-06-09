package com.gdc.nms.server.driver.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

public class TestPrueba {
	@Test
	public void prueba1(){
		assertEquals("hola","hola");
	}
	@Test
	public void prueba2(){

		assertEquals(2,2);
		assertEquals(2,2);
		assertEquals(2,2);
		TestPrueba2 tst=new TestPrueba2();
		tst.prueba1();
	
		//prueba3();

	}
	public void prueba3(){
		assertEquals(25,2);
		assertEquals(2,2);
		
	}
	
	private String driverStringToEnum(String driverName){
		String newDriver=changeParentesis(driverName);
		driverName=driverName.replaceAll(".txt","");
		newDriver=driverName.replaceAll(".", "_");

		if(startWithNumber(newDriver)){
			newDriver="_"+newDriver.substring(0,newDriver.length());
		}
		
		newDriver=driverName.replaceAll("-","_");
		newDriver=driverName.replaceAll(" ","_");
		


		return newDriver.toUpperCase();
	}
	
	private String changeParentesis(String driverName){
		if(driverName.contains("(")){
			System.out.println("driver parent"+driverName);
			driverName=driverName.replaceAll("\\(","_");
			System.out.println("driver parent"+driverName);

			
		}
		if(driverName.contains(")")){
			System.out.println("driver parent"+driverName);

			driverName=driverName.replaceAll("\\)", "");
			System.out.println("driver parent"+driverName);

		}
		return driverName;
	}
	private boolean startWithNumber(String driverName){
		
        return driverName.matches("^[0-9]+.");
    
	}
	public static void main(String[] args) {
//		System.out.println("ImageStream(Net-SNMP Linux).txt");
//		String snm="ImageStream(Net-SNMP Linux).txt";
//		TestPrueba prub=new TestPrueba();
//		snm=snm.replaceAll(".txt", "");
//		snm=snm.replaceAll("\\(", "_");
//		snm=snm.replaceAll("\\)","_");
//		snm=snm.replaceAll("-","_");
//		
//		snm=snm.replaceAll(" ","_");
//		snm=snm.toUpperCase();
//		//snm=snm.replaceAll(".", "_");
		String n="  14579 , ,55  ";
		String [] n2=n.split(",");
		for(String n3:n2){
			System.out.println(""+n3);
		}
//		System.out.println("snm "+snm);
		//String newTest=prub.driverStringToEnum(snm);
		//System.out.println(""+newTest);
	}
}
