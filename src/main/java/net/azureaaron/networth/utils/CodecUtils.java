package net.azureaaron.networth.utils;

import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;

public class CodecUtils {

	public static <K> Codec<Object2IntMap<K>> createObject2IntMapCodec(Codec<K> keyCodec) {
		return Codec.unboundedMap(keyCodec, Codec.INT)
				.xmap(Object2IntOpenHashMap::new, Object2IntOpenHashMap::new);
	}

	public static MapCodec<OptionalInt> optionalInt(MapCodec<Optional<Integer>> codec) {
		return codec.xmap(opt -> opt.map(OptionalInt::of).orElseGet(OptionalInt::empty), optInt -> optInt.isPresent() ? Optional.of(optInt.getAsInt()) : Optional.empty());
	}

	public static MapCodec<OptionalLong> optionalLong(MapCodec<Optional<Long>> codec) {
		return codec.xmap(opt -> opt.map(OptionalLong::of).orElseGet(OptionalLong::empty), optLong -> optLong.isPresent() ? Optional.of(optLong.getAsLong()) : Optional.empty());
	}

	public static MapCodec<OptionalDouble> optionalDouble(MapCodec<Optional<Double>> codec) {
		return codec.xmap(opt -> opt.map(OptionalDouble::of).orElseGet(OptionalDouble::empty), optDouble -> optDouble.isPresent() ? Optional.of(optDouble.getAsDouble()) : Optional.empty());
	}
}
