package com.gdc.nms.robot.util;

import java.util.Locale;
import java.util.ResourceBundle;

public class Language {
	private static ResourceBundle BUNDLE;
	
	public static void load(){
		BUNDLE=ResourceBundle.getBundle("com.gdc.robotmanager.language");
		
	}
	
	public static void load(Locale locale){
		BUNDLE=ResourceBundle.getBundle("com.gdc.robotmanager.language",locale);
	}
	
	public static String get(String key){
		return BUNDLE.getString(key);
	}
	
}
