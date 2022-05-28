package net.dmytrobashynskiy.menu;


import net.dmytrobashynskiy.Inventory;
import net.dmytrobashynskiy.cables.Cable;
import net.dmytrobashynskiy.cables.Cable100;
import net.dmytrobashynskiy.cables.Cable20;
import net.dmytrobashynskiy.cables.Cable500;
import net.dmytrobashynskiy.cables.cable_components.ServiceType;
import net.dmytrobashynskiy.devices.Terminal;
import net.dmytrobashynskiy.devices.Wirecenter;
import net.dmytrobashynskiy.devices.device_utils.Device;
import net.dmytrobashynskiy.devices.device_utils.ServiceLoopMemory;
import net.dmytrobashynskiy.menu.commands.Command;
import net.dmytrobashynskiy.menu.commands.impl.*;
import net.dmytrobashynskiy.utils.CableHandling;
import net.dmytrobashynskiy.utils.Location;

import java.util.HashMap;
import java.util.Optional;
import java.util.Scanner;
import java.util.regex.Pattern;

public class MenuThing {
    private boolean end = false;

    private HashMap<String, Command> commandStorage;

    public MenuThing() {
        this.commandStorage = new HashMap<>();

        commandStorage.put("help", new ComHelp());
        commandStorage.put("spawn network", new ComSpawnNetwork());
        commandStorage.put("check inventory", new ComCheckInv());
        commandStorage.put("get device info", new ComGetDeviceInfo());
        commandStorage.put("edit device info", new ComEditDeviceInfo());
        commandStorage.put("add device", new ComAddDevice());
        commandStorage.put("get cable info", new ComGetCableInfo());
        commandStorage.put("replace cable", new ComReplaceCable());
        commandStorage.put("list loops",new ComListLoops());
        commandStorage.put("list deleted loops", new ComListDeletedLoops());
        commandStorage.put("create loop", new ComCreateLoop());
        commandStorage.put("terminate loop",new ComTerminateLoop());

    }



    public void menu(Inventory inv){
        try(Scanner initScanner = new Scanner(System.in)){
            while(!end){
                System.out.println("  <<<Welcome to the Network Inventory>>>");
                System.out.println("Type 'help' to list all available commands.");
                String lineInput = initScanner.nextLine();

                if(lineInput.equals("exit")){
                    System.out.println("Good bye!");
                    end = true;
                    break;
                }
                executeCommand(lineInput, inv, initScanner);

              /*  switch (lineInput){
                    case "help":
                        System.out.println("0. 'spawn network' generates predefined network. Can be used only once.");
                        System.out.println("1. 'check inventory' prints inventory manifest.");
                        System.out.println("2. 'get device info' prints device info, requires serial number of device.");
                        System.out.println("3. 'edit device info' allows to edit device's location.");
                        System.out.println("4. 'add device' allows to add devices to existing networks or create new networks.");
                        System.out.println("5. 'get cable info' prints cable info, requires serial number of cable.");
                        System.out.println("6. 'replace cable' allows to replace a designated cable.");
                        System.out.println("7. 'list loops' prints existing service loops.");
                        System.out.println("8. 'list deleted loops' prints existing service loops.");
                        System.out.println("9. 'create loop' allows to create a service loop from designated wirecenter\n" +
                                " to the device on its network.");
                        System.out.println("10. 'terminate loop' allows to terminate an existing service loop.");
                        System.out.println("11. 'exit' exits application.");
                        break;
                    case "check inventory":
                        inv.printInventoryManifest();
                        break;
                    case "exit":
                        System.out.println("Good bye!");
                        end = true;
                        break;
                    case "get device info":
                        getDeviceInfo(inv,initScanner);
                        break;
                    case "edit device info":
                        editDeviceInfo(inv,initScanner);
                        break;
                    case "create loop":
                        createServiceLoop(inv, initScanner);
                        break;
                    case "replace cable":
                        replaceCable(inv,initScanner);
                        break;
                    case "get cable info":
                        getCableInfo(inv,initScanner);
                        break;
                    case "terminate loop":
                        terminateServiceLoop(inv,initScanner);
                        break;
                    case "add device":
                        addDevice(inv,initScanner);
                        break;
                    case "list loops":
                        inv.printLoops();
                        break;
                    case "list deleted loops":
                        inv.printDeletedLoops();
                        break;
                    case "spawn network":
                        inv.addPredefinedDevices();
                        break;

                    default:
                        System.out.println("Command not recognized, please try again.");
                        break;
                }*/
            }
        }
    }

