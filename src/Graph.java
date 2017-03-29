import java.io.*;
import java.util.ArrayList;
import java.util.Collections;

/**
 * 
 * @author Brent Atchison (bma862)
 *
 */
public class Graph {
	
	/* ArrayList of Unlocked Houses */
	ArrayList<String> uHouses = new ArrayList<String>();
	
	/* List of Houses and Corresponding List of Lists of Keys Contained */
	ArrayList<String> houses = new ArrayList<String>();
	ArrayList<String> allKeys = new ArrayList<String>();
	ArrayList<Integer> neededKeys = new ArrayList<Integer>();
	ArrayList<ArrayList<String>> containedKeys = new ArrayList<ArrayList<String>>();

    /*
     * Creates a graph to represent the neighborhood, where unlocked is the file name for the unlocked houses
     * and keys is the file name for which houses have which keys.
     */
    public Graph(String unlocked, String keys) {
    	
    	/* BufferedReader for Unlocked Houses */
    	try {
    		BufferedReader unlocked_in = new BufferedReader(new FileReader(unlocked));
        	String s;
        	
        	/* Read until null */
        	while ((s = unlocked_in.readLine()) != null) { uHouses.add(s); }
        	unlocked_in.close();
		}
    	catch (IOException e) { e.printStackTrace(); }
    	
    	/* BufferedReader for Keys */
    	try {
    		BufferedReader keys_in = new BufferedReader(new FileReader(keys));
    		
    		/* Various parts to the input */
    		String s;
    		String house;
    		String keyList = null;
    		
    		/* Read until null */
    		while ((s = keys_in.readLine()) != null) {
    			
    			/* List of keys contained in house */
        		ArrayList<String> khouse = new ArrayList<String>();
        		
        		/* House that contains the keys */
    			house = s.split(":")[0];
    			
    			/* Ensure house actually contains keys */
    			if (s.split(":").length > 1) { keyList = s.split(":")[1];
    			
    				/* Loop through keys, adding to list of keys */
	    			for (int i = 0; i < keyList.split(",").length; i++) {
	    				
	    				/* Trim whitespace from ends before storing */
	    				khouse.add(keyList.split(",")[i].trim());
	    				
	    				/* Add to overall key list */
	    				allKeys.add(keyList.split(",")[i].trim());
	    			}
    			}
    			
    			/* Add houses and keys to respective arrays */
    			houses.add(house);
    			containedKeys.add(khouse);
    		}
    		
    		/* Gotta close that buffer */
    		keys_in.close();
		}
    	catch (IOException e) { e.printStackTrace(); }
    	
    	/* Sort final key array */
    	Collections.sort(allKeys);
    	
    	/* Make neededKeys the proper length */
    	for (int i = 0; i < houses.size(); i++) { neededKeys.add(0); }
    	
    	/* Count number of keys required for each house */
    	for (int i = 0; i < houses.size(); i++) {
    		int count = 0;
    		for (int j = 0; j < allKeys.size(); j++) {
    			if (houses.get(i).equals(allKeys.get(j))) {
    				neededKeys.set(i, ++count);
    			}
    		}
    	}
    	
    }

    /*
     * This method should return true if the Graph contains the vertex described by the input String.
     * 
	 * Vertex = house, so check ArrayList of houses
     */
    public boolean containsVertex(String node) {
    	return houses.contains(node);
    }

    /*
     * This method should return true if there is a direct edge from the vertex
     * represented by start String and end String.
     */
    public boolean containsEdge(String start, String end) {
    	
    	/* Find start house in housing list */
    	int index = houses.indexOf(start);
    	
    	/* In case house isn't on the list */
    	if (index == -1) return false;
    	
    	/* If the house contains the key to end, an edge exists */
    	if (containedKeys.get(index).contains(end)) return true;
    	
    	/* Otherwise it doesn't contain the key */
    	return false;
    }

    /*
     * This method returns true if the house represented by the input String is locked
     * and false is the house has been left unlocked.
     * 
     * Just check the ArrayList of unlocked houses
     */
    public boolean isLocked(String house) {
        return uHouses.contains(house);
    }
}
