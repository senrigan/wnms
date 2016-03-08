package com.gdc.nms.robot.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class CommandExecutor {
	
	public static String executeCommand(String command) throws IOException, InterruptedException {

		StringBuffer output = new StringBuffer();

		Process p;
			p = Runtime.getRuntime().exec(command);
			p.waitFor();
			int exitValue = p.exitValue();
            String line = "";			

			BufferedReader reader = 
                            new BufferedReader(new InputStreamReader(p.getInputStream()));

			while ((line = reader.readLine())!= null) {
				output.append(line + "\n");
			}
			
			
			line = "";			

			BufferedReader readerError= new BufferedReader(new InputStreamReader(p.getErrorStream()));
        	while ((line = readerError.readLine())!= null) {
				output.append(line + "\n");
			}
			
            


		return output.toString();

	}
	public static String deleteRegistryWindows(String registryPath) throws IOException, InterruptedException{
		String deleteReg="REG DELETE "+registryPath +" /f";
		return executeCommand(deleteReg);
	}
	
	public static  String addRegistryWindows(String registryPath, String nameValue , String value) throws IOException, InterruptedException
	{
		String addReg="REG ADD \""+registryPath+"\" /v \""+nameValue +"\" /t REG_SZ /d \""+value +"\" /f";
		return executeCommand(addReg);
	}
	
	
	public static String redRegistryWindows(String registryPath) throws IOException, InterruptedException{
		String queryReg="REG QUERY ";
		String outPut=executeCommand(queryReg+registryPath);
		return outPut;
	}
	

    public static String readRegistrySpecificRegistry(String registryPath, String keyName, String regType) throws Exception {
        String redRegistryWindows = redRegistryWindows(registryPath+" /v "+keyName);
    	
        return redRegistryWindows.split(regType)[1].trim();
    }
}
