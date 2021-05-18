package mod.patrigan.structure_toolkit.world.gen.processors;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.patrigan.structure_toolkit.util.RandomType;
import net.minecraft.block.BlockState;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.gen.feature.template.IStructureProcessorType;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.StructureProcessor;
import net.minecraft.world.gen.feature.template.Template;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static mod.patrigan.structure_toolkit.init.ModProcessors.AIR_RETAINER;
import static mod.patrigan.structure_toolkit.util.RandomType.RANDOM_TYPE_CODEC;

public class AirRetainerProcessor extends StructureProcessor {
    public static final Codec<AirRetainerProcessor> CODEC = RecordCodecBuilder.create(builder ->
            builder.group(
                    ResourceLocation.CODEC.listOf().optionalFieldOf("to_replace", new ArrayList<>()).forGetter(data -> data.toReplace),
                    Codec.FLOAT.optionalFieldOf("rarity", 1F).forGetter(processor -> processor.rarity),
                    RANDOM_TYPE_CODEC.optionalFieldOf("random_type", RandomType.BLOCK).forGetter(processor -> processor.randomType)
            ).apply(builder, AirRetainerProcessor::new));
    private static final long SEED = 478924L;

    private final List<ResourceLocation> toReplace;
    private final float rarity;
    private final RandomType randomType;

    public AirRetainerProcessor(List<ResourceLocation> toReplace, float rarity, RandomType randomType) {
        this.toReplace = toReplace;
        this.rarity = rarity;
        this.randomType = randomType;
    }

    @Override
    public Template.BlockInfo process(IWorldReader world, BlockPos piecePos, BlockPos structurePos, Template.BlockInfo rawBlockInfo, Template.BlockInfo blockInfo, PlacementSettings settings, Template template) {
        Random random = ProcessorUtil.getRandom(randomType, blockInfo.pos, piecePos, structurePos, world, SEED);
        if(world.isEmptyBlock(blockInfo.pos) && random.nextFloat() < rarity && (toReplace.isEmpty() || toReplace.contains(blockInfo.state.getBlock().getRegistryName()))){
            BlockState blockState = world.getBlockState(blockInfo.pos);
            return new Template.BlockInfo(blockInfo.pos, blockState, null);
        }else {
            return blockInfo;
        }
    }

    protected IStructureProcessorType<?> getType() {
        return AIR_RETAINER;
    }
}