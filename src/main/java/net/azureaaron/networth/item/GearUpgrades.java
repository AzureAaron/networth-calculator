package net.azureaaron.networth.item;

import java.util.Optional;
import java.util.OptionalInt;

import org.jetbrains.annotations.ApiStatus;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.azureaaron.networth.utils.CodecUtils;

@ApiStatus.Internal
public record GearUpgrades(OptionalInt hotPotatoes, OptionalInt artOfWars, OptionalInt woodSingularities, OptionalInt artOfPeaces, Optional<String> powerScroll) {
	static final Codec<GearUpgrades> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			CodecUtils.optionalInt(Codec.INT.optionalFieldOf("hot_potato_count")).forGetter(GearUpgrades::hotPotatoes),
			CodecUtils.optionalInt(Codec.INT.optionalFieldOf("art_of_war_count")).forGetter(GearUpgrades::artOfWars),
			CodecUtils.optionalInt(Codec.INT.optionalFieldOf("wood_singularity_count")).forGetter(GearUpgrades::woodSingularities),
			CodecUtils.optionalInt(Codec.INT.optionalFieldOf("artOfPeaceApplied")).forGetter(GearUpgrades::artOfPeaces),
			Codec.STRING.optionalFieldOf("power_ability_scroll").forGetter(GearUpgrades::powerScroll))
			.apply(instance, GearUpgrades::new));
}
