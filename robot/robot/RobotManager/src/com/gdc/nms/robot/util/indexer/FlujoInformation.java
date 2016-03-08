package com.gdc.nms.robot.util.indexer;

import java.nio.file.Path;
import java.util.ArrayList;

public class FlujoInformation {
	private String name;
	private int numSteps;
	private long idFlujo;
	private Path path;
	private ArrayList<StepInformation> steps;
	public int getNumSteps() {
		return numSteps;
	}
	public void setNumSteps(int numSteps) {
		this.numSteps = numSteps;
	}
	public long getIdFlujo() {
		return idFlujo;
	}
	public void setIdFlujo(long idFlujo) {
		this.idFlujo = idFlujo;
	}
	public ArrayList<StepInformation> getSteps() {
		return steps;
	}
	public void setSteps(ArrayList<StepInformation> steps) {
		this.steps = steps;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Override
	public String toString() {
		return "\n FlujoInformation [name=" + name + ", numSteps=" + numSteps
				+ ", idFlujo=" + idFlujo + ", steps=" + steps + "] \n";
	}
	public Path getPath() {
		return path;
	}
	public void setPath(Path path) {
		this.path = path;
	}
	
	
	
	
}
