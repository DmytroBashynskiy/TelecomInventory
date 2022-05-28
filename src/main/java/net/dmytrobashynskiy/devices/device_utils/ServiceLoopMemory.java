package net.dmytrobashynskiy.devices.device_utils;

import net.dmytrobashynskiy.cables.Cable;
import net.dmytrobashynskiy.cables.cable_components.Pair;
import net.dmytrobashynskiy.cables.cable_components.ServiceType;

import java.util.List;
import java.util.Map;

public class ServiceLoopMemory {
    private Device serviceTargetDevice;
    private List<Device> loop;
    private ServiceType serviceType;
    private Map<Device, Pair> devicePairMap;
    private boolean exists;
    private Cable cableThatCausedFailure;

    public ServiceLoopMemory(Device serviceTargetDevice, List<Device> loop,
                             ServiceType serviceType, Map<Device, Pair> devicePairMap, boolean exist, Cable cable) {
        this.serviceTargetDevice = serviceTargetDevice;
        this.loop = loop;
        this.serviceType = serviceType;
        this.devicePairMap = devicePairMap;
        this.exists = exist;
        this.cableThatCausedFailure = cable;
    }

    public Cable getCableThatCausedFailure() {
        return cableThatCausedFailure;
    }

    @Override
    public String toString() {
        return "ServiceLoopMemory{Target device: <"+serviceTargetDevice+">, service type: <"+serviceType+">}";
    }

    public boolean doesExist() {
        return exists;
    }

    public void setExist(boolean exists) {
        this.exists = exists;
    }

    public Map<Device, Pair> getDevicePairMap() {
        return devicePairMap;
    }

    public Device getServiceTargetDevice() {
        return serviceTargetDevice;
    }

    public List<Device> getLoop() {
        return loop;
    }

    public ServiceType getServiceType() {
        return serviceType;
    }

    public String textRepresent(){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Loop #"+this.hashCode()+" contains following devices: "+this.loop.toString()+"\n");
        stringBuilder.append("Service of <"+this.serviceType.serviceName
                +"> type is being provided to "+this.serviceTargetDevice.toString()+".");



        return stringBuilder.toString();
    }
}