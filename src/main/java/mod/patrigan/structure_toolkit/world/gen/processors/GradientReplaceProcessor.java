package mod.patrigan.structure_toolkit.world.gen.processors;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.patrigan.structure_toolkit.util.OpenSimplex2F;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.gen.feature.template.IStructureProcessorType;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.StructureProcessor;
import net.minecraft.world.gen.feature.template.Template;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static mod.patrigan.structure_toolkit.init.ModProcessors.GRADIENT_SPOT_REPLACE;
import static net.minecraftforge.registries.ForgeRegistries.BLOCKS;

public class GradientReplaceProcessor extends StructureProcessor {
    public static final Codec<GradientReplaceProcessor> CODEC = RecordCodecBuilder.create(builder ->
            builder.group(
                    Codec.mapPair(ResourceLocation.CODEC.fieldOf("resource_location"),Codec.floatRange(0, 1).fieldOf("step_size")).codec().listOf().fieldOf("gradient_list").forGetter(processor -> processor.gradientList),
                    Codec.INT.optionalFieldOf("seed_adjustment", 0).forGetter(processor -> processor.seedAdjustment),
                    ResourceLocation.CODEC.fieldOf("to_replace").forGetter(data -> data.toReplace)
            ).apply(builder, GradientReplaceProcessor::new));

    private final List<Pair<ResourceLocation, Float>> gradientList;
    private final int seedAdjustment;
    private final ResourceLocation toReplace;

    protected static Map<Long, OpenSimplex2F> noiseGenSeeds = new HashMap<>();

    public GradientReplaceProcessor(List<Pair<ResourceLocation, Float>> gradientList, int seedAdjustment, ResourceLocation toReplace) {
        this.gradientList = gradientList;
        this.seedAdjustment = seedAdjustment;
        this.toReplace = toReplace;
    }

    public OpenSimplex2F getNoiseGen(long seed) {
        return noiseGenSeeds.computeIfAbsent(seed, OpenSimplex2F::new);
    }

    @Override
    public Template.BlockInfo process(IWorldReader world, BlockPos piecePos, BlockPos structurePos, Template.BlockInfo rawBlockInfo, Template.BlockInfo blockInfo, PlacementSettings settings, @Nullable Template template) {
        OpenSimplex2F noiseGen = null;
        if(world instanceof ISeedReader) {
            noiseGen = getNoiseGen(((ISeedReader) world).getSeed()+seedAdjustment);
        }else{
            noiseGen = getNoiseGen(structurePos.asLong()+seedAdjustment);
        }

        BlockState blockstate = blockInfo.state;
        BlockPos blockPos = blockInfo.pos;
        if(!blockstate.getBlock().getRegistryName().getPath().equals(toReplace.getPath())){
            return blockInfo;
        }

        ResourceLocation newBlockResourceLocation = getReplacementBlockResourceLocation(blockPos, noiseGen);
        Block newBlock = BLOCKS.getValue(newBlockResourceLocation);
        if(newBlock == null){
            return blockInfo;
        }

        if (blockstate.getBlock().is(BlockTags.STAIRS)) {
            return new Template.BlockInfo(blockPos, ProcessorUtil.copyStairsState(blockstate, newBlock), blockInfo.nbt);
        } else if (blockstate.getBlock().is(BlockTags.SLABS)) {
            return new Template.BlockInfo(blockPos, ProcessorUtil.copySlabState(blockstate, newBlock), blockInfo.nbt);
        } else if (blockstate.getBlock().is(BlockTags.WALLS)) {
            return new Template.BlockInfo(blockPos, ProcessorUtil.copyWallState(blockstate, newBlock), blockInfo.nbt);
        }else{
            return new Template.BlockInfo(blockPos, newBlock.defaultBlockState(), blockInfo.nbt);
        }
    }

    private ResourceLocation getReplacementBlockResourceLocation(BlockPos blockPos, OpenSimplex2F noiseGen) {
        double noiseValue = (noiseGen.noise3_Classic(blockPos.getX() * 0.075D, blockPos.getY() * 0.075D, blockPos.getZ() * 0.075D));
        float stepSize = 0;
        for(Pair<ResourceLocation, Float> pair: gradientList){
            stepSize = stepSize+pair.getSecond();
            if (noiseValue < stepSize && noiseValue > (stepSize * -1)) {
                return pair.getFirst();
            }
        }
        return toReplace;
    }

    protected IStructureProcessorType<?> getType() {
        return GRADIENT_SPOT_REPLACE;
    }
}
