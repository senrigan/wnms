package com.gdc.nms.robot.util;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;

import com.gdc.nms.robot.util.indexer.AppInformation;
import com.gdc.nms.robot.util.indexer.FlujoInformation;

public class RobotInformation {
	private long idProcess;
	private long robotId;
	private String appName;
	private HashMap<String,String> propierties;
	
	public long getRobotId() {
		return robotId;
	}
	public void setRobotId(long robotId) {
		this.robotId = robotId;
	}
	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
	public long getIdProcess() {
		return idProcess;
	}
	public void setIdProcess(long idProcess) {
		this.idProcess = idProcess;
	}
	
	public HashMap<String, String> getPropierties() {
		return propierties;
	}
	public void setPropierties(HashMap<String, String> propierties) {
		this.propierties = propierties;
	}
	
	public AppInformation getApplicationInfo(){
		Path app = AppExaminator.getInstallationPath().resolve("data").resolve(getAppName());
		AppInformation appData;
		if(Files.exists(app)){
			 appData= AppExaminator.getAppData(app);
			
		}else{
			appData=new AppInformation();
			appData.setAppName("unknow");
			appData.setIdApp(-1L);
			appData.setFlujos(new ArrayList<FlujoInformation>());
		}
		appData.setPropierties(getPropierties());
		return appData;

	}
	@Override
	public String toString() {
		return "RobotInformation [idProcess=" + idProcess + ", robotId=" + robotId + ", appName=" + appName
				+ ", propierties=" + propierties + "]";
	}
	
	
	
	
	

}
