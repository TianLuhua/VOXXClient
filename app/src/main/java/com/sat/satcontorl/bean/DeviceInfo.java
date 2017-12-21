package com.sat.satcontorl.bean;

public class DeviceInfo {

    private String ipAddress;
    private String name;

    public DeviceInfo(String ipAddress, String name) {
        super();
        this.ipAddress = ipAddress;
        this.name = name;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @Override
    public String toString() {
        return "DeviceInfo{" +
                "ipAddress='" + ipAddress + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
