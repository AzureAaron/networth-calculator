package net.azureaaron.networth.item;

import it.unimi.dsi.fastutil.ints.IntList;

/**
 * Intermediary to retrieve more complex to access item metadata such as a New Year Cake Bag's stored cake years.
 */
public interface ItemMetadataRetriever {

	IntList cakeBagCakeYears();
}
