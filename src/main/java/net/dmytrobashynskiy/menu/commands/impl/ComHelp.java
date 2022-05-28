package net.dmytrobashynskiy.menu.commands.impl;

import net.dmytrobashynskiy.Inventory;
import net.dmytrobashynskiy.menu.commands.Command;

import java.util.Scanner;

public class ComHelp implements Command {
    @Override
    public void execute(Inventory inventory, Scanner inputScanner) {
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
    }
}
