package net.dmytrobashynskiy.menu.commands.impl;

import net.dmytrobashynskiy.Inventory;
import net.dmytrobashynskiy.menu.commands.Command;

import java.util.Scanner;
import java.util.regex.Pattern;

public class ComGetDeviceInfo implements Command {
    @Override
    public void execute(Inventory inventory, Scanner inputScanner) {
        boolean end = false;
        while (!end){
            System.out.println("You're in device info submenu. To exit type 'exit'");
            System.out.println("Otherwise, type in device's serial number, digits only: ");
            String line = inputScanner.nextLine();
            if(line.equals("exit")){
                System.out.println("Exiting submenu.");
                end = true;
            }
            if (!Pattern.matches("[a-zA-Z]+", line)) {
                inventory.getDeviceInfo(Integer.parseInt(line));
                end=true;
            }else{
                System.out.println("Your input was incorrect. Try again.");
            }
        }
    }
}
