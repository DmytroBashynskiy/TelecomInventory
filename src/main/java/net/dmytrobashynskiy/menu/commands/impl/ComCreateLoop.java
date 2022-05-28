package net.dmytrobashynskiy.menu.commands.impl;

import net.dmytrobashynskiy.Inventory;
import net.dmytrobashynskiy.cables.cable_components.ServiceType;
import net.dmytrobashynskiy.devices.Wirecenter;
import net.dmytrobashynskiy.devices.device_utils.Device;
import net.dmytrobashynskiy.menu.commands.Command;

import java.util.Scanner;

public class ComCreateLoop implements Command {
    @Override
    public void execute(Inventory inventory, Scanner inputScanner) {
        boolean end = false;
        while (!end) {
            System.out.println("You're in loop creation submenu. To exit type 'exit'");
            System.out.println("First - type in wirecenters serial number, digits only.");
            int wirecenter;
            int device;
            String serviceType;
            ServiceType serv = null;
            try {
                String localLine = inputScanner.nextLine();
                if (localLine.equals("exit")) {
                    System.out.println("Exiting submenu.");
                    end = true;
                    continue;
                } else wirecenter = Integer.parseInt(localLine);
            } catch (NumberFormatException e) {
                System.out.println("Incorrect input, try again.");
                continue;
            }
            Wirecenter focusWirecenter = null;
            try {
                focusWirecenter = (Wirecenter) inventory.getDevice(wirecenter);
            } catch (ClassCastException e) {
                e.printStackTrace();
            }
            if (focusWirecenter == null) {
                System.out.println("Provided wirecenter serial number doesn't correspond to the devices on the network.\n" +
                        "Try again.");
                continue;
            }
            System.out.println("Second - type in receivers serial number, digits only.");
            try {
                String localLine = inputScanner.nextLine();
                if (localLine.equals("exit")) {
                    System.out.println("Exiting submenu.");
                    end = true;
                    continue;
                } else {
                    device = Integer.parseInt(localLine);
                }

            } catch (NumberFormatException e) {
                System.out.println("Incorrect input, try again.");
                continue;
            }
            Device receiverDevice = inventory.getDevice(device);
            if (receiverDevice == null) {
                System.out.println("Provided Device serial number doesn't correspond to the devices on the network.\n" +
                        "Try again.");
                continue;
            }
            System.out.println("Third - type in service type. Can be either 'phone' or 'dsl'.");
            serviceType = inputScanner.nextLine();
            boolean serviceFail = false;
            switch (serviceType) {
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
            if (serviceFail) continue;
            boolean provisionFail = focusWirecenter.provideServiceTo(receiverDevice, serv);
            if (!provisionFail) {
                System.out.println("Service was not provided.");
                end = true;
            } else {
                System.out.println("Service was successfully provided.");
                end = true;
            }
        }
    }
}
