package net.dmytrobashynskiy;

/**
 * TASKS
 *
 * Create a data storage structure for inventory
 * Solution: Inventory class
 *
 * Create an initializing method that creates three wcs, 12 terminals (8 of which are Service Locations)
 * and a sufficient number of cables in the inventory. The all items have to be connected.
 * Solution: 'spawn network' command in menu, spawns predefined network.
 *
 * Create a service to get information about inventory items
 * Solution: 'check inventory' command in menu, lists current inventory contents
 *
 * Create a service to add new inventory item
 * Solution: 'add device' command in the menu, allows to either add singular device or add whole new network
 *
 * Create a service to edit existed inventory item
 * Solution: 'edit device' command in the menu, allows to edit location.
 *
 * Create a service to get pairs status information
 * Solution: 'get cable info' command in the menu, shows info on pairs in said cable
 *
 * Create a service to create loop (service, which includes the full path from wc to service
 * location in accordance with the connection requirements)
 * Solution: 'create loop' command in menu, creates service loop from the network wirecenter to designated device
 *
 * Create a service to provide info about existed loops
 * Solution: 'list loops' command in menu, lists currently existing service loops.
 * */

public class Main {
    public static void main(String[] args) {
        Inventory inv = new Inventory();
        //fires up the menu system, which is the main method of user communication with the app
        inv.run();
    }
}
