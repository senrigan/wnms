package com.gdc.nms.testing.response;

import java.util.List;

import com.gdc.nms.model.DeviceResource;

public class DeviceResourcesResponse implements Response {
    private List<DeviceResource> resources;

    public void setDeviceResources(List<DeviceResource> resources) {
        this.resources = resources;
    }

    public List<DeviceResource> getDeviceResources() {
        return resources;
    }
}
