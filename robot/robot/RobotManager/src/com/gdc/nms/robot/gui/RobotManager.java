package com.gdc.nms.robot.gui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.gdc.nms.robot.util.AppExaminator;
import com.gdc.nms.robot.util.CommandExecutor;
import com.gdc.nms.robot.util.Constants;
import com.gdc.nms.robot.util.Environment;
import com.gdc.nms.robot.util.RobotInformation;
import com.gdc.nms.robot.util.VirtualMachineExaminator;
import com.gdc.nms.robot.util.indexer.AppInformation;

public class RobotManager extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	private static Path installationPath;
	private static String ubication;
	private static final Logger LOGGER=Logger.getLogger(RobotManager.class.toString());
	public RobotManager() {
		Path regInstallationPath = AppExaminator.getInstallationPath();
		setInstallationPath(regInstallationPath);
		checkWindowsRegistry();
//		  DefaultMutableTreeNode root = new DefaultMutableTreeNode("Root");
//	        //create the child nodes
//	        DefaultMutableTreeNode vegetableNode = new DefaultMutableTreeNode("Vegetables");
//	        DefaultMutableTreeNode fruitNode = new DefaultMutableTreeNode("Fruits");
//	 
//	        //add the child nodes to the root node
//	        root.add(vegetableNode);
//	        root.add(fruitNode);
//	         
//	        //create the tree by passing in the root node
//	        tree = new JTree(root);
//	        add(tree);
//	         
//	        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//	        this.setTitle("JTree Example");       
//	        this.pack();
//	        this.setVisible(true);
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		System.out.println("inicnado robots");
		Thread hilo=new Thread(new Runnable() {
			
			@Override
			public void run() {
				initRobot();
				
			}
		});
		hilo.start();
		System.out.println("inicando gui");
		RobotManagerGui robotGui=new RobotManagerGui();
	}
	
	
	private void checkWindowsRegistry(){
		LOGGER.log(Level.INFO, "reading and creating windows registry necessary for sysprorobotmanager");
		checkRegistryRobotMustRun();
		checkRegistryRobotNoRunning();
		
	}
	
	private void checkRegistryRobotMustRun(){
		String redRegistryWindows="";
		
		try {
			redRegistryWindows=CommandExecutor.readRegistrySpecificRegistry(Constants.LOCALREGISTRY, "robotmustRun", "REG_SZ");
//			if(redRegistryWindows.equals("")){
//				createRegistryRobotMustRun();
//			}
		} catch (Exception e) {
			createRegistryRobotMustRun();
			e.printStackTrace();
		}
	}
	
	private void createRegistryRobotMustRun(){
		ArrayList<AppInformation> installedApps = AppExaminator.getInstalledApps();
		String idRobots="";
		for (AppInformation appInformation : installedApps) {
			idRobots+= appInformation.getIdRobot()+",";
		}
		try {
			CommandExecutor.addRegistryWindows(Constants.LOCALREGISTRY, "robotmustRun",idRobots );
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	private void checkRegistryRobotNoRunning(){
		try {
			CommandExecutor.readRegistrySpecificRegistry(Constants.LOCALREGISTRY, "robotnotRun","REG_SZ");
		} catch (Exception e) {
			createregistryRobotNotRunning();
			e.printStackTrace();
		}
	}
	
	private void createregistryRobotNotRunning(){
		try {
			CommandExecutor.addRegistryWindows(Constants.LOCALREGISTRY, "robotnotRun","");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void initRobot(){
		try {
			String robotIds = CommandExecutor.readRegistrySpecificRegistry(Constants.LOCALREGISTRY,"robotmustRun","REG_SZ");
			LOGGER.info("startup all Robot id "+robotIds);
			System.out.println("hola");
			String[] split = robotIds.split(",");
			System.out.println("split");
			HashMap<Long, String> robotNotRunning = getRobotNotRunning();
			System.out.println("not running"+robotNotRunning.keySet());
			
			ArrayList<AppInformation> runningApps = AppExaminator.getRunningApps();
			System.out.println("split"+split+"lenght"+split.length);
			for (int i = 0; i < split.length; i++) {
				System.out.println("iterando a iniciar"+i+"<"+split.length+" s"+split[i]);
				final int b = i;
//				Thread hilo=new Thread(new Runnable() {
//					@Override
//					public void run() {
						try{
							if(split[b].length()>0){
								
								long  idRobot=Long.parseLong(split[b]);
								if(!robotNotRunning.containsKey(idRobot)){
									boolean alreadyRunning=false;
									for (AppInformation appInformation : runningApps) {
										if(appInformation.getIdRobot()==idRobot){
											alreadyRunning=true;
											break;
										}
									}
									if(!alreadyRunning && runRobot(idRobot)){
										
										System.out.println("corriendo el robot"+runRobot(idRobot));
									}
								}else{
									System.out.println("el robot especifica como detenido"+idRobot);
								}
								
							}
							
						}catch(NumberFormatException nex){
							LOGGER.log(Level.SEVERE,"cannot parse the id for run");
						}						
//					}
//				});
//				hilo.start();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("NO PINCHES MAMES");
	}
	
	
	private static void checkUbicationRegistry(){
		try {
			String ubicationRegist = CommandExecutor.readRegistrySpecificRegistry(Constants.LOCALREGISTRY, "ubicationRobot","REG_SZ");
			setUbication(ubicationRegist);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static HashMap<Long,String> getRobotNotRunning(){
		HashMap<Long,String> robotNotRun=new HashMap<Long,String>();
		try {
			String robotIds = CommandExecutor.readRegistrySpecificRegistry(Constants.LOCALREGISTRY,"robotnotRun","REG_SZ");
			if(robotIds.endsWith(",")){
				robotIds=robotIds.substring(0,robotIds.length()-1 );
				
			}
			String[] split = robotIds.split(",");
			for (int i = 0; i < split.length; i++) {
				String string = split[i];
				if(string.length()>0){
					try{
						
						long idRobot = Long.parseLong(string);
						robotNotRun.put(idRobot, "");
					}catch(NumberFormatException ex){
						ex.printStackTrace();
					}
				}
			}
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		return robotNotRun;
	}
	
	public static boolean runRobotByApp(long idApp){
		ArrayList<AppInformation> notRunnigApps = AppExaminator.getNotRunnigApps();
		for (AppInformation appInformation : notRunnigApps) {
			System.out.println(appInformation.getAppName()+" idapp"+appInformation.getIdApp());
			String appName = appInformation.getAppName();
			System.out.println(appInformation.getIdApp() +" idrobot"+idApp);
			if(appInformation.getIdApp()==idApp){
				runJarRobot(appInformation.getAppName());
				return true;
			}
		}
		return false;
	}
	
	public static boolean runRobot(long idRobot){
		ArrayList<AppInformation> notRunnigApps = AppExaminator.getNotRunnigApps();
		for (AppInformation appInformation : notRunnigApps) {
			System.out.println(appInformation.getAppName()+" idapp"+appInformation.getIdApp());
			System.out.println(appInformation.getIdApp() +" idrobot"+idRobot);
			if(appInformation.getIdRobot()==idRobot){
				LOGGER.info("starup the robot id :"+idRobot);
				return runJarRobot(appInformation.getAppName());
			}
		}
		return false;
	}
	
	private static boolean runJarRobot(String appName){
		String java ="\""+Environment.getJava()+"\"";
		
		Path robotJar=Paths.get(installationPath.resolve("data").resolve(appName).resolve(Constants.JARNAME).toString());
		String jar="\""+robotJar.toString()+"\"";
		String command=java +" -Dname=\"Robot_"+appName+"\" "+" -jar "+jar;
		try {
			System.out.println("command"+command);
			Process exec = Runtime.getRuntime().exec(command);
			BufferedReader in=new BufferedReader(new InputStreamReader(exec.getInputStream()));
			String line;
			while((line=in.readLine())!=null){
				System.out.println("stopjar lines"+line);
				if(line.contains("Robot has been started")){
					return true;
				}
				
			}
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return false;
		
	}
	
	public static void runRobot(String applicationName){
		
	}
	
	public static void stopRobot(long idRobot) throws Exception{
		ArrayList<RobotInformation> runningRobot = VirtualMachineExaminator.getRunningRobot();
		for (RobotInformation robotInformation : runningRobot) {
			if(robotInformation.getRobotId()==idRobot){
				LOGGER.log(Level.INFO, "stopping robotid "+idRobot);
				if(!stopJar(robotInformation.getIdProcess())){
					LOGGER.log(Level.WARNING,"the robotid "+idRobot +"cannot stoped");
					throw new Exception("cannot stop jar of appName"+robotInformation.getAppName());
				}
			}
		}
	}
	public static void stopAllRobots(){
		ArrayList<RobotInformation> runningRobot = VirtualMachineExaminator.getRunningRobot();
		for (RobotInformation robotInformation : runningRobot) {
			try {
				stopRobot(robotInformation.getRobotId());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	private static boolean stopJar(long pid){
		String command="TaskKill /PID "+pid+" /F";
		try {
			Process exec = Runtime.getRuntime().exec(command);
			BufferedReader in=new BufferedReader(new InputStreamReader(exec.getInputStream()));
			String line;
			while((line=in.readLine())!=null){
				System.out.println("stopjar lines"+line);
				if(line.contains("SUCCESS")){
					return true;
				}
				
			}
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public static void stopRobot(String applicationName){
		
	}
	
	
	public static void getRobotMustRunb(){
		
	}
	
	public static void getRobotMustNotRun(){
		
	}
	
	public static void addNewRobot(Path source){
		
	}
	
	public static void updateRobot(long idRobot,Path souruce){
		
	}
	
	public static void updateAllRobots(Path source){
		
	}

	public static Path getInstallationPath() {
		return installationPath;
	}
	
	
	public static void setUbication(String ubicationRegistry){
		ubication=ubicationRegistry;
	}
	
	public static String getUbication(){
		return ubication;
	}

	public static void setInstallationPath(Path installationPath) {
		RobotManager.installationPath = installationPath;
	}
	
	
	
	
	
	
	
}
