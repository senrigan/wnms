package com.gdc.nms.test.util.connection;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;

import com.gdc.nms.common.DeviceUtil;
import com.gdc.nms.model.Device;
import com.gdc.nms.model.Project;
import com.gdc.nms.server.drivers.snmp.DriverManager;

import connection.credential.MysqlCredential;
import connection.credential.Server;



public class MysqlData {
	
	private List<Device> devices;
	private List<Project> projects;
	private HashMap<String,ArrayList<Device>> foldersDevice ;
	private HashMap<String,String> oidSuport;
	private HashMap<String,String> ipDevices;
	private String ipsFileName="mainIp.txt";
	private final String projectsFileName="projects.txt";
	
	public MysqlData(){
		
	}
	
	
	public boolean isServerFilesDevicesCreated(Server server){
		if(isServerDirectoryCreated(server)){
			if(serverDeviceFilesCount(server)>0){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 
	 * @param server
	 * @param driverName the name of driver suport in the server
	 * @return ArraytList<Device> if the name and server is correct in otherwise return null
	 */
	public ArrayList<Device> getDevicesofDriver(Server server,String driverName){
		String name=driverName.endsWith(".txt")?driverName:driverName.concat(".txt");
		System.out.println("server"+name);
		if(getDeviceFileName(server).contains(name)){
			System.out.println("server in"+name);
			File driverFile=new File(getServerFile(server).getAbsolutePath()+"/"+name);
			System.out.println(""+driverFile.getAbsolutePath());
			ArrayList<Device> devices=DeviceXML.fromXML(driverFile.getAbsolutePath());
			return devices;
		}
		return null;
	}
	
	/**
	 * 
	 * @param server
	 * @return the number of files inside of server directory  0 if is empaty
	 */
	private int serverDeviceFilesCount(Server server){
		File serverAddressFile=getServerFile(server);
		File[] deviceFile=serverAddressFile.listFiles();
		return deviceFile.length;
	}
	
	/**
	 * 
	 * @param server
	 * @return list of name device File if no have device file return null
	 */
	public List<String> getDeviceFileName(Server server){
		if(isServerFilesDevicesCreated(server)){
			File serverAddressFile=getServerFile(server);
			File[] deviceFiles=serverAddressFile.listFiles();
			List<String> filesName=new ArrayList<String>() ;
			for(File file:deviceFiles){
				filesName.add(file.getName());
			}
			return filesName;
			
		}
		return null;
		
	}
	
	
	private File getServerFile(Server server){
		File serverAddressFile =new File(server.getSshCredential().getHostname());
		return serverAddressFile;
		
	}
	
	public boolean isServerDirectoryCreated(Server server){
		File file=getServerFile(server);
		if(file.isDirectory()){
			return true;
		}
		return false;
	}
	
	public File[] getFilesDevice(Server server){
		File file=getServerFile(server);
		List list=Arrays.asList(file.list());
		list.remove(new File(this.getServerFile(server).getAbsolutePath()+"/"+ipsFileName));
		
		return file.listFiles();
		
	}
	public void createFilesAndFolders(Server infoServ){
		devices=getMysqlDataDevice(infoServ.getMysqlCredential());
		projects=getMysqlDataProject(infoServ.getMysqlCredential());
		URL url =DeviceUtil.class.getResource("device.properties");
		searchDeviceSuport(new File(url.getPath()));
		System.out.println("total de entradas"+foldersDevice);
		try {
			createDriverFile(new File(infoServ.getSshCredential().getHostname()));
			createIPMainFile(new File(infoServ.getSshCredential().getHostname()));
			createProjectFile(new File(infoServ.getSshCredential().getHostname()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("error al crear los archiivos");
		}
	}
	
	public HashMap<String,ArrayList<Device>> getFolderDevice(){
		return this.foldersDevice;
	}
	/**
	 * this metod create the files of drivers suport in the server
	 * @param directoryServer
	 * @throws IOException
	 */
	private void createDriverFile(File directoryServer) throws IOException{

			
		if(directoryServer.exists()||directoryServer.mkdirs()){
			String xmlDevice;
			for(Map.Entry<String,ArrayList<Device>> entry:foldersDevice.entrySet()){
				ArrayList<Device> devices=entry.getValue();
				xmlDevice="";
				Long time_init,time_end;
				time_init=System.currentTimeMillis();
				xmlDevice+=DeviceXML.toXML(devices);
				time_end=System.currentTimeMillis();
				System.out.println("the time que tard"+(time_end-time_init));
				File driverFile=new File(directoryServer.getName()+"/"+oidSuport.get(entry.getKey())+".txt");
				FileUtils.writeStringToFile(driverFile, xmlDevice);
			}
		}else{
			throw new IOException();
		}
	}
	
	public boolean existProjectFile(Server serv){
		File servDir=new File(serv.getSshCredential().getHostname());
		File projectFile=new File(servDir.getName()+"/"+projectsFileName);
		if(projectFile.exists()){
			return true;
		}
		return false;
	}
	
	private void createProjectFile(File directoryServer) throws IOException{
		System.out.println("creando project file");
		if(directoryServer.exists()||directoryServer.mkdirs()){
			String xmlProject;
				
			xmlProject="";
			xmlProject+=DeviceXML.projectsToXML(projects);
//			System.out.println("xmlProject"+xmlDevice);
			File driverFile=new File(directoryServer.getName()+"/"+projectsFileName);
//			System.out.println("project file"+driverFile.getAbsolutePath());
			FileUtils.writeStringToFile(driverFile, xmlProject);
		
		}else{
			throw new IOException();
		}
	}
	
	
	
	
	private void createIPMainFile(File directoryServer) throws IOException{
		if(directoryServer.exists()||directoryServer.mkdirs()){
			for(Map.Entry<String,String> entry:ipDevices.entrySet()){
				String sysOid=entry.getValue();
				String line=entry.getKey()+"|"+oidSuport.get(sysOid)+"\n";
				File driverFile=new File(directoryServer.getName()+"/"+ipsFileName);
				FileUtils.writeStringToFile(driverFile,line ,true);
			}
		}else{
			throw new IOException();
		}
	}
	
	

	
	public Device getIPDevice(String ip,Server server) throws IOException{
		HashMap<String,String> ipContent=readIPMainFile(getServerFile(server));
		
		if(ipContent.containsKey(ip)){
			String driver=ipContent.get(ip);
			System.out.println("ip"+ip+"driver"+driver);
			ArrayList<Device> devices=getDevicesofDriver(server, driver);
			return searchListDevicebyIP(devices,ip);
		}else{
			return null;
		}
	}
	
	private Device searchListDevicebyIP(ArrayList<Device> devices,String ip){
		for(Device device:devices){
			if(device.getIp().equals(ip)){
				return device;
			}
		}
		return null;
	}
	
	private HashMap<String,String> readIPMainFile(File directoryServer) throws IOException{
		File ipsFile=new File(directoryServer.getAbsoluteFile()+"/"+ipsFileName);
		List<String> lines=FileUtils.readLines(ipsFile);
		System.out.println("lines"+lines);
		HashMap<String,String> ipMap=new HashMap<String,String>();
		for(String line:lines){
			
			String ip=line.substring(0,line.indexOf("|"));
			System.out.println("ip"+ip);
			String driver=line.substring(line.indexOf("|")+1,line.length());
			System.out.println("driver"+driver);
			ipMap.put(ip, driver);
		}
		return ipMap;
		
	}
	
	
	private String PreviousSysOID(String sysOID){
		System.out.println(""+sysOID);
		sysOID=sysOID.substring(0,sysOID.lastIndexOf('.'));
		return sysOID;
	}
	private HashMap<String,ArrayList<Device>>  searchDeviceSuport(File source){
		oidSuport=getSysoOIDSuport(source);
		foldersDevice=new HashMap<String,ArrayList<Device>> ();
		ipDevices=new HashMap<String,String>();
		for(Device device:devices){
			if(oidSuport.containsKey(device.getSysObjectID())){

				addToFolder(device.getSysObjectID(),device);
			}else{
				String newOID=device.getSysObjectID();
				while(device.getSysObjectID().length()>1 ){
					if((oidSuport.containsKey(newOID))){
						System.out.println("encontrado"+device.getSysObjectID()+" "+getDriverName(device));

						addToFolder(newOID,device);
						ipDevices.put(device.getIp(),newOID);
						break;
					}else if(newOID.contains(".")){
							newOID=PreviousSysOID(newOID);

						}else{
							System.out.println("no encontrado"+device.getSysObjectID()+" "+getDriverName(device));
							addToFolder("0",device);
							ipDevices.put(device.getIp(),newOID);
							break;
						}
					}
					
					
				}

			}
		
		return foldersDevice;

	}
	
	private String getDriverName(Device device){
			
		return ""+DriverManager.getInstance().getDriver(device).getClass();
	}
	
	private void  addToFolder(String sysOIDFolder,Device device){
			ArrayList<Device>fileDevice=new ArrayList<Device>();
//			System.out.println("añadiendo al folder"+sysOIDFolder);
//			System.out.println("esta en hash"+foldersDevice.containsKey(sysOIDFolder));
			//System.out.println(""+sysOIDFolder+" "+com.gdc.nms.server.drivers.snmp.DriverManager.getInstance().getDriver(device).getClass());
			if(foldersDevice.containsKey(sysOIDFolder)){
//				System.out.println("contendido en folder"+sysOIDFolder);
				foldersDevice.get(sysOIDFolder).add(device);
			}else{
				fileDevice=new ArrayList<Device>();
				fileDevice.add(device);
				foldersDevice.put(sysOIDFolder,fileDevice);
			}
		
		
	}
	
	private List<Device> getMysqlDataDevice(MysqlCredential mysqlCredent){
		System.out.println("mysqlCredent"+mysqlCredent);
		MySql mysqlBase=new MySql(mysqlCredent);
		try {
			mysqlBase.connect();
			return mysqlBase.getDevices();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
	}
	
	private List<Project> getMysqlDataProject(MysqlCredential mysqlCredent){
		MySql mysqlBase=new MySql(mysqlCredent);
		try {
			mysqlBase.connect();
			return mysqlBase.getProjects();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	
	public String getStringProjects(MysqlCredential mysql){
		MySql mysqlBase=new MySql(mysql);
		try {
			mysqlBase.connect();
			
			String xmlProject;
			
			xmlProject="";
			xmlProject+=DeviceXML.projectsToXML(mysqlBase.getProjects());
			return  xmlProject;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	private HashMap<String,String> getSysoOIDSuport(File deviceFile){
//		 InputStream in = new URL( "http://commons.apache.org" ).openStream();
//		 try {
//		   System.out.println( IOUtils.toString( in ) );
//		 } finally {
//		   IOUtils.closeQuietly(in);
//		 }
		try {
			long time_start, time_end;
//			time_start = System.currentTimeMillis();
//			String st=FileUtils.readFileToString(deviceFile);
//			time_end = System.currentTimeMillis();
//			System.out.println("the task has taken st"+ ( time_end - time_start ) +" milliseconds");
//			time_start = System.currentTimeMillis();
//
//			List<String> lst=FileUtils.readLines(deviceFile);
//			time_end = System.currentTimeMillis();
//			System.out.println("the task has taken "+ ( time_end - time_start ) +" milliseconds");
//			
//			System.out.println("is string"+st);
//			System.out.println("is lines"+lst);
			 LineIterator it = FileUtils.lineIterator(deviceFile, "UTF-8");
			 HashMap<String,String> hash=new HashMap<String,String>(); 
			 try {
				 time_start = System.currentTimeMillis();
				 String name,key;
//				 int i=0,o=0;
				 //Pattern p=Pattern.compile("[^[0-9]*\\.*]*");
				 Pattern p=Pattern.compile("[a-zA-Z]+");
				 while (it.hasNext()) {
			     String line = it.nextLine();
			     
			     if(line.startsWith("#")){
			    	 line=line.replace("#", " ");
			    	 line=line.replaceAll(" ","");
			    	 line=line.replaceAll("/"," ");
			    	 name=line;
			    	 line=it.nextLine();
			    	 key=line.substring(0, line.indexOf("=")-1);
			    	//System.out.println("lne"+key);
//			    	 i++;
			    	 Matcher m=p.matcher(key);
//			    	 if(key.indexOf(".")>=0){
			    	 if(key.contains(".") || key.length()>0){
			    		 //System.out.println(""+key.substring(key.lastIndexOf("."),key.length()-1));
				    	//m=p.matcher(key.substring(key.lastIndexOf("."),key.length()-1));
			    		// System.out.println("key"+key);
				    	 m=p.matcher(key);
			    		 //if(key.matches("[.a-zA-Z]")){
				    	 
				    	 if(!m.find()){

				    		 //key=line.substring(0, line.indexOf("="));
				    		 key.replaceAll(" ","");
				    		 System.out.println("nombre"+name+"key"+key+"key size"+key.length());
					    	 hash.put(key, name);
					    	 
//					    	 System.out.println(" name "+name+" key "+key);
				    	 }
			    	 }
			    		
			    	 
			    	 
			     }
			     /// do something with line
			   }
		    	// System.out.println("i"+i+" 0"+o);

			   time_end = System.currentTimeMillis();
				System.out.println("the task has taken "+ ( time_end - time_start ) +" milliseconds");
				 return hash;
			 } finally {
			   LineIterator.closeQuietly(it);
			   System.out.println("si acabo");
			  
			 }

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	
//	public static void main(String[] args) {
//		MysqlData msql=new MysqlData();
////		URL url =DeviceUtil.class.getResource("device.properties");
////		HashMap<String,String> syso=msql.getSysoOIDSuport(new File(url.getPath()));
////		for(String key:syso.keySet()){
////			System.out.println(syso.get(key).replaceAll("-","_").toUpperCase().replaceAll(" ","_").replaceAll("(", "_")+",");
////		}
//////		try {
//////			HashMap<String,String> hash=msql.getSysoOIDSuport(new File("C:\\Users\\senrigan\\workspace\\workspaceWork\\dev\\development\\source\\packages\\common\\src\\com\\gdc\\nms\\common\\device.properties"));
//////			System.out.println("hash key"+hash.keySet());
//////			System.out.println("content"+hash.entrySet());
//		Server serv2=new Server();
//		serv2.setNameServer("server 2");
//		serv2.setMysqlCredential(new MysqlCredential("192.168.208.3", "root", "root", "nms",3306));
//		serv2.setSshCredential(new SSHCredential("9","10","11",12));
//		serv2.setDeleteAfterClose(true);
//		serv2.setGlassFishPath("home/");
//		msql.createFilesAndFolders(serv2);
//	}
	
	

}
