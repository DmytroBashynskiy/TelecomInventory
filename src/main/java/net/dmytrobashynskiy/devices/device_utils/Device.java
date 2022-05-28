package net.dmytrobashynskiy.devices.device_utils;


import net.dmytrobashynskiy.cables.Cable;
import net.dmytrobashynskiy.devices.input_output.IO;
import net.dmytrobashynskiy.devices.input_output.Input;
import net.dmytrobashynskiy.devices.input_output.Output;
import net.dmytrobashynskiy.utils.Location;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class Device {
    protected int serialNumber;
    protected Location deviceLocation;
    protected IO_count currentIOcount;
    protected List<IO> inputs;
    protected List<IO> outputs;
    protected List<Cable> connectedCables;
    protected Set<Device> connectedDevices;


    public List<Cable> getConnectedCables() {
        return connectedCables;
    }

    public int getSerialNumber() {
        return serialNumber;
    }

    public Location getDeviceLocation() {
        return deviceLocation;
    }

    public List<IO> getInputs() {
        return inputs;
    }

    public List<IO> getOutputs() {
        return outputs;
    }

    public void setDeviceLocation(Location deviceLocation) {
        this.deviceLocation = deviceLocation;
    }

    public int getCurrentIOcount() {
        return currentIOcount.fullIoCount;
    }

    public boolean addMoreIOpoints() {
        switch (currentIOcount) {
            case IO20:
                for (int i = 0; i < 40; i++) {
                    IO input = new Input();
                    input.setParentDevice(this);
                    inputs.add(input);
                    IO output = new Output();
                    output.setParentDevice(this);
                    outputs.add(output);
                }
                currentIOcount = IO_count.IO100;
                break;
            case IO100:
                for (int i = 0; i < 250; i++) {
                    IO input = new Input();
                    input.setParentDevice(this);
                    inputs.add(input);
                    IO output = new Output();
                    outputs.add(output);
                    output.setParentDevice(this);
                }
                currentIOcount = IO_count.IO600;
                break;
            case IO600:
            case IO600_WIRECENTER:
                return false;
        }
        return true;
    }

    public List<Device> listDeviceNetwork() {
        Set<Device> deviceSet = new HashSet<>();
        deviceSet = listDeviceNetwork(this, deviceSet);
        return new ArrayList<>(deviceSet);
    }

    public List<Device> listConnectedDevices() {
        return new ArrayList<>(getConnectedDevices());
    }

    public Set<Device> getConnectedDevices() {
        Set<Device> deviceSet = listDeviceNetwork(this, new HashSet<Device>());
        deviceSet.remove(this);
        return deviceSet;
    }

    private Set<Device> listDeviceNetwork(Device startDevice, Set<Device> devices) {
        if(startDevice!=null){
            devices.add(startDevice);
            for(Cable cable: startDevice.getConnectedCables()){
                if(cable.getChildDevice().equals(startDevice)){
                    devices.add(cable.getParentDevice());
                    continue;
                }
                if(cable.getChildDevice().getConnectedCables().isEmpty()){
                    //do nothing
                }
                else listDeviceNetwork(cable.getChildDevice(),devices);
            }
        }
        return devices;
    }

    public abstract boolean isServiceLocation();

    public abstract String getName();

    @Override
    public String toString() {
        return getName();
    }
}
