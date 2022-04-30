package mod.patrigan.structure_toolkit.world.gen.processors;

import com.mojang.serialization.Codec;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.tags.FluidTags;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

import static mod.patrigan.structure_toolkit.init.ModProcessors.WATERLOGGING_FIX_PROCESSOR;
import static mod.patrigan.structure_toolkit.world.gen.processors.ProcessorUtil.getBlock;

public class WaterloggingFixProcessor extends StructureProcessor {

    public static final Codec<WaterloggingFixProcessor> CODEC = Codec.unit(WaterloggingFixProcessor::new);
    private WaterloggingFixProcessor() { }

    @Override
    public StructureTemplate.StructureBlockInfo process(LevelReader world, BlockPos piecePos, BlockPos seedPos, StructureTemplate.StructureBlockInfo rawBlockInfo, StructureTemplate.StructureBlockInfo blockInfo, StructurePlaceSettings settings, StructureTemplate template) {

        // Workaround for https://bugs.mojang.com/browse/MC-130584
        // Due to a hardcoded field in Templates, any waterloggable blocks in structures replacing water in the world will become waterlogged.
        // Idea of workaround is detect if we are placing a waterloggable block and if so, remove the water in the world instead.
        // ONLY RUN THIS IF STRUCTURE BLOCK IS A DRY WATERLOGGABLE BLOCK
        ChunkPos currentChunkPos = new ChunkPos(blockInfo.pos);
        if(blockInfo.state.getBlock() instanceof SimpleWaterloggedBlock && !blockInfo.state.getValue(BlockStateProperties.WATERLOGGED)){
            ChunkAccess currentChunk = world.getChunk(currentChunkPos.x, currentChunkPos.z);
            if(world.getFluidState(blockInfo.pos).is(FluidTags.WATER)){
                currentChunk.setBlockState(blockInfo.pos, Blocks.STONE.defaultBlockState(), false);
            }

            // Remove water in adjacent blocks across chunk boundaries and above/below as well
            BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
            BlockPos.MutableBlockPos mutableTemplate = new BlockPos.MutableBlockPos();
            for (Direction direction : Direction.values()) {
                mutable.set(blockInfo.pos).move(direction);
                mutableTemplate.set(rawBlockInfo.pos).move(direction);
                if (currentChunkPos.x != mutable.getX() >> 4 || currentChunkPos.z != mutable.getZ() >> 4) {
                    currentChunk = world.getChunk(mutable);
                    currentChunkPos = new ChunkPos(mutable);
                }
                BlockState chunkBlockState = currentChunk.getBlockState(mutable);
                if (chunkBlockState.getFluidState().is(FluidTags.WATER)){
                    StructureTemplate.StructureBlockInfo localBlockInfo = getBlock(settings.getRandomPalette(template.palettes, piecePos).blocks(), mutableTemplate);
                    if(localBlockInfo == null || !localBlockInfo.state.getFluidState().is(FluidTags.WATER)) {
                        currentChunk.setBlockState(mutable, Blocks.STONE.defaultBlockState(), false);
                    }
                }
            }
        }

        return blockInfo;
    }

    @Override
    protected StructureProcessorType<?> getType() {
        return WATERLOGGING_FIX_PROCESSOR;
    }
}