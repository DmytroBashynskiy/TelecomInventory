package net.dmytrobashynskiy.inventorytests

import net.dmytrobashynskiy.cables.cable_components.ServiceType
import net.dmytrobashynskiy.devices.device_utils.Device
import net.dmytrobashynskiy.devices.Terminal
import net.dmytrobashynskiy.devices.Wirecenter
import net.dmytrobashynskiy.utils.CableHandling
import net.dmytrobashynskiy.utils.LocationMaker
import spock.lang.Specification

class DeviceNetworkOperations extends Specification{
    def "simple chaining"(){
        given:
        List<Device> devices;
        Device wirecenter = new Wirecenter(LocationMaker.generateLocation())
        Device terminal = new Terminal(LocationMaker.generateLocation())
        CableHandling.connectDevices(wirecenter,terminal)
        devices = wirecenter.getDeviceToWirecenterChain(terminal);
        expect:
        //println devices
        CableHandling.areConnected(wirecenter,terminal) & devices.size()==2
    }


    def "medium chaining"(){
        given:
        List<Device> devices;
        Device wirecenter = new Wirecenter(LocationMaker.generateLocation())
        Device term1 = new Terminal(LocationMaker.generateLocation())
        Device term2 = new Terminal(LocationMaker.generateLocation())
        Device term2_1 = new Terminal(LocationMaker.generateLocation())
        Device term2_2 = new Terminal(LocationMaker.generateLocation())
        Device term3_1 = new Terminal(LocationMaker.generateLocation())
        Device term3_2 = new Terminal(LocationMaker.generateLocation())
        CableHandling.connectDevices(wirecenter,term1)
        CableHandling.connectDevices(term1,term2)
        CableHandling.connectDevices(term2,term2_1)
        CableHandling.connectDevices(term2,term2_2)
        CableHandling.connectDevices(term2_2,term3_1)
        CableHandling.connectDevices(term2_2,term3_2)
        devices = wirecenter.getDeviceToWirecenterChain(term3_2);
        expect:
        //println devices
        devices.size()==5
    }

    def "with horizontal terminal chaining"(){
        given:
        List<Device> devices;
        Device wirecenter = new Wirecenter(LocationMaker.generateLocation())
        Device term1 = new Terminal(LocationMaker.generateLocation())
        Device term2 = new Terminal(LocationMaker.generateLocation())
        Device term2_1 = new Terminal(LocationMaker.generateLocation())
        Device term2_2 = new Terminal(LocationMaker.generateLocation())
        Device term3_1 = new Terminal(LocationMaker.generateLocation())
        Device term3_2 = new Terminal(LocationMaker.generateLocation())
        CableHandling.connectDevices(wirecenter,term1)
        CableHandling.connectDevices(term1,term2)
        CableHandling.connectDevices(term2,term2_1)
        CableHandling.connectDevices(term2,term2_2)
        //here the term2_1 is connected to the term2_2, creating a cable with term2_2 marked as childDevice
        //by design the first cable in connectedCables will be picked, and it will be the shortest route.
        CableHandling.connectDevices(term2_1,term2_2)
        CableHandling.connectDevices(term2_2,term3_1)
        CableHandling.connectDevices(term2_2,term3_2)
        devices = wirecenter.getDeviceToWirecenterChain(term3_2);
        expect:
        //println devices
        devices.size()==5

        println wirecenter.getCablesList()
    }

    def "service provision test"(){
        given:

        Device wirecenter = new Wirecenter(LocationMaker.generateLocation())
        Device terminal = new Terminal(LocationMaker.generateLocation())
        CableHandling.connectDevices(wirecenter,terminal)
        wirecenter.provideServiceTo(terminal, ServiceType.PHONE)
        expect:
        //println devices
        CableHandling.areConnected(wirecenter,terminal) &
                CableHandling.havePairWithService(wirecenter,terminal, ServiceType.PHONE)
    }

    def "network service provision"(){
        given:

        Device wirecenter = new Wirecenter(LocationMaker.generateLocation())
        Device term1 = new Terminal(LocationMaker.generateLocation())
        Device term2 = new Terminal(LocationMaker.generateLocation())


        CableHandling.connectDevices(wirecenter,term1)
        CableHandling.connectDevices(term1,term2)

        wirecenter.provideServiceTo(term2, ServiceType.PHONE)
        expect:

        CableHandling.havePairWithService(term1,term2, ServiceType.PHONE) & CableHandling.havePairWithService(wirecenter,term1, ServiceType.PHONE)
    }

    def "network multiple service provisions"(){
        given:

        Device wirecenter = new Wirecenter(LocationMaker.generateLocation())
        Device term1 = new Terminal(LocationMaker.generateLocation())
        Device term2 = new Terminal(LocationMaker.generateLocation())
        Device term3 = new Terminal(LocationMaker.generateLocation())

        CableHandling.connectDevices(wirecenter,term1)
        CableHandling.connectDevices(term1,term2)
        CableHandling.connectDevices(term1,term3)
        wirecenter.provideServiceTo(term2, ServiceType.PHONE)
        wirecenter.provideServiceTo(term3, ServiceType.DSL)
        expect:
        //wirecenter.listAllCurrentLoops()
        CableHandling.havePairWithService(wirecenter,term1, ServiceType.DSL) & CableHandling.havePairWithService(term1,term3, ServiceType.DSL)
    }

    def "terminate service"(){
        given:

        Device wirecenter = new Wirecenter(LocationMaker.generateLocation())
        Device term1 = new Terminal(LocationMaker.generateLocation())
        Device term2 = new Terminal(LocationMaker.generateLocation())
        Device term3 = new Terminal(LocationMaker.generateLocation())

        CableHandling.connectDevices(wirecenter,term1)
        CableHandling.connectDevices(term1,term2)
        CableHandling.connectDevices(term1,term3)
        wirecenter.provideServiceTo(term2, ServiceType.PHONE)
        wirecenter.provideServiceTo(term3, ServiceType.DSL)
        //wirecenter.listAllCurrentLoops()
        wirecenter.terminateServiceLoop(wirecenter.getLoops().get(0))
        expect:
        //wirecenter.listAllCurrentLoops()
        !CableHandling.havePairWithService(wirecenter,term1, ServiceType.PHONE)
    }
}
