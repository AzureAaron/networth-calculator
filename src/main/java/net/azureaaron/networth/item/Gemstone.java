package net.azureaaron.networth.item;

import java.util.Map;

import org.jetbrains.annotations.ApiStatus;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;

@ApiStatus.Internal
public record Gemstone(String quality) {
	private static final Codec<Gemstone> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Codec.STRING.fieldOf("quality").forGetter(Gemstone::quality))
			.apply(instance, Gemstone::new));
	private static final Codec<Either<String, Gemstone>> EITHER_CODEC = Codec.either(Codec.STRING, CODEC);
	static final Codec<Map<String, Either<String, Gemstone>>> MAP_EITHER_CODEC = Codec.unboundedMap(Codec.STRING, EITHER_CODEC)
			.xmap(Object2ObjectOpenHashMap::new, map -> new Object2ObjectOpenHashMap<>(map));
}
