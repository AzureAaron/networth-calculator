package net.azureaaron.networth.utils;

import java.util.function.Consumer;

public class Utils {

	/**
	 * Useful for instantiating constant fields containing Maps for example.
	 */
	public static <T> T make(T object, Consumer<T> initializer) {
		initializer.accept(object);

		return object;
	}
}
