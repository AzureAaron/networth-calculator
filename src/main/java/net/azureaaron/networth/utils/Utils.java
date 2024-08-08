package net.azureaaron.networth.utils;

import java.util.function.Consumer;
import java.util.function.Function;

public class Utils {

	/**
	 * Useful for instantiating constant fields containing Maps for example.
	 */
	public static <T> T make(T object, Consumer<T> initializer) {
		initializer.accept(object);

		return object;
	}

	/**
	 * Useful for in-line conversions between two data types.
	 */
	public static <T, R> R transform(T object, Function<T, R> transformer) {
		return transformer.apply(object);
	}
}
