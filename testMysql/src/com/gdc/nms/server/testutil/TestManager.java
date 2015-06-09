package com.gdc.nms.server.testutil;

import jade.core.AID;

import com.gdc.nms.testing.Client;
import com.gdc.nms.testing.exception.IllegalLocalAddress;

public class TestManager {
	private static TestManager instance;
	private static Client client;
	private static AID receiver ;
	
	private TestManager(){
		
	}
	
	public static TestManager getIntance(){
		if(instance==null){
			instance=new TestManager();
		}
		return instance;
	}
	
	public void setReciver(String host){
		System.out.println("se esta cambiando el nombre del reciver"+host);
		receiver=client.getServerByHostname(host);
		System.out.println(""+receiver);
	}
	
	public  AID getReciver(){
		return receiver;
	}
	
	public  Client getClient(){
		
		return client;
	}
	
	public void startClientConection(){
		if(client==null){
		System.out.println("el clientes es nullo");
			try {
				client = new Client();
				client.start();
				System.out.println("se inicio el cliente correctamente"+client);
			} catch (IllegalLocalAddress e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	
	public void stopClientConection(){
		client.stop();
	}
}
