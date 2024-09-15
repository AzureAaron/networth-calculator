package net.azureaaron.networth;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.function.ToDoubleFunction;
import java.util.stream.DoubleStream;

import com.mojang.datafixers.util.Either;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import net.azureaaron.networth.Calculation.Type;
import net.azureaaron.networth.data.ModifierValues;
import net.azureaaron.networth.data.SkyblockItemData;
import net.azureaaron.networth.data.SkyblockItemData.GearUpgrade;
import net.azureaaron.networth.data.SkyblockItemData.GemstoneSlot;
import net.azureaaron.networth.data.SkyblockItemData.GemstoneSlot.GemstoneSlotCost;
import net.azureaaron.networth.item.Gemstone;
import net.azureaaron.networth.item.ItemMetadata;
import net.azureaaron.networth.item.SkyblockItemStack;
import net.azureaaron.networth.utils.ItemConstants;
import net.azureaaron.networth.utils.PrestigeConstants;

public class ItemCalculator {
	private ItemCalculator() {}

	/**
	 * @see {@link #calculate(SkyblockItemStack, ToDoubleFunction, List, ModifierValues)}
	 */
	public static NetworthResult calculate(SkyblockItemStack stack, ToDoubleFunction<String> prices, Object2ObjectMap<String, SkyblockItemData> skyblockItems) {
		return calculate(stack, prices, skyblockItems, ModifierValues.DEFAULT);
	}

