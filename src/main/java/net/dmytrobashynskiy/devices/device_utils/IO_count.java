package net.dmytrobashynskiy.devices.device_utils;

public enum IO_count {
    IO20(10), IO100(50), IO600(300), IO600_WIRECENTER(600);


    public final int halfIoCount;
    public final int fullIoCount;

    IO_count(int i){
        halfIoCount = i;
        fullIoCount = i*2;
    }
}
