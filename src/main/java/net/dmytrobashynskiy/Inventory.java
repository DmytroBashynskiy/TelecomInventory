package net.dmytrobashynskiy;

import net.dmytrobashynskiy.cables.Cable;
import net.dmytrobashynskiy.cables.cable_components.Pair;
import net.dmytrobashynskiy.cables.cable_components.ServiceType;
import net.dmytrobashynskiy.devices.device_utils.Device;
import net.dmytrobashynskiy.devices.Terminal;
import net.dmytrobashynskiy.devices.Wirecenter;
import net.dmytrobashynskiy.devices.device_utils.ServiceLoopMemory;
import net.dmytrobashynskiy.menu.MenuThing;
import net.dmytrobashynskiy.utils.CableHandling;
import net.dmytrobashynskiy.utils.LocationMaker;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Inventory {
    private boolean predef;
    private List<Wirecenter> wirecenters;
    private MenuThing menuThing;

    public List<Wirecenter> getWirecenters() {
        return wirecenters;
    }

    /**
     * The structure that holds all wirecenters, which in turn each hold their respective networks of devices.
     */

    public Inventory() {
        predef = false;
        wirecenters = new ArrayList<>();
        menuThing = new MenuThing();
    }

    public void run() {
        menuThing.menu(this);
    }

    public void printInventoryManifest() {
        if (wirecenters.isEmpty()) {
            System.out.println("There are no devices in the inventory.");
        } else {
            for (Wirecenter wirecenter : wirecenters) {
                List<Device> devices = wirecenter.listConnectedDevices();
                List<Cable> cables = wirecenter.getCablesList();
                if (devices.isEmpty()) {
                    System.out.println(wirecenter + " does not have any devices connected to it.");
                } else {
                    System.out.println(wirecenter + " is a root to following network:\n"
                            + devices + "\n");
                    System.out.println("This network has following cables in itself:\n");
                    System.out.println(cables + "\n");
                }
                if (cables.isEmpty()) {
                    System.out.println("This wirecenter's network doesn't have any cables in itself.");
                }
                printOneWcLoops(wirecenter);
            }
        }
    }

    public void getPairsInfo(Cable cable) {
        List<Pair> cablePairs = cable.getPairs();
        int degradedPairs = (int) cablePairs.stream().filter(pair -> pair.getState() < 1f).count();
        int phoneCapablePairs = (int) cablePairs.stream().filter(pair -> pair.compatCheck(ServiceType.PHONE)).count();
        int dslCapablePairs = (int) cablePairs.stream().filter(pair -> pair.compatCheck(ServiceType.DSL)).count();
        List<Pair> pairsThatProvideSomething = cablePairs.stream().filter(pair -> pair.getServiceType() != null).collect(Collectors.toList());
        int phoneProviderPairs = (int) pairsThatProvideSomething.stream().filter(pair -> pair.getServiceType().
                equals(ServiceType.PHONE)).count();
        int dslProviderPairs = (int) pairsThatProvideSomething.stream().filter(pair -> pair.getServiceType().
                equals(ServiceType.DSL)).count();
        String str1 = String.format("%s pairs are phone capable and %s actually provide phone service.", phoneCapablePairs, phoneProviderPairs);
        String str2 = String.format("%s pairs are DSL capable and %s actually provide DSL service.", dslCapablePairs, dslProviderPairs);
        String str3 = String.format("%s pairs are degraded and cannot provide any service.", degradedPairs);
        System.out.println(cable + " has " + cablePairs.size() + " pairs. Of which: ");
        System.out.println(str1);
        System.out.println(str2);
        System.out.println(str3);
    }

    public void getDeviceInfo(int serialNumber) {
        Device device = getDevice(serialNumber);
        if (device != null) {
            int inputs;
            System.out.println(device + " is located at " + device.getDeviceLocation());
            if (device.getInputs() == null) {
                inputs = 0;
                System.out.println("It has " + inputs + " inputs, since it is a wirecenter, but it has "
                        + device.getOutputs().size() + " outputs,  of which " +
                        device.getOutputs().stream().filter(io -> io.getConnectedPair() != null).count() + " are occupied by cable pairs.");
            } else {
                inputs = device.getInputs().size();
                System.out.println("It has " + inputs + " inputs, of which " + device.
                        getInputs().stream().filter(io -> io.getConnectedPair() != null).count() + " are occupied by cable pairs,\n" +
                        " and " + device.getOutputs().size() + " outputs,  of which " +
                        device.getOutputs().stream().filter(io -> io.getConnectedPair() != null).count() + " are occupied by cable pairs.");
            }
            List<Device> deviceList = device.listConnectedDevices();
            if (deviceList.isEmpty()) {
                System.out.println(device + " does not have any devices connected to it.");
            } else {
                System.out.println(device + " is connected to following devices: \n" + deviceList);
            }

        }
    }

    public boolean replaceCable(String cableName, Cable newCable) {
        Cable oldCable = null;
        for (Wirecenter wirecenter : wirecenters) {
            oldCable = wirecenter.getCable(cableName);
            if (oldCable != null) break;

        }
        if (oldCable != null) {
            Device parent = oldCable.getParentDevice();
            Device child = oldCable.getChildDevice();
            return CableHandling.replaceCable(parent, child, newCable);
        } else return false;
    }

    public Device getDevice(int serialNumber) {
        Device device = null;
        for (Wirecenter wirecenter : wirecenters) {
            if (wirecenter.getSerialNumber() == serialNumber) {
                device = wirecenter;
                return device;
            } else if (wirecenter.isDeviceOnNetwork(serialNumber)) {
                device = wirecenter.getDevice(serialNumber);
                return device;
            }
        }
        return device;
    }

    public void printLoops() {
        for (Wirecenter wirecenter : wirecenters) {
            printOneWcLoops(wirecenter);
        }
    }

    public void printDeletedLoops() {
        for (Wirecenter wirecenter : wirecenters) {
            printOneWcDeletedLoops(wirecenter);
        }
    }

    private void printOneWcDeletedLoops(Wirecenter wirecenter) {
        List<ServiceLoopMemory> deletedLoops = wirecenter.getLoops().stream().
                filter(loop -> !loop.doesExist()).collect(Collectors.toList());
        if (deletedLoops.isEmpty()) {
            System.out.println(wirecenter + " didn't have any service loops terminated.\n");
        } else {
            System.out.println(wirecenter + " had following service loops terminated:\n");
            System.out.println(deletedLoops + "\n");
        }
    }

    private void printOneWcLoops(Wirecenter wirecenter) {
        String loopText = wirecenter.listAllCurrentLoops();
        if (loopText.isEmpty()) {
            System.out.println(wirecenter + " doesn't have any existing service loops.\n");
        } else {
            System.out.println(wirecenter + " has following service loops:\n");
            System.out.println(loopText);
            System.out.println("***************************\n");
        }
    }

    public void addPredefinedDevices() {
        if (!predef) {
            Wirecenter wirecenter1 = new Wirecenter(LocationMaker.generateLocation());
            Wirecenter wirecenter2 = new Wirecenter(LocationMaker.generateLocation());
            Wirecenter wirecenter3 = new Wirecenter(LocationMaker.generateLocation());
            Device term1_1 = new Terminal(LocationMaker.generateLocation());
            Device term1_2 = new Terminal(LocationMaker.generateLocation());
            Device term2_1 = new Terminal(LocationMaker.generateLocation());
            Device term3_1 = new Terminal(LocationMaker.generateLocation());
            Device serv1_1 = new Terminal(LocationMaker.generateLocation());
            Device serv1_2 = new Terminal(LocationMaker.generateLocation());
            Device serv1_3 = new Terminal(LocationMaker.generateLocation());
            Device serv2_1 = new Terminal(LocationMaker.generateLocation());
            Device serv2_2 = new Terminal(LocationMaker.generateLocation());
            Device serv3_1 = new Terminal(LocationMaker.generateLocation());
            Device serv3_2 = new Terminal(LocationMaker.generateLocation());
            Device serv3_3 = new Terminal(LocationMaker.generateLocation());
            //first network
            CableHandling.connectDevices(wirecenter1, term1_1);
            CableHandling.connectDevices(term1_1, term1_2);
            CableHandling.connectDevices(term1_1, serv1_1);
            CableHandling.connectDevices(term1_1, serv1_2);
            CableHandling.connectDevices(term1_2, serv1_3);
            wirecenter1.provideServiceTo(serv1_1, ServiceType.DSL);
            wirecenter1.provideServiceTo(serv1_1, ServiceType.PHONE);
            wirecenters.add(wirecenter1);
            //second network
            CableHandling.connectDevices(wirecenter2, term2_1);
            CableHandling.connectDevices(term2_1, serv2_1);
            CableHandling.connectDevices(term2_1, serv2_2);
            wirecenter2.provideServiceTo(serv2_2, ServiceType.PHONE);
            wirecenters.add(wirecenter2);
            //third network
            CableHandling.connectDevices(wirecenter3, term3_1);
            CableHandling.connectDevices(term3_1, serv3_1);
            CableHandling.connectDevices(term3_1, serv3_2);
            CableHandling.connectDevices(term3_1, serv3_3);
            wirecenters.add(wirecenter3);
            predef = true;
            System.out.println("Predefined network has been spawned.");
        } else System.out.println("Predefined network has already been spawned once.");
    }
}
