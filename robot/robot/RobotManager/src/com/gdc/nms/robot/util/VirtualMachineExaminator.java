package com.gdc.nms.robot.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.sun.tools.attach.AttachNotSupportedException;
import com.sun.tools.attach.VirtualMachine;
import com.sun.tools.attach.VirtualMachineDescriptor;

import sun.tools.jps.Jps;

public class VirtualMachineExaminator {
	/**
	 * get a list of java process with contains a property name = "robotid" 
	 * if the jvm not have any of this process then return a empaty list
	 * @return
	 */
	public static ArrayList<RobotInformation> getRunningRobot(){
		List<VirtualMachineDescriptor> list = VirtualMachine.list();
		ArrayList<RobotInformation> robots=new ArrayList<RobotInformation>();
		System.out.println("procesando virtualmachine");
//		ExecutorService executor=Executors.newFixedThreadPool(10);
		
 		for(VirtualMachineDescriptor descriptor:list){
// 			executor.execute(new Runnable() {
//				
//				@Override
//				public void run() {
					try {
						VirtualMachine vm=VirtualMachine.attach(descriptor.id());
						Set<Object> keySet2 = vm.getSystemProperties().keySet();
						
						String robotId = vm.getSystemProperties().getProperty("robotId");
						if(robotId!=null){
							RobotInformation robotInfo=new RobotInformation();
							robotInfo.setIdProcess(Long.parseLong(descriptor.id()));
							robotInfo.setRobotId(Long.parseLong(robotId));
							robotInfo.setAppName(vm.getSystemProperties().getProperty("appName"));
							Properties robotProperties = vm.getSystemProperties();
							Set<Object> keySet = robotProperties.keySet();
							HashMap<String,String> properties=new HashMap<String,String>();
							for (Object object : keySet) {
								String propertyName=(String)object;
								if(propertyName.contains("robot_")){
									propertyName=propertyName.replaceAll("robot_","");
									propertyName=propertyName.replaceAll("_", " ");
									String propertyValue = (String)robotProperties.get(object);
									properties.put(propertyName, propertyValue);
								}
							}
							robotInfo.setPropierties(properties);
							robots.add(robotInfo);
							
						}
						
						
					} catch (AttachNotSupportedException | IOException e) {
						e.printStackTrace();
					}
//				}
//			});
			
		}
// 		executor.shutdown();
// 		while(!executor.isTerminated()){
// 			
// 		}
 		System.out.println("termino virtal machone");
 		return robots;
	}
	
	
	public static void main(String[] args) {
		ArrayList<RobotInformation> runningRobot = VirtualMachineExaminator.getRunningRobot();
		System.out.println(runningRobot);
	}
	
}
