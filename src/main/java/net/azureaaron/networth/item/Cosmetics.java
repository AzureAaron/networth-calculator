package net.azureaaron.networth.item;

import java.util.Optional;

import org.jetbrains.annotations.ApiStatus;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.azureaaron.networth.utils.CodecUtils;

@ApiStatus.Internal
public record Cosmetics(Optional<Object2IntMap<String>> runes, Optional<String> skin, Optional<String> dye, boolean shiny) {
	static final Codec<Cosmetics> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			CodecUtils.createObject2IntMapCodec(Codec.STRING).optionalFieldOf("runes").forGetter(Cosmetics::runes),
			Codec.STRING.optionalFieldOf("skin").forGetter(Cosmetics::skin),
			Codec.STRING.optionalFieldOf("dye_item").forGetter(Cosmetics::dye),
			Codec.BOOL.optionalFieldOf("is_shiny", false).forGetter(Cosmetics::shiny))
			.apply(instance, Cosmetics::new));
}
