package net.dmytrobashynskiy;

import net.dmytrobashynskiy.devices.Terminal;
import net.dmytrobashynskiy.devices.device_utils.Device;
import net.dmytrobashynskiy.utils.CableHandling;
import net.dmytrobashynskiy.utils.LocationMaker;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DeviceTerminalOperations {

    @Test
    void terminalCreation() {
        Terminal term = new Terminal(LocationMaker.generateLocation());

        assertTrue(term.isServiceLocation());
        assertEquals(term.getCurrentIOcount(), term.getOutputs().size() + term.getInputs().size());
    }

    @Test
    void termOneConnection(){
        Device term1 = new Terminal(LocationMaker.generateLocation());
        Device term2 = new Terminal(LocationMaker.generateLocation());

        CableHandling.connectDevices(term1,term2);
        assertTrue(CableHandling.areConnected(term1,term2));
    }

    @Test
    void termOneConnectionTestByInternalSet(){
        Device term1 = new Terminal(LocationMaker.generateLocation());
        Device term2 = new Terminal(LocationMaker.generateLocation());

        CableHandling.connectDevices(term1,term2);
        assertTrue(term1.getConnectedDevices().contains(term2));
    }

    @Test
    void termMultipleConnectionsOnOneTerm(){
        Device term1 = new Terminal(LocationMaker.generateLocation());
        Device term2 = new Terminal(LocationMaker.generateLocation());
        Device term3 = new Terminal(LocationMaker.generateLocation());
        Device term4 = new Terminal(LocationMaker.generateLocation());
        CableHandling.connectDevices(term1,term2);
        CableHandling.connectDevices(term1,term3);
        CableHandling.connectDevices(term1,term4);

        assertTrue(term1.getConnectedDevices().contains(term2) &&
                term1.getConnectedDevices().contains(term3) &&
                term1.getConnectedDevices().contains(term4));

    }
    //TODO remake all tests from Groovy into JUnit

}
