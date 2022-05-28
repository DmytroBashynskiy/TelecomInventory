package net.dmytrobashynskiy.menu.commands.impl;

import net.dmytrobashynskiy.Inventory;
import net.dmytrobashynskiy.cables.Cable;
import net.dmytrobashynskiy.devices.Wirecenter;
import net.dmytrobashynskiy.menu.commands.Command;

import java.util.Scanner;

public class ComGetCableInfo implements Command {
    @Override
    public void execute(Inventory inventory, Scanner inputScanner) {
        boolean end = false;
        while (!end) {
            System.out.println("You're in cable info submenu. To exit type 'exit'");
            System.out.println("Please, type in cable's name, for example 'Cable20-N202120': ");
            Cable getCable = null;
            String line = inputScanner.nextLine();
            if (line.equals("exit")) {
                System.out.println("Exiting submenu.");
                end = true;
            }
            for (Wirecenter wc : inventory.getWirecenters()) {
                getCable = wc.getCable(line);
                if (getCable != null) {
                    break;
                }
            }
            if (getCable == null) {
                System.out.println("Cable was not found. Check the name and try again.");
            } else {
                System.out.println(getCable.getCableName() + " connects parent <" + getCable.getParentDevice() + "> and child <" +
                        getCable.getChildDevice() + "> devices.");
                inventory.getPairsInfo(getCable);
                end = true;
            }
        }
    }
}
