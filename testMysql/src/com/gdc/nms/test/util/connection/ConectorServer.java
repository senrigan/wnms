package com.gdc.nms.test.util.connection;

import java.util.List;
import java.io.File;

import connection.credential.Server;



public class ConectorServer {
	private List<Server> servers;
	
	public ConectorServer(){
		DataServer data=new DataServer();
		servers=data.getServerData(new File("ServersData.xml"));
	}
	
	public List<Server> getServerList(){
		return this.servers;
	}

	
	public Server getServerObject(String hostName){
		DataServer data=new DataServer();
		List<Server> servers=data.getServerData(new File("ServersData.xml"));
		for(Server serv:servers){
			System.out.println("server "+serv.getSshCredential().getHostname()+"igual "+hostName);
			if(serv.getSshCredential().getHostname().equals(hostName)||serv.getNameServer().equals(hostName)){
				return serv;
			}
		}
		return null;
	}
	
	
	
	
	

}
