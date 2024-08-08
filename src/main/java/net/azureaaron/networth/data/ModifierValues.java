package net.azureaaron.networth.data;

import it.unimi.dsi.fastutil.objects.Object2DoubleMap;
import it.unimi.dsi.fastutil.objects.Object2DoubleMaps;
import it.unimi.dsi.fastutil.objects.Object2DoubleOpenHashMap;
import net.azureaaron.networth.utils.Utils;

/**
 * @implSpec The default return value of both {@link Object2DoubleMap}s must be {@code 1}.
 */
public record ModifierValues(Object2DoubleMap<String> regular, Object2DoubleMap<String> enchantments) {
	public static final Object2DoubleMap<String> REGULAR_DEFAULTS = Object2DoubleMaps.unmodifiable(Utils.make(new Object2DoubleOpenHashMap<>(), map -> {
		map.put("enrichment", 0.5);
		map.put("farming4Dummies", 0.5);
		map.put("powerScroll", 0.5);
		map.put("woodSingularity", 0.5);
		map.put("artOfWar", 0.6);
		map.put("fumingPotatoBook", 0.6);
		map.put("gemstoneSlots", 0.6);
		map.put("runes", 0.6);
		map.put("skins", 0.67); //From some quick math skins have an average of 33% loss in value when applied
		map.put("transmissionTuner", 0.7);
		map.put("pocketSackInASack", 0.7);
		map.put("essence", 0.75);
		map.put("goldenBounty", 0.75);
		map.put("silex", 0.75);
		map.put("artOfPeace", 0.8);
		map.put("divanPowderCoating", 0.8);
		map.put("jalapenoBook", 0.8);
		map.put("manaDisintegrator", 0.8);
		map.put("recombobulator", 0.8);
		map.put("thunderInABottle", 0.8);
		map.put("enchantments", 0.85);
		map.put("shensAuctionPrice", 0.85);
		map.put("dye", 0.9);
		map.put("gemstoneChambers", 0.9);
		map.put("attributes", 1);
		map.put("drillPart", 1);
		map.put("etherwarp", 1);
		map.put("masterStars", 1);
		map.put("gemstones", 1);
		map.put("hotPotatoBook", 1);
		map.put("polarvoidBook", 1);
		map.put("prestigeItem", 1);
		map.put("reforge", 1);
		map.put("winningBid", 1);
		map.put("witherScroll", 1);

		map.put("petCandy", 0.65);
		map.put("soulboundPetSkins", 0.8);
		map.put("petItem", 1);

		//Set default return value
		map.defaultReturnValue(1d);
	}));

	public static final Object2DoubleMap<String> ENCHANTMENT_DEFAULTS = Object2DoubleMaps.unmodifiable(Utils.make(new Object2DoubleOpenHashMap<>(), map -> {
		map.put("COUNTER_STRIKE", 0.2);
		map.put("BIG_BRAIN", 0.35);
		map.put("ULTIMATE_INFERNO", 0.35);
		map.put("OVERLOAD", 0.35);
		map.put("ULTIMATE_SOUL_EATER", 0.35);
		map.put("ULTIMATE_FATAL_TEMPO", 0.65);

		//Set default return value
		map.defaultReturnValue(1d);
	}));

	public static final ModifierValues DEFAULT = new ModifierValues(REGULAR_DEFAULTS, ENCHANTMENT_DEFAULTS);
}
