package net.azureaaron.networth;

import java.util.List;

import org.jetbrains.annotations.ApiStatus;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;

/**
 * Used to describe a calculation made for the networth of an an object.
 * There is a {@link Codec} provided for serializing a list of this to some other format such as JSON.
 */
public record Calculation(Type type, String id, double price, int count) {
	private static final Codec<Calculation> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Codec.STRING.comapFlatMap(name -> {
				try {
					return DataResult.success(Type.valueOf(name));
				} catch (IllegalArgumentException ignored) {
					return DataResult.error(() -> "Unknown calculation type: " + name);
				}
			}, Type::name).fieldOf("type").forGetter(Calculation::type),
			Codec.STRING.fieldOf("id").forGetter(Calculation::id),
			Codec.DOUBLE.fieldOf("price").forGetter(Calculation::price),
			Codec.INT.fieldOf("count").forGetter(Calculation::count))
			.apply(instance, Calculation::new));
	public static final Codec<List<Calculation>> LIST_CODEC = CODEC.listOf();

	@ApiStatus.Internal
	public Calculation(Type type, String id, double price, int count) {
		this.type = type;
		this.id = id;
		this.price = price;
		this.count = count;
	}

	static Calculation of(Type type, String id, double price) {
		return new Calculation(type, id, price, 1);
	}

	static Calculation of(Type type, String id, double price, int count) {
		return new Calculation(type, id, price, count);
	}

	Calculation add(double addedPrice) {
		return new Calculation(type, id, price + addedPrice, count);
	}

	/**
	 * @implNote New constants may be added regularly, so if you switch on this ensure that you have a default case to avoid
	 * exceptions about incompatible class changes.
	 */
	public enum Type {
		STAR,
		PRESTIGE,
		GOD_ROLL,
		SHEN_AUCTION,
		WINNING_BID,
		ENCHANTMENT,
		SKIN,
		SILEX,
		GOLDEN_BOUNTY,
		ATTRIBUTE,
		POCKET_SACK_IN_A_SACK,
		WOOD_SINGULARITY,
		JALAPENO_BOOK,
		TRANSMISSION_TUNER,
		MANA_DISINTEGRATOR,
		THUNDER_IN_A_BOTTLE,
		RUNE,
		FUMING_POTATO_BOOK,
		HOT_POTATO_BOOK,
		DYE,
		ART_OF_WAR,
		ART_OF_PEACE,
		FARMING_FOR_DUMMIES,
		TALISMAN_ENRICHMENT,
		RECOMBOBULATOR,
		GEMSTONE_SLOT,
		GEMSTONE,
		POWER_SCROLL,
		REFORGE,
		MASTER_STAR,
		WITHER_BLADE_SCROLL,
		DRILL_PART,
		POLARVOID_BOOK,
		DIVAN_POWDER_COATING,
		ETHERWARP_CONDUIT,
		NEW_YEAR_CAKES,

		//Pet
		PET_ITEM,

		//Misc
		SACK_ITEM,
		ESSENCE;
	}
}
