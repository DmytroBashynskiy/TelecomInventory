package net.dmytrobashynskiy.menu.commands.impl;

import net.dmytrobashynskiy.Inventory;
import net.dmytrobashynskiy.cables.cable_components.ServiceType;
import net.dmytrobashynskiy.devices.Wirecenter;
import net.dmytrobashynskiy.devices.device_utils.Device;
import net.dmytrobashynskiy.devices.device_utils.ServiceLoopMemory;
import net.dmytrobashynskiy.menu.commands.Command;

import java.util.Optional;
import java.util.Scanner;

public class ComTerminateLoop implements Command {
    @Override
    public void execute(Inventory inventory, Scanner inputScanner) {
        boolean end = false;
        while (!end) {
            System.out.println("You're in loop termination submenu. To exit type 'exit'");
            System.out.println("First - type in wirecenter's serial number, digits only.");
            int wirecenter;
            int device;
            String serviceType;
            final ServiceType serv;
            try {
                String localLine = inputScanner.nextLine();
                if (localLine.equals("exit")) {
                    end = true;
                    continue;
                } else wirecenter = Integer.parseInt(localLine);
            } catch (NumberFormatException e) {
                System.out.println("Incorrect input, try again.");
                continue;
            }
            System.out.println("Second - type in receiver's serial number, digits only.");
            try {
                device = Integer.parseInt(inputScanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Incorrect input, try again.");
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
                default:
                    System.out.println("Incorrect input.");
                    serv = null;
                    serviceFail = true;
            }
            if (serviceFail) continue;
            Wirecenter focusWirecenter = (Wirecenter) inventory.getDevice(wirecenter);
            Device receiverDevice = inventory.getDevice(device);
            Optional<ServiceLoopMemory> lookForLoop = focusWirecenter.getLoops().
                    stream().filter(loop -> loop.getServiceTargetDevice().equals(receiverDevice) &&
                            loop.getServiceType().equals(serv)).findAny();
            ServiceLoopMemory focusLoop;
            if (lookForLoop.isPresent()) {
                focusLoop = lookForLoop.get();
                boolean terminateMarker = focusWirecenter.terminateServiceLoop(focusLoop);
                if (terminateMarker) {
                    System.out.println("Provision of <" + serv.serviceName + "> service to <"
                            + receiverDevice + "> was successfully terminated.");
                    end = true;
                } else {
                    System.out.println("Specified service was not found. Try again.");
                    end = true;
                }
            } else {
                System.out.println("Specified service was not found. Try again.");
                end = true;
            }
        }
    }
}
