package mod.patrigan.structure_toolkit.world.gen.processors;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.material.Fluids;

import static mod.patrigan.structure_toolkit.init.ModProcessors.WATERFLOW_FIX_PROCESSOR;
import static net.minecraft.world.level.material.Fluids.FLOWING_WATER;
import static net.minecraft.world.level.material.Fluids.WATER;

public class WaterflowFixProcessor extends StructureProcessor {

    public static final Codec<WaterflowFixProcessor> CODEC = Codec.unit(WaterflowFixProcessor::new);
    private WaterflowFixProcessor() { }

    @Override
    public StructureTemplate.StructureBlockInfo process(LevelReader world, BlockPos piecePos, BlockPos seedPos, StructureTemplate.StructureBlockInfo rawBlockInfo, StructureTemplate.StructureBlockInfo blockInfo, StructurePlaceSettings settings, StructureTemplate template) {

        // Sometimes placed pieces with water flowing down don't seem to flow anymore. This will schedule a post placement fluid tick that makes them flow again.
        if(blockInfo.state.getFluidState().is(WATER)) {
            ((ServerLevelAccessor) world).scheduleTick(blockInfo.pos, Fluids.WATER, 5);
        }else if(blockInfo.state.getFluidState().is(FLOWING_WATER)) {
            ((ServerLevelAccessor) world).scheduleTick(blockInfo.pos, Fluids.FLOWING_WATER, 5);
        }

        return blockInfo;
    }

    @Override
    protected StructureProcessorType<?> getType() {
        return WATERFLOW_FIX_PROCESSOR;
    }
}