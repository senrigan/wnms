
package com.gdc.robothelper.webservice.robot;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.4-b01
 * Generated source version: 2.2
 * 
 */
@WebService(name = "MultirecargaPortType", targetNamespace = "http://localhost/webservice/status/")
@SOAPBinding(style = SOAPBinding.Style.RPC)
public interface MultirecargaPortType {


    /**
     * retornar datetime actual
     * 
     * @return
     *     returns java.lang.String
     */
    @WebMethod(action = "urn:webservice#getNow")
    @WebResult(partName = "return")
    public String getNow();

    /**
     * agregar datos a la bd
     * 
     * @param app
     * @param monitorId
     * @param time
     * @param monitor
     * @param error
     * @param status
     * @param robotId
     * @param totalLoad
     * @param finishTime
     * @param cadena
     * @return
     *     returns java.lang.String
     */
    @WebMethod(action = "urn:webservice#agregartest")
    @WebResult(partName = "return")
    public String agregartest(
        @WebParam(name = "cadena", partName = "cadena")
        String cadena,
        @WebParam(name = "robot_id", partName = "robot_id")
        String robotId,
        @WebParam(name = "app", partName = "app")
        String app,
        @WebParam(name = "monitor", partName = "monitor")
        String monitor,
        @WebParam(name = "status", partName = "status")
        String status,
        @WebParam(name = "time", partName = "time")
        String time,
        @WebParam(name = "finishTime", partName = "finishTime")
        String finishTime,
        @WebParam(name = "error", partName = "error")
        String error,
        @WebParam(name = "total_load", partName = "total_load")
        String totalLoad,
        @WebParam(name = "monitorId", partName = "monitorId")
        String monitorId);

    /**
     * retornar datos de un robot
     * 
     * @param robotId
     * @return
     *     returns java.lang.String
     */
    @WebMethod(action = "urn:webservice#getDataRobot")
    @WebResult(partName = "return")
    public String getDataRobot(
        @WebParam(name = "robot_id", partName = "robot_id")
        String robotId);

    /**
     * crear registro robot en la bd
     * 
     * @param idApp
     * @param testEvery
     * @param location
     * @param name
     * @param retries
     * @param intExt
     * @param steps
     * @return
     *     returns java.lang.String
     */
    @WebMethod(action = "urn:webservice#createRobot")
    @WebResult(partName = "return")
    public String createRobot(
        @WebParam(name = "name", partName = "name")
        String name,
        @WebParam(name = "location", partName = "location")
        String location,
        @WebParam(name = "idApp", partName = "idApp")
        String idApp,
        @WebParam(name = "intExt", partName = "intExt")
        String intExt,
        @WebParam(name = "testEvery", partName = "testEvery")
        String testEvery,
        @WebParam(name = "retries", partName = "retries")
        String retries,
        @WebParam(name = "steps", partName = "steps")
        String steps);

    /**
     * borrado logico de registro robot en la bd
     * 
     * @param robotId
     * @return
     *     returns java.lang.String
     */
    @WebMethod(action = "urn:webservice#deleteRobot")
    @WebResult(partName = "return")
    public String deleteRobot(
        @WebParam(name = "robotId", partName = "robotId")
        String robotId);

}
