package net.dmytrobashynskiy.devices;


import net.dmytrobashynskiy.cables.cable_components.Pair;
import net.dmytrobashynskiy.devices.device_utils.Device;
import net.dmytrobashynskiy.devices.input_output.IO;
import net.dmytrobashynskiy.devices.input_output.Input;
import net.dmytrobashynskiy.devices.input_output.Output;
import net.dmytrobashynskiy.devices.device_utils.IO_count;
import net.dmytrobashynskiy.utils.Location;
import net.dmytrobashynskiy.utils.RandomNumberMaker;

import java.util.*;


public class Terminal extends Device {

    /**
     * Terminal (term) - An object with fixed coordinates that manually or automatically switches pairs between the input
     * of a terminal and the output of a terminal. Each terminal contains 10, 50 or 300 Inputs / Outputs, which can be
     * switched manually or automatically. The terminal that has a connected cable only at the input is Service Location
     * and is used to connect the service to the end consumer.
     */
    public Terminal(Location deviceLocation) {
        this(deviceLocation, IO_count.IO20);
    }

    public Terminal(Location deviceLocation, IO_count someCount) {
        this.deviceLocation = deviceLocation;
        connectedDevices = new HashSet<>();
        connectedCables = new ArrayList<>();
        inputs = new ArrayList<>();
        outputs = new ArrayList<>();
        for(int i=0;i<someCount.halfIoCount;i++) {
            IO input = new Input();
            input.setParentDevice(this);
            inputs.add(input);
            IO output = new Output();
            output.setParentDevice(this);
            outputs.add(output);
        }
        currentIOcount = someCount;
        serialNumber = RandomNumberMaker.getInstance().generateNumber();
    }
    @Override
    public boolean isServiceLocation(){
        return outputs.stream().allMatch(element -> element.getConnectedPair()==null);
    }

    @Override
    public String getName() {
        if(isServiceLocation()){
            return String.format("Service Terminal: SN-%d",serialNumber);
        }else return String.format("Terminal: SN-%d",serialNumber);
    }
    //These pair switch methods were implemented as per Terminal device description.
    //I couldn't find a use for these methods, though.
    //Methods switch some pair from Output IO to Input IO and vice versa.

    public boolean softSwitchPair(Pair pair){
        if(pair.getServiceType()==null){
            return switchPair(pair);
        }else return false;
    }

    public boolean forceSwitchPair(Pair pair){
        return switchPair(pair);
    }

    private boolean switchPair(Pair pair){
        if(pair!=null){
            //this looks if specified pair is present in either input or output IOs
            Optional<IO> in_match = inputs.stream().filter(x -> x.getConnectedPair().equals(pair)).findFirst();
            Optional<IO> out_match = outputs.stream().filter(x -> x.getConnectedPair().equals(pair)).findFirst();
            //the pair is in the inputs section
            if(in_match.isPresent()){
                IO focusInput = in_match.get();
                Pair focusPair = focusInput.getConnectedPair();
                //this looks if there are free outputs
                Optional<IO> freeOutput = outputs.stream().filter(x -> x.getConnectedPair()==null).findFirst();
                if(freeOutput.isPresent()){
                    //free output
                    IO output = freeOutput.get();
                    //redefine the connected child IO pointer
                    if(focusPair.getConnectedChild().equals(focusInput)){
                        focusPair.setConnectedChild(output);
                    }else focusPair.setConnectedParent(output);
                    //disconnect the pair from the input it is currently connected to
                    focusInput.disconnect();
                    //connect it to the output
                    output.connectPair(focusPair);
                    //tell pair what it is connected to
                    focusPair.setConnectedParent(output);
                    return true;
                }else return false;
            //the pair is in the outputs section
            }else if(out_match.isPresent()){
                IO focusOutput = in_match.get();
                Pair focusPair = focusOutput.getConnectedPair();
                //this looks if there are free inputs
                Optional<IO> freeInput = inputs.stream().filter(x -> x.getConnectedPair()==null).findFirst();
                if(freeInput.isPresent()){
                    //free output
                    IO input = freeInput.get();
                    //redefine the connected child IO pointer
                    if(focusPair.getConnectedChild().equals(focusOutput)){
                        focusPair.setConnectedChild(input);
                    }else focusPair.setConnectedParent(input);
                    //disconnect the pair from the output it is currently connected to
                    focusOutput.disconnect();
                    //connect it to the input
                    input.connectPair(focusPair);
                    //tell pair what it is connected to
                    focusPair.setConnectedParent(input);
                    return true;
                }else return false;
            //this last return false comes back if the pair is not connected to any of these IOs
            }else return false;
        }
        return false;
    }
}
