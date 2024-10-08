package net.azureaaron.networth.utils;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntMaps;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectMaps;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import it.unimi.dsi.fastutil.objects.ObjectSet;

public interface ItemConstants {
	Set<String> COLOURED_ANNIVERSARY_ITEMS = Set.of("PARTY_HAT_CRAB", "PARTY_HAT_CRAB_ANIMATED", "BALLOON_HAT_2024");

	int PICKONIMBUS_DURABILITY = 2000;

	Map<String, Set<String>> BLOCKED_ENCHANTMENTS = Map.of(
			"BONE_BOOMERANG", Set.of("OVERLOAD", "POWER", "ULTIMATE_SOUL_EATER"),
			"DEATH_BOW", Set.of("OVERLOAD", "POWER", "ULTIMATE_SOUL_EATER"),
			"GARDENING_AXE", Set.of("REPLENISH"),
			"GARDENING_HOE", Set.of("REPLENISH"),
			"ADVANCED_GARDENING_AXE", Set.of("REPLENISH"),
			"ADVANCED_GARDENING_HOE", Set.of("REPLENISH")
			);
	Object2IntMap<String> IGNORED_ENCHANTMENTS = Object2IntMaps.unmodifiable(Utils.make(new Object2IntOpenHashMap<>(), map -> map.put("SCAVENGER", 5)));
	Set<String> STACKING_ENCHANTMENTS = Set.of("EXPERTISE", "COMPACT", "CULTIVATING", "CHAMPION", "HECATOMB", "TOXOPHILITE");

	Set<String> IGNORE_SILEX = Set.of("PROMISING_SPADE", "STONK_PICKAXE");

	Map<String, String> ATTRIBUTE_BASE_COSTS = Map.of(
			"GLOWSTONE_GAUNTLET", "GLOWSTONE_GAUNTLET",
			"VANQUISHED_GLOWSTONE_GAUNTLET", "GLOWSTONE_GAUNTLET",
			"BLAZE_BELT", "BLAZE_BELT",
			"VANQUISHED_BLAZE_BELT", "BLAZE_BELT",
			"MAGMA_NECKLACE", "MAGMA_NECKLACE",
			"VANQUISHED_MAGMA_NECKLACE", "MAGMA_NECKLACE",
			"MAGMA_ROD", "MAGMA_ROD",
			"INFERNO_ROD", "MAGMA_ROD",
			"HELLFIRE_ROD", "MAGMA_ROD");

	Set<String> VALID_RUNES = Set.of(
			"MUSIC_RUNE_1",
			"MUSIC_RUNE_2",
			"MUSIC_RUNE_3",
			"MEOW_MUSIC_RUNE_3",
			"ENCHANT_RUNE_1",
			"ENCHANT_RUNE_2",
			"ENCHANT_RUNE_3",
			"GRAND_SEARING_RUNE_3",
			"SPELLBOUND_RUNE_3",
			"GRAND_FREEZING_RUNE_3",
			"PRIMAL_FEAR_RUNE_3",
			"GOLDEN_CARPET_RUNE_3",
			"ICE_SKATES_RUNE_3",
			"BARK_TUNES_RUNE_3",
			"SMITTEN_RUNE_3",
			"RAINY_DAY_RUNE_3");

	Set<String> ENRICHMENTS = Set.of(
			"TALISMAN_ENRICHMENT_ATTACK_SPEED",
			"TALISMAN_ENRICHMENT_CRITICAL_CHANCE",
			"TALISMAN_ENRICHMENT_CRITICAL_DAMAGE",
			"TALISMAN_ENRICHMENT_DEFENSE",
			"TALISMAN_ENRICHMENT_FEROCITY",
			"TALISMAN_ENRICHMENT_HEALTH",
			"TALISMAN_ENRICHMENT_INTELLIGENCE",
			"TALISMAN_ENRICHMENT_MAGIC_FIND",
			"TALISMAN_ENRICHMENT_SEA_CREATURE_CHANCE",
			"TALISMAN_ENRICHMENT_STRENGTH",
			"TALISMAN_ENRICHMENT_WALK_SPEED");

