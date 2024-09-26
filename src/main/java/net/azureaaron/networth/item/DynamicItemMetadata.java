package net.azureaaron.networth.item;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import org.jetbrains.annotations.ApiStatus;

import com.google.gson.JsonParser;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.JsonOps;

import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.azureaaron.networth.utils.CodecUtils;

@ApiStatus.Internal
record DynamicItemMetadata(Object2IntMap<String> enchantments, int rarityUpgrades, Optional<String> reforge, int upgradeLevel, DungeonUpgrades dungeonUpgrades,
		GearUpgrades gearUpgrades, List<String> gemstoneSlots, Map<String, Either<String, Gemstone>> gemstones, DrillInfo drillInfo, Object2IntMap<String> attributes,
		AuctionBidInfo auctionBidInfo, MiscModifiers miscModifiers, AccessoryUpgrades accessoryUpgrades, Cosmetics cosmetics, PetInfo petInfo,
		LimitedEditionInfo limitedEditionInfo, IntList cakeBagCakeYears) implements ItemMetadata {
	private static final Codec<Object2IntMap<String>> OBJECT_2_INT_MAP_CODEC = CodecUtils.createObject2IntMapCodec(Codec.STRING);
	private static final Function<Dynamic<?>, String> STRING_MAPPER = dynamic -> dynamic.asString("");

	static DynamicItemMetadata of(Dynamic<?> customData, ItemMetadataRetriever retriever) {
		//TODO Add an EMPTY state for each record and use that as a partial if all else fails, perhaps setup some logging for it too
		Object2IntMap<String> enchantments = OBJECT_2_INT_MAP_CODEC.parse(customData.get("enchantments").orElseEmptyMap()).getOrThrow();
		int rarityUpgrades = customData.get("rarity_upgrades").asInt(0);
		Optional<String> reforge = customData.get("modifier").result().map(STRING_MAPPER);
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
		PetInfo petInfo = customData.get("petInfo").result()
				//NB: The petInfo field is expected to be JSON if its present
				.map(petInfoJsonDynamic -> PetInfo.CODEC.parse(JsonOps.INSTANCE, JsonParser.parseString(petInfoJsonDynamic.asString("")))
						.setPartial(PetInfo.EMPTY)
						.getPartialOrThrow()).orElse(PetInfo.EMPTY);
		LimitedEditionInfo limitedEditionInfo = LimitedEditionInfo.CODEC.parse(customData).getOrThrow();
		IntList cakeBagCakeYears = retriever.cakeBagCakeYears();

		return new DynamicItemMetadata(enchantments, rarityUpgrades, reforge, upgradeLevel, dungeonUpgrades,
				gearUpgrades, gemstoneSlots, gemstones, drillInfo, attributes,
				auctionBidInfo, miscModifiers, accessoryUpgrades, cosmetics, petInfo,
				limitedEditionInfo, cakeBagCakeYears);
	}
}
