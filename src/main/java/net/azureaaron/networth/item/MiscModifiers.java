package net.azureaaron.networth.item;

import java.util.OptionalInt;

import org.jetbrains.annotations.ApiStatus;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.azureaaron.networth.utils.CodecUtils;

/**
 * More item-specific modifiers that cannot be broadly applied. The record signature may change at any time.
 */
@ApiStatus.Internal
public record MiscModifiers(OptionalInt pocketSackInASacks, OptionalInt jalapenoBooks, OptionalInt manaDisintegrators, OptionalInt transmissionTuners, OptionalInt ethermerge, OptionalInt farming4Dummies) {
	static final Codec<MiscModifiers> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			CodecUtils.optionalInt(Codec.INT.optionalFieldOf("sack_pss")).forGetter(MiscModifiers::pocketSackInASacks),
			CodecUtils.optionalInt(Codec.INT.optionalFieldOf("jalapeno_count")).forGetter(MiscModifiers::jalapenoBooks),
			CodecUtils.optionalInt(Codec.INT.optionalFieldOf("mana_disintegrator_count")).forGetter(MiscModifiers::manaDisintegrators),
			CodecUtils.optionalInt(Codec.INT.optionalFieldOf("tuned_transmission")).forGetter(MiscModifiers::transmissionTuners),
			CodecUtils.optionalInt(Codec.INT.optionalFieldOf("ethermerge")).forGetter(MiscModifiers::ethermerge),
			CodecUtils.optionalInt(Codec.INT.optionalFieldOf("farming_for_dummies_count")).forGetter(MiscModifiers::farming4Dummies))
			.apply(instance, MiscModifiers::new));
}
