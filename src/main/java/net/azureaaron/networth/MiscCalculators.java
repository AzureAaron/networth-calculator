package net.azureaaron.networth;

import java.util.List;
import java.util.function.ToDoubleFunction;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.azureaaron.networth.Calculation.Type;
import net.azureaaron.networth.utils.ItemConstants;

public class MiscCalculators {
	private static final Pattern API_RUNE_PATTERN = Pattern.compile("RUNE_(?<rune>[A-Z_]+)_(?<level>\\d)");

	private MiscCalculators() {}

	/**
	 * Calculates the worth of item's in a player's sack.
	 * 
	 * @param id the id of the item in the sack
	 */
	public static NetworthResult calculateSackItem(String id, int count, ToDoubleFunction<String> prices) {		
		Matcher matcher = API_RUNE_PATTERN.matcher(id);

		if (matcher.matches()) {
			id = matcher.group("rune") + "_RUNE_" + Integer.parseInt(matcher.group("level"));

			if (!ItemConstants.VALID_RUNES.contains(id)) return NetworthResult.EMPTY;
		}

		Calculation calculation = Calculation.of(Type.SACK_ITEM, id, prices.applyAsDouble(id) * count, count);

		return NetworthResult.of(calculation.price(), calculation.price(), List.of(calculation));
	}

	/**
	 * Calculates the value of essence.
	 */
	public static NetworthResult calculateEssence(String essenceType, int count, ToDoubleFunction<String> prices) {
		Calculation calculation = Calculation.of(Type.ESSENCE, essenceType, prices.applyAsDouble("ESSENCE_" + essenceType) * count, count);

		return NetworthResult.of(calculation.price(), calculation.price(), List.of(calculation));
	}
}