	/**
	 * Calculates the networth for a skyblock item. Automatically handles pets in their item form.
	 * 
	 * @param stack         the {@link SkyblockItemStack} instance
	 * @param prices        used to retrieve the price for an item via its Item Id, note some specialized IDs are passed through here and may need additional processing.
	 * @param skyblockItems a map of item ids to {@link SkyblockItemData}, parsed from the Skyblock Items Resource API.
	 * @param modifiers     a list of weights for each item/calculation modifier, its recommended to use the defaults (via overloaded method) or you can pass in your own instance.
	 * 
	 * @return a {@link NetworthResult} instance describing the item's value and the calculations performed.
	 * 
	 * @apiNote All method parameters passed in must be non-null.
	 */
	public static NetworthResult calculate(SkyblockItemStack stack, ToDoubleFunction<String> prices, Object2ObjectMap<String, SkyblockItemData> skyblockItems, ModifierValues modifiers) {
		String itemId = stack.id();
		SkyblockItemData skyblockItemData = skyblockItems.get(itemId);
		ItemMetadata meta = stack.metadata();
		List<Calculation> calculations = new ArrayList<>();

		//Pet Items
		if (itemId.equals("PET")) {
			return meta.petInfo() != null ? PetCalculator.calculate(meta.petInfo(), prices, modifiers) : NetworthResult.EMPTY;
		}

		//Runes
		if ((itemId.equals("RUNE") || itemId.equals("UNIQUE_RUNE")) && meta.cosmetics().runes().isPresent() && meta.cosmetics().runes().get().size() > 0) {
			Object2IntMap.Entry<String> first = meta.cosmetics().runes().get().object2IntEntrySet().iterator().next();

			itemId = first.getKey().toUpperCase(Locale.CANADA) + "_RUNE_" + first.getIntValue();
		}

		//Limited Edition Items
		switch (itemId) {
			case String s when s.equals("NEW_YEAR_CAKE") && meta.limitedEditionInfo().newYearCakeYear().isPresent() -> itemId += "_" + meta.limitedEditionInfo().newYearCakeYear().getAsInt();
			case String s when ItemConstants.COLOURED_ANNIVERSARY_ITEMS.contains(s) && meta.limitedEditionInfo().partyHatColour().isPresent() -> itemId += "_" + meta.limitedEditionInfo().partyHatColour().get().toUpperCase(Locale.CANADA);
			case String s when s.equals("PARTY_HAT_SLOTH") && meta.limitedEditionInfo().partyHatEmoji().isPresent() -> itemId += "_" + meta.limitedEditionInfo().partyHatEmoji().get().toUpperCase(Locale.CANADA);
			case String s when s.equals("ABICASE") && meta.limitedEditionInfo().abicaseModel().isPresent() -> itemId += "_" + meta.limitedEditionInfo().abicaseModel().get().toUpperCase(Locale.CANADA);

			default -> {} //Do Nothing
		}

		//Shiny Items (Cosmetic Effect)
		if (meta.cosmetics().shiny()) itemId = "SHINY_" + itemId;

		//We multiply by stack count ahead of time because items with modifiers cannot be stacked.
		double price = prices.applyAsDouble(itemId) * stack.count();
		double base = prices.applyAsDouble(itemId) * stack.count();

		//Shen's price
		if (price == 0d && meta.auctionBidInfo().price().isPresent()) {
			price = meta.auctionBidInfo().price().getAsLong() * 0.85;
			base = meta.auctionBidInfo().price().getAsLong() * 0.85;
		}

		//Pickonimbus Durability scaling
		if (itemId.equals("PICKONIMBUS") && meta.drillInfo().pickonimbusDurability().isPresent()) {
			double reduction = meta.drillInfo().pickonimbusDurability().getAsInt() / (double) ItemConstants.PICKONIMBUS_DURABILITY;

			price += price * (reduction - 1);
			base += price * (reduction - 1);
		}

		//God Roll Attributes
		if (!itemId.equals("ATTRIBUTE_SHARD") && !meta.attributes().isEmpty()) {
			List<String> sortedAttributes = meta.attributes().keySet().stream().map(s -> s.toUpperCase(Locale.CANADA)).sorted(String::compareTo).toList();
			String baseId = itemId.replaceAll("(?:(?:HOT|BURNING|FIERY|INFERNAL)_)", "");
			String godRollId = baseId += "-" + String.join("-", sortedAttributes);
			double godRollPrice = prices.applyAsDouble(godRollId);

			if (godRollPrice > price) {
				price = godRollPrice;
				base = godRollPrice;

				calculations.add(Calculation.of(Type.GOD_ROLL, godRollId, godRollPrice));
			}
		}

		//Armour Upgrades like crimson
		//The price is checked for whether its 0, because upgraded pieces are soul bound and do not have a price
		if (price == 0 && PrestigeConstants.PRESTIGES.containsKey(itemId)) {
			for (String prestigeItemId : PrestigeConstants.PRESTIGES.get(itemId)) {
				SkyblockItemData itemData = skyblockItems.get(prestigeItemId);

				if (itemData != null) {
					if (itemData.upgradeCosts().isPresent()) price += upgradeCosts(prices, calculations, itemData.upgradeCosts().get(), prestigeItemId, modifiers);
					if (itemData.prestige().isPresent()) price += upgradeCosts(prices, calculations, itemData.prestige().get().costs(), prestigeItemId, modifiers);
				}
			}
		}

		//Shen's Auction Price Paid
		if (meta.auctionBidInfo().price().isPresent() && meta.auctionBidInfo().auction().isPresent() && meta.auctionBidInfo().bid().isPresent()) {
			double scaledPricePaid = meta.auctionBidInfo().price().getAsLong() * modifiers.regular().getDouble("shensAuctionPrice");

			if (scaledPricePaid > price) {
				price = scaledPricePaid;
				calculations.add(Calculation.of(Type.SHEN_AUCTION, itemId, scaledPricePaid));
			}
		}

		//Midas Stuff
		if (itemId.equals("MIDAS_STAFF") || itemId.equals("MIDAS_SWORD")) {
			int maxBid = itemId.equals("MIDAS_SWORD") ? 50_000_000 : 100_000_000;

			if (meta.auctionBidInfo().winningBid().isPresent()) {
				if (meta.auctionBidInfo().winningBid().getAsLong() >= maxBid) {
					String maxItem = itemId.equals("MIDAS_SWORD") ? "MIDAS_SWORD_50M" : "MIDAS_STAFF_100M";
					Calculation calculation = Calculation.of(Type.WINNING_BID, maxItem, prices.applyAsDouble(maxItem));

					price = calculation.price();
					calculations.add(calculation);
				} else {
					Calculation calculation = Calculation.of(Type.WINNING_BID, itemId, meta.auctionBidInfo().winningBid().getAsLong() * modifiers.regular().getDouble("winningBid"));
					price = calculation.price();
					calculations.add(calculation);
				}
			}
		}

		//Enchanted Books
		if (!meta.enchantments().isEmpty() && itemId.equals("ENCHANTED_BOOK")) {
			boolean multiEnchantmentBook = meta.enchantments().size() > 1;

			//Single Enchant Book
			if (!multiEnchantmentBook) {
				Object2IntMap.Entry<String> first = meta.enchantments().object2IntEntrySet().iterator().next();
				String enchantmentApiName = first.getKey().toUpperCase(Locale.CANADA) + "_" + first.getIntValue();
				Calculation calculation = Calculation.of(Type.ENCHANTMENT, enchantmentApiName, prices.applyAsDouble("ENCHANTMENT_" + enchantmentApiName));

				price = calculation.price();
				calculations.add(calculation);
			} else { //Multiple Enchant Book
				double enchantmentsPrice = 0;

				for (Object2IntMap.Entry<String> entry : meta.enchantments().object2IntEntrySet()) {
					String enchantmentApiName = entry.getKey().toUpperCase(Locale.CANADA) + "_" + entry.getIntValue();
					Calculation calculation = Calculation.of(Type.ENCHANTMENT, enchantmentApiName, prices.applyAsDouble("ENCHANTMENT_" + enchantmentApiName) * modifiers.regular().getDouble("enchantments"));
					enchantmentsPrice += calculation.price();

					calculations.add(calculation);
				}

				price = enchantmentsPrice;
			}
		}

		//Skins
		if (meta.cosmetics().skin().isPresent()) {
			Calculation calculation = Calculation.of(Type.SKIN, meta.cosmetics().skin().get(), prices.applyAsDouble(meta.cosmetics().skin().get()) * modifiers.regular().getDouble("skins"));

			price += calculation.price();
			calculations.add(calculation);
		}

		//Regular Enchanted Items
		if (!meta.enchantments().isEmpty() && !itemId.equals("ENCHANTED_BOOK")) {
			for (Object2IntMap.Entry<String> entry : meta.enchantments().object2IntEntrySet()) {
				String enchantmentId = entry.getKey().toUpperCase(Locale.CANADA);
				int level = entry.getIntValue();

				//Skip blocked and ignored enchantments
				if (ItemConstants.BLOCKED_ENCHANTMENTS.containsKey(itemId) && ItemConstants.BLOCKED_ENCHANTMENTS.get(itemId).contains(enchantmentId)) continue;
				if (ItemConstants.IGNORED_ENCHANTMENTS.containsKey(enchantmentId) && ItemConstants.IGNORED_ENCHANTMENTS.getInt(enchantmentId) == level) continue;

				//Set stacking enchant level to 1 so we can get a price off the bazaar for it
				if (ItemConstants.STACKING_ENCHANTMENTS.contains(enchantmentId)) level = 1;

				//Add Silex price
				if (enchantmentId.equals("EFFICIENCY") && level > 5 && !ItemConstants.IGNORE_SILEX.contains(itemId)) {
					int silexes = level - (itemId.equals("STONK_PICKAXE") ? 6 : 5);

					if (silexes > 0) {
						Calculation calculation = Calculation.of(Type.SILEX, "SIL_EX", prices.applyAsDouble("SIL_EX") * silexes * modifiers.regular().getDouble("silex"), silexes);

						price += calculation.price();
						calculations.add(calculation);
					}
				}

				//Add Golden Bounty price
				if (enchantmentId.equals("SCAVENGER") && level == 6) {
					Calculation calculation = Calculation.of(Type.GOLDEN_BOUNTY, "GOLDEN_BOUNTY", prices.applyAsDouble("GOLDEN_BOUNTY") * modifiers.regular().getDouble("goldenBounty"));

					price += calculation.price();
					calculations.add(calculation);
				}

				double enchantmentWorth = modifiers.enchantments().getOrDefault(enchantmentId, modifiers.regular().getDouble("enchantments"));
				double enchantmentPrice = switch (enchantmentId) {
					case String s when s.startsWith("ULTIMATE") -> { //For some ultimate enchants like Chimera the t5 books prices are way lower (due to nobody selling them) than if u got 16 t1 books
						int lvl1BooksNeeded = (int) Math.pow(2, level - 1);
						double directPrice = prices.applyAsDouble("ENCHANTMENT_" + enchantmentId + "_" + level);
						double priceFromLvl1s = prices.applyAsDouble("ENCHANTMENT_" + enchantmentId + "_" + 1) * lvl1BooksNeeded;

						yield priceFromLvl1s > directPrice ? priceFromLvl1s : directPrice;
					}

					default -> prices.applyAsDouble("ENCHANTMENT_" + enchantmentId + "_" + level);
				};

				Calculation calculation = Calculation.of(Type.ENCHANTMENT, enchantmentId + "_" + level, enchantmentPrice * enchantmentWorth);

				price += calculation.price();
				calculations.add(calculation);
			}
		}

		//Attributes
		if (!meta.attributes().isEmpty()) {
			for (Object2IntMap.Entry<String> entry : meta.attributes().object2IntEntrySet()) {
				String attribute = entry.getKey().toUpperCase(Locale.CANADA);
				int tier = entry.getIntValue();

				//Better described as the # of level 1 attributes needed to get to the tier
				int shards = (int) (Math.pow(2, tier - 1) - 1);
				String baseAttributeId = "ATTRIBUTE_SHARD-" + attribute + "_";

				//Calculate all possible level 1 attribute combinations to avoid cases where the price is over inflated
				//because there is very few level 1 attribute shards on the ah
				DoubleStream attributePrices = DoubleStream.of(
						prices.applyAsDouble(baseAttributeId + 1),
						prices.applyAsDouble(baseAttributeId + 2) / 2,
						prices.applyAsDouble(baseAttributeId + 3) / 4);
				double baseAttributePrice = attributePrices
						.filter(shardPrice -> shardPrice > 0) //For some attributes like dominance and mana regeneration its impossible to get a lvl 1 shard
						.min().orElse(0); //Choose lowest price to further prevent prices being inflated

				//Some items like Hellfire Rods for example are typically start out as Magma Rods because they are way cheaper to
				//craft & merge together to get high attribute levels on, then these Magma Rods are upgraded to Hellfire Rods.
				if (ItemConstants.ATTRIBUTE_BASE_COSTS.containsKey(itemId) && prices.applyAsDouble(ItemConstants.ATTRIBUTE_BASE_COSTS.get(itemId)) < baseAttributePrice) {
					baseAttributePrice = prices.applyAsDouble(ItemConstants.ATTRIBUTE_BASE_COSTS.get(itemId));
				}

				//Kuudra Helmets are valued differently since they usually aren't as useful and we don't want to "pollute" the values of
				//the other armour prices
				if (ItemConstants.KUUDRA_HELMETS.contains(itemId) && prices.applyAsDouble("KUUDRA_HELMET_" + attribute) < baseAttributePrice) {
					baseAttributePrice = prices.applyAsDouble("KUUDRA_HELMET_" + attribute);
				} else if (ItemConstants.KUUDRA_OTHERS.contains(itemId)) { //Same thing as above but for chestplates, leggings, and boots
					DoubleStream kuudraArmourPrices = DoubleStream.of(
							prices.applyAsDouble("KUUDRA_CHESTPLATE_" + attribute),
							prices.applyAsDouble("KUUDRA_LEGGINGS_" + attribute),
							prices.applyAsDouble("KUUDRA_BOOTS_" + attribute));
					double meanArmourPrice = kuudraArmourPrices.average().orElse(0);

					if (meanArmourPrice > 0 && (baseAttributePrice <= 0 || meanArmourPrice < baseAttributePrice)) baseAttributePrice = meanArmourPrice;
				}

				Calculation calculation = Calculation.of(Type.ATTRIBUTE, attribute + "_" + tier, baseAttributePrice * shards * modifiers.regular().getDouble("attributes"));

				price += calculation.price();
				calculations.add(calculation);
			}
		}

		//Pocket Sack-in-a-Sack
		if (meta.miscModifiers().pocketSackInASacks().isPresent()) {
			Calculation calculation = Calculation.of(
					Type.POCKET_SACK_IN_A_SACK,
					"POCKET_SACK_IN_A_SACK",
					prices.applyAsDouble("POCKET_SACK_IN_A_SACK") * meta.miscModifiers().pocketSackInASacks().getAsInt() * modifiers.regular().getDouble("pocketSackInASack"),
					meta.miscModifiers().pocketSackInASacks().getAsInt());

			price += calculation.price();
			calculations.add(calculation);
		}

		//Wood Singularity
		if (meta.gearUpgrades().woodSingularities().isPresent()) {
			Calculation calculation = Calculation.of(
					Type.WOOD_SINGULARITY,
					"WOOD_SINGULARITY",
					prices.applyAsDouble("WOOD_SINGULARITY") * meta.gearUpgrades().woodSingularities().getAsInt() * modifiers.regular().getDouble("woodSingularity"),
					meta.gearUpgrades().woodSingularities().getAsInt());

			price += calculation.price();
			calculations.add(calculation);
		}

		//Jalapeno Book
		if (meta.miscModifiers().jalapenoBooks().isPresent()) {
			Calculation calculation = Calculation.of(
					Type.JALAPENO_BOOK,
					"JALAPENO_BOOK",
					prices.applyAsDouble("JALAPENO_BOOK") * meta.miscModifiers().jalapenoBooks().getAsInt() * modifiers.regular().getDouble("jalapenoBook"),
					meta.miscModifiers().jalapenoBooks().getAsInt());

			price += calculation.price();
			calculations.add(calculation);
		}

		//Transmission Tuners
		if (meta.miscModifiers().transmissionTuners().isPresent()) {
			Calculation calculation = Calculation.of(
					Type.TRANSMISSION_TUNER,
					"TRANSMISSION_TUNER",
					prices.applyAsDouble("TRANSMISSION_TUNER") * meta.miscModifiers().transmissionTuners().getAsInt() * modifiers.regular().getDouble("transmissionTuner"),
					meta.miscModifiers().transmissionTuners().getAsInt());

			price += calculation.price();
			calculations.add(calculation);
		}

		//Mana Disintegrators
		if (meta.miscModifiers().manaDisintegrators().isPresent()) {
			Calculation calculation = Calculation.of(
					Type.MANA_DISINTEGRATOR,
					"MANA_DISINTEGRATOR",
					prices.applyAsDouble("MANA_DISINTEGRATOR") * meta.miscModifiers().manaDisintegrators().getAsInt() * modifiers.regular().getDouble("manaDisintegrator"),
					meta.miscModifiers().manaDisintegrators().getAsInt());

			price += calculation.price();
			calculations.add(calculation);
		}

		//Pulse Ring
		if (meta.accessoryUpgrades().thunderCharges().isPresent() && itemId.equals("PULSE_RING")) {
			int thunderBottlesUsed = Math.floorDiv(meta.accessoryUpgrades().thunderCharges().getAsInt(), 50_000);
			Calculation calculation = Calculation.of(
					Type.THUNDER_IN_A_BOTTLE,
					"THUNDER_IN_A_BOTTLE",
					prices.applyAsDouble("THUNDER_IN_A_BOTTLE") * thunderBottlesUsed * modifiers.regular().getDouble("thunderInABottle"),
					thunderBottlesUsed);

			price += calculation.price();
			calculations.add(calculation);
		}

		//Runes (Applied)
		if (meta.cosmetics().runes().isPresent() && !meta.cosmetics().runes().get().isEmpty() && !itemId.contains("RUNE")) {
			Object2IntMap.Entry<String> rune = meta.cosmetics().runes().get().object2IntEntrySet().iterator().next();
			String runeItemId = rune.getKey().toUpperCase(Locale.CANADA) + "_RUNE_" + rune.getIntValue();

			if (ItemConstants.VALID_RUNES.contains(runeItemId)) {
				Calculation calculation = Calculation.of(Type.RUNE, runeItemId, prices.applyAsDouble(runeItemId) * modifiers.regular().getDouble("runes"));

				price += calculation.price();
				calculations.add(calculation);
			}
		}

		//Hot Potato Books
		if (meta.gearUpgrades().hotPotatoes().isPresent()) {
			int hotPotatoes = meta.gearUpgrades().hotPotatoes().getAsInt();

			//Fuming Potato Books
			if (hotPotatoes > 10) {
				int fumingPotatoBooksUsed = hotPotatoes - 10;
				Calculation calculation = Calculation.of(
						Type.FUMING_POTATO_BOOK,
						"FUMING_POTATO_BOOK",
						prices.applyAsDouble("FUMING_POTATO_BOOK") * fumingPotatoBooksUsed * modifiers.regular().getDouble("fumingPotatoBook"),
						fumingPotatoBooksUsed);

				price += calculation.price();
				calculations.add(calculation);
			}

			//Regular Potato Books
			int hotPotatoBooksUsed = Math.min(hotPotatoes, 10);
			Calculation calculation = Calculation.of(
					Type.HOT_POTATO_BOOK,
					"HOT_POTATO_BOOK",
					prices.applyAsDouble("HOT_POTATO_BOOK") * hotPotatoBooksUsed * modifiers.regular().getDouble("hotPotatoBook"),
					hotPotatoBooksUsed);

			price += calculation.price();
			calculations.add(calculation);
		}

		//Dyes
		if (meta.cosmetics().dye().isPresent()) {
			Calculation calculation = Calculation.of(
					Type.DYE,
					meta.cosmetics().dye().get(),
					prices.applyAsDouble(meta.cosmetics().dye().get()) * modifiers.regular().getDouble("dye"));

			price += calculation.price();
			calculations.add(calculation);
		}

		//Art of War
		if (meta.gearUpgrades().artOfWars().isPresent()) {
			Calculation calculation = Calculation.of(
					Type.ART_OF_WAR,
					"THE_ART_OF_WAR",
					prices.applyAsDouble("THE_ART_OF_WAR") * meta.gearUpgrades().artOfWars().getAsInt() * modifiers.regular().getDouble("artOfWar"),
					meta.gearUpgrades().artOfWars().getAsInt());

			price += calculation.price();
			calculations.add(calculation);
		}

		//Art of Peace
		if (meta.gearUpgrades().artOfPeaces().isPresent()) {
			Calculation calculation = Calculation.of(
					Type.ART_OF_PEACE,
					"THE_ART_OF_PEACE",
					prices.applyAsDouble("THE_ART_OF_PEACE") * meta.gearUpgrades().artOfPeaces().getAsInt() * modifiers.regular().getDouble("artOfPeace"),
					meta.gearUpgrades().artOfPeaces().getAsInt());

			price += calculation.price();
			calculations.add(calculation);
		}

		//Farming for Dummies
		if (meta.miscModifiers().farming4Dummies().isPresent()) {
			Calculation calculation = Calculation.of(
					Type.FARMING_FOR_DUMMIES,
					"FARMING_FOR_DUMMIES",
					prices.applyAsDouble("FARMING_FOR_DUMMIES") * meta.miscModifiers().farming4Dummies().getAsInt() * modifiers.regular().getDouble("farming4Dummies"),
					meta.miscModifiers().farming4Dummies().getAsInt());

			price += calculation.price();
			calculations.add(calculation);
		}

		//Enrichments
		if (meta.accessoryUpgrades().enrichment().isPresent()) {
			//Since all enrichments are the same bits price, find the cheapest one to avoid inflated prices
			double enrichmentPrice = ItemConstants.ENRICHMENTS.stream().mapToDouble(prices::applyAsDouble).min().getAsDouble();
			Calculation calculation = Calculation.of(Type.TALISMAN_ENRICHMENT, "TALISMAN_ENRICHMENT", enrichmentPrice * modifiers.regular().getDouble("enrichment"));

			price += calculation.price();
			calculations.add(calculation);
		}

		//Recombobulators - ignore dungeon mob drop items
		if (meta.rarityUpgrades() > 0 && meta.dungeonUpgrades().itemTier().isEmpty()) {
			//SkyHelper has extra filtering, I don't know why it matters but I won't use it for now
			Calculation calculation = Calculation.of(
					Type.RECOMBOBULATOR,
					"RECOMBOBULATOR_3000",
					prices.applyAsDouble("RECOMBOBULATOR_3000") * meta.rarityUpgrades() * modifiers.regular().getDouble("recombobulator"),
					meta.rarityUpgrades());

			price += calculation.price();
			calculations.add(calculation);
		}

		//Gemstones
		if ((!meta.gemstoneSlots().isEmpty() || !meta.gemstones().isEmpty()) && skyblockItemData != null) {
			if (skyblockItemData.gemstoneSlots().isPresent()) {
				List<String> unlockedSlotsOnItem = new ArrayList<>(meta.gemstoneSlots());
				List<String> unlockedSlots = new ArrayList<>();
				List<AppliedGemstone> appliedGemstones = new ArrayList<>();

				for (GemstoneSlot slot : skyblockItemData.gemstoneSlots().get()) {
					if (!slot.costs().isEmpty() && !unlockedSlotsOnItem.isEmpty()) {
						for (String type : unlockedSlotsOnItem) {
							if (type.startsWith(slot.type())) {
								unlockedSlots.add(slot.type());
								unlockedSlotsOnItem.remove(type);

								break;
							}
						}
					}

					if (slot.costs().isEmpty()) unlockedSlots.add(slot.type());

					Optional<String> key = meta.gemstones().keySet().stream().filter(k -> k.startsWith(slot.type()) && !k.endsWith("_gem")).findFirst();

					if (key.isPresent()) {
						String gemstoneType = ItemConstants.GEMSTONE_GROUP_SLOT_TYPES.contains(slot.type()) ? meta.gemstones().get(key.get() + "_gem").left().get() : slot.type();
						Either<String, Gemstone> gemstone = meta.gemstones().get(key.get());
						String gemstoneQuality = gemstone.right().isPresent() ? gemstone.right().get().quality() : gemstone.left().get();

						appliedGemstones.add(new AppliedGemstone(gemstoneType, gemstoneQuality, slot.type()));
						meta.gemstones().remove(key.get());

						if (!slot.costs().isEmpty() && meta.gemstoneSlots().isEmpty()) unlockedSlots.add(slot.type());
					}
				}

				//Unlocked Gemstone Slots
				boolean isDivansPiece = ItemConstants.DIVAN_GEAR.contains(itemId);

				if (isDivansPiece || ItemConstants.KUUDRA_ARMOUR.contains(itemId)) {
					double applicationValue = isDivansPiece ? modifiers.regular().getDouble("gemstoneChambers") : modifiers.regular().getDouble("gemstoneSlots");
					List<GemstoneSlot> gemstoneSlots = new ArrayList<>(skyblockItemData.gemstoneSlots().get());

					for (String unlockedSlot : unlockedSlots) {
						Optional<GemstoneSlot> slot = gemstoneSlots.stream().filter(s -> s.type().equals(unlockedSlot)).findFirst();

						if (slot.isPresent()) {
							int total = 0;

							for (GemstoneSlotCost cost : slot.get().costs().orElse(List.of())) {
								switch (cost.type()) {
									case "COINS" -> total += cost.coins().getAsInt();
									case "ITEM" -> total += prices.applyAsDouble(cost.itemId().get()) * cost.amount().getAsInt();
								}
							}

							Calculation calculation = Calculation.of(Type.GEMSTONE_SLOT, "GEMSTONE_SLOT", total * applicationValue);

							price += calculation.price();
							calculations.add(calculation);

							gemstoneSlots.remove(slot.get());
						}
					}
				}

				//Gemstones
				for (AppliedGemstone gemstone : appliedGemstones) {
					String gemstoneId = (gemstone.quality() + "_" + gemstone.gemstoneType() + "_GEM").toUpperCase(Locale.CANADA);
					Calculation calculation = Calculation.of(Type.GEMSTONE, gemstoneId, prices.applyAsDouble(gemstoneId) * modifiers.regular().getDouble("gemstones"));

					price += calculation.price();
					calculations.add(calculation);
				}
			}
		}

		//Power Scrolls
		if (meta.gearUpgrades().powerScroll().isPresent()) {
			Calculation calculation = Calculation.of(
					Type.POWER_SCROLL,
					meta.gearUpgrades().powerScroll().get(),
					prices.applyAsDouble(meta.gearUpgrades().powerScroll().get()) * modifiers.regular().getDouble("powerScroll"));

			price += calculation.price();
			calculations.add(calculation);
		}

		//Reforges
		if (meta.reforge().isPresent()) {
			boolean isAccessory = skyblockItemData != null && skyblockItemData.category().isPresent() && skyblockItemData.category().orElse("").equals("ACCESSORY");

			//Accessory reforges do not count anymore
			if (!isAccessory) {
				Calculation calculation = Calculation.of(
						Type.REFORGE,
						meta.reforge().get(),
						prices.applyAsDouble(ItemConstants.REFORGES.get(meta.reforge().get())) * modifiers.regular().getDouble("reforge"));

				price += calculation.price();
				calculations.add(calculation);
			}
		}

		//Stars
		//In a separate block to prevent leaking the LVs
		{
			int dungeonItemLevel = meta.dungeonUpgrades().itemLevel().orElse(0);
			int upgradeLevel = meta.upgradeLevel();
			boolean hasUpgradeCosts = skyblockItemData != null && skyblockItemData.upgradeCosts().isPresent();

			//Master Stars
			if (hasUpgradeCosts && (dungeonItemLevel > 5 || upgradeLevel > 5)) {
				int starsUsedDungeons = dungeonItemLevel - 5;
				int starsUsedUpgrade = upgradeLevel - 5;
				int starsUsed = Math.max(starsUsedDungeons, starsUsedUpgrade);
				List<List<GearUpgrade>> upgradeCosts = skyblockItemData.upgradeCosts().get();

				if (upgradeCosts.size() <= 5) {
					for (int i = 0; i < starsUsed; i++) {
						String masterStarId = ItemConstants.MASTER_STARS.get(i);
						Calculation calculation = Calculation.of(
								Type.MASTER_STAR,
								masterStarId,
								prices.applyAsDouble(masterStarId) * modifiers.regular().getDouble("masterStars"));

						price += calculation.price();
						calculations.add(calculation);
					}
				}
			}

			if (hasUpgradeCosts && (dungeonItemLevel > 0 || upgradeLevel > 0)) {
				int level = Math.max(dungeonItemLevel, upgradeLevel);
				List<List<GearUpgrade>> upgradeCosts = skyblockItemData.upgradeCosts().get();

				price += upgradeCosts(prices, calculations, upgradeCosts.subList(0, Math.min(level + 1, upgradeCosts.size())), null, modifiers);
			}
		}

		//Wither Blade Scrolls
		if (meta.dungeonUpgrades().abilityScrolls().isPresent()) {
			for (String scroll : meta.dungeonUpgrades().abilityScrolls().get()) {
				Calculation calculation = Calculation.of(Type.WITHER_BLADE_SCROLL, scroll, prices.applyAsDouble(scroll) * modifiers.regular().getDouble("witherScroll"));

				price += calculation.price();
				calculations.add(calculation);
			}
		}

		//Drills
		if (meta.drillInfo().isAnyDrillPartPresent()) {
			if (meta.drillInfo().fuelTank().isPresent()) {
				Calculation calculation = Calculation.of(
						Type.DRILL_PART,
						meta.drillInfo().fuelTank().get().toUpperCase(Locale.CANADA),
						prices.applyAsDouble(meta.drillInfo().fuelTank().get().toUpperCase(Locale.CANADA)) * modifiers.regular().getDouble("drillPart"));

				price += calculation.price();
				calculations.add(calculation);
			}

			if (meta.drillInfo().engine().isPresent()) {
				Calculation calculation = Calculation.of(
						Type.DRILL_PART,
						meta.drillInfo().engine().get().toUpperCase(Locale.CANADA),
						prices.applyAsDouble(meta.drillInfo().engine().get().toUpperCase(Locale.CANADA)) * modifiers.regular().getDouble("drillPart"));

				price += calculation.price();
				calculations.add(calculation);
			}

			if (meta.drillInfo().upgradeModule().isPresent()) {
				Calculation calculation = Calculation.of(
						Type.DRILL_PART,
						meta.drillInfo().upgradeModule().get().toUpperCase(Locale.CANADA),
						prices.applyAsDouble(meta.drillInfo().upgradeModule().get().toUpperCase(Locale.CANADA)) * modifiers.regular().getDouble("drillPart"));

				price += calculation.price();
				calculations.add(calculation);
			}

			//Polarvoid Books
			if (meta.drillInfo().polarvoids() > 0) {
				Calculation calculation = Calculation.of(
						Type.POLARVOID_BOOK,
						"POLARVOID_BOOK",
						prices.applyAsDouble("POLARVOID_BOOK") * meta.drillInfo().polarvoids() * modifiers.regular().getDouble("polarvoidBook"),
						meta.drillInfo().polarvoids());

				price += calculation.price();
				calculations.add(calculation);
			}

			if (meta.drillInfo().divanPowderCoating().isPresent()) {
				Calculation calculation = Calculation.of(
						Type.DIVAN_POWDER_COATING,
						"DIVAN_POWDER_COATING",
						prices.applyAsDouble("DIVAN_POWDER_COATING") * meta.drillInfo().divanPowderCoating().getAsInt() * modifiers.regular().getDouble("divanPowderCoating"));

				price += calculation.price();
				calculations.add(calculation);
			}
		}

		//Etherwarp Conduit
		if (meta.miscModifiers().ethermerge().isPresent()) {
			Calculation calculation = Calculation.of(
					Type.ETHERWARP_CONDUIT,
					"ETHERWARP_CONDUIT",
					prices.applyAsDouble("ETHERWARP_CONDUIT") * modifiers.regular().getDouble("etherwarp"));

			price += calculation.price();
			calculations.add(calculation);
		}

		//New Year Cake Bag
		if (itemId.equals("NEW_YEAR_CAKE_BAG")) {
			double cakesPrice = 0;

			for (int year : meta.cakeBagCakeYears()) {
				cakesPrice += prices.applyAsDouble("NEW_YEAR_CAKE_" + year);
			}

			Calculation calculation = Calculation.of(Type.NEW_YEAR_CAKES, "NEW_YEAR_CAKES", cakesPrice);

			price += calculation.price();
			calculations.add(calculation);
		}

		return NetworthResult.of(price, base, calculations);
	}

