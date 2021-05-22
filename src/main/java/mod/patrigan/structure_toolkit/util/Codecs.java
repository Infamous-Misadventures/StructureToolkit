package mod.patrigan.structure_toolkit.util;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ITagCollection;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public class Codecs {
    public static <T> Codec<ITag<T>> iTagCodec(Supplier<ITagCollection<T>> p_232947_0_) {
        return ResourceLocation.CODEC.flatXmap(
                id -> Optional.ofNullable(p_232947_0_.get().getTag(id)).map(DataResult::success).orElseGet(() -> null),
                tag -> Optional.ofNullable(p_232947_0_.get().getId(tag)).map(DataResult::success).orElseGet(() -> null));
    }
}
