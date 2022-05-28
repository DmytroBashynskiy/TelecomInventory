package net.dmytrobashynskiy.menu.commands;

import net.dmytrobashynskiy.Inventory;

import java.util.Scanner;

public interface Command {
    void execute(Inventory inventory, Scanner inputScanner);
}
