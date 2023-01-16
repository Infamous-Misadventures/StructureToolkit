package mod.patrigan.structure_toolkit.world.gen.processors;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.patrigan.structure_toolkit.init.ModProcessors;
import mod.patrigan.structure_toolkit.util.RandomType;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import net.minecraft.util.RandomSource;
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;
import static mod.patrigan.structure_toolkit.util.RandomType.RANDOM_TYPE_CODEC;
import static net.minecraft.tags.BlockTags.*;
import static net.minecraft.world.level.block.Blocks.FLOWER_POT;
import static net.minecraftforge.registries.ForgeRegistries.BLOCKS;

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
    public StructureTemplate.StructureBlockInfo process(LevelReader world, BlockPos piecePos, BlockPos structurePos, StructureTemplate.StructureBlockInfo rawBlockInfo, StructureTemplate.StructureBlockInfo blockInfo, StructurePlaceSettings settings, StructureTemplate template) {
        RandomSource random = ProcessorUtil.getRandom(randomType, blockInfo.pos, piecePos, structurePos, world, SEED);
        BlockState blockstate = blockInfo.state;
        BlockPos blockpos = blockInfo.pos;
        if(blockstate.getBlock().equals(FLOWER_POT)){
            List<Block> flowerPotList = ForgeRegistries.BLOCKS.tags().getTag(FLOWER_POTS).stream().filter(this::flowerPotFilter).collect(Collectors.toList());
            return new StructureTemplate.StructureBlockInfo(blockpos, flowerPotList.get(random.nextInt(flowerPotList.size())).defaultBlockState(), blockInfo.nbt);
        }
        return blockInfo;
    }

    private boolean flowerPotFilter(Block block) {
        if(!(block instanceof FlowerPotBlock)){
            return false;
        }
        FlowerPotBlock flowerPotBlock = (FlowerPotBlock) block;
        Block content = flowerPotBlock.getContent();
        if(includeSaplings && BLOCKS.tags().getTag(SAPLINGS).contains(content) && !exclusionList.contains(ForgeRegistries.BLOCKS.getKey(content))){
            return true;
        }
        if(includeFlowers && BLOCKS.tags().getTag(FLOWERS).contains(content) && !exclusionList.contains(ForgeRegistries.BLOCKS.getKey(content))){
            return true;
        }
        if(content.defaultBlockState().isAir() && !exclusionList.contains(ForgeRegistries.BLOCKS.getKey(content))){
            return true;
        }
        return false;
    }

    protected StructureProcessorType<?> getType() {
        return ModProcessors.FLOWER_POTS;
    }
}