	Object2ObjectMap<String, String> REFORGES = Object2ObjectMaps.unmodifiable(Utils.make(new Object2ObjectOpenHashMap<>(), map -> {
		//Page 1
		map.put("stiff", "HARDENED_WOOD");
		map.put("salty", "SALT_CUBE");
		map.put("aote_stone", "AOTE_STONE");
		map.put("blazing", "BLAZEN_SPHERE");
		map.put("waxed", "BLAZE_WAX");
		map.put("rooted", "BURROWING_SPORES");
		map.put("candied", "CANDY_CORN");
		map.put("perfect", "DIAMOND_ATOM");
		map.put("fleet", "DIAMONITE");
		map.put("fabled", "DRAGON_CLAW");
		map.put("spiked", "DRAGON_SCALE");
		map.put("royal", "DWARVEN_TREASURE");
		map.put("hyper", "ENDSTONE_GEODE");
		map.put("coldfusion", "ENTROPY_SUPPRESSOR");
		map.put("blooming", "FLOWERING_BOUQUET");
		map.put("fanged", "FULL_JAW_FANGED_KIT");
		map.put("jaded", "JADERALD");
		map.put("jerry", "JERRY_STONE");
		map.put("magnetic", "LAPIS_CRYSTAL");
		map.put("earthy", "LARGE_WALNUT");
		map.put("fortified", "METEOR_SHARD");
		map.put("gilded", "MIDAS_JEWEL");
		map.put("cubic", "MOLTEN_CUBE");
		map.put("necrotic", "NECROMANCER_BROOCH");
		map.put("fruitful", "ONYX"); //Black Diamond
		map.put("precise", "OPTICAL_LENS");
		map.put("mossy", "OVERGROWN_GRASS");
		map.put("pitchin", "PITCHIN_KOI");

		//Page 2
		map.put("undead", "PREMIUM_FLESH");
		map.put("blood_soaked", "PRESUMED_GALLON_OF_RED_PAINT");
		map.put("mithraic", "PURE_MITHRIL");
		map.put("reinforced", "RARE_DIAMOND");
		map.put("ridiculous", "RED_NOSE");
		map.put("loving", "RED_SCARF");
		map.put("auspicious", "ROCK_GEMSTONE"); //Dwarven Geode
		map.put("treacherous", "RUSTY_ANCHOR");
		map.put("headstrong", "SALMON_OPAL"); //Kaleidoscopic Mineral
		map.put("strengthened", "SEARING_STONE");
		map.put("glistening", "SHINY_PRISM");
		map.put("bustling", "SKYMART_BROCHURE");
		map.put("spiritual", "SPIRIT_DECOY");
		map.put("suspicious", "SUSPICIOUS_VIAL");
		map.put("snowy", "TERRY_SNOWGLOBE");
		map.put("dimensional", "TITANIUM_TESSERACT");
		map.put("ambered", "AMBER_MATERIAL");
		map.put("beady", "BEADY_EYES");
		map.put("blessed", "BLESSED_FRUIT");
		map.put("bulky", "BULKY_STONE");
		map.put("buzzing", "CLIPPED_WINGS");
		map.put("submerged", "DEEP_SEA_ORB");
		map.put("renowned", "DRAGON_HORN");
		map.put("festive", "FROZEN_BAUBLE");
		map.put("giant", "GIANT_TOOTH");
		map.put("lustrous", "GLEAMING_CRYSTAL");
		map.put("bountiful", "GOLDEN_BALL");
		map.put("chomp", "KUUDRA_MANDIBLE");

		//Page 3
		map.put("lucky", "LUCKY_DICE");
		map.put("stellar", "PETRIFIED_STARFALL");
		map.put("scraped", "POCKET_ICEBERG");
		map.put("ancient", "PRECURSOR_GEAR");
		map.put("refined", "REFINED_AMBER"); //Andesite Whetstone
		map.put("empowered", "SADAN_BROOCH");
		map.put("withered", "WITHER_BLOOD");
		map.put("glacial", "FRIGID_HUSK");
		map.put("heated", "HOT_STUFF"); //Scorched Topaz

		//Non Bazaar
		map.put("dirty", "DIRT_BOTTLE");
		map.put("moil", "MOIL_LOG");
		map.put("toil", "TOIL_LOG");
		map.put("greater_spook", "BOO_STONE");
	}));

	Set<String> GEMSTONE_GROUP_SLOT_TYPES = Set.of("COMBAT", "OFFENSIVE", "DEFENSIVE", "MINING", "UNIVERSAL", "CHISEL");
	Set<String> DIVAN_GEAR = Set.of("DIVAN_HELMET", "DIVAN_CHESTPLATE", "DIVAN_LEGGINGS", "DIVAN_BOOTS", "DIVAN_PENDANT");

	Set<String> KUUDRA_ARMOUR = getKuudraArmourIds()
			.collect(Collectors.toUnmodifiableSet());
	Set<String> KUUDRA_HELMETS = getKuudraArmourIds()
			.filter(id -> id.endsWith("_HELMET"))
			.collect(Collectors.toUnmodifiableSet());
	Set<String> KUUDRA_OTHERS = getKuudraArmourIds()
			.filter(id -> !id.endsWith("_HELMET"))
			.collect(Collectors.toUnmodifiableSet());

	List<String> MASTER_STARS = List.of("FIRST_MASTER_STAR", "SECOND_MASTER_STAR", "THIRD_MASTER_STAR", "FOURTH_MASTER_STAR", "FIFTH_MASTER_STAR");

	private static Stream<String> getKuudraArmourIds() {
		ObjectSet<String> collectedIds = new ObjectOpenHashSet<>();

		for (Object2ObjectMap.Entry<String, Set<String>> entry : Object2ObjectMaps.fastIterable(PrestigeConstants.PRESTIGES)) {
			collectedIds.add(entry.getKey());

			//We also have to iterate the set since there isn't any map entries for basic kuudra armour pieces
			for (String id : entry.getValue()) {
				collectedIds.add(id);
			}
		}

		return collectedIds.stream();
	}
}
