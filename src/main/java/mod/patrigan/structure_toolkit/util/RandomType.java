package mod.patrigan.structure_toolkit.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import net.minecraft.item.DyeColor;

public enum RandomType {
    BLOCK,
    PIECE,
    STRUCTURE;

    public static final Codec<RandomType> RANDOM_TYPE_CODEC = Codec.STRING.flatComapMap(s -> RandomType.byName(s, null), d -> DataResult.success(d.name()));

    public static RandomType byName(String input, RandomType defaultRandomType) {
        for(RandomType enumEntry : values()) {
            if (enumEntry.name().equals(input.toUpperCase())) {
                return enumEntry;
            }
        }

        return defaultRandomType;
    }

}
