package com.gdc.nms.robot.util;

public class JavaProcessInfo {
	private int idProcess;
	private String data;
	public int getIdProcess() {
		return idProcess;
	}
	public void setIdProcess(int idProcess) {
		this.idProcess = idProcess;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	@Override
	public String toString() {
		return "JavaProcessInfo [idProcess=" + idProcess + ", data=" + data
				+ "]";
	}
	
	
}
