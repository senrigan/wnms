package com.gdc.nms.test.util.connection;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;










import org.apache.commons.io.FileUtils;

import com.gdc.nms.common.DeviceUtil;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
//import com.thoughtworks.xstream.io.xml.StaxDriver;



import connection.credential.MysqlCredential;
import connection.credential.SSHCredential;
import connection.credential.Server;












public class DataServer {
	public DataServer(){
		
	}
	
	private XStream getXStreamConfig(){
		XStream xstream = new XStream(new DomDriver()); 
		//xstream.processAnnotations(Server.class);
		xstream.alias("ServerItem", Server.class);
//		xstream.alias("ServersList", ServersList.class);
		//xstream.addImplicitCollection(ServersList.class,"Servers");
		
		//xstream.addImplicitCollection(ArrayList.class, "ServerItem",Server.class);
		
		return xstream;
	}
	
	public String toXML(List<Server> data){
		//XStream xstream =new XStream();
		String xmlData="";
//		XStream xstream = new XStream(new DomDriver()); 
//		//XStream xstream = new XStream(new StaxDriver()); // does not require XPP3 library starting with Java 6
//
//		//xstream.alias("Server Item",Server.class);
//		xstream.processAnnotations(Server.class);
		XStream xstream=getXStreamConfig();
//		ServersList server=new ServersList();
//		server.setServers(data);
		//xstream.addImplicitCollection(Server.class,"Server List");
		//xstream.setMode(XStream.XPATH_RELATIVE_REFERENCES);
		//xmlData=xstream.toXML(server);
		xmlData=xstream.toXML(data);
//		for(Server item:data){
//			xmlData+=xstream.toXML(item);
//		}
		return xmlData;
	}
	
	
	@SuppressWarnings("unchecked")
	public List<Server> getServerData(File xmlFile){
		List<Server> servers;
		//			String st=FileUtils.readFileToString(xmlFile);
		XStream xstream=getXStreamConfig();
		servers=(List<Server>) xstream.fromXML(xmlFile);
		return servers;
	}
	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		
//		System.out.println(DeviceUtil.class.getResourceAsStream("device.properties"));
//		System.out.println(DeviceUtil.class.getResource("device.properties"));
//	
//		if(true)
//			System.exit(1);
		DataServer data=new DataServer();
		List<Server> servers =new ArrayList<Server>();
		Server serv=new Server();
		
		List<String>host=new ArrayList<String>();
		String hostAgent="192.168.1.254";
		host.add(hostAgent);
		serv.setJadeHosts(host);
		MysqlCredential mysql=new MysqlCredential("1","2","3","mundo",4);
		MysqlCredential msql=new MysqlCredential();
		msql.setHostname("hola");
		msql.setPassword("mundo");
		msql.setPort(35);
		msql.setUsername("snr");
		System.out.println("msql"+msql);
		serv.setMysqlCredential(mysql);
		SSHCredential sshCredent=new SSHCredential("5","6","7",8);
		serv.setSshCredential(sshCredent);
		serv.setDeleteAfterClose(true);
		serv.setGlassFishPath("home/");
		Server serv2=new Server();
		serv.setNameServer("server 1");
		serv2.setJadeHosts(serv.getJadeHost());
		serv2.setNameServer("server 2");
		serv2.setMysqlCredential(new MysqlCredential("5","6","7","mund",5));;
		serv2.setSshCredential(new SSHCredential("9","10","11",12));
		serv2.setDeleteAfterClose(true);
		serv2.setGlassFishPath("home/");
		servers.add(serv);
		servers.add(serv2);

		//servers.add(serv2);

		
		String info=data.toXML(servers);
		try {
			FileUtils.writeStringToFile(new File("ServersData.txt"),info);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("info"+info);
		XStream xstream=data.getXStreamConfig();
		servers=(List<Server>) xstream.fromXML(info);
		System.out.println("servers"+servers.size());
	}
	
	
//	private class ServersList{
//		private List<Server> serves=new ArrayList<Server>();
//		
//		public List<Server> getServers(){
//			return this.serves;
//		}
//		
//		public void setServers(List<Server> servers){
//			this.serves=servers;
//		}
//		
//		
//	}
}
