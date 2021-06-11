package mod.patrigan.structure_toolkit.world.gen.processors;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.patrigan.structure_toolkit.init.ModProcessors;
import mod.patrigan.structure_toolkit.util.RandomType;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FlowerPotBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.gen.feature.template.IStructureProcessorType;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.StructureProcessor;
import net.minecraft.world.gen.feature.template.Template;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;
import static mod.patrigan.structure_toolkit.util.RandomType.RANDOM_TYPE_CODEC;
import static net.minecraft.block.Blocks.FLOWER_POT;
import static net.minecraft.tags.BlockTags.*;

public class FlowerPotProcessor extends StructureProcessor {
    public static final Codec<FlowerPotProcessor> CODEC = RecordCodecBuilder.create(builder ->
            builder.group(
                    Codec.BOOL.optionalFieldOf("include_saplings", true).forGetter(processor -> processor.includeSaplings),
                    Codec.BOOL.optionalFieldOf("include_flowers", true).forGetter(processor -> processor.includeFlowers),
                    ResourceLocation.CODEC.listOf().optionalFieldOf("exclusion_list", emptyList()).forGetter(data -> data.exclusionList),
                    RANDOM_TYPE_CODEC.optionalFieldOf("random_type", RandomType.BLOCK).forGetter(processor -> processor.randomType)
            ).apply(builder, FlowerPotProcessor::new));
    private static final long SEED = 9348841L;
    private final boolean includeSaplings;
    private final boolean includeFlowers;
    private final List<ResourceLocation> exclusionList;
    private final RandomType randomType;

    public FlowerPotProcessor(boolean includeSaplings, boolean includeFlowers, List<ResourceLocation> exclusionList, RandomType randomType) {
        this.includeSaplings = includeSaplings;
        this.includeFlowers = includeFlowers;
        this.exclusionList = exclusionList;
        this.randomType = randomType;
    }

    @Override
    public Template.BlockInfo process(IWorldReader world, BlockPos piecePos, BlockPos structurePos, Template.BlockInfo rawBlockInfo, Template.BlockInfo blockInfo, PlacementSettings settings, Template template) {
        Random random = ProcessorUtil.getRandom(randomType, blockInfo.pos, piecePos, structurePos, world, SEED);
        BlockState blockstate = blockInfo.state;
        BlockPos blockpos = blockInfo.pos;
        if(blockstate.getBlock().equals(FLOWER_POT)){
            List<Block> flowerPotList = FLOWER_POTS.getValues().stream().filter(this::flowerPotFilter).collect(Collectors.toList());
            return new Template.BlockInfo(blockpos, flowerPotList.get(random.nextInt(flowerPotList.size())).defaultBlockState(), blockInfo.nbt);
        }
        return blockInfo;
    }

    private boolean flowerPotFilter(Block block) {
        if(!(block instanceof FlowerPotBlock)){
            return false;
        }
        FlowerPotBlock flowerPotBlock = (FlowerPotBlock) block;
        Block content = flowerPotBlock.getContent();
        if(includeSaplings && content.is(SAPLINGS) && !exclusionList.contains(content.getRegistryName())){
            return true;
        }
        if(includeFlowers && content.is(FLOWERS) && !exclusionList.contains(content.getRegistryName())){
            return true;
        }
        if(content.defaultBlockState().isAir() && !exclusionList.contains(content.getRegistryName())){
            return true;
        }
        return false;
    }

    protected IStructureProcessorType<?> getType() {
        return ModProcessors.FLOWER_POTS;
    }
}