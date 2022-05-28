package net.dmytrobashynskiy.inventorytests

import net.dmytrobashynskiy.devices.device_utils.Device
import net.dmytrobashynskiy.devices.Terminal
import net.dmytrobashynskiy.devices.Wirecenter
import net.dmytrobashynskiy.utils.CableHandling
import net.dmytrobashynskiy.utils.LocationMaker
import spock.lang.Specification

class DeviceNetworkCreation extends Specification {
    def "Several terminals"(){
        given:

        Device term1 = new Terminal(LocationMaker.generateLocation())
        Device term2 = new Terminal(LocationMaker.generateLocation())
        Device term2_1 = new Terminal(LocationMaker.generateLocation())
        Device term2_2 = new Terminal(LocationMaker.generateLocation())
        Device term2_3 = new Terminal(LocationMaker.generateLocation())
        Device term2_4 = new Terminal(LocationMaker.generateLocation())
        CableHandling.connectDevices(term1,term2)
        CableHandling.connectDevices(term2,term2_1)
        CableHandling.connectDevices(term2,term2_2)
        CableHandling.connectDevices(term2,term2_3)
        CableHandling.connectDevices(term2,term2_4)
        expect:

        CableHandling.areConnected(term2,term2_4)



    }
    def "wirecenter + terminal"(){
        given:
        List<Device> devices = new ArrayList<>()
        Device wirecenter = new Wirecenter(LocationMaker.generateLocation())
        Device terminal = new Terminal(LocationMaker.generateLocation())
        CableHandling.connectDevices(wirecenter,terminal)
        devices = wirecenter.listDeviceNetwork()
        expect:
        //println devices
        CableHandling.areConnected(wirecenter,terminal) & devices.size()==2
    }

    def "listing full network"(){
        given:
        List<Device> devices = new ArrayList<>()
        Device wirecenter = new Wirecenter(LocationMaker.generateLocation())
        Device term2 = new Terminal(LocationMaker.generateLocation())
        Device term2_1 = new Terminal(LocationMaker.generateLocation())
        Device term2_2 = new Terminal(LocationMaker.generateLocation())
        Device term2_3 = new Terminal(LocationMaker.generateLocation())
        Device term2_4 = new Terminal(LocationMaker.generateLocation())
        Device term3 = new Terminal(LocationMaker.generateLocation())
        CableHandling.connectDevices(wirecenter,term2)
        CableHandling.connectDevices(term2,term2_1)
        CableHandling.connectDevices(term2,term2_2)
        CableHandling.connectDevices(term2,term2_3)
        CableHandling.connectDevices(term2,term2_4)
        CableHandling.connectDevices(term2_2, term3)
        devices = wirecenter.listDeviceNetwork()
        expect:
        //println devices
        devices.size()==7
    }

    def "listing network from wirecenter perspective"(){
        given:
        List<Device> devices = new ArrayList<>()
        Device wirecenter = new Wirecenter(LocationMaker.generateLocation())
        Device term2 = new Terminal(LocationMaker.generateLocation())
        Device term2_1 = new Terminal(LocationMaker.generateLocation())
        Device term2_2 = new Terminal(LocationMaker.generateLocation())
        Device term2_3 = new Terminal(LocationMaker.generateLocation())
        Device term2_4 = new Terminal(LocationMaker.generateLocation())
        Device term3 = new Terminal(LocationMaker.generateLocation())
        CableHandling.connectDevices(wirecenter,term2)
        CableHandling.connectDevices(term2,term2_1)
        CableHandling.connectDevices(term2,term2_2)
        CableHandling.connectDevices(term2,term2_3)
        CableHandling.connectDevices(term2,term2_4)
        CableHandling.connectDevices(term2_2, term3)
        devices = wirecenter.listConnectedDevices()
        expect:
        //println devices
        devices.size()==6
    }

    def "find device"(){
        given:

        Device wirecenter = new Wirecenter(LocationMaker.generateLocation())
        Device term2 = new Terminal(LocationMaker.generateLocation())
        Device term2_1 = new Terminal(LocationMaker.generateLocation())
        Device term2_2 = new Terminal(LocationMaker.generateLocation())
        Device term2_3 = new Terminal(LocationMaker.generateLocation())
        Device term2_4 = new Terminal(LocationMaker.generateLocation())
        Device term3 = new Terminal(LocationMaker.generateLocation())
        int lookup = term2_2.getSerialNumber()
        CableHandling.connectDevices(wirecenter,term2)
        CableHandling.connectDevices(term2,term2_1)
        CableHandling.connectDevices(term2,term2_2)
        CableHandling.connectDevices(term2,term2_3)
        CableHandling.connectDevices(term2,term2_4)
        CableHandling.connectDevices(term2_2, term3)

        expect:

        wirecenter.isDeviceOnNetwork(lookup)
    }
}
