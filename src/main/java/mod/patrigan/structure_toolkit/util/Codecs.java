package mod.patrigan.structure_toolkit.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Rotations;

public class Codecs {
    public static final Codec<Rotations> CODEC = RecordCodecBuilder.create(builder ->
            builder.group(
                    Codec.FLOAT.fieldOf("x").forGetter(Rotations::getX),
                    Codec.FLOAT.fieldOf("y").forGetter(Rotations::getY),
                    Codec.FLOAT.fieldOf("z").forGetter(Rotations::getZ)
            ).apply(builder, Rotations::new));
}
