package net.dmytrobashynskiy.devices;


import net.dmytrobashynskiy.cables.Cable;
import net.dmytrobashynskiy.cables.cable_components.ServiceType;
import net.dmytrobashynskiy.devices.device_utils.Device;
import net.dmytrobashynskiy.devices.device_utils.ServiceLoopMemory;
import net.dmytrobashynskiy.devices.device_utils.ServiceProvider;
import net.dmytrobashynskiy.devices.input_output.IO;
import net.dmytrobashynskiy.devices.input_output.Output;
import net.dmytrobashynskiy.devices.device_utils.IO_count;
import net.dmytrobashynskiy.utils.Location;
import net.dmytrobashynskiy.utils.RandomNumberMaker;

import java.util.*;

public class Wirecenter extends Device {
    private ServiceProvider serviceProvider;
    private List<ServiceLoopMemory> loops;
    /**
     * Wirecenter (wc) - An object with fixed coordinates (you can use geographic coordinates (latitude, longitude)
     * or conditional coordinates). Physically, it is the starting point for the provision of services.
     * The services are telephone line connection and DSL (Digital Subscriber line) dedicated line connection
     */
    public Wirecenter(Location deviceLocation) {
        this.deviceLocation = deviceLocation;
        connectedCables = new ArrayList<>();
        connectedDevices = new HashSet<>();
        outputs = new ArrayList<>();
        for(int i=0;i<600;i++) {
            IO output = new Output();
            output.setParentDevice(this);
            outputs.add(output);
        }
        currentIOcount = IO_count.IO600_WIRECENTER;
        loops = new ArrayList<>();
        serialNumber = RandomNumberMaker.getInstance().generateNumber();
    }

    public List<ServiceLoopMemory> getLoops() {
        return loops;
    }

    public boolean isDeviceOnNetwork(int serialNumber){
        if(getDevice(serialNumber)!=null){
            return true;
        }else return false;
    }

    public Device getDevice(int serialNumber){
        List<Device> devices = listConnectedDevices();
        Device device = null;
        Optional<Device> deviceInQuestion = devices.stream().filter(x -> x.getSerialNumber()==serialNumber).findFirst();
        if(deviceInQuestion.isPresent()){
            device = deviceInQuestion.get();
            return device;
        }
        return device;
    }

    public String listAllCurrentLoops(){
        StringBuilder stringBuilder = new StringBuilder();
        if(loops.size()!=0){
            for(int i=0;i<loops.size();i++){
                int y = i+1;
                stringBuilder.append("Service loop #").append(y).
                        append(".\n").append("Provides ").
                        append(loops.get(i).getServiceType().serviceName).
                        append(" service to device ").
                        append(loops.get(i).getServiceTargetDevice().toString()).
                        append("\n");
            }
        }
        return stringBuilder.toString();
    }

    public Cable getCable(String cableName){
        List<Cable> cables = this.getCablesList();
        Cable returnCable = null;
        for(Cable cable: cables){
            if(cable.getCableName().equals(cableName)){
                returnCable = cable;
                break;
            }
        }
        return returnCable;
    }

    public List<Cable> getCablesList(){
        Set<Cable> cablesSet = new HashSet<>();
        List<Device> devices = listConnectedDevices();
        for(Device device: devices){
            List<Cable> deviceCables = device.getConnectedCables();
            for(Cable cable: deviceCables){
                cablesSet.add(cable);
            }
        }
        return new ArrayList<>(cablesSet);
    }

    public List<Device> getDeviceToWirecenterChain(Device startDevice){
        List<Device> chain = new ArrayList<>();
        if(startDevice!=null){
            chain.add(startDevice);
            if(startDevice instanceof Wirecenter)return chain;
            boolean isFirst =false;
            for(Cable cable: startDevice.getConnectedCables()){
                if(isFirst)break;
                if(cable.getChildDevice().equals(startDevice)){
                    chain.addAll(getDeviceToWirecenterChain(cable.getParentDevice()));
                    isFirst = true;
                }
            }
        }
        return chain;
    }

    public boolean terminateServiceLoop(ServiceLoopMemory loop){
        if(this.serviceProvider==null){
            this.serviceProvider = new ServiceProvider(this);
        }
        if(loop!=null){
            return serviceProvider.terminateServiceLoop(loop);
        }else return false;


    }

    public boolean provideServiceTo(Device device, ServiceType serviceType){
        if(this.serviceProvider==null){
            this.serviceProvider = new ServiceProvider(this);
        }
        if(device!=null){
            if(isDeviceOnNetwork(device.getSerialNumber())){
                ServiceLoopMemory loopMemory = serviceProvider.createServiceLoop(device, serviceType);
                if (loopMemory!=null){
                    if(loopMemory.doesExist()){
                        loops.add(loopMemory);
                        return true;
                    }else{
                        System.out.println("Unable to provide <"+serviceType.serviceName+"> connection to <"+device+"> due to \n" +
                                " bad cable "+loopMemory.getCableThatCausedFailure()+". It should be replaced.");
                        return false;
                    }
                }
            }
        }
        return false;
    }



    @Override
    public String getName() {
        return String.format("Wirecenter: SN-%d",serialNumber);
    }

    @Override
    public boolean isServiceLocation() {
        return false;
    }
}
