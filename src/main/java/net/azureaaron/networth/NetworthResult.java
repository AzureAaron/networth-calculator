package net.azureaaron.networth;

import java.util.Collections;
import java.util.List;

import org.jetbrains.annotations.ApiStatus;

/**
 * The result of a networth calculation on some object, contains the price, base price, and calculations performed.
 */
public record NetworthResult(double price, double base, List<Calculation> calculations) {
	static final NetworthResult EMPTY = new NetworthResult(0, 0, List.of());

	@ApiStatus.Internal
	public NetworthResult(double price, double base, List<Calculation> calculations) {
		this.price = price;
		this.base = base;
		this.calculations = calculations;
	}

	static NetworthResult of(double price, double base, List<Calculation> calculations) {
		return new NetworthResult(price, base, Collections.unmodifiableList(calculations));
	}
}
