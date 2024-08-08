package net.azureaaron.networth;

import java.util.ArrayList;
import java.util.List;
import java.util.function.ToDoubleFunction;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntIntPair;
import it.unimi.dsi.fastutil.ints.IntList;
import net.azureaaron.networth.Calculation.Type;
import net.azureaaron.networth.data.ModifierValues;
import net.azureaaron.networth.item.PetInfo;
import net.azureaaron.networth.utils.PetConstants;

public class PetCalculator {
	private PetCalculator() {}

	/**
	 * Only call this when you want to calculate the value of a pet when its not in item form.
	 * 
	 * @return a {@link NetworthResult} instance describing the item's value and the calculations performed.
	 * 
	 * @see {@link ItemCalculator#calculate(net.azureaaron.networth.item.SkyblockItemStack, ToDoubleFunction, List, ModifierValues)}
	 */
	public static NetworthResult calculate(PetInfo info, ToDoubleFunction<String> prices, ModifierValues modifiers) {
		double lvl1 = prices.applyAsDouble("LVL_1_" + info.tier() + "_" + info.type());
		double lvl100 = prices.applyAsDouble("LVL_100_" + info.tier() + "_" + info.type());
		double lvl200 = prices.applyAsDouble("LVL_200_" + info.tier() + "_" + info.type());

		if (lvl1 == 0 || (lvl100 == 0 && !info.type().equals("GOLDEN_DRAGON"))) return NetworthResult.EMPTY;

		List<Calculation> calculations = new ArrayList<>();
		IntIntPair levelInfo = calculatePetLevel(info);
		int level = levelInfo.leftInt();
		int maxXp = levelInfo.rightInt();

		double price = lvl200 != 0 ? lvl200 : lvl100;

		//Base calculations
		if (level < 100 && maxXp != 0) {
			double baseFormula = (lvl100 - lvl1) / maxXp;

			price = baseFormula * info.xp() + lvl1;
		}

		if (level > 100 && level < 200) {
			//Trim 1st digit off number
			int incrementalLevel = level % (int) Math.pow(10, (int) Math.log10(level));

			if (incrementalLevel != 1) {
				double baseFormula = (lvl200 - lvl100) / 100;

				price = baseFormula * incrementalLevel + lvl100;
			}
		}

		double base = price;

		//Pet Skins
		if (info.skin().isPresent()) {
			Calculation calculation = Calculation.of(Type.SKIN, info.skin().get(), prices.applyAsDouble("PET_SKIN_" + info.skin().get()) * modifiers.regular().getDouble("skins"));

			price += calculation.price();
			calculations.add(calculation);
		}

		//Held Item
		if (info.heldItem().isPresent()) {
			Calculation calculation = Calculation.of(Type.PET_ITEM, info.heldItem().get(), prices.applyAsDouble(info.heldItem().get()) * modifiers.regular().getDouble("petItem"));

			price += calculation.price();
			calculations.add(calculation);
		}

		//Pet Candy Price Reduction
		int maxPetXpFromCandy = info.candies() * 1_000_000;
		double xpWithoutCandy = info.xp() - maxPetXpFromCandy;

		if (info.candies() > 0 && !PetConstants.BLOCKED_CANDY_REDUCTION_PETS.contains(info.type()) && xpWithoutCandy >= maxXp) {
			double reducedValue = price * modifiers.regular().getDouble("petCandy");

			price = Math.max(reducedValue, price - (level == 100 ? 5_000_000 : 2_500_000));
		}

		return NetworthResult.of(price, base, calculations);
	}

	/**
	 * @return an {@link IntIntPair} where the left {@code int} is the pet's level, and the right {@code int} is the cumulative XP of the maximum level this pet can be.
	 */
	public static IntIntPair calculatePetLevel(PetInfo info) {
		//Legendary pets can't be tier boosted so we don't need to do any bounds checks
		String petTier = info.heldItem().orElse("").equals("PET_ITEM_TIER_BOOST") ? PetConstants.PET_RARITIES.get(PetConstants.PET_RARITIES.indexOf(info.tier()) + 1) : info.tier();
		int maxPetLevel = PetConstants.SPECIAL_MAX_LVLS.getOrDefault(info.type(), 100);
		int petOffset = PetConstants.RARITY_OFFSETS.getInt(info.type().equals("BINGO") ? "COMMON" : petTier);
		//Create a new IntArrayList to work around a FastUtil bug with the immutable sub list iterator - FastUtil#321
		IntList petLevels = new IntArrayList(PetConstants.PET_LEVELS.subList(petOffset, petOffset + maxPetLevel - 1));

		int level = 1;
		int totalXp = 0;

		for (int lvlXp : petLevels) {
			totalXp += lvlXp;

			if (totalXp > info.xp()) {
				totalXp -= lvlXp;

				break;
			}

			level++;
		}

		return IntIntPair.of(Math.min(level, maxPetLevel), petLevels.intStream().sum());
	}
}
