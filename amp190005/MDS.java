/**
 * Implementation of Multi-dimensional Search (LP4).
 *
 * @author Anish Patel amp190005
 * @author Henil Doshi hxd180025
 * @author Neel Gotecha nxg180023
 * @author Ishan Shah ixs180019
 * @version 1.0
 */

// Change to your net id
package LP4.amp190005;

// If you want to create additional classes, place them in this file as subclasses of MDS

import java.util.*;
import java.util.List;

public class MDS {
	/**
	 * Fields of MDS
	 */
	TreeMap<Long, List> tree;
    HashMap<Long, TreeSet<Long>> table;
    TreeSet<Long> set;

    /**
     * Constructor for MDS
     */
    public MDS() {
        tree = new TreeMap<>();
        table = new HashMap<>();
        set = new TreeSet<>();
    }

    /* Public methods of MDS. Do not change their signatures.
       __________________________________________________________________
     */
    
    /**
     * a. Insert(id,price,list): insert a new item whose description is given
     * in the list.  If an entry with the same id already exists, then its
     * description and price are replaced by the new values, unless list
     * is null or empty, in which case, just the price is updated. 
     * Returns 1 if the item is new, and 0 otherwise.
     * @param id: id of the item
     * @param price: price of the item
     * @param list: description of the item
     * @return int Returns 1 if the item is new, and 0 otherwise.
     **/
    public int insert(long id, Money price, List<Long> list)  {
    	if (tree.containsKey(id)){
            if(!list.isEmpty()){
                delete(id);
                insert(id,price,list);
            }
            else {
                List value = new ArrayList();
                value = tree.get(id);
                value.remove(0);
                value.add(0,price);
                tree.put(id,value);
            }
            return 0;
    	}
        else{
            List value = new ArrayList();
            value.add(price);
            List ls  = new ArrayList();
            ls.addAll(list);
            value.add(ls);
            tree.put(id,value);

            // add keys to the table and initialize empty set for them
            for(Long l: list){
                if(!table.containsKey(l))
                    table.put(l,new TreeSet<>());
            }

            //add sets of IDs in
            for(Long l: list){
                for (Long s: table.get(l))
                    set.add(s);
            }

            if(set.isEmpty()){
                for(Long l: list){
                    table.get(l).add(id);
                }
            }
            else {
                for (Long l : list) {
                    //store a set corresponding respective value in list update it and again add it
                    table.get(l).add(id);
                }
            }
        }

        return 1;
    }

    /**
     * b. Find(id): return price of item with given id (or 0, if not found).
     * @param id Item's id whose price we want to return
     * @return Money price of given item id, if item not present then return 0
     */
    public Money find(long id) {
    	if (tree.containsKey(id)) {
    		return (Money) tree.get(id).get(0);
    	}
    	return new Money();
    }

    /**
     * c. Delete(id): delete item from storage. Returns the sum of the
     * long ints that are in the description of the item deleted,
     * or 0, if such an id did not exist.
     * @param id: id of the item to be deleted
     * @return Returns the sum of the long ints that are in the description of the item deleted
     **/
    public long delete(long id) {
        if(!tree.containsKey(id))
            return 0;

        ArrayList descList= new ArrayList();
        descList.addAll((Collection) tree.get(id).get(1));
        tree.remove(id);

        int sum = 0;
        for(Object l : descList){
            table.get(l).remove(id);
            sum += Integer.parseInt(l.toString());
            if(table.get(l).isEmpty())
                table.remove(l);
        }
        return sum;
    }

    /**
     * d. FindMinPrice(n): given a long int, find items whose description
     * contains that number (exact match with one of the long ints in the
     * item's description), and return lowest price of those items.
     * Return 0 if there is no such item.
     * @param n item's description
     * @return Money lowest price if there is such item, else return 0
     */
    public Money findMinPrice(long n) {
    	if(!table.containsKey(n)) {
    		return new Money();
    	}
    	TreeSet<Long> minSet = table.get(n);
    	Money minPrice = new Money();
    	boolean first = false;
    	for(Long id: minSet) {
    		Money tempPrice = (Money) tree.get(id).get(0);
    		if (minPrice.compareTo(tempPrice) == 1 || !first) {
    			minPrice = tempPrice;
    			first = true;
    		}
    	}
    	return minPrice;
    }

