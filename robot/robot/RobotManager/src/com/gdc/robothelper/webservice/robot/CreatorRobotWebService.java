package com.gdc.robothelper.webservice.robot;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.security.cert.X509Certificate;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.WebServiceException;

public class CreatorRobotWebService {

	private static int generaNumeroAleatorio(int minimo, int maximo){
        
        int num=(int)Math.floor(Math.random()*(minimo-(maximo+1))+(maximo+1));
        return num;
    }
	public static String getIdRobot(String nameApp,String status,String location,
				String idApp, String retries ,String period,String jsonSteps){
		String robotID="-1";
		try{
			System.out.println("\n Obteniendo WebService Lllego un robot  nombre"+nameApp+"\n status"+status+
					"\nlocation"+location+" \nidapp"+idApp+"\nretires"+retries+ "\nperiod"+ period);
			fixUntrustCertificate();

				Multirecarga service= new Multirecarga();
				int connectionTimeOutInMs = 5000;
				
//				Map<String, Object> context = ((BindingProvider)service).getRequestContext();
//				//Set timeout params
//				context.put("com.sun.xml.internal.ws.connect.timeout", connectionTimeOutInMs);
//				context.put("com.sun.xml.internal.ws.request.timeout", connectionTimeOutInMs);
//				context.put("com.sun.xml.ws.request.timeout", connectionTimeOutInMs);
//				context.put("com.sun.xml.ws.connect.timeout", connectionTimeOutInMs);
				MultirecargaPortType port = service.getMultirecargaPort();
								
	//			//TODO LEER ARCHIVO APP.XML / APPLICATION Y SACAR EL NOMBRE DE APLICAION Y ID
				//-1 id de aplicacion 

				try{

					 robotID= port.createRobot(nameApp,location , idApp,status, period, retries,jsonSteps);
					System.out.println(robotID);
	//				return robotID;
				}catch( WebServiceException e){
					System.out.println("retry .. webservices consult");

					robotID = port.createRobot(nameApp,location , idApp,status, period,retries,jsonSteps);
					System.out.println(robotID);
					
				}
			}catch(WebServiceException ex){
//			return robotID;
				ex.printStackTrace();
				return robotID;
			}catch(Exception ex ){
				ex.printStackTrace();
			}
		return robotID;
		
		
		
//		int generaNumeroAleatorio = generaNumeroAleatorio(-1, -10000);
//		return ""+generaNumeroAleatorio;

	}
	
	
	public static void fixUntrustCertificate() throws KeyManagementException, NoSuchAlgorithmException{


        TrustManager[] trustAllCerts = new TrustManager[]{
            new X509TrustManager() {
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
               
                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }
                
                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
				@Override
				public void checkClientTrusted(
						java.security.cert.X509Certificate[] arg0, String arg1)
						throws CertificateException {
					
				}
				@Override
				public void checkServerTrusted(
						java.security.cert.X509Certificate[] arg0, String arg1)
						throws CertificateException {
					// TODO Auto-generated method stub
					
				}

            }
        };

        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, new java.security.SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

        HostnameVerifier allHostsValid = new HostnameVerifier() {
           

			@Override
			public boolean verify(String hostname, SSLSession session) {
				return true;
			}
        };

        // set the  allTrusting verifier
        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
}
	public static void main(String[] args) {
		String idRobot = CreatorRobotWebService.getIdRobot("prueba", "0", "computadora de prueba", "20", "2", "10","prueba");
		System.out.println("numero de id"+idRobot);
	}
	
	
	
}
