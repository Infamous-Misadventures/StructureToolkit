package mod.patrigan.structure_toolkit.mixin;

import net.minecraft.fluid.Fluids;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.structure.AbstractVillagePiece;
import net.minecraft.world.gen.feature.structure.StructureManager;
import net.minecraft.world.server.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;

import static net.minecraft.tags.FluidTags.WATER;

@Mixin(AbstractVillagePiece.class)
public class AbstractVillagePieceMixin {

    @Inject(at = @At("HEAD"), method = "Lnet/minecraft/world/gen/feature/structure/AbstractVillagePiece;postProcess(Lnet/minecraft/world/ISeedReader;Lnet/minecraft/world/gen/feature/structure/StructureManager;Lnet/minecraft/world/gen/ChunkGenerator;Ljava/util/Random;Lnet/minecraft/util/math/MutableBoundingBox;Lnet/minecraft/util/math/ChunkPos;Lnet/minecraft/util/math/BlockPos;)Z", cancellable = true)
    public void structure_toolkit_postProcessFluidTicks(ISeedReader p_230383_1_, StructureManager p_230383_2_, ChunkGenerator p_230383_3_, Random p_230383_4_, MutableBoundingBox p_230383_5_, ChunkPos p_230383_6_, BlockPos p_230383_7_, CallbackInfoReturnable<Boolean> cir){
        BlockPos.betweenClosedStream(p_230383_5_).forEach(blockPos -> {
            scheduleFluidTick(p_230383_1_, blockPos);
        });
    }

    private void scheduleFluidTick(ISeedReader p_230383_1_, BlockPos blockPos) {
        if(p_230383_1_.getBlockState(blockPos).getFluidState().is(WATER)) {
            p_230383_1_.getLiquidTicks().scheduleTick(blockPos, Fluids.WATER, 5);
        }
    }

}
