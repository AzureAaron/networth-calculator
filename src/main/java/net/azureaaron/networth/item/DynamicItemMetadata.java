package net.azureaaron.networth.item;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.jetbrains.annotations.ApiStatus;

import com.google.gson.JsonParser;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.JsonOps;

import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.azureaaron.networth.utils.CodecUtils;
import net.azureaaron.networth.utils.Utils;

@ApiStatus.Internal
record DynamicItemMetadata(Object2IntMap<String> enchantments, int rarityUpgrades, Optional<String> reforge, int upgradeLevel, DungeonUpgrades dungeonUpgrades,
		GearUpgrades gearUpgrades, List<String> gemstoneSlots, Map<String, Either<String, Gemstone>> gemstones, DrillInfo drillInfo, Object2IntMap<String> attributes,
		AuctionBidInfo auctionBidInfo, MiscModifiers miscModifiers, AccessoryUpgrades accessoryUpgrades, Cosmetics cosmetics, PetInfo petInfo,
		LimitedEditionInfo limitedEditionInfo, IntList cakeBagCakeYears) implements ItemMetadata {
	private static final Codec<Object2IntMap<String>> OBJECT_2_INT_MAP_CODEC = CodecUtils.createObject2IntMapCodec(Codec.STRING);

	static DynamicItemMetadata of(Dynamic<?> customData, ItemMetadataRetriever retriever) {
		Object2IntMap<String> enchantments = OBJECT_2_INT_MAP_CODEC.parse(customData.get("enchantments").orElseEmptyMap()).getOrThrow();
		int rarityUpgrades = customData.get("rarity_upgrades").asInt(0);
		Optional<String> reforge = Utils.transform(customData.get("modifier").asString(""), reforge1 -> !reforge1.isEmpty() ? Optional.of(reforge1) : Optional.empty());
		int upgradeLevel = customData.get("upgrade_level").asInt(0);
		DungeonUpgrades dungeonUpgrades = DungeonUpgrades.CODEC.parse(customData).getOrThrow();
		GearUpgrades gearUpgrades = GearUpgrades.CODEC.parse(customData).getOrThrow();
		List<String> gemstoneSlots = Codec.STRING.listOf().parse(customData.get("gems").orElseEmptyMap().get("unlocked_slots").orElseEmptyList()).getOrThrow();
		Map<String, Either<String, Gemstone>> gemstones = Gemstone.MAP_EITHER_CODEC.parse(customData.get("gems").orElseEmptyMap().remove("unlocked_slots")).getOrThrow();
		DrillInfo drillInfo = DrillInfo.CODEC.parse(customData).getOrThrow();
		Object2IntMap<String> attributes = OBJECT_2_INT_MAP_CODEC.parse(customData.get("attributes").orElseEmptyMap()).getOrThrow();
		AuctionBidInfo auctionBidInfo = AuctionBidInfo.CODEC.parse(customData).getOrThrow();
		MiscModifiers miscModifiers = MiscModifiers.CODEC.parse(customData).getOrThrow();
		AccessoryUpgrades accessoryUpgrades = AccessoryUpgrades.CODEC.parse(customData).getOrThrow();
		Cosmetics cosmetics = Cosmetics.CODEC.parse(customData).getOrThrow();
		PetInfo petInfo = Utils.transform(customData.get("petInfo").asString(""), petInfoJson -> !petInfoJson.isEmpty() ? PetInfo.CODEC.parse(JsonOps.INSTANCE, JsonParser.parseString(petInfoJson)).getOrThrow() : null);
		LimitedEditionInfo limitedEditionInfo = LimitedEditionInfo.CODEC.parse(customData).getOrThrow();
		IntList cakeBagCakeYears = retriever.cakeBagCakeYears();

		return new DynamicItemMetadata(enchantments, rarityUpgrades, reforge, upgradeLevel, dungeonUpgrades,
				gearUpgrades, gemstoneSlots, gemstones, drillInfo, attributes,
				auctionBidInfo, miscModifiers, accessoryUpgrades, cosmetics, petInfo,
				limitedEditionInfo, cakeBagCakeYears);
	}
}
