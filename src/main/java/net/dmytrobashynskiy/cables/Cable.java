package net.dmytrobashynskiy.cables;


import net.dmytrobashynskiy.cables.cable_components.Pair;
import net.dmytrobashynskiy.devices.device_utils.Device;

import java.util.List;

public abstract class Cable {
    /**
     *  Cable (ca) - An object communicating objects with fixed coordinates (wc, term).
     *  Provides communication (wc \ term) - (term). Consists of a set of pairs. One cable can contain 20, 100 or 500 pairs
     *
     */
    protected String cableName;
    private Device parentDevice;
    private Device childDevice;
    //list for storing the Pairs
    protected List<Pair> pairs;

    public Cable(){}

    public String getCableName() {
        return cableName;
    }

    public List<Pair> getPairs(){
        return pairs;
    }

    public Device getParentDevice() {
        return parentDevice;
    }

    public void setParentDevice(Device parentDevice) {
        this.parentDevice = parentDevice;
    }

    public Device getChildDevice() {
        return childDevice;
    }

    public void setChildDevice(Device childDevice) {
        this.childDevice = childDevice;
    }

    @Override
    public String toString() {
        return cableName;
    }
}
