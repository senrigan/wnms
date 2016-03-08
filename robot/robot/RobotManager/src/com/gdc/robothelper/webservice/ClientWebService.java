package com.gdc.robothelper.webservice;


public class ClientWebService {

	
	private static SisproRobotManagerHelper getPort(){
		SRMHelper service=new SRMHelper();
		SisproRobotManagerHelper port= service.getSisproRobotManagerHelperPort();
		return port;
	}
	
	
	public static String getAppName(){
		try{
			
			String applications = getPort().getApplications();
			if(!applications.equals("-1")){
				return applications;
			}else{
				return null;
				
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return null;
	}
	
	public static String getFlujosByIdApp(long idApp){
		try{
			String flowsByApplicationId = getPort().getFlowsByApplicationId(idApp);
			if(!flowsByApplicationId.equals("-1")){
				return flowsByApplicationId;
			}else{
				return null;
			}
		}catch(Exception ex){
			
		}
		return null;
		
		
	}
	
	public static void main(String[] args) throws InterruptedException {
		while(true){
		
		String flujosByIdApp = getFlujosByIdApp(2);
		System.out.println(flujosByIdApp);
		System.out.println(ClientWebService.getAppName());
		 Thread.sleep(60000);
		}	
	}

}
