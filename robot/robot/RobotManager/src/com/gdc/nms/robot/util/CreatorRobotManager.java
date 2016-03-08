package com.gdc.nms.robot.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;
import java.util.zip.ZipException;

import org.apache.commons.io.FileUtils;

import com.gdc.nms.robot.gui.RobotManager;
import com.gdc.nms.robot.util.indexer.FlujoInformation;
import com.gdc.nms.robot.util.indexer.StepInformation;
import com.gdc.robothelper.webservice.robot.CreatorRobotWebService;

public class CreatorRobotManager {
	
	private String applicationName;
	private long idApp;
	private ArrayList<FlujoInformation> flujos;
	private long realRobot;
	private Path appPath;
	private Path dataPath;
	public CreatorRobotManager(){
		
	}
	
	public  boolean createRobot(String applicationName,long idApp,ArrayList<FlujoInformation> flujos){
		this.applicationName=applicationName;
		this.idApp=idApp;
		this.flujos=flujos;
		return createFilesForRobot();
	}
	
	public  boolean createRobot(Path dataPath,String applicationName,long idApp,ArrayList<FlujoInformation> flujos){
		this.applicationName=applicationName;
		this.idApp=idApp;
		this.flujos=flujos;
		this.dataPath=dataPath;
		return createFilesForRobot();
	}
	
	public boolean createFilesForRobot(){
		String location=RobotManager.getUbication();
		
		String idRobot = CreatorRobotWebService.getIdRobot(applicationName, "1", location, ""+idApp, "2","5", "");
		try{
			realRobot=Long.parseLong(idRobot);
			System.out.println(realRobot);
			Path installationPath = RobotManager.getInstallationPath();
			Path botJar = installationPath.resolve("inMonitor").resolve("bot-1.0.jar");
			Path propertiesPath=installationPath.resolve("inMonitor").resolve("robot.properties");
			modifyFileRobotId(realRobot, propertiesPath);
			modifyJar(botJar, propertiesPath, Constants.PROPERTIESJARBOT);
			if(getRobotID(botJar)==realRobot){
				
				if(createFolderApplication()){
					if(copyRobotJarToAppFolder(botJar)){
						createAppConfig();
						if(dataPath!=null){
							if(createDataApplication()){
							
							}
						}
						return true;
					}
				}
			}
			
		}catch(Exception ex){
			ex.printStackTrace();
			return false;
		}
		return false;
	}
	
