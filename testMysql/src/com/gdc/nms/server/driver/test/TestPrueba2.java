package com.gdc.nms.server.driver.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TestPrueba2 {
	
	@Test
	public void prueba1(){
		assertEquals("hola","hola");
	}
	@Test
	public void prueba2(){

		assertEquals(2,2);
		assertEquals(2,2);
		assertEquals(2,2);
		prueba3();

	}
	public void prueba3(){
		assertEquals(25,2);
		assertEquals(2,2);
	}
	
	public static void main(String[] args) throws InterruptedException {
		for(;;){
			System.out.println("hola mundo");
			Thread.sleep(3000);
		}
	}
}
