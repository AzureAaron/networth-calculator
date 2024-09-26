package net.azureaaron.networth.item;

import java.util.Optional;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record PetInfo(String type, double xp, String tier, int candies, Optional<String> heldItem, Optional<String> skin) {
	public static final Codec<PetInfo> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Codec.STRING.fieldOf("type").forGetter(PetInfo::type),
			Codec.DOUBLE.fieldOf("exp").forGetter(PetInfo::xp),
			Codec.STRING.fieldOf("tier").forGetter(PetInfo::tier),
			Codec.INT.optionalFieldOf("candyUsed", 0).forGetter(PetInfo::candies),
			Codec.STRING.optionalFieldOf("heldItem").forGetter(PetInfo::heldItem),
			Codec.STRING.optionalFieldOf("skin").forGetter(PetInfo::skin))
			.apply(instance, PetInfo::new));
	/**
	 * Provides for a fallback {code PetInfo} instance for deserialization failures.
	 */
	public static final PetInfo EMPTY = new PetInfo("", 0, "", 0, Optional.empty(), Optional.empty());
}
