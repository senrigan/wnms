package com.gdc.nms.testing.response;

public class DeviceDriverResponse implements Response {

    private String className;

    public DeviceDriverResponse(String className) {
        this.className = className;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    @Override
    public String toString() {
        return getClassName();
    }
}
