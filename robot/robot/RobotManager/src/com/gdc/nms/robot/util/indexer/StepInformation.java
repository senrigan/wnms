package com.gdc.nms.robot.util.indexer;

import java.nio.file.Path;

public class StepInformation {
	private String name;
	private int numStep;
	private Path path;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getNumStep() {
		return numStep;
	}
	public void setNumStep(int numStep) {
		this.numStep = numStep;
	}
	@Override
	public String toString() {
		return "StepInformation [name=" + name + ", numStep=" + numStep + ", path=" + path + "]";
	}
	public Path getPath() {
		return path;
	}
	public void setPath(Path path) {
		this.path = path;
	}
	
	
}
