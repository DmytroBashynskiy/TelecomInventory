package net.dmytrobashynskiy;

import net.dmytrobashynskiy.devices.Terminal;
import net.dmytrobashynskiy.devices.Wirecenter;
import net.dmytrobashynskiy.devices.device_utils.Device;
import net.dmytrobashynskiy.utils.LocationMaker;
import org.junit.jupiter.api.Test;

public class DeviceNetworkCreation {
    Wirecenter wirecenter1 = new Wirecenter(LocationMaker.generateLocation());
    Device term1_1 = new Terminal(LocationMaker.generateLocation());
    Device term1_2 = new Terminal(LocationMaker.generateLocation());


}
