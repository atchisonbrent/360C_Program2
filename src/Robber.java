import java.io.*;
import java.util.*;

/**
 */
public class Robber {
	
	/* Weight the Robber Can Carry */
	double carryWeight;
	
	/* Queue of Obtained Keys */
	Queue<String> keys = new LinkedList<String>();
	ArrayList<Integer> heldKeys = new ArrayList<Integer>();
	
	/* Order of Houses to Rob */
	ArrayList<String> houseOrder = new ArrayList<String>();
	
	/* Loot ArrayLists */
	ArrayList<String> item = new ArrayList<String>();
	ArrayList<Double> amount = new ArrayList<Double>();
	ArrayList<Integer> value = new ArrayList<Integer>();
	
	/* Loot for Printing */
	ArrayList<String> loot = new ArrayList<String>();
	ArrayList<Double> weight = new ArrayList<Double>();
	
	/* Scheduling ArrayLists */
	ArrayList<String> names = new ArrayList<String>();
	ArrayList<Double> startTimes = new ArrayList<Double>();
	ArrayList<Double> endTimes = new ArrayList<Double>();
	ArrayList<String> meet = new ArrayList<String>();

	/**
	 * This method should return true if the robber can rob all the houses in the neighborhood,
     * which are represented as a graph, and false if he cannot. The function should also print to the console the
     * order in which the robber should rob the houses if he can rob the houses. You do not need to print anything
     * if all the houses cannot be robbed.
     * 
	 * @param neighborhood
	 * @return
	 */
    public boolean canRobAllHouses(Graph neighborhood) {
    	
    	/* Make heldKeys the proper length */
    	for (int i = 0; i < neighborhood.houses.size(); i++) { heldKeys.add(0); }
    	
    	/* First rob all unlocked houses */
    	for (int i = 0; i < neighborhood.uHouses.size(); i++) {
    		
    		/* Add all unlocked houses first */
    		houseOrder.add(neighborhood.uHouses.get(i));
    		
    		/* Find location of unlocked house in all house list */
    		int index = neighborhood.houses.indexOf(neighborhood.uHouses.get(i));
    		
    		/* Set keyList to the list containing the keys that house holds */
    		ArrayList<String> keyList = neighborhood.containedKeys.get(index);
    		
    		/* Loop through found keys, adding all keys */
    		for (int j = 0; j < keyList.size(); j++) {
    			heldKeys.set(neighborhood.houses.indexOf(keyList.get(j)), heldKeys.get(neighborhood.houses.indexOf(keyList.get(j))) + 1);
    			keys.add(keyList.get(j));
    		}
    	}
    	
    	/* Rob locked houses with pilfered keys */
    	while (!keys.isEmpty()) {
    		
    		/* Remove first key */
    		String key = keys.remove();
    		
    		/* Check if we have the required number of keys to loot house */
    		if (heldKeys.get(neighborhood.houses.indexOf(key)) < (neighborhood.neededKeys.get(neighborhood.houses.indexOf(key)))) {
    			keys.add(key);
    			continue;
    		}
    		
    		/* Haven't looted yet, add house to looted houses */
    		if (!houseOrder.contains(key)) { houseOrder.add(key); }
    		
    		/* We have already looted, move on to next house */
    		else { continue; }
    		
    		/* Locate house we're looting in house list */
    		int index = neighborhood.houses.indexOf(key);
    		
    		/* See what keys that house contains */
    		ArrayList<String> keyList = neighborhood.containedKeys.get(index);
    		
    		/* Add any keys we find to end of key queue */
    		for (int j = 0; j < keyList.size(); j++) {
    			if (!houseOrder.contains(keyList.get(j))) {
    				keys.add(keyList.get(j));
    				heldKeys.set(neighborhood.houses.indexOf(keyList.get(j)), heldKeys.get(neighborhood.houses.indexOf(keyList.get(j))) + 1);
    			}
    		}
    	}
    	
    	/* Print List in Order if Robbing Successful */
    	if (houseOrder.containsAll(neighborhood.houses)) {
        	Iterator<String> orderIterator = houseOrder.iterator();
        	if (orderIterator.hasNext()) { System.out.print(orderIterator.next()); }
        	while (orderIterator.hasNext()) { System.out.print(", " + orderIterator.next()); }
        	System.out.println();
        	return true;
    	}
    	
        return false;
    }

