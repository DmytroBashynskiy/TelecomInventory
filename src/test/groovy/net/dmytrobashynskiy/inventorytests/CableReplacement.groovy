package net.dmytrobashynskiy.inventorytests

import net.dmytrobashynskiy.cables.Cable
import net.dmytrobashynskiy.cables.Cable100
import net.dmytrobashynskiy.cables.cable_components.ServiceType
import net.dmytrobashynskiy.devices.device_utils.Device
import net.dmytrobashynskiy.devices.Terminal
import net.dmytrobashynskiy.devices.Wirecenter
import net.dmytrobashynskiy.utils.CableHandling
import net.dmytrobashynskiy.utils.LocationMaker
import spock.lang.Specification

class CableReplacement extends Specification {
    def "simple replacement"(){
        given:
        List<Device> devices;
        Cable newCable = new Cable100();
        Device wirecenter = new Wirecenter(LocationMaker.generateLocation())
        Device terminal = new Terminal(LocationMaker.generateLocation())
        CableHandling.connectDevices(wirecenter,terminal)
        //println wirecenter.getConnectedCables().get(0)
        //devices = wirecenter.getDeviceToWirecenterChain(terminal)
        CableHandling.replaceCable(wirecenter, terminal, newCable);
        //println wirecenter.getConnectedCables().get(0)
        expect:

        CableHandling.areConnected(wirecenter,terminal)
    }

    def "simple replacement with services"(){
        given:
        List<Device> devices;
        Cable newCable = new Cable100();
        Device wirecenter = new Wirecenter(LocationMaker.generateLocation())
        Device terminal = new Terminal(LocationMaker.generateLocation())
        CableHandling.connectDevices(wirecenter,terminal)
        //devices = wirecenter.getDeviceToWirecenterChain(terminal)
        wirecenter.provideServiceTo(terminal, ServiceType.PHONE)
        wirecenter.provideServiceTo(terminal, ServiceType.DSL)
        CableHandling.replaceCable(wirecenter, terminal, newCable);
        expect:

        CableHandling.havePairWithService(wirecenter, terminal, ServiceType.DSL)
    }

    def "replacement with better cable"(){
        given:
        List<Device> devices;
        Cable newCable = new Cable100();
        Device wirecenter = new Wirecenter(LocationMaker.generateLocation())
        Device term1 = new Terminal(LocationMaker.generateLocation())
        Device term2 = new Terminal(LocationMaker.generateLocation())
        Device term3 = new Terminal(LocationMaker.generateLocation())
        CableHandling.connectDevices(wirecenter,term1)
        CableHandling.connectDevices(term1,term2)
        CableHandling.connectDevices(term2,term3)
        wirecenter.provideServiceTo(term3, ServiceType.PHONE)
        wirecenter.provideServiceTo(term3, ServiceType.DSL)
        CableHandling.replaceCable(term2, term3, newCable);
        expect:

        CableHandling.havePairWithService(term2, term3, ServiceType.DSL)
    }
}
