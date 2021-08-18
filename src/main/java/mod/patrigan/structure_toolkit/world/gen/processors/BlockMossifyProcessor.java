package mod.patrigan.structure_toolkit.world.gen.processors;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.patrigan.structure_toolkit.util.RandomType;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.gen.feature.template.IStructureProcessorType;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.StructureProcessor;
import net.minecraft.world.gen.feature.template.Template;

import java.util.Random;

import static mod.patrigan.structure_toolkit.StructureToolkit.MOD_ID;
import static mod.patrigan.structure_toolkit.init.ModProcessors.BLOCK_MOSSIFY;
import static mod.patrigan.structure_toolkit.util.RandomType.RANDOM_TYPE_CODEC;
import static net.minecraft.block.Blocks.AIR;
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
    public Template.BlockInfo process(IWorldReader world, BlockPos piecePos, BlockPos structurePos, Template.BlockInfo rawBlockInfo, Template.BlockInfo blockInfo, PlacementSettings settings, Template template) {
        Random random = ProcessorUtil.getRandom(randomType, blockInfo.pos, piecePos, structurePos, world, SEED);
        BlockState blockstate = blockInfo.state;
        BlockPos blockpos = blockInfo.pos;
        BlockState blockstate1 = null;
        Block newBlock = BLOCKS.getValue(new ResourceLocation("mossy_" + blockstate.getBlock().getRegistryName().getPath()));
        if(newBlock == null || newBlock.equals(AIR) ){
            newBlock = BLOCKS.getValue(new ResourceLocation(MOD_ID, "mossy_" + blockstate.getBlock().getRegistryName().getPath()));
        }
        if(newBlock != null && !newBlock.equals(AIR) && random.nextFloat() < mossiness){
            if (newBlock.is(BlockTags.STAIRS)) {
                blockstate1 = ProcessorUtil.copyStairsState(blockstate, newBlock);
            } else if (newBlock.is(BlockTags.SLABS)) {
                blockstate1 = ProcessorUtil.copySlabState(blockstate, newBlock);
            } else if (newBlock.is(BlockTags.WALLS)) {
                blockstate1 = ProcessorUtil.copyWallState(blockstate, newBlock);
            }else if (blockstate.getBlock().equals(Blocks.COBBLESTONE) || blockstate.getBlock().equals(Blocks.STONE_BRICKS)){
                blockstate1 = newBlock.defaultBlockState();
            }
        }

        return blockstate1 != null ? new Template.BlockInfo(blockpos, blockstate1, blockInfo.nbt) : blockInfo;
    }

    protected IStructureProcessorType<?> getType() {
        return BLOCK_MOSSIFY;
    }
}