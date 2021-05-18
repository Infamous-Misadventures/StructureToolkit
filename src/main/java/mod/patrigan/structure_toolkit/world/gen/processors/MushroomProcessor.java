package mod.patrigan.structure_toolkit.world.gen.processors;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.patrigan.structure_toolkit.init.ModProcessors;
import mod.patrigan.structure_toolkit.util.RandomType;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.gen.feature.template.IStructureProcessorType;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.StructureProcessor;
import net.minecraft.world.gen.feature.template.Template;

import java.util.Collections;
import java.util.List;
import java.util.Random;

import static mod.patrigan.structure_toolkit.init.ModTags.Blocks.MUSHROOMS;
import static mod.patrigan.structure_toolkit.util.RandomType.RANDOM_TYPE_CODEC;
import static mod.patrigan.structure_toolkit.world.gen.processors.ProcessorUtil.*;
import static net.minecraft.block.Blocks.AIR;
import static net.minecraft.util.Direction.DOWN;
import static net.minecraft.util.Direction.UP;

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
    public Template.BlockInfo process(IWorldReader world, BlockPos piecePos, BlockPos structurePos, Template.BlockInfo rawBlockInfo, Template.BlockInfo blockInfo, PlacementSettings settings, Template template) {
        Random random = ProcessorUtil.getRandom(randomType, blockInfo.pos, piecePos, structurePos, world, SEED);
        BlockState blockstate = blockInfo.state;
        BlockPos blockpos = blockInfo.pos;
        if(blockstate.getBlock().equals(AIR) && random.nextFloat() <= rarity){
            List<Template.BlockInfo> pieceBlocks = settings.getRandomPalette(template.palettes, piecePos).blocks();
            if(isFaceFull(getBlock(pieceBlocks, rawBlockInfo.pos.relative(DOWN)), UP)) {
                Random mushroomRandom = ProcessorUtil.getRandom(randomType, blockInfo.pos, piecePos, structurePos, world, SEED);
                Block mushroom = getRandomBlockFromTag(MUSHROOMS, mushroomRandom, exclusionList);
                return new Template.BlockInfo(blockpos, mushroom.defaultBlockState(), blockInfo.nbt);
            }
        }
        return blockInfo;
    }

    protected IStructureProcessorType<?> getType() {
        return ModProcessors.MUSHROOMS;
    }
}