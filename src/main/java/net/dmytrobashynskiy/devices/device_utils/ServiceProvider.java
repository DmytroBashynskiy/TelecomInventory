package net.dmytrobashynskiy.devices.device_utils;

import net.dmytrobashynskiy.cables.Cable;
import net.dmytrobashynskiy.cables.cable_components.Pair;
import net.dmytrobashynskiy.cables.cable_components.ServiceType;
import net.dmytrobashynskiy.devices.Wirecenter;
import net.dmytrobashynskiy.devices.input_output.IO;
import net.dmytrobashynskiy.utils.CableHandling;

import java.util.*;
import java.util.stream.Collectors;

public class ServiceProvider {
    private Wirecenter wirecenter;

    public ServiceProvider(Wirecenter wirecenter) {
        this.wirecenter = wirecenter;
    }

    public Wirecenter getWirecenter() {
        return wirecenter;
    }


    public boolean terminateServiceLoop(ServiceLoopMemory loop){
        if(loop!=null){
            Map<Device, Pair> pairs = loop.getDevicePairMap();
            List<Device> devices = loop.getLoop();
            for(int i=0; i<devices.size()-1; i++){
                pairs.get(devices.get(i)).clearServiceType();
            }
            wirecenter.getLoops().get(wirecenter.getLoops().indexOf(loop)).setExist(false);
            return true;
        }else return false;
    }

    public ServiceLoopMemory createServiceLoop(Device receiverDevice, ServiceType type){

        List<Device> chain = wirecenter.getDeviceToWirecenterChain(receiverDevice);
        Collections.reverse(chain);
        Map<Device, Pair> devicePairMap = new HashMap<>();

        for(int i=0; i<chain.size()-1;i++){
            Device parentDevice = chain.get(i);
            Device childDevice = chain.get(i+1);
            //checking if devices are not connected just in case
            if(!CableHandling.areConnected(parentDevice,childDevice))return null;
            Cable theirCable = CableHandling.getConnectingCable(parentDevice,childDevice);

            List<IO> connectedOutputs = parentDevice.getOutputs().stream().
                    //this stream looks at the outputs list of parent, finds what output have connected pairs, and returns
                    // a list of connected pairs, that only belong to the connecting cable
                    filter(x -> x.getConnectedPair()!=null).
                            filter(x -> x.getConnectedPair().getParentCable().equals(theirCable)).collect(Collectors.toList());

            List<Pair> pairsInOutput = new ArrayList<>();

            for(IO parentOutput: connectedOutputs){
                pairsInOutput.add(parentOutput.getConnectedPair());
            }

            List<IO> connectedInputs = childDevice.getInputs().stream().
                    //this stream looks at the inputs list of child, finds what input have connected pairs, and returns
                    // a list of connected pairs, that only belong to the connecting cable
                            filter(x -> x.getConnectedPair()!=null).
                            filter(x -> x.getConnectedPair().getParentCable().equals(theirCable)).collect(Collectors.toList());

            List<Pair> pairsInInput = new ArrayList<>();

            for(IO childInput: connectedInputs){
                pairsInInput.add(childInput.getConnectedPair());
            }
            //pairs that are in both lists are the pairs that connect the two devices
            List<Pair> lookForEmptyPairList = pairsInOutput.stream().
                    filter(pairsInInput::contains).collect(Collectors.toList());
            //this next part finds one connected pair that does not provide any service but can provide the required service
            //if it finds it - it sets the service, otherwise reports false upstairs
            Optional<Pair> lookForEmptyPair = lookForEmptyPairList.stream().filter(pair -> pair.getServiceType()==null && pair.compatCheck(type)).findAny();
            //this looks for any empty pair that is connected. This pair will be then replaced with another from the cable.
            Optional<Pair> lookForAnyConnectedEmptyPair = lookForEmptyPairList.stream().filter(pair -> pair.getServiceType()==null).findAny();
            //this looks for any viable empty pair in the cable
            Optional<Pair> lookForViablePairInCable = theirCable.getPairs().stream().filter(pair -> pair.getServiceType()==null && pair.compatCheck(type)).findAny();
            if(lookForEmptyPair.isPresent()){
                    Pair emptyPair = lookForEmptyPair.get();
                    emptyPair.setServiceType(type);
                    devicePairMap.put(parentDevice, emptyPair);


            }else if(lookForAnyConnectedEmptyPair.isPresent()) {
                if (lookForViablePairInCable.isPresent()) {
                    Pair connectedTargetEmptyPair = lookForAnyConnectedEmptyPair.get();
                    Pair viablePairInCable = lookForViablePairInCable.get();

                    IO parentIO = connectedTargetEmptyPair.getConnectedParent();
                    IO childIO = connectedTargetEmptyPair.getConnectedChild();
                    //disconnect old pair
                    parentIO.disconnect();
                    childIO.disconnect();
                    //connect new pair
                    parentIO.connectPair(viablePairInCable);
                    childIO.connectPair(viablePairInCable);

                    viablePairInCable.setServiceType(type);
                    devicePairMap.put(parentDevice, viablePairInCable);
                }
                //this takes a viable pair from the cable, takes viable IOs from parent+child and connects everything
            }else if(lookForAnyConnectedEmptyPair.isEmpty()){
                    if(lookForViablePairInCable.isPresent()){
                        Pair viablePairInCable = lookForViablePairInCable.get();
                        Optional<IO> findParentIO = parentDevice.getOutputs().stream().
                                filter(io -> io.getConnectedPair()==null).findAny();
                        Optional<IO> findChildIO = childDevice.getInputs().stream().
                                filter(io -> io.getConnectedPair()==null).findAny();
                        if(findParentIO.isPresent() && findChildIO.isPresent()){
                            IO parentIO = findParentIO.get();
                            IO childIO = findChildIO.get();

                            parentIO.connectPair(viablePairInCable);
                            childIO.connectPair(viablePairInCable);

                            viablePairInCable.setServiceType(type);
                            devicePairMap.put(parentDevice, viablePairInCable);
                        }

                    }
                }

            else return new ServiceLoopMemory(null,null,null,null,false, theirCable);

        }

        return new ServiceLoopMemory(receiverDevice,chain,type,devicePairMap,true, null);
    }

}
