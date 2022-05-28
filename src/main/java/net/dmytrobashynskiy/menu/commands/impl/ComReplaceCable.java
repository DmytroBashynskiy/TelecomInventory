package net.dmytrobashynskiy.menu.commands.impl;

import net.dmytrobashynskiy.Inventory;
import net.dmytrobashynskiy.cables.Cable;
import net.dmytrobashynskiy.cables.Cable100;
import net.dmytrobashynskiy.cables.Cable20;
import net.dmytrobashynskiy.cables.Cable500;
import net.dmytrobashynskiy.menu.commands.Command;
import net.dmytrobashynskiy.utils.CableHandling;

import java.util.Scanner;

public class ComReplaceCable implements Command {
    @Override
    public void execute(Inventory inventory, Scanner inputScanner) {
        boolean end = false;
        while (!end) {
            System.out.println("You're in cable replacement submenu. To exit type 'exit'");
            System.out.println("Otherwise, type in old cable's full name, for example 'Cable20-N202120': ");
            String oldCableName = inputScanner.nextLine();
            if (oldCableName.equals("exit")) {
                System.out.println("Exiting submenu.");
                end = true;
            }
            if(CableHandling.getCableByName(inventory, oldCableName)==null){
                System.out.println("Cable was not found. Try again.");
                continue;
            }
            System.out.println("Now choose the type of the cable you wish to replace the old one with. Possible variants: \n"+
                    "'Cable20', 'Cable100', 'Cable500'. Please note, that if your old cable has greater pair count\n"+"" +
                    "than your new one, such as Cable100(old) > Cable20(new) - you will not be able to replace the cable.");
            String newCableType = inputScanner.nextLine();
            switch (newCableType){
                case "Cable20":
                    Cable newCable = new Cable20();
                    boolean replacement = inventory.replaceCable(oldCableName,newCable);
                    if(replacement){
                        System.out.println("Old cable was successfully replaced with <"+newCable.getCableName()+">.");
                    }else
                        System.out.println("Old cable was not replaced due to incompatible type.");
                    end = true;
                    break;
                case "Cable100":
                    Cable newCable1 = new Cable100();
                    boolean replacement1 = inventory.replaceCable(oldCableName,newCable1);
                    if(replacement1){
                        System.out.println("Old cable was successfully replaced with <"+newCable1.getCableName()+">.");
                    }else
                        System.out.println("Old cable was not replaced due to incompatible type.");
                    end = true;
                    break;
                case "Cable500":
                    Cable newCable2 = new Cable500();
                    boolean replacement2 = inventory.replaceCable(oldCableName,newCable2);
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
}