	public  boolean createDataApplication(){
		
		try {
			FileUtils.copyDirectoryToDirectory(dataPath.toFile(), appPath.toFile());
			return true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	
	private void modifyFileRobotId(long robotId,Path robotFile){
		
		try {
			FileWriter fw = new FileWriter(robotFile.toFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write("id="+robotId);
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	private boolean createFolderApplication(){
		Path installationPath = RobotManager.getInstallationPath();
		appPath = installationPath.resolve("data").resolve(applicationName);
		System.out.println("creando folder aplicaction");
		if(Files.exists(appPath)){
			if(createSubFolderApplication(appPath))
				return true;
		}else{
			try {
				Files.createDirectories(appPath);
				if(createSubFolderApplication(appPath))
					return true;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return false;
	}
	
	
	private boolean createSubFolderApplication(Path parentFolder){
		Path applicationPath = parentFolder.resolve("application");
		System.out.println("creadno subfolderApplication");
		if(Files.exists(applicationPath)){
			return true;
		}else{
			try {
				System.out.println("creando aplicacion");
				Files.createDirectories(applicationPath);
				if(createFlujosFolder(applicationPath))
					return true;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return false;
	}
	
	
	private boolean createFlujosFolder(Path parentFolder){
		System.out.println("cradno Flujos Folder");
		for (FlujoInformation flujo : flujos) {
			System.out.println("///"+flujo);
			Path newFlujoPath = parentFolder.resolve(flujo.getIdFlujo()+"."+flujo.getName());
			System.out.println("//newFlujosPAth"+newFlujoPath);
			ArrayList<StepInformation> steps = flujo.getSteps();
			if(Files.exists(newFlujoPath)){
				try {
					createStepsFiles(newFlujoPath,flujo.getSteps());
				} catch (IOException e) {
					e.printStackTrace();
					deleteStepsCreate(newFlujoPath, steps);
				}
			}else{
				try{
					
					Files.createDirectories(newFlujoPath);
					createStepsFiles(newFlujoPath,flujo.getSteps());
					deleteStepsCreate(newFlujoPath, steps);
					return true;
				}catch(IOException e){
					e.printStackTrace();
				}
					
				
			}
		}
		return false;
	}
	
	
	private void createStepsFiles(Path flujoPath,ArrayList<StepInformation> steps) throws IOException{
		System.out.println("flujos path"+flujoPath);
	 	
    	InputStream inStream = null;
    	OutputStream outStream = null;
		for (StepInformation stepInformation : steps) {
			System.out.println("///steps"+stepInformation);
//			FileUtils.copyFileToDirectory(stepInformation.getPath().toFile(),flujoPath.toFile() );
//			Files.copy(stepInformation.getPath(), flujoPath);
//			FileUtils.cop
//			System.out.println("copi from"+stepInformation.getPath().toFile()+" to"+flujoPath.toFile());
			String nameFile = stepInformation.getPath().toFile().getName();
			System.out.println("namefiled "+nameFile);
			Path toPath = flujoPath.resolve(nameFile);
			
			inStream = new FileInputStream(stepInformation.getPath().toFile());
	    	    outStream = new FileOutputStream(toPath.toFile());
	        	
	    	    byte[] buffer = new byte[1024];
	    		
	    	    int length;
	    	    //copy the file content in bytes 
	    	    while ((length = inStream.read(buffer)) > 0){
	    	  
	    	    	outStream.write(buffer, 0, length);
	    	 
	    	    }
	    	 
	    	    inStream.close();
	    	    outStream.close();
		}
		
	}
	
	
	private void deleteStepsCreate(Path flujoPath,ArrayList<StepInformation> steps){
		for (StepInformation stepInformation : steps) {
			Path actualStepPath=flujoPath.resolve(stepInformation.getPath().getFileName());
			if(Files.exists(actualStepPath)){
				
				try {
					Files.delete(actualStepPath);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}
	}
	
	
	 private void modifyJar(Path jarPath,Path FileToAdd,String fileJarPath ) throws IOException{
		  String jarName = jarPath.toString();
		  String fileName = FileToAdd.toString();

		  // Create file descriptors for the jar and a temp jar.

		  File jarFile = new File(jarName);
		  File tempJarFile = new File(jarName + ".tmp");

		  // Open the jar file.

		  JarFile jar = new JarFile(jarFile);
		  System.out.println(jarName + " opened.");

		  // Initialize a flag that will indicate that the jar was updated.

		  boolean jarUpdated = false;

		  try {
		     // Create a temp jar file with no manifest. (The manifest will
		     // be copied when the entries are copied.)

		     Manifest jarManifest = jar.getManifest();
		     JarOutputStream tempJar =
		        new JarOutputStream(new FileOutputStream(tempJarFile));

		     // Allocate a buffer for reading entry data.

		     byte[] buffer = new byte[1024];
		     int bytesRead;

		     try {
		        // Open the given file.

		        FileInputStream file = new FileInputStream(fileName);

		        try {
		           // Create a jar entry and add it to the temp jar.

		           JarEntry entry = new JarEntry(fileJarPath);
		           tempJar.putNextEntry(entry);

		           // Read the file and write it to the jar.

		           while ((bytesRead = file.read(buffer)) != -1) {
		              tempJar.write(buffer, 0, bytesRead);
		           }

		           System.out.println(entry.getName() + " added.");
		        }
		        finally {
		           file.close();
		        }

		        // Loop through the jar entries and add them to the temp jar,
		        // skipping the entry that was added to the temp jar already.

		        for (Enumeration entries = jar.entries(); entries.hasMoreElements(); ) {
		           // Get the next entry.

		           JarEntry entry = (JarEntry) entries.nextElement();

		           // If the entry has not been added already, add it.
		           if (! entry.getName().equals(fileJarPath)) {
		              // Get an input stream for the entry.

		              InputStream entryStream = jar.getInputStream(entry);

		              // Read the entry and write it to the temp jar.

		              tempJar.putNextEntry(entry);

		              while ((bytesRead = entryStream.read(buffer)) != -1) {
		                 tempJar.write(buffer, 0, bytesRead);
		              }
		           }
		        }

		        jarUpdated = true;
		     }
		     catch (Exception ex) {
		    	 ex.printStackTrace();
		        System.out.println(ex);

		        // Add a stub entry here, so that the jar will close without an
		        // exception.

		        tempJar.putNextEntry(new JarEntry("stub"));
		     }
		     finally {
		        tempJar.close();
		     }
		  }
		  finally {
		     jar.close();
		     System.out.println(jarName + " closed.");

		     // If the jar was not updated, delete the temp jar file.

		     if (! jarUpdated) {
		        tempJarFile.delete();
		     }
		  }

		  // If the jar was updated, delete the original jar file and rename the
		  // temp jar file to the original name.

		  if (jarUpdated) {
		     jarFile.delete();
		     tempJarFile.renameTo(jarFile);
		     System.out.println(jarName + " updated.");
		  }
	 }
	
	 private static long getRobotID(Path jarPath){
	    	long robotID=-1;
	    	try {
	        	URL url = new URL("jar:file:"+jarPath.toString()+"!/META-INF/robot.properties");
	        	System.out.println(url.toString());
	        	InputStream is = url.openStream();
	        	BufferedReader reader = new BufferedReader(new InputStreamReader(is));
	            StringBuilder out = new StringBuilder();
	            String line;
	            while ((line = reader.readLine()) != null) {
	                out.append(line);
	            }
	            String[] split = out.toString().split("=");
	            robotID=Long.parseLong(split[1].trim());
			} catch (ZipException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
	    	return robotID;
	    	
	    }
	    
	 
	 private boolean copyRobotJarToAppFolder(Path robotPath){
		 try {
			FileUtils.copyFileToDirectory(robotPath.toFile(),appPath.toFile(),false);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		 return false;
	 }
	 
	 
	 private boolean createAppConfig() throws IOException{
		 Path file = appPath.resolve("application").resolve("app-config.xml");
		 ArrayList<String> list=new ArrayList<String>();
		 list.add("<app-config id=\""+idApp+"\" alias=\""+applicationName+"\"  />");
		 try{
			 Files.write(file, list, Charset.forName("UTF-8"));
			 return true;
		 }catch(Exception ex){
			 
		 }
		 return false;
	 }
	 public static void main(String[] args) {
		File src=new File("E:\\test\\VUCEM\\application\\VUCEM\\01.LOAD_PAGE.iim");
		File dis=new File("E:\\test\\pru");
		try {
			FileUtils.copyFileToDirectory(src, dis);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
