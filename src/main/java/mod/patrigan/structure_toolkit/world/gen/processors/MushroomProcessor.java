package mod.patrigan.structure_toolkit.world.gen.processors;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.patrigan.structure_toolkit.init.ModProcessors;
import mod.patrigan.structure_toolkit.util.RandomType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

import java.util.Collections;
import java.util.List;
import net.minecraft.util.RandomSource;

import static mod.patrigan.structure_toolkit.init.ModTags.Blocks.MUSHROOMS;
import static mod.patrigan.structure_toolkit.util.RandomType.RANDOM_TYPE_CODEC;
import static mod.patrigan.structure_toolkit.world.gen.processors.ProcessorUtil.*;
import static net.minecraft.core.Direction.DOWN;
import static net.minecraft.core.Direction.UP;

public class MushroomProcessor extends StructureProcessor {
    public static final Codec<mod.patrigan.structure_toolkit.world.gen.processors.MushroomProcessor> CODEC = RecordCodecBuilder.create(builder ->
            builder.group(
                    ResourceLocation.CODEC.listOf().optionalFieldOf("exclusion_list", Collections.emptyList()).forGetter(data -> data.exclusionList),
                    Codec.FLOAT.fieldOf("rarity").forGetter(processor -> processor.rarity),
                    RANDOM_TYPE_CODEC.optionalFieldOf("random_type", RandomType.BLOCK).forGetter(processor -> processor.randomType),
                    RANDOM_TYPE_CODEC.optionalFieldOf("mushroom_random_type", RandomType.PIECE).forGetter(processor -> processor.mushroomRandomType)
            ).apply(builder, mod.patrigan.structure_toolkit.world.gen.processors.MushroomProcessor::new));
    private static final long SEED = 3478985L;

    private final List<ResourceLocation> exclusionList;
    private final float rarity;
    private final RandomType randomType;
    private final RandomType mushroomRandomType;

    public MushroomProcessor(List<ResourceLocation> exclusionList, float rarity, RandomType randomType, RandomType mushroomRandomType) {
        this.exclusionList = exclusionList;
        this.rarity = rarity;
        this.randomType = randomType;
        this.mushroomRandomType = mushroomRandomType;
    }

    @Override
    public StructureTemplate.StructureBlockInfo process(LevelReader world, BlockPos piecePos, BlockPos structurePos, StructureTemplate.StructureBlockInfo rawBlockInfo, StructureTemplate.StructureBlockInfo blockInfo, StructurePlaceSettings settings, StructureTemplate template) {
        RandomSource random = ProcessorUtil.getRandom(randomType, blockInfo.pos, piecePos, structurePos, world, SEED);
        BlockState blockstate = blockInfo.state;
        BlockPos blockpos = blockInfo.pos;
        if(blockstate.isAir() && random.nextFloat() <= rarity){
            List<StructureTemplate.StructureBlockInfo> pieceBlocks = settings.getRandomPalette(template.palettes, piecePos).blocks();
            if(isFaceFull(getBlock(pieceBlocks, rawBlockInfo.pos.relative(DOWN)), UP)) {
                RandomSource mushroomRandom = ProcessorUtil.getRandom(randomType, blockInfo.pos, piecePos, structurePos, world, SEED);
                Block mushroom = getRandomBlockFromTag(MUSHROOMS, mushroomRandom, exclusionList);
                return new StructureTemplate.StructureBlockInfo(blockpos, mushroom.defaultBlockState(), blockInfo.nbt);
            }
        }
        return blockInfo;
    }

    protected StructureProcessorType<?> getType() {
        return ModProcessors.MUSHROOMS;
    }
}