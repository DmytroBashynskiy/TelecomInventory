package net.dmytrobashynskiy.utils;

import net.dmytrobashynskiy.Inventory;
import net.dmytrobashynskiy.cables.*;
import net.dmytrobashynskiy.cables.cable_components.Pair;
import net.dmytrobashynskiy.cables.cable_components.ServiceType;
import net.dmytrobashynskiy.devices.device_utils.Device;
import net.dmytrobashynskiy.devices.Wirecenter;
import net.dmytrobashynskiy.devices.input_output.IO;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CableHandling {
    public static boolean connectDevices(Device parent, Device child){
        if(parent==null || child==null){
            return false;
        }else if(areConnected(parent,child)){
            return false;
        }


        //these two stream-made lists are formed from free IO's of child's and parent's
        List<IO> childFreeInputs = findFreeInputs(child);
        List<IO> parentFreeOutputs = findFreeOutputs(parent);
        //this if-else block detects if there is need to add more IO points
        // to the current devices to connect a cable
        if(parentFreeOutputs.isEmpty()){
            if(parent.addMoreIOpoints())
            connectDevices(parent,child);
        }else if(childFreeInputs.isEmpty()){
            if(child.addMoreIOpoints())
            connectDevices(parent,child);
        }
        if(parentFreeOutputs.size()<=10){
            Cable cable = new Cable20();
            return connectPairs(childFreeInputs,parentFreeOutputs, cable, parent,child);
        }else if(parentFreeOutputs.size()<=50){
            Cable cable = new Cable100();
            return connectPairs(childFreeInputs,parentFreeOutputs, cable, parent,child);
        }else if(parentFreeOutputs.size()<=300){
            Cable cable = new Cable500();
            return connectPairs(childFreeInputs,parentFreeOutputs, cable, parent,child);
        }else if(parentFreeOutputs.size()>300){
            Cable cable = new Cable100();
            return connectPairs(childFreeInputs,parentFreeOutputs, cable, parent,child);
        }
        else return false;
    }
    public static boolean replaceCable(Device parent, Device child, Cable newCable){
        Cable oldCable;
        if(areConnected(parent,child)){
            oldCable = getConnectingCable(parent,child);
        }else return false;
        List<Pair> oldCablePairs = oldCable.getPairs();
        List<Pair> newCablePairs = newCable.getPairs();
        if(oldCablePairs.size()>newCablePairs.size())return false;
        List<IO> parentConnectedOutputs = parent.getOutputs().stream().
                filter(io -> oldCablePairs.contains(io.getConnectedPair())).collect(Collectors.toList());
        List<IO> childConnectedInputs =  child.getInputs().stream().
                filter(io -> oldCablePairs.contains(io.getConnectedPair())).collect(Collectors.toList());
        Iterator<Pair> newPairs = newCablePairs.iterator();
        Iterator<IO> childInputs = childConnectedInputs.iterator();
        IO focusInput;
        Pair focusNewPair;
        for(IO parentOutput: parentConnectedOutputs){
            focusInput = childInputs.next();
            Pair oldPair = parentOutput.getConnectedPair();
            //if oldPair was providing a service we need to replace it with appropiate new pair and set up the service there too
            if(oldPair.getServiceType()!=null){
                if (oldPair.getServiceType().equals(ServiceType.PHONE)){
                    Optional<Pair> optionalPair = newCablePairs.stream().filter(pair -> pair.compatCheck(ServiceType.PHONE)).findAny();
                    if(optionalPair.isPresent()){
                        oldPair.setConnectedChild(null);
                        oldPair.setConnectedParent(null);
                        oldPair.setServiceType(null);
                        focusNewPair = optionalPair.get();
                        parentOutput.disconnect();
                        focusInput.disconnect();
                        parentOutput.connectPair(focusNewPair);
                        focusNewPair.setConnectedParent(parentOutput);
                        focusInput.connectPair(focusNewPair);
                        focusNewPair.setConnectedChild(focusInput);
                        focusNewPair.setServiceType(ServiceType.PHONE);
                    }else return false;
                }else{//DSL
                    Optional<Pair> optionalPair = newCablePairs.stream().filter(pair -> pair.compatCheck(ServiceType.DSL)).findAny();
                    if(optionalPair.isPresent()){
                        oldPair.setConnectedChild(null);
                        oldPair.setConnectedParent(null);
                        oldPair.setServiceType(null);
                        focusNewPair = optionalPair.get();
                        parentOutput.disconnect();
                        focusInput.disconnect();
                        parentOutput.connectPair(focusNewPair);
                        focusNewPair.setConnectedParent(parentOutput);
                        focusInput.connectPair(focusNewPair);
                        focusNewPair.setConnectedChild(focusInput);
                        focusNewPair.setServiceType(ServiceType.DSL);
                    }else return false;
                }
            }else{//get any good avalaible pair and set it
                Optional<Pair> optionalPair = newCablePairs.stream().filter(pair -> pair.getState()>1 && pair.getServiceType()==null).findAny();
                if(optionalPair.isPresent()){
                    oldPair.setConnectedChild(null);
                    oldPair.setConnectedParent(null);
                    oldPair.setServiceType(null);
                    focusNewPair = optionalPair.get();
                    parentOutput.disconnect();
                    focusInput.disconnect();
                    parentOutput.connectPair(focusNewPair);
                    focusNewPair.setConnectedParent(parentOutput);
                    focusInput.connectPair(focusNewPair);
                    focusNewPair.setConnectedChild(focusInput);
                }else return false;
            }
        }
        //replace the old cable links with new one
        int oldCablePositionParent = parent.getConnectedCables().indexOf(oldCable);
        int oldCablePositionChild = child.getConnectedCables().indexOf(oldCable);
        parent.getConnectedCables().set(oldCablePositionParent, newCable);
        oldCable.setParentDevice(null);
        newCable.setParentDevice(parent);
        child.getConnectedCables().set(oldCablePositionChild , newCable);
        newCable.setChildDevice(child);
        oldCable.setChildDevice(null);
        return true;
    }

    public static Cable getConnectingCable (Device parent, Device child){
        Cable focusCable = null;
        for(Cable cable:parent.getConnectedCables()){
            if(cable.getChildDevice().equals(child)){
                focusCable = cable;
            }
        }
        return focusCable;
    }

    private static boolean connectPairs(List<IO> childFreeInputs, List<IO> parentFreeOutputs, Cable cable,
                                        Device parent, Device child){
        if(parentFreeOutputs.isEmpty()||childFreeInputs.isEmpty())
            return false;
        if(parentFreeOutputs.size()>childFreeInputs.size()){
            List<Pair> pairs = cable.getPairs();
            Optional<Pair> phoneCapablePairOpt = pairs.stream().
                    filter(pair -> pair.compatCheck(ServiceType.PHONE)&&pair.getServiceType()==null).findAny();
            Optional<Pair> dslCapablePairOpt = pairs.stream().
                    filter(pair -> pair.compatCheck(ServiceType.DSL)&&pair.getServiceType()==null).findAny();
            Pair phoneCapablePair;
            Pair dslCapablePair;
            if(phoneCapablePairOpt.isPresent()){
                phoneCapablePair = phoneCapablePairOpt.get();
                IO freeParent = parentFreeOutputs.stream().findAny().get();
                IO freeChild = childFreeInputs.stream().findAny().get();
                freeParent.connectPair(phoneCapablePair);
                freeChild.connectPair(phoneCapablePair);
                phoneCapablePair.setConnectedParent(freeParent);
                phoneCapablePair.setConnectedChild(freeChild);
                parentFreeOutputs.remove(freeParent);
                childFreeInputs.remove(freeChild);
            }
            if(dslCapablePairOpt.isPresent()){
                dslCapablePair = dslCapablePairOpt.get();
                Optional<IO> freeChildIO_Opt = childFreeInputs.stream().findAny();
                if(freeChildIO_Opt.isPresent()){
                    IO freeParent = parentFreeOutputs.stream().findAny().get();
                    IO freeChild = freeChildIO_Opt.get();
                    freeParent.connectPair(dslCapablePair);
                    freeChild.connectPair(dslCapablePair);
                    dslCapablePair.setConnectedParent(freeParent);
                    dslCapablePair.setConnectedChild(freeChild);
                    parentFreeOutputs.remove(freeParent);
                    childFreeInputs.remove(freeChild);
                }
            }
        }else {
            //mirror situation
            List<Pair> pairs = cable.getPairs();
            Optional<Pair> phoneCapablePairOpt = pairs.stream().
                    filter(pair -> pair.compatCheck(ServiceType.PHONE)&&pair.getServiceType()==null).findAny();
            Optional<Pair> dslCapablePairOpt = pairs.stream().
                    filter(pair -> pair.compatCheck(ServiceType.DSL)&&pair.getServiceType()==null).findAny();
            Pair phoneCapablePair;
            Pair dslCapablePair;
            if(phoneCapablePairOpt.isPresent()){                ;
                phoneCapablePair = phoneCapablePairOpt.get();
                IO freeParent = parentFreeOutputs.stream().findAny().get();
                IO freeChild = childFreeInputs.stream().findAny().get();
                freeParent.connectPair(phoneCapablePair);
                freeChild.connectPair(phoneCapablePair);
                phoneCapablePair.setConnectedParent(freeParent);
                phoneCapablePair.setConnectedChild(freeChild);
                parentFreeOutputs.remove(freeParent);
                childFreeInputs.remove(freeChild);
            }
            if(dslCapablePairOpt.isPresent()){
                dslCapablePair = dslCapablePairOpt.get();
                Optional<IO> freeParentIO_Opt = parentFreeOutputs.stream().findAny();
                if(freeParentIO_Opt.isPresent()){
                    IO freeChild = childFreeInputs.stream().findAny().get();
                    IO freeParent = freeParentIO_Opt.get();
                    freeParent.connectPair(dslCapablePair);
                    freeChild.connectPair(dslCapablePair);
                    dslCapablePair.setConnectedParent(freeParent);
                    dslCapablePair.setConnectedChild(freeChild);
                    parentFreeOutputs.remove(freeParent);
                    childFreeInputs.remove(freeChild);
                }
            }
        }
        cable.setParentDevice(parent);
        parent.getConnectedCables().add(cable);
        cable.setChildDevice(child);
        child.getConnectedCables().add(cable);
        //parent.getConnectedDevices().add(child);
        //child.getConnectedDevices().add(parent);
        return true;
    }

    public static boolean areConnected(Device parent, Device child){
        List<Cable> parentCables = parent.getConnectedCables();
        return parentCables.stream().anyMatch(cable -> cable.getChildDevice().equals(child));
    }

    public static boolean havePairWithService(Device parent, Device child, ServiceType serviceType){
        if(areConnected(parent,child)){
            Cable focusCable = getConnectingCable(parent,child);
            List<IO> parentOccupiedOutputs = parent.getOutputs().stream().
                    //this stream looks at the outputs list of parent, finds what output have connected pairs, and returns
                    // a list of connected pairs, that only belong to the connecting cable
                            filter(x -> !(x.getConnectedPair()==null)).
                            filter(x -> x.getConnectedPair().getParentCable().equals(focusCable)).collect(Collectors.toList());

            List<Pair> pairsInOutput = new ArrayList<>();

            for(IO parentOutput: parentOccupiedOutputs){
                pairsInOutput.add(parentOutput.getConnectedPair());
            }

            List<IO> connectedInputs = child.getInputs().stream().
                    //this stream looks at the inputs list of child, finds what input have connected pairs, and returns
                    // a list of connected pairs, that only belong to the connecting cable
                            filter(x -> !(x.getConnectedPair()==null)).
                            filter(x -> x.getConnectedPair().getParentCable().equals(focusCable)).collect(Collectors.toList());

            List<Pair> pairsInInput = new ArrayList<>();

            for(IO childInput: connectedInputs){
                pairsInInput.add(childInput.getConnectedPair());
            }
            //pairs that are in both lists are the pairs that connect the two devices
            List<Pair> lookForViablePair = pairsInOutput.stream().
                    filter(pairsInInput::contains).collect(Collectors.toList());

            for(Pair pair: lookForViablePair){
                if(pair.getServiceType()!=null && pair.getServiceType().equals(serviceType)){
                    return true;
                }
            }
        }


        return false;
    }

    private static List<IO> findFreeInputs(Device someDevice){
        return someDevice.getInputs().stream()
                .filter(x -> x.getConnectedPair()==null).collect(Collectors.toList());
    }
    private static List<IO> findFreeOutputs(Device someDevice){
        return someDevice.getOutputs().stream()
                .filter(x -> x.getConnectedPair()==null).collect(Collectors.toList());
    }

    public static Cable getCableByName (Inventory inventory, String cableName){
        Cable cable = null;
        for(Wirecenter wirecenter:inventory.getWirecenters()){
            cable = wirecenter.getCable(cableName);
            if(cable!=null)break;
        }
        return cable;
    }
}
