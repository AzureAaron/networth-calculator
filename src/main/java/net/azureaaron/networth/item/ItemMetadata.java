package net.azureaaron.networth.item;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import com.mojang.datafixers.util.Either;

import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.objects.Object2IntMap;

/**
 * This interface is used for retrieving formatted "metadata" from an item's custom data.
 * 
 * @implNote This is part of the internal implementation (as made obvious by the interface being sealed) and thus is subjected
 * to breaking changes at any time. It is not recommended to call any of these methods ever for your own use.
 */
@ApiStatus.Internal
public sealed interface ItemMetadata permits DynamicItemMetadata {

	Object2IntMap<String> enchantments();

	int rarityUpgrades();

	Optional<String> reforge();

	int upgradeLevel();

	DungeonUpgrades dungeonUpgrades();

	GearUpgrades gearUpgrades();

	List<String> gemstoneSlots();

	Map<String, Either<String, Gemstone>> gemstones();

	DrillInfo drillInfo();

	Object2IntMap<String> attributes();

	AuctionBidInfo auctionBidInfo();

	MiscModifiers miscModifiers();

	AccessoryUpgrades accessoryUpgrades();

	Cosmetics cosmetics();

	@Nullable
	PetInfo petInfo();

	LimitedEditionInfo limitedEditionInfo();

	IntList cakeBagCakeYears();
}
