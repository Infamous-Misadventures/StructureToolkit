package mod.patrigan.structure_toolkit.world.gen.processors;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.patrigan.structure_toolkit.init.ModProcessors;
import mod.patrigan.structure_toolkit.util.RandomType;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.gen.feature.template.IStructureProcessorType;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.StructureProcessor;
import net.minecraft.world.gen.feature.template.Template;

import java.util.List;
import java.util.Random;

import static mod.patrigan.structure_toolkit.util.RandomType.RANDOM_TYPE_CODEC;
import static mod.patrigan.structure_toolkit.world.gen.processors.ProcessorUtil.getBlock;
import static mod.patrigan.structure_toolkit.world.gen.processors.ProcessorUtil.isFaceFull;
import static net.minecraft.block.Blocks.AIR;
import static net.minecraft.block.SnowBlock.LAYERS;
import static net.minecraft.util.Direction.DOWN;
import static net.minecraft.util.Direction.UP;

public class SnowProcessor extends StructureProcessor {

    public static final Codec<SnowProcessor> CODEC = RecordCodecBuilder.create(builder ->
        builder.group(
                Codec.FLOAT.fieldOf("rarity").forGetter(processor -> processor.rarity),
                Codec.INT.optionalFieldOf("max_height", 2).forGetter(processor -> processor.maxHeight),
                RANDOM_TYPE_CODEC.optionalFieldOf("random_type", RandomType.BLOCK).forGetter(processor -> processor.randomType),
                RANDOM_TYPE_CODEC.optionalFieldOf("height_random_type", RandomType.BLOCK).forGetter(processor -> processor.heightRandomType)
        ).apply(builder, SnowProcessor::new));
    private static final long SEED = 3718417L;
    private final float rarity;
    private final int maxHeight;
    private final RandomType randomType;
    private final RandomType heightRandomType;

    public SnowProcessor(float rarity, int maxHeight, RandomType randomType, RandomType heightRandomType) {
        this.rarity = rarity;
        this.maxHeight = maxHeight;
        this.randomType = randomType;
        this.heightRandomType = heightRandomType;
    }

    @Override
    public Template.BlockInfo process(IWorldReader world, BlockPos piecePos, BlockPos structurePos, Template.BlockInfo rawBlockInfo, Template.BlockInfo blockInfo, PlacementSettings settings, Template template) {
        Random random = ProcessorUtil.getRandom(randomType, blockInfo.pos, piecePos, structurePos, SEED);
        BlockState blockstate = blockInfo.state;
        BlockPos blockpos = blockInfo.pos;
        if(blockstate.getBlock().equals(AIR) && random.nextFloat() <= rarity){
            List<Template.BlockInfo> pieceBlocks = settings.getRandomPalette(template.palettes, piecePos).blocks();
            if(isFaceFull(getBlock(pieceBlocks, rawBlockInfo.pos.relative(DOWN)), UP)) {
                Block snow = Blocks.SNOW;
                Random pieceRandom = ProcessorUtil.getRandom(randomType, blockInfo.pos, piecePos, structurePos, SEED);
                BlockState blockState = snow.defaultBlockState().setValue(LAYERS, pieceRandom.nextInt(maxHeight) + 1);
                return new Template.BlockInfo(blockpos, blockState, blockInfo.nbt);
            }
        }
        return blockInfo;
    }

    protected IStructureProcessorType<?> getType() {
        return ModProcessors.SNOW;
    }
}