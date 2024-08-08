package net.azureaaron.networth.item;

import java.util.Optional;
import java.util.OptionalInt;

import org.jetbrains.annotations.ApiStatus;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.azureaaron.networth.utils.CodecUtils;

@ApiStatus.Internal
public record AccessoryUpgrades(Optional<String> enrichment, OptionalInt thunderCharges) {
	static final Codec<AccessoryUpgrades> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Codec.STRING.optionalFieldOf("talisman_enrichment").forGetter(AccessoryUpgrades::enrichment),
			CodecUtils.optionalInt(Codec.INT.optionalFieldOf("thunder_charge")).forGetter(AccessoryUpgrades::thunderCharges))
			.apply(instance, AccessoryUpgrades::new));
}
