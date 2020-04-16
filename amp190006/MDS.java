
package LP4.amp190006;
import java.util.*;

// If you want to create additional classes, place them in this file as subclasses of MDS
public class MDS {

	// Inner class
	class MDSEntry {

		private long id;
		TreeSet<Long> descriptionSet;
		private Money price;

		// Constructor
		public MDSEntry(long id, TreeSet<Long> description, Money price) {
			this.id = id;
			this.descriptionSet = description;
			this.price = price;
		}
		public Money getPrice(){
		    return price;
        }
        public void setPrice(Money price) {
            this.price = price;
        }

		public String toString() {
			return "Id: " + id + ", Price: " + price + ", Description: " + descriptionSet;
		}

		public boolean equals(MDSEntry entry) {
			return this.id == entry.id;
		}

	}

	TreeMap<Long, MDSEntry> treeMap;//id to entry mapping
	HashMap<Long, TreeSet<Long>> hashMap; //id to description mapping

	// Constructors
	public MDS() {
		treeMap = new TreeMap<>();
		hashMap = new HashMap<>();
	}

	/*
	 * Public methods of MDS. Do not change their signatures.
	 * ______________________ a.
	 * Insert(id,price,list): insert a new item whose description is given in the
	 * list. If an entry with the same id already exists, then its description and
	 * price are replaced by the new values, unless list is null or empty, in which
	 * case, just the price is updated. Returns 1 if the item is new, and 0
	 * otherwise.
	 */
	public int insert(long id, Money price, java.util.List<Long> list) {
		// If treeMap doesn't contain id, add a new record and return 1
		if (!treeMap.containsKey(id)) {
			addToTreeSet(id, price, list);
			addToHashMap(id, list);
			return 1;
		} else { // else TreeMap contains the id
			// if list is empty just update the price
			if (list.isEmpty()) {
				updatePrice(id, price);
			} else {
				updateDescAndPrice(id, list, price);
			}
		}
		return 0;
	}

	// Create a new MDS entry object and add it to treeMap
	public void addToTreeSet(long productId, Money price, java.util.List<Long> descriptionIdList) {
		// Create a new MDS entry object
		MDSEntry mdsEntry = new MDSEntry(productId, new TreeSet<>(descriptionIdList), price);
		// Add to treeMap
		treeMap.put(productId, mdsEntry);
	}

	// Function to add entry to hashMap
	public void addToHashMap(long productId, java.util.List<Long> descriptionIdList) {
		// Iterate the list and check
		for (long descriptionId : descriptionIdList) {
			// If description id is present in the HashMap, add the product id to it and
			// then update the HashMap
			if (hashMap.containsKey(descriptionId)) {
				TreeSet<Long> oldTreeSet = hashMap.get(descriptionId);
				oldTreeSet.add(productId);
				hashMap.replace(descriptionId, oldTreeSet);
			}
			// HashMap doesn't contain any product Id corresponding to the desc Id
			// Add a new descId, productId to hashMap
			else {
				TreeSet<Long> newTreeSet = new TreeSet<>();
				newTreeSet.add(productId);
				hashMap.put(descriptionId, newTreeSet);
			}
		}
	}

	// if in insert, list is null, price is updated
	public void updatePrice(Long productId, Money newPrice) {
		// Retrieve the MDS Entry object
		MDSEntry mdsEntry = treeMap.get(productId);
		// update the price
		mdsEntry.price = newPrice;
		treeMap.replace(productId, mdsEntry);
	}

	public void updateDescAndPrice(Long productId, List<Long> list, Money price) {
		MDSEntry oldDescription = treeMap.get(productId);
		TreeSet<Long> oldTreeSet = oldDescription.descriptionSet;
		treeMap.replace(productId, new MDSEntry(productId, new TreeSet<Long>(list), price));
		removeFromHashMap(productId, oldTreeSet);
		addToHashMap(productId, list);
	}

	public void removeFromHashMap(Long productId, TreeSet<Long> oldTreeSet) {
		for (Long descriptionId : oldTreeSet) {
			if (hashMap.containsKey(descriptionId)) {
				TreeSet<Long> updateSet = hashMap.get(descriptionId);
				updateSet.remove(productId);
				// if after removing the product id, treeSet becomes null
				// remove from hashmap
				if (updateSet.size() == 0)
					hashMap.remove(descriptionId);
				// update the hashMap with the new treeSet
				else
					hashMap.replace(descriptionId, updateSet);
			}
		}
	}

	// b. Find(id): return price of item with given id (or 0, if not found).
	public Money find(long id) {
		if (treeMap.containsKey(id))
			return treeMap.get(id).price;
		return new Money();
	}

	/*
	 * c. Delete(id): delete item from storage. Returns the sum of the long ints
	 * that are in the description of the item deleted, or 0, if such an id did not
	 * exist.
	 */
	public long delete(long id) {
		if (treeMap.containsKey(id)) {
			TreeSet<Long> treeSetToDelete = treeMap.get(id).descriptionSet;
			treeMap.remove(id);
			removeFromHashMap(id, treeSetToDelete);
			Long sum = (long) 0;
			for (Long descId : treeSetToDelete)
				sum += descId;
			return sum;
		}
		return 0;
	}

