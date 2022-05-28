package net.dmytrobashynskiy.cables.cable_components;

public enum ServiceType {

    PHONE("<Phone>"), DSL("<DSL>");
    public final String serviceName;
    private ServiceType(String serviceName){
        this.serviceName = serviceName;
    }
}
