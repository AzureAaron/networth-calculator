package net.azureaaron.networth.item;

import java.util.Optional;
import java.util.OptionalInt;

import org.jetbrains.annotations.ApiStatus;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.azureaaron.networth.utils.CodecUtils;

@ApiStatus.Internal
public record DrillInfo(Optional<String> fuelTank, Optional<String> engine, Optional<String> upgradeModule, int polarvoids, OptionalInt pickonimbusDurability, OptionalInt divanPowderCoating) {
	static final Codec<DrillInfo> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Codec.STRING.optionalFieldOf("drill_part_fuel_tank").forGetter(DrillInfo::fuelTank),
			Codec.STRING.optionalFieldOf("drill_part_engine").forGetter(DrillInfo::engine),
			Codec.STRING.optionalFieldOf("drill_part_upgrade_module").forGetter(DrillInfo::upgradeModule),
			Codec.INT.optionalFieldOf("polarvoid", 0).forGetter(DrillInfo::polarvoids),
			CodecUtils.optionalInt(Codec.INT.optionalFieldOf("pickonimbus_durability")).forGetter(DrillInfo::pickonimbusDurability),
			CodecUtils.optionalInt(Codec.INT.optionalFieldOf("divan_powder_coating")).forGetter(DrillInfo::divanPowderCoating))
			.apply(instance, DrillInfo::new));

	public boolean isAnyDrillPartPresent() {
		return fuelTank.isPresent() || engine.isPresent() || upgradeModule.isPresent() || polarvoids > 0;
	}
}
