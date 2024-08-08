package net.azureaaron.networth.data;

import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.azureaaron.networth.utils.CodecUtils;

/**
 * Skyblock item information from the Hypixel API in record form. Use the {@link #LIST_CODEC} to obtain a list of this that can be passed
 * to the {@link net.azureaaron.networth.ItemCalculator ItemCalculator}.
 * 
 * @see <a href="https://api.hypixel.net/v2/resources/skyblock/items">Hypixel API - Skyblock Items</a>
 */
public record SkyblockItemData(String id, Optional<String> category, Optional<List<List<GearUpgrade>>> upgradeCosts, Optional<List<GemstoneSlot>> gemstoneSlots, Optional<PrestigeItem> prestige) {
	private static final Codec<SkyblockItemData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Codec.STRING.fieldOf("id").forGetter(SkyblockItemData::id),
			Codec.STRING.optionalFieldOf("category").forGetter(SkyblockItemData::category),
			GearUpgrade.CODEC.listOf().listOf().optionalFieldOf("upgrade_costs").forGetter(SkyblockItemData::upgradeCosts),
			GemstoneSlot.CODEC.listOf().optionalFieldOf("gemstone_slots").forGetter(SkyblockItemData::gemstoneSlots),
			PrestigeItem.CODEC.optionalFieldOf("prestige").forGetter(SkyblockItemData::prestige))
			.apply(instance, SkyblockItemData::new));
	private static final Codec<List<SkyblockItemData>> LIST_CODEC = CODEC.listOf();
	public static final Codec<Object2ObjectMap<String, SkyblockItemData>> MAP_CODEC = LIST_CODEC.xmap(SkyblockItemData::list2Map, map -> new ObjectArrayList<>(map.values()));

	/**
	 * Converts a list of {@code SkyblockItemData} to a map.
	 */
	private static Object2ObjectMap<String, SkyblockItemData> list2Map(List<SkyblockItemData> items) {
		return items.stream().collect(Collectors.toMap(SkyblockItemData::id, Function.identity(), (a, b) -> a, Object2ObjectOpenHashMap::new));
	}

	public record GearUpgrade(Optional<String> itemId, Optional<String> essenceType, OptionalInt amount) {
		private static final Codec<GearUpgrade> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				Codec.STRING.optionalFieldOf("item_id").forGetter(GearUpgrade::itemId),
				Codec.STRING.optionalFieldOf("essence_type").forGetter(GearUpgrade::essenceType),
				CodecUtils.optionalInt(Codec.INT.optionalFieldOf("amount")).forGetter(GearUpgrade::amount))
				.apply(instance, GearUpgrade::new));
	}

	public record PrestigeItem(String itemId, List<GearUpgrade> costs) {
		private static final Codec<PrestigeItem> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				Codec.STRING.fieldOf("item_id").forGetter(PrestigeItem::itemId),
				GearUpgrade.CODEC.listOf().fieldOf("costs").forGetter(PrestigeItem::costs))
				.apply(instance, PrestigeItem::new));
	}

	public record GemstoneSlot(String type, Optional<List<GemstoneSlotCost>> costs) {
		private static final Codec<GemstoneSlot> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				Codec.STRING.fieldOf("slot_type").forGetter(GemstoneSlot::type),
				GemstoneSlotCost.CODEC.listOf().optionalFieldOf("costs").forGetter(GemstoneSlot::costs))
				.apply(instance, GemstoneSlot::new));

		public record GemstoneSlotCost(String type, Optional<String> itemId, OptionalInt amount, OptionalInt coins) {
			private static final Codec<GemstoneSlotCost> CODEC = RecordCodecBuilder.create(instance -> instance.group(
					Codec.STRING.fieldOf("type").forGetter(GemstoneSlotCost::type),
					Codec.STRING.optionalFieldOf("item_id").forGetter(GemstoneSlotCost::itemId),
					CodecUtils.optionalInt(Codec.INT.optionalFieldOf("amount")).forGetter(GemstoneSlotCost::amount),
					CodecUtils.optionalInt(Codec.INT.optionalFieldOf("coins")).forGetter(GemstoneSlotCost::coins))
					.apply(instance, GemstoneSlotCost::new));
		}
	}
}
