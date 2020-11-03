package test;

import org.bukkit.Location;
import java.io.File;
import java.util.HashMap;


/**
 * SQL statements to use for interacting with the database.
 *
 *
 * SELECT * FROM locations WHERE locations.name='home';
 *
 * INSERT INTO locations (name, x, y, z) VALUES ('temp', 10, -194, 34);
 *
 * SELECT name, x, y, z FROM locations;
 *
 *
 * CREATE TABLE "locations" (
 * 	"id"	INTEGER NOT NULL,
 * 	"name"	TEXT NOT NULL,
 * 	"x"	INTEGER NOT NULL,
 * 	"y"	INTEGER NOT NULL,
 * 	"z"	INTEGER NOT NULL,
 * 	PRIMARY KEY("id")
 * )
 */




public class SavedLocations extends ReadWriteObjectStream {

    private static final String FILE_NAME = "locations.db";
    private HashMap<String, Location> locations;

    public SavedLocations(File directoryLocation){
        super(directoryLocation, FILE_NAME);
        this.locations = retrieveDataFile();
    }

    /**
     * Add a location to the list.
     * @param key the ID of the location to be added to the list.
     * @param location the location to be added, associated with the key.
     * @return false if the key already exists in the list and can't be added; true if added successfully.
     */
    public boolean add(String key, Location location)
    {
        System.out.println("STARTING ADDING TO LIST");
        System.out.print("lower, ");
        key = key.toLowerCase();
        System.out.println("if-start, ");
        if (locations.get(key) != null){
            System.out.print("key not null, ");
            return false;
        }
        else{
            System.out.println("attempting to add key, ");
            locations.put(key, location);
            return true;
        }
    }

    /**
     * Removes the location associated with the given key from the list.
     * @param key the ID of the location to be removed from the HashMap.
     * @return false if the key given was invalid and could not be removed, true if the key was removed successfully.
     */
    public boolean remove(String key)
    {
        key = key.toLowerCase();
        if(locations.get(key) != null){
            locations.remove(key);
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * Get a location associated with the given key.
     * @param key the ID of the location to be removed from the HashMap.
     * @return location associated to the key passed, returns null if key is invalid.
     */
    public Location get(String key)
    {
        key = key.toLowerCase();
        if(locations.get(key) != null){
            return locations.get(key);
        }
        else {
            return null;
        }
    }

    /**
     * Returns a String list of all keys and their associated locations.
     * @return all keys and their associated locations as a string.
     */
    public String[] listAll(){
        String[] allLocations = new String[locations.size()];
        int counter = 0;
        for (String key: locations.keySet()) {
            key = key.toLowerCase();
            Location currentLocation = locations.get(key);
            allLocations[counter] = key + "> World: " + currentLocation.getWorld().getName() + ", X: " + currentLocation.getZ() + ", Y: " + currentLocation.getY() + ", Z: " + currentLocation.getZ();
            counter++;
        }
        return allLocations;
    }

    public boolean save() {
        return super.saveDataFile(locations);
    }
}
