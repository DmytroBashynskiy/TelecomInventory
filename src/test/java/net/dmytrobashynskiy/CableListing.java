package net.dmytrobashynskiy;

import net.dmytrobashynskiy.devices.Terminal;
import net.dmytrobashynskiy.devices.Wirecenter;
import net.dmytrobashynskiy.devices.device_utils.Device;
import net.dmytrobashynskiy.utils.CableHandling;
import net.dmytrobashynskiy.utils.LocationMaker;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CableListing {
    @Test
    void simpleCableListing(){
        Wirecenter wirecenter = new Wirecenter(LocationMaker.generateLocation());
        Terminal terminal = new Terminal(LocationMaker.generateLocation());

        CableHandling.connectDevices(wirecenter,terminal);

        assertEquals(1,wirecenter.getCablesList().size());
    }

    @Test
    void severalCablesListing(){
        Wirecenter wirecenter = new Wirecenter(LocationMaker.generateLocation());
        Device term1 = new Terminal(LocationMaker.generateLocation());
        Device term2 = new Terminal(LocationMaker.generateLocation());
        Device term2_1 = new Terminal(LocationMaker.generateLocation());
        Device term2_2 = new Terminal(LocationMaker.generateLocation());
        Device term3_1 = new Terminal(LocationMaker.generateLocation());
        Device term3_2 = new Terminal(LocationMaker.generateLocation());
        CableHandling.connectDevices(wirecenter,term1);
        CableHandling.connectDevices(term1,term2);
        CableHandling.connectDevices(term2,term2_1);
        CableHandling.connectDevices(term2,term2_2);
        //here the term2_1 is connected to the term2_2, creating a cable with term2_2 marked as childDevice
        //by design the first cable in connectedCables will be picked, and it will be the shortest route.
        CableHandling.connectDevices(term2_1,term2_2);
        CableHandling.connectDevices(term2_2,term3_1);
        CableHandling.connectDevices(term2_2,term3_2);

        assertEquals(7,wirecenter.getCablesList().size());
    }


}
