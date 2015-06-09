package com.gdc.nms.testing.server.task;

import java.io.Serializable;
import java.util.List;

import com.gdc.nms.common.Ip;
import com.gdc.nms.model.Device;
import com.gdc.nms.model.DeviceResource;
import com.gdc.nms.server.drivers.snmp.Driver;
import com.gdc.nms.server.drivers.snmp.DriverManager;
import com.gdc.nms.server.drivers.snmp.SnmpConnectorException;
import com.gdc.nms.testing.response.DeviceResourcesResponse;

public class GetDeviceResourcesTask extends Task {

    private Device device;

    public GetDeviceResourcesTask(Device device) {
        this.device = device;
    }

    @Override
    public void run() {
        boolean ping = Ip.ping(device.getIp(), 120000, 1000);
        if (ping) {
            Driver driver = DriverManager.getInstance().getDriver(device);
            Serializable content;
            try {
                List<DeviceResource> deviceResources = driver.getDeviceResources();

                content = new DeviceResourcesResponse();
                ((DeviceResourcesResponse) content).setDeviceResources(deviceResources);

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