    /**
     * e. FindMaxPrice(n): given a long int, find items whose description
     * contains that number, and return highest price of those items.
     * Return 0 if there is no such item.
     * @param n item's description
     * @return Money Highest price if there is such item, else return 0
     */
    public Money findMaxPrice(long n) {
    	if(!table.containsKey(n)) {
    		return new Money();
    	}
    	TreeSet<Long> maxSet = table.get(n);
    	Money maxPrice = new Money();
    	boolean first = false;
    	for(Long id: maxSet) {
    		Money tempPrice = (Money) tree.get(id).get(0);
    		if (maxPrice.compareTo(tempPrice) == -1 || !first) {
    			maxPrice = tempPrice;
    			first = true;
    		}
    	}
    	return maxPrice;
    }

    /* 
       f. FindPriceRange(n,low,high): given a long int n, find the number
       of items whose description contains n, and in addition,
       their prices fall within the given range, [low, high].
    */
    public int findPriceRange(long n, Money low, Money high) {
	return 0;
    }

    /* 
       g. PriceHike(l,h,r): increase the price of every product, whose id is
       in the range [l,h] by r%.  Discard any fractional pennies in the new
       prices of items.  Returns the sum of the net increases of the prices.
    */
    public Money priceHike(long l, long h, double rate) {
	return new Money();
    }

    /*
      h. RemoveNames(id, list): Remove elements of list from the description of id.
      It is possible that some of the items in the list are not in the
      id's description.  Return the sum of the numbers that are actually
      deleted from the description of id.  Return 0 if there is no such id.
    */
    public long removeNames(long id, java.util.List<Long> list) {
	   long result =0;
       if (tree.containsKey(id)){
        List value = new ArrayList();
        value=tree.get(id);
        for( long i: list){
            if (value.contains(i)){
                result += i;
                this.tree.get(id).remove(i);
                this.table.get(i).remove(id);
            }
        }
       }
       return result;
    }
    
    // Do not modify the Money class in a way that breaks LP3Driver.java
    public static class Money implements Comparable<Money> { 
		long d;  
		int c;
		public Money() { 
			d = 0; 
			c = 0; 
		}
		public Money(long d, int c) { 
			this.d = d; 
			this.c = c; 
		}
		public Money(String s) {
		    String[] part = s.split("\\.");
		    int len = part.length;
		    if(len < 1) { 
		    	d = 0; 
		    	c = 0; 
		    }
		    else if(part.length == 1) { 
		    	d = Long.parseLong(s);  
		    	c = 0; 
		    }
		    else { 
			    d = Long.parseLong(part[0]);  
			    c = Integer.parseInt(part[1]); 
		    }
		}
		public long dollars() { 
			return d; 
		}
		public int cents() { 
			return c; 
		}
		public int compareTo(Money other) { // Complete this, if needed
			if (this.d > other.d)
				return 1;
			else if (this.d < other.d)
				return -1;
			else if (this.c > other.c)
				return 1;
			else if (this.c < other.c)
				return -1;
		    return 0;
		}
		public String toString() { 
			return d + "." + c; 
		}
    }

    public void printTable(){
        System.out.println("Hash Table: " + table);
        /*Set<Long> keys = table.keySet();
        for(Long k: keys){
            System.out.print(k + ": ");
            for(Object v: table.get(k))
                System.out.print(v.toString() + " ");
            System.out.println();
        }*/
    }

    public void printTree(){
        System.out.println("TreeMap: " + tree);
        /*Set<Long> keys = tree.keySet();

        for(Long k: keys){
            System.out.print(k + ": ");
            System.out.print(tree.get(k).toString() + " ");
            System.out.println();
        }*/
    }
}
