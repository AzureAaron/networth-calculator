package net.azureaaron.networth.utils;

import java.util.Set;

import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectMaps;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;

public interface PrestigeConstants {
	Object2ObjectMap<String, Set<String>> PRESTIGES = Object2ObjectMaps.unmodifiable(Utils.make(new Object2ObjectOpenHashMap<>(), map -> {
		//Crimson Armour
		map.put("HOT_CRIMSON_HELMET", Set.of("CRIMSON_HELMET"));
		map.put("HOT_CRIMSON_CHESTPLATE", Set.of("CRIMSON_CHESTPLATE"));
		map.put("HOT_CRIMSON_LEGGINGS", Set.of("CRIMSON_LEGGINGS"));
		map.put("HOT_CRIMSON_BOOTS", Set.of("CRIMSON_BOOTS"));

		map.put("BURNING_CRIMSON_HELMET", Set.of("HOT_CRIMSON_HELMET", "CRIMSON_HELMET"));
		map.put("BURNING_CRIMSON_CHESTPLATE", Set.of("HOT_CRIMSON_CHESTPLATE", "CRIMSON_CHESTPLATE"));
		map.put("BURNING_CRIMSON_LEGGINGS", Set.of("HOT_CRIMSON_LEGGINGS", "CRIMSON_LEGGINGS"));
		map.put("BURNING_CRIMSON_BOOTS", Set.of("HOT_CRIMSON_BOOTS", "CRIMSON_BOOTS"));

		map.put("FIERY_CRIMSON_HELMET", Set.of("BURNING_CRIMSON_HELMET", "HOT_CRIMSON_HELMET", "CRIMSON_HELMET"));
		map.put("FIERY_CRIMSON_CHESTPLATE", Set.of("BURNING_CRIMSON_CHESTPLATE", "HOT_CRIMSON_CHESTPLATE", "CRIMSON_CHESTPLATE"));
		map.put("FIERY_CRIMSON_LEGGINGS", Set.of("BURNING_CRIMSON_LEGGINGS", "HOT_CRIMSON_LEGGINGS", "CRIMSON_LEGGINGS"));
		map.put("FIERY_CRIMSON_BOOTS", Set.of("BURNING_CRIMSON_BOOTS", "HOT_CRIMSON_BOOTS", "CRIMSON_BOOTS"));

		map.put("INFERNAL_CRIMSON_HELMET", Set.of("FIERY_CRIMSON_HELMET", "BURNING_CRIMSON_HELMET", "HOT_CRIMSON_HELMET", "CRIMSON_HELMET"));
		map.put("INFERNAL_CRIMSON_CHESTPLATE", Set.of("FIERY_CRIMSON_CHESTPLATE", "BURNING_CRIMSON_CHESTPLATE", "HOT_CRIMSON_CHESTPLATE", "CRIMSON_CHESTPLATE"));
		map.put("INFERNAL_CRIMSON_LEGGINGS", Set.of("FIERY_CRIMSON_LEGGINGS", "BURNING_CRIMSON_LEGGINGS", "HOT_CRIMSON_LEGGINGS", "CRIMSON_LEGGINGS"));
		map.put("INFERNAL_CRIMSON_BOOTS", Set.of("FIERY_CRIMSON_BOOTS", "BURNING_CRIMSON_BOOTS", "HOT_CRIMSON_BOOTS", "CRIMSON_BOOTS"));

		//Aurora Armour
		map.put("HOT_AURORA_HELMET", Set.of("AURORA_HELMET"));
		map.put("HOT_AURORA_CHESTPLATE", Set.of("AURORA_CHESTPLATE"));
		map.put("HOT_AURORA_LEGGINGS", Set.of("AURORA_LEGGINGS"));
		map.put("HOT_AURORA_BOOTS", Set.of("AURORA_BOOTS"));

		map.put("BURNING_AURORA_HELMET", Set.of("HOT_AURORA_HELMET", "AURORA_HELMET"));
		map.put("BURNING_AURORA_CHESTPLATE", Set.of("HOT_AURORA_CHESTPLATE", "AURORA_CHESTPLATE"));
		map.put("BURNING_AURORA_LEGGINGS", Set.of("HOT_AURORA_LEGGINGS", "AURORA_LEGGINGS"));
		map.put("BURNING_AURORA_BOOTS", Set.of("HOT_AURORA_BOOTS", "AURORA_BOOTS"));

		map.put("FIERY_AURORA_HELMET", Set.of("BURNING_AURORA_HELMET", "HOT_AURORA_HELMET", "AURORA_HELMET"));
		map.put("FIERY_AURORA_CHESTPLATE", Set.of("BURNING_AURORA_CHESTPLATE", "HOT_AURORA_CHESTPLATE", "AURORA_CHESTPLATE"));
		map.put("FIERY_AURORA_LEGGINGS", Set.of("BURNING_AURORA_LEGGINGS", "HOT_AURORA_LEGGINGS", "AURORA_LEGGINGS"));
		map.put("FIERY_AURORA_BOOTS", Set.of("BURNING_AURORA_BOOTS", "HOT_AURORA_BOOTS", "AURORA_BOOTS"));

		map.put("INFERNAL_AURORA_HELMET", Set.of("FIERY_AURORA_HELMET", "BURNING_AURORA_HELMET", "HOT_AURORA_HELMET", "AURORA_HELMET"));
		map.put("INFERNAL_AURORA_CHESTPLATE", Set.of("FIERY_AURORA_CHESTPLATE", "BURNING_AURORA_CHESTPLATE", "HOT_AURORA_CHESTPLATE", "AURORA_CHESTPLATE"));
		map.put("INFERNAL_AURORA_LEGGINGS", Set.of("FIERY_AURORA_LEGGINGS", "BURNING_AURORA_LEGGINGS", "HOT_AURORA_LEGGINGS", "AURORA_LEGGINGS"));
		map.put("INFERNAL_AURORA_BOOTS", Set.of("FIERY_AURORA_BOOTS", "BURNING_AURORA_BOOTS", "HOT_AURORA_BOOTS", "AURORA_BOOTS"));

		//Terror Armour
		map.put("HOT_TERROR_HELMET", Set.of("TERROR_HELMET"));
		map.put("HOT_TERROR_CHESTPLATE", Set.of("TERROR_CHESTPLATE"));
		map.put("HOT_TERROR_LEGGINGS", Set.of("TERROR_LEGGINGS"));
		map.put("HOT_TERROR_BOOTS", Set.of("TERROR_BOOTS"));

		map.put("BURNING_TERROR_HELMET", Set.of("HOT_TERROR_HELMET", "TERROR_HELMET"));
		map.put("BURNING_TERROR_CHESTPLATE", Set.of("HOT_TERROR_CHESTPLATE", "TERROR_CHESTPLATE"));
		map.put("BURNING_TERROR_LEGGINGS", Set.of("HOT_TERROR_LEGGINGS", "TERROR_LEGGINGS"));
		map.put("BURNING_TERROR_BOOTS", Set.of("HOT_TERROR_BOOTS", "TERROR_BOOTS"));

		map.put("FIERY_TERROR_HELMET", Set.of("BURNING_TERROR_HELMET", "HOT_TERROR_HELMET", "TERROR_HELMET"));
		map.put("FIERY_TERROR_CHESTPLATE", Set.of("BURNING_TERROR_CHESTPLATE", "HOT_TERROR_CHESTPLATE", "TERROR_CHESTPLATE"));
		map.put("FIERY_TERROR_LEGGINGS", Set.of("BURNING_TERROR_LEGGINGS", "HOT_TERROR_LEGGINGS", "TERROR_LEGGINGS"));
		map.put("FIERY_TERROR_BOOTS", Set.of("BURNING_TERROR_BOOTS", "HOT_TERROR_BOOTS", "TERROR_BOOTS"));

		map.put("INFERNAL_TERROR_HELMET", Set.of("FIERY_TERROR_HELMET", "BURNING_TERROR_HELMET", "HOT_TERROR_HELMET", "TERROR_HELMET"));
		map.put("INFERNAL_TERROR_CHESTPLATE", Set.of("FIERY_TERROR_CHESTPLATE", "BURNING_TERROR_CHESTPLATE", "HOT_TERROR_CHESTPLATE", "TERROR_CHESTPLATE"));
		map.put("INFERNAL_TERROR_LEGGINGS", Set.of("FIERY_TERROR_LEGGINGS", "BURNING_TERROR_LEGGINGS", "HOT_TERROR_LEGGINGS", "TERROR_LEGGINGS"));
		map.put("INFERNAL_TERROR_BOOTS", Set.of("FIERY_TERROR_BOOTS", "BURNING_TERROR_BOOTS", "HOT_TERROR_BOOTS", "TERROR_BOOTS"));

		//Hollow Armour
		map.put("HOT_HOLLOW_HELMET", Set.of("HOLLOW_HELMET"));
		map.put("HOT_HOLLOW_CHESTPLATE", Set.of("HOLLOW_CHESTPLATE"));
		map.put("HOT_HOLLOW_LEGGINGS", Set.of("HOLLOW_LEGGINGS"));
		map.put("HOT_HOLLOW_BOOTS", Set.of("HOLLOW_BOOTS"));

		map.put("BURNING_HOLLOW_HELMET", Set.of("HOT_HOLLOW_HELMET", "HOLLOW_HELMET"));
		map.put("BURNING_HOLLOW_CHESTPLATE", Set.of("HOT_HOLLOW_CHESTPLATE", "HOLLOW_CHESTPLATE"));
		map.put("BURNING_HOLLOW_LEGGINGS", Set.of("HOT_HOLLOW_LEGGINGS", "HOLLOW_LEGGINGS"));
		map.put("BURNING_HOLLOW_BOOTS", Set.of("HOT_HOLLOW_BOOTS", "HOLLOW_BOOTS"));

		map.put("FIERY_HOLLOW_HELMET", Set.of("BURNING_HOLLOW_HELMET", "HOT_HOLLOW_HELMET", "HOLLOW_HELMET"));
		map.put("FIERY_HOLLOW_CHESTPLATE", Set.of("BURNING_HOLLOW_CHESTPLATE", "HOT_HOLLOW_CHESTPLATE", "HOLLOW_CHESTPLATE"));
		map.put("FIERY_HOLLOW_LEGGINGS", Set.of("BURNING_HOLLOW_LEGGINGS", "HOT_HOLLOW_LEGGINGS", "HOLLOW_LEGGINGS"));
		map.put("FIERY_HOLLOW_BOOTS", Set.of("BURNING_HOLLOW_BOOTS", "HOT_HOLLOW_BOOTS", "HOLLOW_BOOTS"));

		map.put("INFERNAL_HOLLOW_HELMET", Set.of("FIERY_HOLLOW_HELMET", "BURNING_HOLLOW_HELMET", "HOT_HOLLOW_HELMET", "HOLLOW_HELMET"));
		map.put("INFERNAL_HOLLOW_CHESTPLATE", Set.of("FIERY_HOLLOW_CHESTPLATE", "BURNING_HOLLOW_CHESTPLATE", "HOT_HOLLOW_CHESTPLATE", "HOLLOW_CHESTPLATE"));
		map.put("INFERNAL_HOLLOW_LEGGINGS", Set.of("FIERY_HOLLOW_LEGGINGS", "BURNING_HOLLOW_LEGGINGS", "HOT_HOLLOW_LEGGINGS", "HOLLOW_LEGGINGS"));
		map.put("INFERNAL_HOLLOW_BOOTS", Set.of("FIERY_HOLLOW_BOOTS", "BURNING_HOLLOW_BOOTS", "HOT_HOLLOW_BOOTS", "HOLLOW_BOOTS"));

		//Fervor Armour
		map.put("HOT_FERVOR_HELMET", Set.of("FERVOR_HELMET"));
		map.put("HOT_FERVOR_CHESTPLATE", Set.of("FERVOR_CHESTPLATE"));
		map.put("HOT_FERVOR_LEGGINGS", Set.of("FERVOR_LEGGINGS"));
		map.put("HOT_FERVOR_BOOTS", Set.of("FERVOR_BOOTS"));

		map.put("BURNING_FERVOR_HELMET", Set.of("HOT_FERVOR_HELMET", "FERVOR_HELMET"));
		map.put("BURNING_FERVOR_CHESTPLATE", Set.of("HOT_FERVOR_CHESTPLATE", "FERVOR_CHESTPLATE"));
		map.put("BURNING_FERVOR_LEGGINGS", Set.of("HOT_FERVOR_LEGGINGS", "FERVOR_LEGGINGS"));
		map.put("BURNING_FERVOR_BOOTS", Set.of("HOT_FERVOR_BOOTS", "FERVOR_BOOTS"));

		map.put("FIERY_FERVOR_HELMET", Set.of("BURNING_FERVOR_HELMET", "HOT_FERVOR_HELMET", "FERVOR_HELMET"));
		map.put("FIERY_FERVOR_CHESTPLATE", Set.of("BURNING_FERVOR_CHESTPLATE", "HOT_FERVOR_CHESTPLATE", "FERVOR_CHESTPLATE"));
		map.put("FIERY_FERVOR_LEGGINGS", Set.of("BURNING_FERVOR_LEGGINGS", "HOT_FERVOR_LEGGINGS", "FERVOR_LEGGINGS"));
		map.put("FIERY_FERVOR_BOOTS", Set.of("BURNING_FERVOR_BOOTS", "HOT_FERVOR_BOOTS", "FERVOR_BOOTS"));

		map.put("INFERNAL_FERVOR_HELMET", Set.of("FIERY_FERVOR_HELMET", "BURNING_FERVOR_HELMET", "HOT_FERVOR_HELMET", "FERVOR_HELMET"));
		map.put("INFERNAL_FERVOR_CHESTPLATE", Set.of("FIERY_FERVOR_CHESTPLATE", "BURNING_FERVOR_CHESTPLATE", "HOT_FERVOR_CHESTPLATE", "FERVOR_CHESTPLATE"));
		map.put("INFERNAL_FERVOR_LEGGINGS", Set.of("FIERY_FERVOR_LEGGINGS", "BURNING_FERVOR_LEGGINGS", "HOT_FERVOR_LEGGINGS", "FERVOR_LEGGINGS"));
		map.put("INFERNAL_FERVOR_BOOTS", Set.of("FIERY_FERVOR_BOOTS", "BURNING_FERVOR_BOOTS", "HOT_FERVOR_BOOTS", "FERVOR_BOOTS"));
	}));
}