package net.azureaaron.networth.item;

import java.util.Locale;

import com.mojang.serialization.Dynamic;

/**
 * To construct an instance, use the static factory provided. The record's signature is an implementation detail and subject to change.
 */
public record SkyblockItemStack(String id, int count, ItemMetadata metadata) {

	/**
	 * Returns a SkyblockItemStack instance for use with the {@link net.azureaaron.networth.ItemCalculator ItemCalculator}.
	 * 
	 * @param id         the {@code ItemStack}'s Skyblock Item Id
	 * @param count      the {@code ItemStack}'s count
	 * @param customData a {@link Dynamic} of the {@code ItemStack}'s custom data
	 * @param retriever  a {@link ItemMetadataRetriever} instance
	 * 
	 * @apiNote All parameters must not be null.
	 */
	public static SkyblockItemStack of(String id, int count, Dynamic<?> customData, ItemMetadataRetriever retriever) {
		return new SkyblockItemStack(id.toUpperCase(Locale.CANADA), count, DynamicItemMetadata.of(customData, retriever));
	}
}