    /**
     * Find optimal loot given a text file of the form:
     * 
     * Total carryWeight
	 * Ingredient, Amount, Value
	 * . . .
	 * Ingredient, Amount, Value
     * 
     * @param lootList
     */
    public void maximizeLoot(String lootList) {
    	
    	/* BufferedReader for Loot */
    	try {
    		BufferedReader loot_in = new BufferedReader(new FileReader(lootList));
    		
    		/* Read first line weight */
    		carryWeight = Double.parseDouble(loot_in.readLine());
    		
    		/* Read until null */
        	String s;
        	while ((s = loot_in.readLine()) != null) {
        		
        		/* List divider */
            	ArrayList<String> divList = new ArrayList<String>();
            	
            	/* Add all segments to divider */
    			for (int i = 0; i < s.split(",").length; i++) {
    				divList.add(s.split(",")[i].trim());
    			}
    			
    			/* Appropriate segments */
    			item.add(divList.get(0));
    			amount.add(Double.parseDouble(divList.get(1)));
    			value.add(Integer.parseInt(divList.get(2)));
        	}
        	
        	/* Close buffer */
        	loot_in.close();
		}
    	catch (IOException e) { e.printStackTrace(); }
    	
    	/* Figure Out Amounts of Each Item */
    	while (carryWeight > 0) {
    		
    		/* Find max valued item */
    		int maxValue = value.get(0);
    		for (int i = 0; i < value.size(); i++) {
    			if ((value.get(i) > maxValue) && (amount.get(i) > 0)) {
    				maxValue = value.get(i);
				}
    		}
    		
    		/* Break if all values are 0 */
    		if (maxValue == 0) { break; }
    		
    		/* Set index to location of maxValue */
    		int index = value.indexOf(maxValue);
    		
    		/* Set value at that index to 0 */
    		value.set(index, 0);
    		
    		/* Add item to loot output */
    		loot.add(item.get(index));
    		
    		/* Check if amount exceeds carryWeight */
    		if (amount.get(index) > carryWeight) {
    			weight.add(carryWeight);
    			carryWeight = 0;
    		}
    		
    		/* It doesn't, so take all of it */
    		else {
    			
    			/* Add amount taken to weight ArrayList */
    			weight.add(amount.get(index));
    			
    			/* Subtract what we've taken from our remaining carryWeight */
    			carryWeight -= amount.get(index);
    			
    			/* Set remaining amount of item to 0 */
    			amount.set(index, (double) 0);
    		}
    	}
    	
    	/* Print Finalized List */
    	for (int i = 0; i < loot.size(); i++) {
    		System.out.println(loot.get(i) + " " + weight.get(i));
    	}
    	
    }

