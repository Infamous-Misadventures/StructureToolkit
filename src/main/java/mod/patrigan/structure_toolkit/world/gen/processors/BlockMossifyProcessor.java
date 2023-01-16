package mod.patrigan.structure_toolkit.world.gen.processors;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.patrigan.structure_toolkit.util.RandomType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.tags.BlockTags;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Random;

import static mod.patrigan.structure_toolkit.StructureToolkit.MOD_ID;
import static mod.patrigan.structure_toolkit.init.ModProcessors.BLOCK_MOSSIFY;
import static mod.patrigan.structure_toolkit.util.RandomType.RANDOM_TYPE_CODEC;
import static net.minecraftforge.registries.ForgeRegistries.BLOCKS;

public class BlockMossifyProcessor extends StructureProcessor {
    public static final Codec<BlockMossifyProcessor> CODEC = RecordCodecBuilder.create(builder ->
            builder.group(
                    Codec.FLOAT.fieldOf("mossiness").forGetter(processor -> processor.mossiness),
                    RANDOM_TYPE_CODEC.optionalFieldOf("randomType", RandomType.BLOCK).forGetter(processor -> processor.randomType)
            ).apply(builder, BlockMossifyProcessor::new));
    private static final long SEED = 135175L;

    private final float mossiness;
    private final RandomType randomType;

    public BlockMossifyProcessor(float mossiness, RandomType randomType) {
        this.mossiness = mossiness;
        this.randomType = randomType;
    }

    @Override
    public StructureTemplate.StructureBlockInfo process(LevelReader world, BlockPos piecePos, BlockPos structurePos, StructureTemplate.StructureBlockInfo rawBlockInfo, StructureTemplate.StructureBlockInfo blockInfo, StructurePlaceSettings settings, StructureTemplate template) {
        Random random = ProcessorUtil.getRandom(randomType, blockInfo.pos, piecePos, structurePos, world, SEED);
        BlockState blockstate = blockInfo.state;
        BlockPos blockpos = blockInfo.pos;
        BlockState blockstate1 = null;
        Block newBlock = BLOCKS.getValue(new ResourceLocation("mossy_" + blockstate.getBlock().getRegistryName().getPath()));
        if(newBlock == null || newBlock.defaultBlockState().isAir() ){
            newBlock = BLOCKS.getValue(new ResourceLocation(MOD_ID, "mossy_" + blockstate.getBlock().getRegistryName().getPath()));
        }
        if(newBlock != null && !newBlock.defaultBlockState().isAir() && random.nextFloat() < mossiness){
            if (BLOCKS.tags().getTag(BlockTags.STAIRS).contains(newBlock)) {
                blockstate1 = ProcessorUtil.copyStairsState(blockstate, newBlock);
            } else if (BLOCKS.tags().getTag(BlockTags.SLABS).contains(newBlock)) {
                blockstate1 = ProcessorUtil.copySlabState(blockstate, newBlock);
            } else if (BLOCKS.tags().getTag(BlockTags.WALLS).contains(newBlock)) {
                blockstate1 = ProcessorUtil.copyWallState(blockstate, newBlock);
            }else if (blockstate.getBlock().equals(Blocks.COBBLESTONE) || blockstate.getBlock().equals(Blocks.STONE_BRICKS)){
                blockstate1 = newBlock.defaultBlockState();
            }
        }

        return blockstate1 != null ? new StructureTemplate.StructureBlockInfo(blockpos, blockstate1, blockInfo.nbt) : blockInfo;
    }

    protected StructureProcessorType<?> getType() {
        return BLOCK_MOSSIFY;
    }
}