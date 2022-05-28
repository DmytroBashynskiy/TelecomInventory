package net.dmytrobashynskiy.cables.cable_components;

import net.dmytrobashynskiy.cables.Cable;
import net.dmytrobashynskiy.devices.input_output.IO;

public class Pair {

    /**
     * Pair (pair) - Physically it is one twisted pair. Each pair has a physical parameter rz, which can range
     * from 0.5 to 2.5. Depending on this parameter, the state of the pair corresponds to the possibility of
     * using it for a telephone connection (1 - 2.5) or DSL (1.5 - 2.5). A pair for which rz <1 is considered
     * degraded and cannot be used to create a service
     */
    private ServiceType serviceType;
    private Cable parentCable;
    private IO connectedParent;
    private IO connectedChild;
    private float rz;

    public Pair(float rz, Cable parentCable) {
        this.parentCable = parentCable;
        this.rz = rz;
    }

    public Cable getParentCable() {
        return parentCable;
    }

    public IO getConnectedParent() {
        return connectedParent;
    }

    public void setConnectedParent(IO connectedParent) {
        this.connectedParent = connectedParent;
    }

    public IO getConnectedChild() {
        return connectedChild;
    }

    public void setConnectedChild(IO connectedChild) {
        this.connectedChild = connectedChild;
    }

    public float getState() {//gets RZ value
        return rz;
    }



    public ServiceType getServiceType() {
        return serviceType;
    }

    public boolean clearServiceType(){
        this.serviceType = null;
        return true;
    }

    public boolean setServiceType(ServiceType serviceType) {
        if(serviceType!=null){
            if(compatCheck(serviceType)){
                this.serviceType = serviceType;
                return true;
            }else return false;
        }else return clearServiceType();
    }
    //checks if the desired connection can be applied given the state of the pair
    public boolean compatCheck(ServiceType serviceType){
        if((rz >= 1.0f && rz <= 2.5f ) && (serviceType.equals(ServiceType.PHONE))){
            return true;
        }else if((rz >= 1.5f && rz <= 2.5f ) && (serviceType.equals(ServiceType.DSL))){
            return true;
        } else return false;

    }
}
