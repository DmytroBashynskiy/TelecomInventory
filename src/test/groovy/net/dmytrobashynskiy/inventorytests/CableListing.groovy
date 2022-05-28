package net.dmytrobashynskiy.inventorytests


import net.dmytrobashynskiy.devices.device_utils.Device
import net.dmytrobashynskiy.devices.Terminal
import net.dmytrobashynskiy.devices.Wirecenter
import net.dmytrobashynskiy.utils.CableHandling
import net.dmytrobashynskiy.utils.LocationMaker
import spock.lang.Specification

class CableListing extends Specification{
    def "simple cable listing"(){
        given:

        Device wirecenter = new Wirecenter(LocationMaker.generateLocation())
        Device terminal = new Terminal(LocationMaker.generateLocation())
        CableHandling.connectDevices(wirecenter,terminal)


        expect:

        wirecenter.getCablesList().size()==1
    }

    def "large network cable listing"(){
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

        expect:
        println wirecenter.getCablesList()
        wirecenter.getCablesList().size()==7



    }
}
