package net.dmytrobashynskiy.menu.commands.impl;

import net.dmytrobashynskiy.Inventory;
import net.dmytrobashynskiy.devices.device_utils.Device;
import net.dmytrobashynskiy.menu.commands.Command;
import net.dmytrobashynskiy.utils.Location;
import net.dmytrobashynskiy.utils.LocationMaker;

import java.util.Scanner;
import java.util.regex.Pattern;

public class ComEditDeviceInfo implements Command {
    @Override
    public void execute(Inventory inventory, Scanner inputScanner) {
        Device device = null;
        boolean end = false;
        while (!end){
            System.out.println("You're in device editing submenu. To exit type 'exit'");
            System.out.println("First - type in device's serial number.");
            String line = inputScanner.nextLine();
            if (!Pattern.matches("[a-zA-Z]+", line)) {
                device = inventory.getDevice(Integer.parseInt(line));
                if(device==null){
                    System.out.println("Device not found. Either you've mistyped, or it does not exist. Try again.");
                    continue;
                }
            }else if(line.equals("exit")) {
                break;
            }
            editDeviceInternals(inputScanner,device);
            end = true;
        }
    }

    private void editDeviceInternals(Scanner inputScanner, Device device){
        System.out.println("Device found. It is <"+device+">.");
        boolean localEnd = false;
        while(!localEnd){
            System.out.println("Type 'help' for local commands info.");
            String line = inputScanner.nextLine();
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
                    Location location = LocationMaker.createLocation(inputScanner);
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


}