    private void executeCommand(String commandName, Inventory inv, Scanner initScanner){
        if(commandStorage.containsKey(commandName)) {
            commandStorage.get(commandName).execute(inv,initScanner);
        }else{
            System.out.println("Command not recognized, please try again.");
        }

    }

    /*private void getDeviceInfo(Inventory inv,Scanner initScanner){
            boolean end = false;
            while (!end){
                System.out.println("You're in device info submenu. To exit type 'exit'");
                System.out.println("Otherwise, type in device's serial number, digits only: ");
                String line = initScanner.nextLine();
                if(line.equals("exit")){
                    System.out.println("Exiting submenu.");
                    end = true;
                }
                if (!Pattern.matches("[a-zA-Z]+", line)) {
                    inv.getDeviceInfo(Integer.parseInt(line));
                    end=true;
                }else{
                    System.out.println("Your input was incorrect. Try again.");
                }
            }
    }


    private void getCableInfo(Inventory inv,Scanner initScanner){
        boolean end = false;
        while (!end){
            System.out.println("You're in cable info submenu. To exit type 'exit'");
            System.out.println("Please, type in cable's name, for example 'Cable20-N202120': ");
            Cable getCable = null;
            String line = initScanner.nextLine();
            if(line.equals("exit")){
                System.out.println("Exiting submenu.");
                end = true;
            }
            for(Wirecenter wc: inv.getWirecenters()){
                getCable = wc.getCable(line);
                if(getCable!=null){
                    break;
                }
            }
            if(getCable==null){
                System.out.println("Cable was not found. Check the name and try again.");
            }else{
                System.out.println(getCable.getCableName()+" connects parent <"+getCable.getParentDevice()+"> and child <"+
                        getCable.getChildDevice()+"> devices.");
                inv.getPairsInfo(getCable);
                end = true;
            }
        }
    }
    private void editDeviceInfo(Inventory inv,Scanner initScanner){
        Device device = null;
        boolean end = false;
        while (!end){
            System.out.println("You're in device editing submenu. To exit type 'exit'");
            System.out.println("First - type in device's serial number.");
            String line = initScanner.nextLine();
            if (!Pattern.matches("[a-zA-Z]+", line)) {
                device = inv.getDevice(Integer.parseInt(line));
                if(device==null){
                    System.out.println("Device not found. Either you've mistyped, or it does not exist. Try again.");
                    continue;
                }
            }else if(line.equals("exit")) {
                break;
            }
            editDeviceInternals(initScanner,device);
            end = true;
        }
    }
    private void editDeviceInternals(Scanner initScanner, Device device){
        System.out.println("Device found. It is <"+device+">.");
        boolean localEnd = false;
        while(!localEnd){
            System.out.println("Type 'help' for local commands info.");
            String line = initScanner.nextLine();
            switch (line){
                case "exit":
                    System.out.println("Exiting submenu.");
                    localEnd = true;
                    break;
                case "add more io":
                    boolean tryAddIO = device.addMoreIOpoints();
                    if(tryAddIO){
                        System.out.println(device+" IO number was increased. It has now "+device.getInputs().size()+
                                " inputs and "+device.getOutputs().size()+" outputs.");
                    }else{
                        System.out.println(device+" cannot have more IO points than it has now.");
                    }
                    break;
                case "edit location":
                    Location location = createLocation(initScanner);
                    device.setDeviceLocation(location);
                    System.out.println("New location for <"+device+"> is set.");
                    localEnd = true;
                    break;
                case "help":
                    System.out.println("'exit' is to exit this submenu.\n" +
                            "'add more io' is to add more IO points to the device.\n"+
                            "'edit location' is to set new location for the device.");
                    break;
                default:
                    System.out.println("Command not recognized, try again.");
                    break;
            }
        }
    }
    private void createServiceLoop(Inventory inv,Scanner initScanner){
        boolean end = false;
        while (!end) {
            System.out.println("You're in loop creation submenu. To exit type 'exit'");
            System.out.println("First - type in wirecenter's serial number, digits only.");
            int wirecenter;
            int device;
            String serviceType;
            ServiceType serv = null;
            try{
                String localLine = initScanner.nextLine();
                if(localLine.equals("exit")){
                    System.out.println("Exiting submenu.");
                    end = true;
                    continue;
                }else wirecenter = Integer.parseInt(localLine);
            }catch(NumberFormatException e){
                System.out.println("Incorrect input, try again.");
                continue;
            }
            Wirecenter focusWirecenter = (Wirecenter) inv.getDevice(wirecenter);
            if(focusWirecenter==null){
                System.out.println("Provided wirecenter serial number doesn't correspond to the devices on the network.\n" +
                        "Try again.");
                continue;
            }
            System.out.println("Second - type in receiver's serial number, digits only.");
            try{
                String localLine = initScanner.nextLine();
                if(localLine.equals("exit")){
                    System.out.println("Exiting submenu.");
                    end = true;
                    continue;
                }else device = Integer.parseInt(initScanner.nextLine());
            }catch(NumberFormatException e){
                System.out.println("Incorrect input, try again.");
                continue;
            }
            Device receiverDevice = inv.getDevice(device);
            if(receiverDevice==null){
                System.out.println("Provided Device serial number doesn't correspond to the devices on the network.\n" +
                        "Try again.");
                continue;
            }
            System.out.println("Third - type in service type. Can be either 'phone' or 'dsl'.");
            serviceType = initScanner.nextLine();
            boolean serviceFail = false;
            switch (serviceType){
                case "phone":
                    serv = ServiceType.PHONE;
                    break;
                case "dsl":
                    serv = ServiceType.DSL;
                    break;
                case "exit":
                    System.out.println("Exiting submenu.");
                    end = true;
                    break;
                default:
                    System.out.println("Incorrect input.");
                    serviceFail = true;
            }
            if(serviceFail)continue;
            boolean provisionFail = focusWirecenter.provideServiceTo(receiverDevice, serv);
            if (!provisionFail){
                System.out.println("Service was not provided.");
                end = true;
            }else{
                System.out.println("Service was successfully provided.");
                end = true;
            }
        }
    }
    private void terminateServiceLoop(Inventory inv,Scanner initScanner){
        boolean end = false;
        while (!end) {
            System.out.println("You're in loop termination submenu. To exit type 'exit'");
            System.out.println("First - type in wirecenter's serial number, digits only.");
            int wirecenter;
            int device;
            String serviceType;
            final ServiceType serv;
            try{
                String localLine = initScanner.nextLine();
                if(localLine.equals("exit")){
                    end = true;
                    continue;
                }else wirecenter = Integer.parseInt(localLine);
            }catch(NumberFormatException e){
                System.out.println("Incorrect input, try again.");
                continue;
            }
            System.out.println("Second - type in receiver's serial number, digits only.");
            try{
                device = Integer.parseInt(initScanner.nextLine());
            }catch(NumberFormatException e){
                System.out.println("Incorrect input, try again.");
                continue;
            }
            System.out.println("Third - type in service type. Can be either 'phone' or 'dsl'.");
            serviceType = initScanner.nextLine();
            boolean serviceFail = false;
            switch (serviceType){
                case "phone":
                    serv = ServiceType.PHONE;
                    break;
                case "dsl":
                    serv = ServiceType.DSL;
                    break;
                default:
                    System.out.println("Incorrect input.");
                    serv = null;
                    serviceFail = true;
            }
            if(serviceFail)continue;
            Wirecenter focusWirecenter = (Wirecenter) inv.getDevice(wirecenter);
            Device receiverDevice = inv.getDevice(device);
            Optional<ServiceLoopMemory> lookForLoop = focusWirecenter.getLoops().
                    stream().filter(loop -> loop.getServiceTargetDevice().equals(receiverDevice) &&
                    loop.getServiceType().equals(serv)).findAny();
            ServiceLoopMemory focusLoop;
            if(lookForLoop.isPresent()){
                focusLoop = lookForLoop.get();
                boolean terminateMarker = focusWirecenter.terminateServiceLoop(focusLoop);
                if(terminateMarker){
                    System.out.println("Provision of <"+serv.serviceName+"> service to <"
                            +receiverDevice+"> was successfully terminated.");
                    end=true;
                }else{
                    System.out.println("Specified service was not found. Try again.");
                    end=true;
                }
            }else{
                System.out.println("Specified service was not found. Try again.");
                end=true;
            }
        }
    }
    private void replaceCable(Inventory inv,Scanner initScanner){
        boolean end = false;
        while (!end) {
            System.out.println("You're in cable replacement submenu. To exit type 'exit'");
            System.out.println("Otherwise, type in old cable's full name, for example 'Cable20-N202120': ");
            String oldCableName = initScanner.nextLine();
            if (oldCableName.equals("exit")) {
                System.out.println("Exiting submenu.");
                end = true;
            }
            if(CableHandling.getCableByName(inv, oldCableName)==null){
                System.out.println("Cable was not found. Try again.");
                continue;
            }
            System.out.println("Now choose the type of the cable you wish to replace the old one with. Possible variants: \n"+
                    "'Cable20', 'Cable100', 'Cable500'. Please note, that if your old cable has greater pair count\n"+"" +
                    "than your new one, such as Cable100(old) > Cable20(new) - you will not be able to replace the cable.");
            String newCableType = initScanner.nextLine();
            switch (newCableType){
                case "Cable20":
                    Cable newCable = new Cable20();
                    boolean replacement = inv.replaceCable(oldCableName,newCable);
                    if(replacement){
                        System.out.println("Old cable was successfully replaced with <"+newCable.getCableName()+">.");
                    }else
                        System.out.println("Old cable was not replaced due to incompatible type.");
                    end = true;
                    break;
                case "Cable100":
                    Cable newCable1 = new Cable100();
                    boolean replacement1 = inv.replaceCable(oldCableName,newCable1);
                    if(replacement1){
                        System.out.println("Old cable was successfully replaced with <"+newCable1.getCableName()+">.");
                    }else
                        System.out.println("Old cable was not replaced due to incompatible type.");
                    end = true;
                    break;
                case "Cable500":
                    Cable newCable2 = new Cable500();
                    boolean replacement2 = inv.replaceCable(oldCableName,newCable2);
                    if(replacement2){
                        System.out.println("Old cable was successfully replaced with <"+newCable2.getCableName()+">.");
                    }else
                        System.out.println("Old cable was not replaced due to incompatible type.");
                    end = true;
                    break;
                default:
                    System.out.println("Incorrect input.");
                }
        }
    }
    private void addDevice(Inventory inv,Scanner initScanner){
        boolean end = false;
        while (!end) {
            System.out.println("You're in device creation submenu. To exit type 'exit'");
            System.out.println("Otherwise, type in device's type, can be 'wirecenter' or 'terminal': ");
            String line = initScanner.nextLine();
            if (line.equals("exit")) {
                System.out.println("Exiting submenu.");
                end = true;
                continue;
            }
            switch (line){
                case "wirecenter":
                    Wirecenter wirecenter = new Wirecenter(createLocation(initScanner));
                    inv.getWirecenters().add(wirecenter);
                    System.out.println(wirecenter+" was successfully created and placed into inventory.");
                    break;
                case "terminal":
                    addTerminal(inv, initScanner);
                    break;
                default:
                    System.out.println("Command not recognized.");
                    break;
            }
        }
    }

    private Location createLocation(Scanner initScanner){
        int newLatitude = 0;
        int newLongitude = 0;
        boolean localEnd1 = false;
        boolean localEnd2 = false;
        System.out.println("Location consists of 2 integers, latitude and longitude.");
        System.out.println("Type in the latitude: ");
        while (!localEnd1) {
            try {
                newLatitude = Integer.parseInt(initScanner.nextLine());
                localEnd1 = true;
            } catch (NumberFormatException e) {
                System.out.println("Incorrect input, try again.");
            }
        }
        System.out.println("Type in the longitude: ");
        while (!localEnd2) {
            try {
                newLongitude = Integer.parseInt(initScanner.nextLine());
                localEnd2 = true;
            } catch (NumberFormatException e) {
                System.out.println("Incorrect input, try again.");
            }
        }
        return new Location(newLatitude, newLongitude);
    }

    private void addTerminal(Inventory inv, Scanner initScanner){
        boolean end = false;
        Device terminal = new Terminal(createLocation(initScanner));
        while(!end) {
            System.out.println("You're in terminal creation and connection submenu.");
            System.out.println(terminal + " was successfully created. Enter serial number of a device you wish to connect it to.\n" +
                    "For example a parent wirecenter or some other terminal that exists in the inventory.");
            int parentSN;
            try {
                String line = initScanner.nextLine();
                if(line.equals("exit")){
                    System.out.println("Exiting terminal connection submenu. Created terminal will not be placed in the inventory.");
                    break;
                }else parentSN = Integer.parseInt(line);

            } catch (NumberFormatException s) {
                System.out.println("Incorrect input, try again.");
                continue;
            }
            Device parentDevice = inv.getDevice(parentSN);
            if (parentDevice != null) {
                System.out.println(parentDevice + " exists on the network. Connecting devices...");
                boolean connect = CableHandling.connectDevices(parentDevice, terminal);
                if (connect) {
                    System.out.println("Connection successful, " + parentDevice + " and " + terminal + " are now connected.");
                    end = true;

                } else {
                    System.out.println("Something went wrong during the connection, it was not established.");
                    end = true;
                }
            } else System.out.println("Parent device could not be located. Check the serial number you've provided.");
        }
    }*/
}
