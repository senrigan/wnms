package com.gdc.nms.testing.server.task;

import com.gdc.nms.common.Ip;
import com.gdc.nms.model.Device;
import com.gdc.nms.server.drivers.snmp.Driver;
import com.gdc.nms.server.drivers.snmp.DriverManager;
import com.gdc.nms.testing.response.DeviceDriverResponse;
import com.gdc.nms.testing.response.ErrorResponse;

public class GetDriverClassTask extends Task {

    private Device device;

    public GetDriverClassTask(Device device) {
        this.device = device;
    }

    @Override
    public void run() {
        try {
            if (device == null) {
                sendMessage(new ErrorResponse("Device must not be null"));
            } else {

                boolean ping = Ip.ping(device.getIp(), 120000, 1000);

                if (ping) {
                    System.out.println("[Server] Ping ok");
                    Driver driver = DriverManager.getInstance().getDriver(device);
                    DeviceDriverResponse response = new DeviceDriverResponse(driver.getClass().getName());
                    driver.unbind();
                    sendMessage(response);
                } else {
                    System.out.println("[Server] - device unreachable");
                }
            }
        } catch (Exception e) {
            sendMessage(e);
        }
    }
}
