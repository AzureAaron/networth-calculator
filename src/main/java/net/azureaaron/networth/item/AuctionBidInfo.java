package net.azureaaron.networth.item;

import java.util.OptionalInt;
import java.util.OptionalLong;

import org.jetbrains.annotations.ApiStatus;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.azureaaron.networth.utils.CodecUtils;

@ApiStatus.Internal
public record AuctionBidInfo(OptionalLong winningBid, /* Shen's Specific*/ OptionalLong price, OptionalInt bid, OptionalInt auction) {
	static final Codec<AuctionBidInfo> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			CodecUtils.optionalLong(Codec.LONG.optionalFieldOf("winning_bid")).forGetter(AuctionBidInfo::winningBid),
			CodecUtils.optionalLong(Codec.LONG.optionalFieldOf("price")).forGetter(AuctionBidInfo::price),
			CodecUtils.optionalInt(Codec.INT.optionalFieldOf("bid")).forGetter(AuctionBidInfo::bid),
			CodecUtils.optionalInt(Codec.INT.optionalFieldOf("auction")).forGetter(AuctionBidInfo::auction))
			.apply(instance, AuctionBidInfo::new));
}
