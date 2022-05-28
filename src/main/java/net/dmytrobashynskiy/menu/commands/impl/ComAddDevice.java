package net.dmytrobashynskiy.menu.commands.impl;

import net.dmytrobashynskiy.Inventory;
import net.dmytrobashynskiy.devices.Terminal;
import net.dmytrobashynskiy.devices.Wirecenter;
import net.dmytrobashynskiy.devices.device_utils.Device;
import net.dmytrobashynskiy.menu.commands.Command;
import net.dmytrobashynskiy.utils.CableHandling;
import net.dmytrobashynskiy.utils.LocationMaker;

import java.util.Scanner;

public class ComAddDevice implements Command {
    @Override
    public void execute(Inventory inventory, Scanner inputScanner) {
        boolean end = false;
        while (!end) {
            System.out.println("You're in device creation submenu. To exit type 'exit'");
            System.out.println("Otherwise, type in device's type, can be 'wirecenter' or 'terminal': ");
            String line = inputScanner.nextLine();
            if (line.equals("exit")) {
                System.out.println("Exiting submenu.");
                end = true;
                continue;
            }
            switch (line){
                case "wirecenter":
                    Wirecenter wirecenter = new Wirecenter(LocationMaker.createLocation(inputScanner));
                    inventory.getWirecenters().add(wirecenter);
                    System.out.println(wirecenter+" was successfully created and placed into inventory.");
                    break;
                case "terminal":
                    addTerminal(inventory, inputScanner);
                    break;
                default:
                    System.out.println("Command not recognized.");
                    break;
            }
        }
    }

    private void addTerminal(Inventory inv, Scanner initScanner){
        //TODO replace all string concatenations with String.format
        boolean end = false;
        Device terminal = new Terminal(LocationMaker.createLocation(initScanner));
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
    }
}
