package com.gdc.nms.testing.response;

import java.util.Set;

import com.gdc.nms.model.Interface;

public class DeviceInterfacesResponse implements Response {
    private Set<Interface> interfaces;

    public Set<Interface> getInterfaces() {
        return interfaces;
    }

    public void setInterfaces(Set<Interface> interfaces) {
        this.interfaces = interfaces;
    }

}
