package net.dmytrobashynskiy.menu.commands.impl;

import net.dmytrobashynskiy.Inventory;
import net.dmytrobashynskiy.menu.commands.Command;

import java.util.Scanner;

public class ComSpawnNetwork implements Command {
    @Override
    public void execute(Inventory inventory, Scanner inputScanner) {
        inventory.addPredefinedDevices();
    }
}
