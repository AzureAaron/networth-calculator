package net.azureaaron.networth.item;

import java.util.Optional;
import java.util.OptionalInt;

import org.jetbrains.annotations.ApiStatus;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.azureaaron.networth.utils.CodecUtils;

/**
 * Contains metadata about limited edition items such as party hats, and new year cakes.
 */
@ApiStatus.Internal
public record LimitedEditionInfo(OptionalInt newYearCakeYear, Optional<String> partyHatColour, Optional<String> partyHatEmoji) {
	static final Codec<LimitedEditionInfo> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			CodecUtils.optionalInt(Codec.INT.optionalFieldOf("new_years_cake")).forGetter(LimitedEditionInfo::newYearCakeYear),
			Codec.STRING.optionalFieldOf("party_hat_color").forGetter(LimitedEditionInfo::partyHatColour),
			Codec.STRING.optionalFieldOf("party_hat_emoji").forGetter(LimitedEditionInfo::partyHatEmoji))
			.apply(instance, LimitedEditionInfo::new));
}
