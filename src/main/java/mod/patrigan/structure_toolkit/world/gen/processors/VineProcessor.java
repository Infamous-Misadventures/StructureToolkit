package mod.patrigan.structure_toolkit.world.gen.processors;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.patrigan.structure_toolkit.util.RandomType;
import net.minecraft.block.BlockState;
import net.minecraft.state.BooleanProperty;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.gen.feature.template.IStructureProcessorType;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.StructureProcessor;
import net.minecraft.world.gen.feature.template.Template;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static mod.patrigan.structure_toolkit.init.ModProcessors.VINES;
import static mod.patrigan.structure_toolkit.util.RandomType.RANDOM_TYPE_CODEC;
import static mod.patrigan.structure_toolkit.world.gen.processors.ProcessorUtil.*;
import static net.minecraft.block.Blocks.AIR;
import static net.minecraft.block.Blocks.VINE;
import static net.minecraft.block.VineBlock.PROPERTY_BY_DIRECTION;
import static net.minecraft.util.Direction.*;

public class VineProcessor extends StructureProcessor {
    public static final Codec<VineProcessor> CODEC = RecordCodecBuilder.create(builder ->
            builder.group(
                    Codec.BOOL.optionalFieldOf("attach_to_wall", true).forGetter(processor -> processor.attachToWall),
                    Codec.BOOL.optionalFieldOf("attach_to_ceiling", true).forGetter(processor -> processor.attachToCeiling),
                    Codec.FLOAT.fieldOf("rarity").forGetter(processor -> processor.rarity),
                    RANDOM_TYPE_CODEC.optionalFieldOf("random_type", RandomType.BLOCK).forGetter(processor -> processor.randomType)
            ).apply(builder, VineProcessor::new));
    private static final long SEED = 8514174L;

    private final boolean attachToWall;
    private final boolean attachToCeiling;
    private final float rarity;
    private final RandomType randomType;

    public VineProcessor(boolean attachToWall, boolean attachToCeiling, float rarity, RandomType randomType) {
        this.attachToWall = attachToWall;
        this.attachToCeiling = attachToCeiling;
        this.rarity = rarity;
        this.randomType = randomType;
    }

    @Override
    public Template.BlockInfo process(IWorldReader world, BlockPos piecePos, BlockPos structurePos, Template.BlockInfo rawBlockInfo, Template.BlockInfo blockInfo, PlacementSettings settings, Template template) {
        Random random = ProcessorUtil.getRandom(randomType, blockInfo.pos, piecePos, structurePos, world, SEED);
        BlockState blockstate = blockInfo.state;
        BlockPos blockpos = blockInfo.pos;
        List<Direction> possibleDirections = new ArrayList<>();
        if(blockstate.getBlock().equals(AIR) && random.nextFloat() <= rarity){
            List<Template.BlockInfo> pieceBlocks = settings.getRandomPalette(template.palettes, piecePos).blocks();
            if(attachToWall){
                possibleDirections.addAll(Arrays.asList(NORTH, EAST, SOUTH, WEST));
            }
            if(attachToCeiling){
                possibleDirections.add(UP);
            }
            possibleDirections = possibleDirections.stream().filter(direction -> isDirectionPossible(pieceBlocks, rawBlockInfo.pos, direction)).collect(Collectors.toList());
        }
        if(possibleDirections.isEmpty()){
            return blockInfo;
        }else{
            Direction direction = possibleDirections.get(random.nextInt(possibleDirections.size()));
            BooleanProperty property = PROPERTY_BY_DIRECTION.get(direction);
            return new Template.BlockInfo(blockpos, VINE.defaultBlockState().setValue(property, true), blockInfo.nbt);
        }
    }

    private boolean isDirectionPossible(List<Template.BlockInfo> pieceBlocks, BlockPos pos, Direction direction){
        Template.BlockInfo tempBlock = getBlock(pieceBlocks, pos.relative(direction));
        return isFaceFull(tempBlock, direction.getOpposite());
    }

    protected IStructureProcessorType<?> getType() {
        return VINES;
    }
}
