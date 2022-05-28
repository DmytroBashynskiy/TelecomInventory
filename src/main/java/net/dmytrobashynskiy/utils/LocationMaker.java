package net.dmytrobashynskiy.utils;


import java.util.Scanner;

public  class LocationMaker {
    private final static int lowerBoundary = 0;
    private final static int upperBoundary = 100;

    public static Location generateLocation(){
        Location location = new Location(getRandomNumber(),getRandomNumber());
        return location;
    }
    public static Location createLocation(Scanner initScanner){
        int newLatitude = 0;
        int newLongitude = 0;
        boolean localEnd1 = false;
        boolean localEnd2 = false;
        System.out.println("Location consists of 2 integers, latitude and longitude.");
        System.out.println("Type in the latitude: ");
        while (!localEnd1) {
            try {
                newLatitude = Integer.parseInt(initScanner.nextLine());
                localEnd1 = true;
            } catch (NumberFormatException e) {
                System.out.println("Incorrect input, try again.");
            }
        }
        System.out.println("Type in the longitude: ");
        while (!localEnd2) {
            try {
                newLongitude = Integer.parseInt(initScanner.nextLine());
                localEnd2 = true;
            } catch (NumberFormatException e) {
                System.out.println("Incorrect input, try again.");
            }
        }
        return new Location(newLatitude, newLongitude);
    }

    private static int getRandomNumber() {
        return (int) ((Math.random() * (upperBoundary - lowerBoundary)) + lowerBoundary);
    }
}