	/*
	 * d. FindMinPrice(n): given a long int, find items whose description contains
	 * that number (exact match with one of the long ints in the item's
	 * description), and return lowest price of those items. Return 0 if there is no
	 * such item.
	 */
	public Money findMinPrice(long n) {
		if (!hashMap.containsKey(n)) {
			return new Money();
		}
		TreeSet<Long> idSet = hashMap.get(n);
		Money min = new Money();
		boolean flag = false;
		for (Long id : idSet) {
		    if(treeMap.containsKey(id)){
			Money current = treeMap.get(id).price;
			if (min.compareTo(current) == 1 || !flag) {
				min = current;
				flag = true;
			}
		    }
		}
		return min;
	}

	/*
	 * e. FindMaxPrice(n): given a long int, find items whose description contains
	 * that number, and return highest price of those items. Return 0 if there is no
	 * such item.
	 */
	public Money findMaxPrice(long n) {
		if (!hashMap.containsKey(n)) {
			return new Money();
		}
		TreeSet<Long> idSet = hashMap.get(n);
		Money max = new Money();
		boolean flag = false;
		for (Long id : idSet) {
		    if(treeMap.containsKey(id)){
			Money current = treeMap.get(id).price;
			if (max.compareTo(current) == -1 || !flag) {
				max = current;
				flag = true;
			}
		}
		}
		return max;
	}

	/*
	 * f. FindPriceRange(n,low,high): given a long int n, find the number of items
	 * whose description contains n, and in addition, their prices fall within the
	 * given range, [low, high].
	 */
	public int findPriceRange(long n, Money low, Money high) {
		int count = 0;
		if (hashMap.containsKey(n)) {
			TreeSet<Long> idSet = hashMap.get(n);
			for (Long id : idSet) {
			    if(treeMap.containsKey(id)){
			        if ((treeMap.get(id).price.compareTo(low)>= 0) && (treeMap.get(id).price.compareTo(high)<=0)) {
					count++;
				}
			}
			}
		}
		return count;
	}

	/*
	 * g. PriceHike(l,h,r): increase the price of every product, whose id is in the
	 * range [l,h] by r%. Discard any fractional pennies in the new prices of items.
	 * Returns the sum of the net increases of the prices.
	 */

	public Money priceHike(long l, long h, double rate) {
        double net = 0;
        Set<Map.Entry<Long, MDSEntry>> treeMapEntrySet = treeMap.entrySet();
        for (Map.Entry<Long, MDSEntry> entry : treeMapEntrySet) {
            Long key = entry.getKey();
            MDSEntry obj = entry.getValue();
            if (key >= l && key <= h) {
                long price = obj.getPrice().totalCents();
                long updatedPrice = price + (long)(price * rate / 100.0);
                obj.setPrice(new Money(updatedPrice / 100, (int)(updatedPrice % 100)));
                treeMap.put(obj.id, obj);
                net += (updatedPrice - price);
            }

        }
        return new Money((long)(net/100.0), (int) (net%100));
}

	/*
	 * h. RemoveNames(id, list): Remove elements of list from the description of id.
	 * It is possible that some of the items in the list are not in the id's
	 * description. Return the sum of the numbers that are actually deleted from the
	 * description of id. Return 0 if there is no such id.
	 */
	public long removeNames(long id, java.util.List<Long> list) {
		if(treeMap.containsKey(id)) {
			TreeSet<Long> ts = treeMap.get(id).descriptionSet;
			TreeSet<Long> desc = new TreeSet<>(list);
			if (list.isEmpty())
				return 0;
			Long sum = 0L;
			for (Long descId : list) {
				if (ts.contains(descId)) {
					sum += descId;
					if (hashMap.containsKey(descId)) {
						TreeSet<Long> updateValues = hashMap.get(descId);
						updateValues.remove(id);
						if (updateValues.isEmpty()) {
							hashMap.remove(descId);
						} else {
							hashMap.replace(descId, updateValues);
						}
					}
					ts.remove(descId);
				}
			}
			MDSEntry mdsEntry = new MDSEntry(id, ts, treeMap.get(id).price);
			treeMap.replace(id, mdsEntry);
			return sum;
		}
		return 0;
	}

	// Do not modify the Money class in a way that breaks LP3Driver.java
	public static class Money implements Comparable<Money> {

		long d;
		int c;
		boolean oneDigitCent=false;


        public Money() {
			d = 0;
			c = 0;
		}
        public long totalCents() {
            return d * 100 + c;
        }

		public Money(long d, int c) {
			this.d = d;
			this.c = c;
		}

		public Money(String s) {
			String[] part = s.split("\\.");
			int len = part.length;
			if (len < 1) {
				d = 0;
				c = 0;
			} else if (part.length == 1) {
				d = Long.parseLong(s);
				c = 0;
			} else {
			    d = Long.parseLong(part[0]);
                c = Integer.parseInt(part[1]);
            }oneDigitCent = c<10 && c!= 0;

		}

		public long dollars() {
			return d;
		}

		public int cents() {
			return c;
		}



		public int compareTo(Money other) { // Complete this, if needed
			if (this.d > other.d) {
				return 1;
			} else if (this.d < other.d) {
				return -1;
			} else if (this.c > other.c) {
				return 1;
			} else if (this.c < other.c) {
				return -1;
			}else {
				return 0;
			}
		}

        public String toString() {
            return (d + "." + c);
        }
	}

}