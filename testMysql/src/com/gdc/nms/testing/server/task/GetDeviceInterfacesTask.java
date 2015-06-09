package com.gdc.nms.testing.server.task;

import java.io.Serializable;
import java.util.Set;

import com.gdc.nms.common.Ip;
import com.gdc.nms.model.Device;
import com.gdc.nms.model.Interface;
import com.gdc.nms.server.drivers.snmp.Driver;
import com.gdc.nms.server.drivers.snmp.DriverManager;
import com.gdc.nms.server.drivers.snmp.SnmpConnectorException;
import com.gdc.nms.testing.response.DeviceInterfacesResponse;

public class GetDeviceInterfacesTask extends Task {

    private Device device;

    public GetDeviceInterfacesTask(Device device) {
        this.device = device;
    }

    @Override
    public void run() {
        boolean ping = Ip.ping(device.getIp(), 120000, 1000);
        if (ping) {
            Driver driver = DriverManager.getInstance().getDriver(device);
            Serializable content;

            try {
                Set<Interface> interfaces = driver.getInterfaces();
                DeviceInterfacesResponse response = new DeviceInterfacesResponse();
                response.setInterfaces(interfaces);
                content = response;
            } catch (SnmpConnectorException e) {
                content = e;
            } catch (Exception e) {
                content = e;
            }

            sendMessage(content);
        } else {
            // TODO unreachable
        }
    }
}