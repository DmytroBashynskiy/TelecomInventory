package net.dmytrobashynskiy;

import net.dmytrobashynskiy.devices.Terminal;
import net.dmytrobashynskiy.devices.Wirecenter;
import net.dmytrobashynskiy.devices.device_utils.Device;
import net.dmytrobashynskiy.utils.CableHandling;
import net.dmytrobashynskiy.utils.LocationMaker;
import org.junit.jupiter.api.Test;

public class DeviceWirecenterOperations {
    @Test
    void listDeviceNetworkSimple(){
        Wirecenter wc = new Wirecenter(LocationMaker.generateLocation());
        Device term1 = new Terminal(LocationMaker.generateLocation());
        Device term2 = new Terminal(LocationMaker.generateLocation());
        Device term3 = new Terminal(LocationMaker.generateLocation());
        Device term4 = new Terminal(LocationMaker.generateLocation());
        CableHandling.connectDevices(wc, term1);
        CableHandling.connectDevices(wc, term2);
        CableHandling.connectDevices(wc, term3);
        CableHandling.connectDevices(wc, term4);

        System.out.println(wc.listDeviceNetwork());

    }
}