	@SuppressWarnings("unchecked")
	private static <T> double upgradeCosts(ToDoubleFunction<String> prices, List<Calculation> calculations, List<T> upgradeCosts, String prestigeItem, ModifierValues modifiers) {
		double price = 0;
		int star = 0;
		List<Calculation> gearCalculations = new ArrayList<>();

		for (T upgrade : upgradeCosts) {
			star++;
			Calculation calculation = null;

			if (upgrade instanceof List<?> upgradesList) {
				for (GearUpgrade gearUpgrade : (List<GearUpgrade>) upgradesList) {
					calculation = upgradeCost(prices, gearUpgrade, star, modifiers);
					gearCalculations.add(calculation);

					if (prestigeItem == null) {
						price += calculation.price();
						calculations.add(calculation);
					}
				}
			} else if (upgrade instanceof GearUpgrade gearUpgrade) { //Check should never fail if it comes to this branch
				calculation = upgradeCost(prices, gearUpgrade, 0, modifiers);
				gearCalculations.add(calculation);

				if (prestigeItem == null) {
					price += calculation.price();
					calculations.add(calculation);
				}
			}
		}

		if (prestigeItem != null && gearCalculations.size() > 0) {
			boolean isPrestige = gearCalculations.getFirst().type() == Type.PRESTIGE;
			double priceSum = gearCalculations.stream().mapToDouble(Calculation::price).sum(); //Reduce calculations to one single number
			Calculation reduced = Calculation.of(isPrestige ? Type.PRESTIGE : Type.STAR, prestigeItem, priceSum, isPrestige ? 1 : star);
			double prestigeItemPrice = prices.applyAsDouble(prestigeItem);

			if (isPrestige && prestigeItemPrice != 0d) reduced = reduced.add(prestigeItemPrice);

			price += reduced.price();
			calculations.add(reduced);
		}

		return price;
	}

	private static Calculation upgradeCost(ToDoubleFunction<String> prices, GearUpgrade upgrade, int star, ModifierValues modifiers) {
		boolean isEssenceUpgrade = upgrade.essenceType().isPresent();
		double upgradePrice = isEssenceUpgrade ? prices.applyAsDouble("ESSENCE_" + upgrade.essenceType().get()) : prices.applyAsDouble(upgrade.itemId().orElse(""));

		Calculation calculation = Calculation.of(
				star == 0 ? Type.PRESTIGE : Type.STAR,
						isEssenceUpgrade ? "ESSENCE_" + upgrade.essenceType().get() : upgrade.itemId().get(),
								upgrade.amount().orElse(0) * upgradePrice * (upgrade.essenceType().isPresent() ? modifiers.regular().getDouble("essence") : 1),
								upgrade.amount().orElse(0));

		return calculation;
	}

	private record AppliedGemstone(String gemstoneType, String quality, String slotType) {}
}
