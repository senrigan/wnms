package com.gdc.nms.server.driver.test;



import jade.core.AID;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;





import java.util.List;

import org.apache.commons.validator.routines.InetAddressValidator;
import org.junit.Test;

import com.gdc.nms.model.Device;
import com.gdc.nms.server.testutil.LoaderDriver;
import com.gdc.nms.server.testutil.SillyDevice;
import com.gdc.nms.server.testutil.TestConfig;
import com.gdc.nms.server.testutil.TestConfig.TestMode;
import com.gdc.nms.server.testutil.TestInformed;
import com.gdc.nms.server.testutil.TestManager;
import com.gdc.nms.test.util.connection.ConectorServer;
import com.gdc.nms.test.util.connection.MysqlData;
import com.gdc.nms.testing.Client;
import com.gdc.nms.testing.exception.IllegalLocalAddress;

import connection.credential.Server;


/**
 * 
 * @author Gilberto Cordero
 *
 */
public class RunTest {
	private String testConfigPath="./src./TestProperties.ini";
	public static SillyDevice device;
	public static String serverInUse;
	private boolean ipExist=false;
	public TestManager  managerDriverTest;

	/**
	 * 
	 * this enum is because i running in java 6 and i canot use a switch with String
	 * the method run by the test mode in the testconfiguration file
	 * @throws IOException if the testconfiguration file cannot found or red
	 */
	
	public HashMap<String,ArrayList<SillyDevice>>  runTestMode() throws IOException{
		
		TestConfig configTest=new TestConfig(testConfigPath);
		TestMode mode=configTest.getTestMode();
		HashMap<String,ArrayList<SillyDevice>> driverToTest=null;
		if(configTest.canGenerateData()){
			System.out.println("generando data");
			generateDataPerRun();
		}else{
			System.out.println("usando data");
			useDataPerRun();
		}
		if(configTest.isvalidFile()){
			LoaderDriver driverLoader=new LoaderDriver();
			
			switch(mode){
			case ALL:
				if(configTest.existDriverNames()){
					driverToTest=driverLoader.getDriversWithNames(configTest.getDriverNames(),configTest.getMaximumPerDriver());

				}else{
					driverToTest=driverLoader.getDriversWithoutNames(configTest.getMaximumPerDriver());
				}
				
				break;
			case BOTH:
				if(configTest.existDriverNames()){
					driverToTest=driverLoader.getEspecifcAndGeneralDriver(configTest.getDriverNames(),configTest.getMaximumPerDriver(),configTest.getIpDataDevice());
					ipExist=true;
				}else{
					driverToTest=driverLoader.getEspecifcAndGeneralDriver(configTest.getMaximumPerDriver(),configTest.getIpDataDevice());
					driverLoader.getDeviceByIp(configTest.getIpDataDevice());
				}
				
				break;
			case ESPECIFIC:
				
				driverToTest=driverLoader.getDeviceByIp(configTest.getIpDataDevice());
				ipExist=true;
				System.out.println("driver to tes test mode"+driverToTest);
				break;
		
				
			}
		}else{
			System.out.println("algo en el archivo de configuracion esta mal");
		}
		return driverToTest;
		
		

	}
	public boolean existIpDevice(){
		return ipExist;
	}
	
	public void generateDataPerRun(){
	
			ConectorServer conector=new ConectorServer();
			List<Server> servers=conector.getServerList();
			MysqlData mysql=new MysqlData();
			for(Server serv:servers){
				mysql.createFilesAndFolders(serv);

			}
	
	}
	
	public void useDataPerRun(){
		ConectorServer conector=new ConectorServer();
		List<Server> servers=conector.getServerList();
		MysqlData mysql=new MysqlData();
		System.out.println("servers"+servers);
		for(Server serv:servers){
			if(mysql.isServerFilesDevicesCreated(serv)){
				if(mysql.existProjectFile(serv)){
					System.out.println("existe el rpoeject file");
				}else{
					mysql.createFilesAndFolders(serv);
				}
			}else{
				System.out.println("no existe el project file");
				mysql.createFilesAndFolders(serv);

			}

		}
	}

	
	
