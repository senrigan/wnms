package com.gdc.nms.robot.util.indexer;

import java.util.ArrayList;
import java.util.HashMap;

public class AppInformation  implements Comparable<AppInformation>{
	private String alias;
	private String appName;
	private long idApp;
	private ArrayList<FlujoInformation> flujos;
	private long idRobot;
	private HashMap<String,String> propierties;
	
	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
	public long getIdApp() {
		return idApp;
	}
	public void setIdApp(long idApp) {
		this.idApp = idApp;
	}
	public ArrayList<FlujoInformation> getFlujos() {
		return flujos;
	}
	public void setFlujos(ArrayList<FlujoInformation> flujos) {
		this.flujos = flujos;
	}
	public String getAlias() {
		return alias;
	}
	public void setAlias(String alias) {
		this.alias = alias;
	}
	
	
	public long getIdRobot() {
		return idRobot;
	}
	public void setIdRobot(long idRobot) {
		this.idRobot = idRobot;
	}
	
	
	@Override
	public String toString() {
		return "AppInformation [alias=" + alias + ", appName=" + appName + ", idApp=" + idApp + ", flujos=" + flujos
				+ ", idRobot=" + idRobot + "]";
	}
	@Override
	public int compareTo(AppInformation o) {
		return new Integer(new Long(this.idApp).compareTo(o.getIdApp()));
	}
	public HashMap<String, String> getPropierties() {
		return propierties;
	}
	public void setPropierties(HashMap<String, String> propierties) {
		this.propierties = propierties;
	}
	
	
	
	
}
