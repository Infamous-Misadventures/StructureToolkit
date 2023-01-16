package mod.patrigan.structure_toolkit.world.gen.processors;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.patrigan.structure_toolkit.init.ModProcessors;
import mod.patrigan.structure_toolkit.util.RandomType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

import java.util.List;
import net.minecraft.util.RandomSource;

import static mod.patrigan.structure_toolkit.util.RandomType.RANDOM_TYPE_CODEC;
import static mod.patrigan.structure_toolkit.world.gen.processors.ProcessorUtil.getBlock;
import static mod.patrigan.structure_toolkit.world.gen.processors.ProcessorUtil.isFaceFull;
import static net.minecraft.core.Direction.DOWN;
import static net.minecraft.core.Direction.UP;
import static net.minecraft.world.level.block.SnowLayerBlock.LAYERS;

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
    public StructureTemplate.StructureBlockInfo process(LevelReader world, BlockPos piecePos, BlockPos structurePos, StructureTemplate.StructureBlockInfo rawBlockInfo, StructureTemplate.StructureBlockInfo blockInfo, StructurePlaceSettings settings, StructureTemplate template) {
        RandomSource random = ProcessorUtil.getRandom(randomType, blockInfo.pos, piecePos, structurePos, world, SEED);
        BlockState blockstate = blockInfo.state;
        BlockPos blockpos = blockInfo.pos;
        if(blockstate.isAir() && random.nextFloat() <= rarity){
            List<StructureTemplate.StructureBlockInfo> pieceBlocks = settings.getRandomPalette(template.palettes, piecePos).blocks();
            if(isFaceFull(getBlock(pieceBlocks, rawBlockInfo.pos.relative(DOWN)), UP)) {
                Block snow = Blocks.SNOW;
                RandomSource pieceRandom = ProcessorUtil.getRandom(randomType, blockInfo.pos, piecePos, structurePos, world, SEED);
                BlockState blockState = snow.defaultBlockState().setValue(LAYERS, pieceRandom.nextInt(maxHeight) + 1);
                return new StructureTemplate.StructureBlockInfo(blockpos, blockState, blockInfo.nbt);
            }
        }
        return blockInfo;
    }

    protected StructureProcessorType<?> getType() {
        return ModProcessors.SNOW;
    }
}