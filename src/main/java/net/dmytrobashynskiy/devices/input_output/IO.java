package net.dmytrobashynskiy.devices.input_output;

import net.dmytrobashynskiy.cables.cable_components.Pair;
import net.dmytrobashynskiy.devices.device_utils.Device;

public abstract class IO {
    private Device parentDevice;
    private Pair connectedPair;

    public Device getParentDevice() {
        return parentDevice;
    }

    public void setParentDevice(Device parentDevice) {
        this.parentDevice = parentDevice;
    }

    public Pair getConnectedPair() {
        return connectedPair;
    }

    public void connectPair(Pair connectedPair) {
        this.connectedPair = connectedPair;
    }

    public void disconnect() {
        this.connectedPair = null;
    }
}
