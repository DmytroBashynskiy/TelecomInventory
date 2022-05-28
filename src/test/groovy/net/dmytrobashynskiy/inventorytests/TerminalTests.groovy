package net.dmytrobashynskiy.inventorytests


import net.dmytrobashynskiy.devices.device_utils.Device
import net.dmytrobashynskiy.devices.Terminal
import net.dmytrobashynskiy.utils.CableHandling
import net.dmytrobashynskiy.utils.LocationMaker
import spock.lang.Specification

class TerminalTests extends Specification {
    def "Basic term creation"(){
        given:
        Device term1 = new Terminal(LocationMaker.generateLocation())

        expect:
        term1.isServiceLocation()

    }

    def "Terminal with one connection"(){
        given:
        Device term1 = new Terminal(LocationMaker.generateLocation())
        Device term2 = new Terminal(LocationMaker.generateLocation())
        CableHandling.connectDevices(term1,term2)
        expect:
        CableHandling.areConnected(term1,term2)



    }

    def "Terminal with some connections"(){
        given:
        Device term1 = new Terminal(LocationMaker.generateLocation())
        Device term2 = new Terminal(LocationMaker.generateLocation())
        Device term3 = new Terminal(LocationMaker.generateLocation())
        Device term4 = new Terminal(LocationMaker.generateLocation())
        CableHandling.connectDevices(term1,term2)
        CableHandling.connectDevices(term1,term3)
        CableHandling.connectDevices(term1,term4)


        expect:
        CableHandling.areConnected(term1,term2) &
                CableHandling.areConnected(term1,term3) &
                CableHandling.areConnected(term1,term4)



    }


}
