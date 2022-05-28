package net.dmytrobashynskiy.utils;



public class Location {
    private int latitude;
    private int longitude;

    public Location(int latitude, int longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }
    public int getLatitude() {
        return latitude;
    }
    public int getLongitude() {
        return longitude;
    }
    @Override
    public String toString() {
        return "{latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
