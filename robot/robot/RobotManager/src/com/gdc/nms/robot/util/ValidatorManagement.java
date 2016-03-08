package com.gdc.nms.robot.util;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JOptionPane;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.gdc.nms.robot.util.indexer.AppJsonObject;
import com.gdc.nms.robot.util.indexer.FlujoInformation;
import com.gdc.nms.robot.util.indexer.FlujoJsonObject;
import com.gdc.robothelper.webservice.ClientWebService;

public class ValidatorManagement {
	public static void isValidFlujo(String nameFlujo){
		
	}
	
	public static ArrayList<AppJsonObject> getAppsName(){
		String appName = ClientWebService.getAppName();
		if(appName!=null){
			ArrayList<AppJsonObject> appsJson=new ArrayList<AppJsonObject>();
			JSONArray fromXML;
			try {
				fromXML = new JSONArray(appName);
				for(int i=0;i<fromXML.length();i++){
					try {
						JSONObject jSonObject=fromXML.getJSONObject(i);
						AppJsonObject appObjecto=new AppJsonObject();
						appObjecto.setAlias(jSonObject.getString("alias"));
						long idApp=Long.parseLong(jSonObject.getString("id"));
						appObjecto.setId(idApp);
						boolean active=Boolean.parseBoolean(jSonObject.getString("active"));
						appObjecto.setActive(active);
						appsJson.add(appObjecto);
						System.out.println();;
					} catch (JSONException e) {
						e.printStackTrace();
					}
					
				}
				return appsJson;
			} catch (Exception ex) {
				ex.printStackTrace();
				
			}
			
			
		}
		return null;
	}
	
	
	public static ArrayList<FlujoJsonObject> getFlujosName(long idApp){
		String flujosByIdApp = ClientWebService.getFlujosByIdApp(idApp);
		if(flujosByIdApp!=null){
			ArrayList<FlujoJsonObject> flujoJson=new ArrayList<FlujoJsonObject>();
			try{
				
				JSONArray jsonArrya=new  JSONArray(flujosByIdApp);
				for (int i = 0; i < jsonArrya.length(); i++) {
					JSONObject jSonObject=jsonArrya.getJSONObject(i);
					FlujoJsonObject flujoObject=new FlujoJsonObject();
					long id=Long.parseLong(jSonObject.getString("id"));
					String alias= jSonObject.getString("alias");
					boolean active = Boolean.parseBoolean(jSonObject.getString("active"));
					flujoObject.setActive(active);
					flujoObject.setAlias(alias);
					flujoObject.setId(id);
					flujoJson.add(flujoObject);
				}
				return flujoJson;
			}catch(Exception ex){
				ex.printStackTrace();
			}
			
		}
		return null;
	}
	
	
	
	public static void main(String[] args) {
		ArrayList<AppJsonObject> appsName = ValidatorManagement.getAppsName();
		System.out.println(appsName);
	}
	
	
	public static boolean validateFlujosFolderPath(Path folder,long appId){
		System.out.println("folder"+folder+" appid"+appId);
		ArrayList<FlujoJsonObject> flujosName = getFlujosName(appId);
		for (FlujoJsonObject flujoJsonObject : flujosName) {
			System.out.println(flujoJsonObject.getAlias());
		}
		ArrayList<FlujoInformation> flujosApp = AppExaminator.getFlujosApp(folder);
//		ArrayList<FlujoInformation> validFlujo=new ArrayList<FlujoInformation>();
		for (FlujoInformation flujoInformation : flujosApp) {
			String flujoName = flujoInformation.getName();
			System.out.println(flujoName);
			boolean found=false;
			for (FlujoJsonObject object : flujosName) {
				String alias = object.getAlias();
				System.out.println("alias name"+alias);
				if(flujoName.equalsIgnoreCase(alias)){
					System.out.println("si son iguales");
					found=true;
//					validFlujo.add(flujoInformation);
					break;
				}
			}
			if(!found){
				return false;
			}
		}
		return true;
		
	}
	
	public static ArrayList<FlujoInformation> getValidFlujos(Path folder,long appId){
		System.out.println("folder"+folder+" appid"+appId);
		ArrayList<FlujoJsonObject> flujosName = getFlujosName(appId);

		ArrayList<FlujoInformation> flujosApp = AppExaminator.getFlujosApp(folder);
		ArrayList<FlujoInformation> validFluj=new ArrayList<FlujoInformation>();
//		ArrayList<FlujoInformation> validFlujo=new ArrayList<FlujoInformation>();
		for (FlujoInformation flujoInformation : flujosApp) {
			String flujoName = flujoInformation.getName();
			System.out.println(flujoName);
			boolean found=false;
			for (FlujoJsonObject object : flujosName) {
				String alias = object.getAlias();
				System.out.println("alias name"+alias);
				if(flujoName.equalsIgnoreCase(alias)){
					flujoInformation.setIdFlujo(object.getId());
					validFluj.add(flujoInformation);
//					validFlujo.add(flujoInformation);
					break;
				}
			}
		}
		return validFluj;
	}
	
	
	public static ArrayList<FlujoInformation> getValidFlujosTest(Path folder,long appId){
		ArrayList<FlujoInformation> flujosApp = AppExaminator.getFlujosApp(folder);
		return flujosApp;
	}
	
	
	public static boolean isValidMainFolder(Path path){
		File[] listFiles = path.toFile().listFiles();
		int count=0;
		for (File file : listFiles) {
			if(file.isDirectory()){
				
				String name = file.getName();
				if(name.equals("application")){
					count++;
					continue;
				}
				
				if(name.equals("data")){
					count++;
					continue;
				}
			}
		}
		if(count==2){
			return true;
		}
		return false;
	}
	
	
	
}
