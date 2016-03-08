package com.gdc.nms.robot.gui.tree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import com.gdc.nms.robot.gui.RobotManagerGui;
import com.gdc.nms.robot.gui.RobotManagerGui.ButtonType;
import com.gdc.nms.robot.util.indexer.AppInformation;
import com.gdc.nms.robot.util.indexer.FlujoInformation;

public class TreeListener implements TreeSelectionListener{
	private RobotManagerGui gui;
	private JTree tree;
	public TreeListener(RobotManagerGui gui, JTree tree){
		this.gui=gui;
		this.tree=tree;
	}
	@Override
	public void valueChanged(TreeSelectionEvent e) {
		Element app = (Element) tree.getLastSelectedPathComponent();
		AppInformation appinfo = app.getAppinfo();
		if(appinfo!=null){
			
			gui.setInformation(proccesText(appinfo));
			
			if(app.isStopAble()){
				
				gui.enableButton(ButtonType.STOP,true);
				gui.enableButton(ButtonType.START,false);

				
			}else{
				gui.enableButton(ButtonType.STOP,false);

				gui.enableButton(ButtonType.START,true);

			}
		}
		
	}
	
	
	
	private String proccesText(AppInformation appInfo){
		appInfo.getAppName();
		StringBuilder builder=new StringBuilder();
		builder.append("Nombre de Aplicacion :");
		builder.append(appInfo.getAppName()+"\n");
		builder.append("Alias :");
		builder.append(appInfo.getAlias()+"\n");
		builder.append("RobotID:");
		builder.append(appInfo.getIdRobot()+"\n");
		builder.append("Propiedades del Robot \n");
		HashMap<String, String> propierties = appInfo.getPropierties();
		if(!propierties.isEmpty()){
			
			Set<String> keySet = propierties.keySet();
			for (String key : keySet) {
				String value = propierties.get(key);
				builder.append("\t"+key+" :"+value);
				
			}
		}
		builder.append("Flujos : \n");
		ArrayList<FlujoInformation> flujos = appInfo.getFlujos();
		for (FlujoInformation flujoInformation : flujos) {
			
			builder.append("\n"+flujoInformation.getName()+" Pasos"+flujoInformation.getNumSteps());
		}
		
		
		return builder.toString();
	}
		
	

}
