package com.gdc.nms.robot.util;

import java.util.ArrayList;
import java.util.Set;


import sun.jvmstat.monitor.HostIdentifier;
import sun.jvmstat.monitor.MonitoredHost;
import sun.jvmstat.monitor.MonitoredVm;
import sun.jvmstat.monitor.MonitoredVmUtil;
import sun.jvmstat.monitor.VmIdentifier;
import sun.tools.jps.Arguments;

public class JpsManager {
	   private static Arguments arguments;
	   private static Object localObject1;
	   
	   private static void identifyArgument(String [] param){
		   
		   try{
			 arguments=new Arguments(param);  
		   }catch(IllegalArgumentException ex){
			   
		   }
		   
	   }
	   public static ArrayList<JavaProcessInfo> getRunningApps(String [] param) {
		  return getRunningApps(false, param);

	   }
	   
	   
	   public static ArrayList<JavaProcessInfo> getRunningApps(boolean extraInformation,String [] param) {
		   ArrayList<JavaProcessInfo> processList=new ArrayList<JavaProcessInfo>();
		   identifyArgument(param);
		   if(!arguments.isHelp()){
			   HostIdentifier localHostIdentifier = arguments.hostId();
			   try{
				   localObject1 = MonitoredHost.getMonitoredHost(localHostIdentifier);
				   Set<Integer> localSet = ((MonitoredHost)localObject1).activeVms();
				   JavaProcessInfo processInfo;
				   for(Integer localInteger : localSet){
					   StringBuilder localStringBuilder=new StringBuilder();
					   processInfo=new JavaProcessInfo();
					   int i=localInteger.intValue();
					   processInfo.setIdProcess(i);
					   getInfoApp(localStringBuilder, i,extraInformation);
					   processInfo.setData(localStringBuilder.toString());
					   processList.add(processInfo);
				   }
			   }catch(Exception ex){
				   return processList;
			   }
			   
		   }
		   return processList;
	   }
	   
	   
	   private static void getInfoApp(StringBuilder localStringBuilder,int  i,boolean extraInformation){
		 
		   Object localObject2=null;

		   //localStringBuilder.append(String.valueOf(i));
		   if(arguments.isQuiet()){
			   return ;
		   }else{
			   MonitoredVm localMonitoredVm = null;
			   String str1 = "//" + i + "?mode=r";
			   String str2 = null;
			   try
			   {
		          str2 = " -- process information unavailable";
		          VmIdentifier localVmIdentifier = new VmIdentifier(str1);
		          localMonitoredVm = ((MonitoredHost)localObject1).getMonitoredVm(localVmIdentifier, 0);
		             
	             str2 = " -- main class information unavailable";
	             localStringBuilder.append(" " + MonitoredVmUtil.mainClass(localMonitoredVm, arguments
	               .showLongPaths()));
	             String str3;
	             if (arguments.showMainArgs()) {
	               str2 = " -- main args information unavailable";
	               str3 = MonitoredVmUtil.mainArgs(localMonitoredVm);
	               if ((str3 != null) && (str3.length() > 0)) {
	                 localStringBuilder.append(" " + str3);
	               }
	             }
	             if (arguments.showVmArgs()) {
	               str2 = " -- jvm args information unavailable";
	               str3 = MonitoredVmUtil.jvmArgs(localMonitoredVm);
	               if ((str3 != null) && (str3.length() > 0)) {
	                 localStringBuilder.append(" " + str3);
	               }
	             }
	             if (arguments.showVmFlags()) {
	               str2 = " -- jvm flags information unavailable";
	               str3 = MonitoredVmUtil.jvmFlags(localMonitoredVm);
	               if ((str3 != null) && (str3.length() > 0)) {
	                 localStringBuilder.append(" " + str3);
		             str2 = " -- detach failed";
		             ((MonitoredHost)localObject1).detach(localMonitoredVm);
		             System.out.println(localStringBuilder);
		             str2 = null;
	               }
	             }
	          }catch(Exception ex){
	        	  
	          }finally {
	        	  if (str2 != null && extraInformation){
		               localStringBuilder.append(str2);
		               if ((arguments.isDebug()) && (localObject2 != null) && 
		                 (((Throwable)localObject2).getMessage() != null)) {
		            	   localStringBuilder.append("\n\t");
		            	   localStringBuilder.append(((Throwable)localObject2).getMessage());
		               }
//	            	   if (!arguments.printStackTrace())
	            	   //((Throwable)localObject2).printStackTrace();
	               }
	        	  
        	  }
	        	  
	      
		}
	   }
	   
	   

}