	@Test
	public void runAllTest(){
		managerDriverTest=TestManager.getIntance();
		
		try {
			HashMap<String,ArrayList<SillyDevice>> driverToTest=runTestMode();
			System.out.println("in the tes**"+driverToTest);
			managerDriverTest.startClientConection();
			
		
			
			for(String driverName:driverToTest.keySet()){
				TestInformed tester=new TestInformed();
				System.out.println("probando dispositivos del driver"+driverName);
				ArrayList<SillyDevice> devices=driverToTest.get(driverName);
				for(SillyDevice deviceToTest:devices){
					RunTest.device=deviceToTest;
					System.out.println("drive a rpobar "+driverName);
					
//					System.out.println("hostname"+device.getHostname());
//					System.out.println("ip"+device.getIp());
//					System.out.println("location"+device.getLocation());
					changeServerInUse(deviceToTest.getServerHost());
					tester.runTestClass(TestDevice.class);
					System.out.println(""+tester);
					
					if(errors(tester)){
						InetAddressValidator validator=InetAddressValidator.getInstance();

						if(!validator.isValid(driverName)){
							driverName=driverStringToEnum(driverName);
							System.out.println("driver Name cambiado"+driverName);
							typeTest testerDriver=typeTest.valueOf(driverName);
							System.out.println("tester driver"+testerDriver);
							switch(testerDriver){
								case APCUPS:
									break;
								case BLUECOATPACKETSHAPER9_2_7:
									break;
								case CISCO:
									break;
								case CISCO619:
									break;
								case CISCOACE:
									break;
								case CISCOIPS:
									break;
								case CISCONEXUS7000SERIES:
									break;
								case CISCONX:
									break;
								case EMERSONUPS_UNITRONICS:
									break;
								case FORTINET:
									break;
								case FOUNDRYNETWORKS:
									break;
								case GDCTELECONTROLLER:
									break;
								case GENERICANDPINGONLYSTATS:
									break;
								case H3C:
									break;
								case HUAWEI:
									break;
								case HUAWEIAR1220_2220_2240:
									break;
								case HUAWEINE40:
									break;
								case HUAWEIS5300:
									break;
								case HUAWEIS5700:
									break;
								case HUAWEIS5700_10P_PWR_LI_AC:
									break;
								case HUAWEIS5710_52C_PWR_EL:
									break;
								case HUAWEIS7700:
									break;
								case IMAGESTREAM:
									break;
								case IMAGESTREAMTELECONTROLLER:
									break;
								case IMAGESTREAM_NET_SNMP_LINUX:
									break;
								case JUNIPER:
									break;
								case NETSCREEN:
									break;
								case NTI_NET_SNMP_LINUX:
									break;
								case PIPELINE:
									break;
								case PROXIM:
									break;
								case RIVERBEDSTEELHEAD:
									break;
								case TELDAT:
									break;
								case TRIPPLITEUPS:
									break;
								case UPSONICUPS:
									break;
								case VYATTA:
									break;
								case _3COM:
									break;
								default:
										System.out.println("llego algo inesperado");
									break;
							
								
							}
						
							
						}else{
							
						}
					}
						
					
						

				}
				
			}
			System.out.println("se esta deteniendo el cliente");
			managerDriverTest.stopClientConection();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("hubo un error al leer el archivo");
			e.printStackTrace();
			
		}
		
		
	}
	
	private void changeServerInUse(String newServer){
		if(!(RunTest.serverInUse==null)){
			if(RunTest.serverInUse.equals(newServer)){
				return;
			}else{
				RunTest.serverInUse=newServer;
				managerDriverTest.setReciver(newServer);
			}
		}else{
			RunTest.serverInUse=newServer;
			managerDriverTest.setReciver(newServer);
		}
		
	}
	private ArrayList<Class<?>> testClassEspecific(String driverName){
		
		return null;
	}
	private String driverStringToEnum(String driverName){
		if(startWithNumber(driverName)){
			System.out.println("el driver tiene numero"+driverName);
		driverName="_"+driverName.substring(0,driverName.length());
		System.out.println("driver sin numero"+driverName);
		}
		driverName=driverName.replaceAll(".txt", "");
		driverName=driverName.replaceAll("\\(", "_");
		driverName=driverName.replaceAll("\\)","_");
		driverName=driverName.replaceAll("-","_");
		
		driverName=driverName.replaceAll(" ","_");	
		if(driverName.endsWith("_")){
			driverName=driverName.substring(0,driverName.length()-1);
		}

		return driverName.toUpperCase();
	}
	
	private boolean startWithNumber(String driverName){
	        return Character.isDigit(driverName.charAt(0));
	        
	    
	}
	

	
	enum typeTest{
		TRIPPLITEUPS,
		IMAGESTREAM_NET_SNMP_LINUX,
		NTI_NET_SNMP_LINUX,
		HUAWEIS5710_52C_PWR_EL,
		CISCO619,
		NETSCREEN,
		RIVERBEDSTEELHEAD,
		HUAWEI,
		IMAGESTREAMTELECONTROLLER,
		APCUPS,
		JUNIPER,
		UPSONICUPS,
		H3C,
		IMAGESTREAM,
		GDCTELECONTROLLER,
		HUAWEIS5300,
		FORTINET,
		HUAWEINE40,
		BLUECOATPACKETSHAPER9_2_7,
		EMERSONUPS_UNITRONICS,
		PIPELINE,
		GENERICANDPINGONLYSTATS,
		CISCONX,
		HUAWEIS5700,
		HUAWEIS5700_10P_PWR_LI_AC,
		FOUNDRYNETWORKS,
		CISCONEXUS7000SERIES,
		VYATTA,
		HUAWEIS7700,
		PROXIM,
		_3COM,
		CISCOIPS,
		CISCOACE,
		TELDAT,
		HUAWEIAR1220_2220_2240,
		CISCO,
		
		
	}
	/**
	 * check if te test class generate errors
	 * @param informed
	 * @return
	 */
	private boolean errors(TestInformed informed){
		return informed.getFailuresCaseCount()==0;
	}
	public static void main(String[] args) {
		RunTest run=new RunTest();
	
//			run.loadTestProperties();
			run.runAllTest();
			

		
	}

}
