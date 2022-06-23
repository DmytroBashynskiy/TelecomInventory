package net.dmytrobashynskiy;

import net.dmytrobashynskiy.cables.Cable;
import net.dmytrobashynskiy.cables.Cable100;
import net.dmytrobashynskiy.cables.cable_components.ServiceType;
import net.dmytrobashynskiy.devices.Terminal;
import net.dmytrobashynskiy.devices.Wirecenter;
import net.dmytrobashynskiy.devices.device_utils.Device;
import net.dmytrobashynskiy.utils.CableHandling;
import net.dmytrobashynskiy.utils.LocationMaker;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class CableReplacement {
    @Test
    void simpleCableReplacement(){
        Cable newCable = new Cable100();
        Device wirecenter = new Wirecenter(LocationMaker.generateLocation());
        Device terminal = new Terminal(LocationMaker.generateLocation());
        CableHandling.connectDevices(wirecenter,terminal);
        //println wirecenter.getConnectedCables().get(0);
        //devices = wirecenter.getDeviceToWirecenterChain(terminal);
        CableHandling.replaceCable(wirecenter, terminal, newCable);
        //println wirecenter.getConnectedCables().get(0);

        assertTrue(CableHandling.areConnected(wirecenter,terminal));
        assertTrue(wirecenter.getConnectedCables().contains(newCable));
    }

    @Test
    void cableReplaceWithServicePreservation(){

        Wirecenter wirecenter = new Wirecenter(LocationMaker.generateLocation());
        Device terminal = new Terminal(LocationMaker.generateLocation());
        CableHandling.connectDevices(wirecenter,terminal);

        wirecenter.provideServiceTo(terminal, ServiceType.PHONE);
        wirecenter.provideServiceTo(terminal, ServiceType.DSL);

        CableHandling.replaceCable(wirecenter, terminal, new Cable100());

        //assertTrue(CableHandling.havePairWithService(wirecenter, terminal, ServiceType.DSL));
        assertTrue(CableHandling.havePairWithService(wirecenter, terminal, ServiceType.PHONE));
    }

}