    /**
     * Schedule meetings with most buyers possible
     * 
     * @param buyerList
     */
    public void scheduleMeetings(String buyerList) {
    	
    	/* BufferedReader for Scheduling */
    	try {
    		BufferedReader buyer_in = new BufferedReader(new FileReader(buyerList));
        	String s;
        	while ((s = buyer_in.readLine()) != null) {
        		
        		/* Add buyer name to name list */
        		names.add(s.split(",")[0]);
        		
        		/* Temporary String for holding times */
        		String times = s.split(",")[1].trim();
        		
        		/* Temporary Strings for storing start and end times */
        		String start = times.split("-")[0].trim();
        		String end = times.split("-")[1].trim();
        		
        		/* Stores the time itself without AM/PM */
        		String time;
        		double beginTime;
        		double endTime;
        		
        		/* Check if start time is morning or evening */
        		if (start.contains("am")) {
        			time = start.split("a")[0];
        			if (time.contains(":")) {
        				double hours = Double.parseDouble(time.split(":")[0]);
        				double minutes = (Double.parseDouble(time.split(":")[1])) / 60;
        				if (hours > 11) { hours = 0; }
        				beginTime = hours + minutes;
        			} else { beginTime = Double.parseDouble(time); }
        		}
        		else {
        			time = start.split("p")[0];
        			if (time.contains(":")) {
        				double hours = Double.parseDouble(time.split(":")[0]);
        				double minutes = (Double.parseDouble(time.split(":")[1])) / 60;
        				if (hours < 12) { hours += 12; }
        				beginTime = hours + minutes;
        			} else { beginTime = Double.parseDouble(time); }
        		}
        		
        		/* Add starting time to ArrayList */
        		startTimes.add(beginTime);
        		
        		/* Check if end time is morning or evening */
        		if (end.contains("am")) {
        			time = end.split("a")[0];
        			if (time.contains(":")) {
        				double hours = Double.parseDouble(time.split(":")[0]);
        				double minutes = ((Double.parseDouble(time.split(":")[1])) / 60) + 0.25;
        				if (hours > 11) { hours = 0; }
        				endTime = hours + minutes;
        			} else {
        				if (time.contains("12")) { endTime = 0.25; }
        				else { endTime = Double.parseDouble(time) + 0.25; }
    				}
        		}
        		else {
        			time = end.split("p")[0];
        			if (time.contains(":")) {
        				double hours = Double.parseDouble(time.split(":")[0]);
        				double minutes = ((Double.parseDouble(time.split(":")[1])) / 60) + 0.25;
        				if (hours < 12) { hours += 12; }
        				endTime = hours + minutes;
        			} else {
        				if (time.contains("12")) { endTime = Double.parseDouble(time) + 0.25; }
        				else { endTime = Double.parseDouble(time) + 12.25; }
    				}
        		}
        		
        		/* Add ending time to ArrayList */
        		endTimes.add(endTime);
        	}
        	buyer_in.close();
		}
    	catch (IOException e) { e.printStackTrace(); }
    	
    	/* Copy endTimes to refer back to later */
    	ArrayList<Double> endTimesCopy = new ArrayList<Double>();
    	for (int i = 0; i < endTimes.size(); i++) { endTimesCopy.add(endTimes.get(i)); }
    	
    	/* Find the earliest end time */
    	double earliest = endTimesCopy.get(0);
    	double previousEnd = 0;
    	int index = 0;
    	
    	/* Will break from loop inside */
    	while (true) {
    		
    		/* Loop for earliest end time */
        	for (int i = 0; i < endTimesCopy.size(); i++) {
        		if (endTimesCopy.get(i) < earliest) {
        			earliest = endTimesCopy.get(i);
        			index = i;
        		}
        	}
        	
        	/* We've gone through everyone */
        	if (earliest == 25) { break; }
        	
        	/* Meet the first person */
        	if (meet.size() == 0) {
        		meet.add(names.get(index));
        		previousEnd = endTimes.get(index);
        		endTimesCopy.set(index, (double) 25);
        		earliest = 100;
        		continue;
    		}
        	
        	/* Add subsequent people */
        	if (startTimes.get(index) >= previousEnd) {
        		meet.add(names.get(index));
        		previousEnd = endTimes.get(index);
        		earliest = 100;
        	}
        	
        	/* Clear old values */
        	endTimesCopy.set(index, (double) 25);
    		earliest = 100;
    	}
    	
    	/* Print Finalized List */
    	for (int i = 0; i < meet.size(); i++) {
    		System.out.println(meet.get(i));
    	}
    	
    }
}
