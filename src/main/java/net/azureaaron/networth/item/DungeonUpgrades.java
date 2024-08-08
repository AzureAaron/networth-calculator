package net.azureaaron.networth.item;

import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

import org.jetbrains.annotations.ApiStatus;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.azureaaron.networth.utils.CodecUtils;

@ApiStatus.Internal
public record DungeonUpgrades(/* Mob Drops */ OptionalInt itemTier, /* Essence Upgrades */ OptionalInt itemLevel, Optional<List<String>> abilityScrolls) {
	static final Codec<DungeonUpgrades> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			CodecUtils.optionalInt(Codec.INT.optionalFieldOf("item_tier")).forGetter(DungeonUpgrades::itemTier),
			CodecUtils.optionalInt(Codec.INT.optionalFieldOf("dungeon_item_level")).forGetter(DungeonUpgrades::itemLevel),
			Codec.STRING.listOf().optionalFieldOf("ability_scroll").forGetter(DungeonUpgrades::abilityScrolls))
			.apply(instance, DungeonUpgrades::new));
}
