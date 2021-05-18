package mod.patrigan.structure_toolkit.world.gen.processors;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.patrigan.structure_toolkit.init.ModProcessors;
import mod.patrigan.structure_toolkit.util.RandomType;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.Fluids;
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
import static net.minecraft.block.Blocks.AIR;
import static net.minecraft.block.Blocks.LILY_PAD;
import static net.minecraft.util.Direction.DOWN;

public class LilyPadProcessor extends StructureProcessor {
    public static final Codec<LilyPadProcessor> CODEC = RecordCodecBuilder.create(builder ->
            builder.group(
                    Codec.FLOAT.fieldOf("rarity").forGetter(processor -> processor.rarity),
                    RANDOM_TYPE_CODEC.optionalFieldOf("random_type", RandomType.BLOCK).forGetter(processor -> processor.randomType)
            ).apply(builder, LilyPadProcessor::new));
    private static final long SEED = 837145L;

    private final float rarity;
    private final RandomType randomType;

    public LilyPadProcessor(float rarity, RandomType randomType) {
        this.rarity = rarity;
        this.randomType = randomType;
    }

    @Override
    public Template.BlockInfo process(IWorldReader world, BlockPos piecePos, BlockPos structurePos, Template.BlockInfo rawBlockInfo, Template.BlockInfo blockInfo, PlacementSettings settings, Template template) {
        Random random = ProcessorUtil.getRandom(randomType, blockInfo.pos, piecePos, structurePos, SEED);
        BlockState blockstate = blockInfo.state;
        BlockPos blockpos = blockInfo.pos;
        if(blockstate.getBlock().equals(AIR) && random.nextFloat() <= rarity){
            List<Template.BlockInfo> pieceBlocks = settings.getRandomPalette(template.palettes, piecePos).blocks();
            Template.BlockInfo below = getBlock(pieceBlocks, rawBlockInfo.pos.relative(DOWN));
            if(below != null && below.state.getFluidState().getType() == Fluids.WATER) {
                return new Template.BlockInfo(blockpos, LILY_PAD.defaultBlockState(), blockInfo.nbt);
            }
        }
        return blockInfo;
    }

    protected IStructureProcessorType<?> getType() {
        return ModProcessors.LILY_PADS;
    }
}