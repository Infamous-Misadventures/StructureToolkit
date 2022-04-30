package mod.patrigan.structure_toolkit.world.gen.processors;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.patrigan.structure_toolkit.util.RandomType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static mod.patrigan.structure_toolkit.init.ModProcessors.VINES;
import static mod.patrigan.structure_toolkit.util.RandomType.RANDOM_TYPE_CODEC;
import static mod.patrigan.structure_toolkit.world.gen.processors.ProcessorUtil.*;
import static net.minecraft.core.Direction.*;
import static net.minecraft.world.level.block.Blocks.VINE;
import static net.minecraft.world.level.block.VineBlock.PROPERTY_BY_DIRECTION;

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
    public StructureTemplate.StructureBlockInfo process(LevelReader world, BlockPos piecePos, BlockPos structurePos, StructureTemplate.StructureBlockInfo rawBlockInfo, StructureTemplate.StructureBlockInfo blockInfo, StructurePlaceSettings settings, StructureTemplate template) {
        Random random = ProcessorUtil.getRandom(randomType, blockInfo.pos, piecePos, structurePos, world, SEED);
        BlockState blockstate = blockInfo.state;
        BlockPos blockpos = blockInfo.pos;
        List<Direction> possibleDirections = new ArrayList<>();
        if(blockstate.isAir() && random.nextFloat() <= rarity){
            List<StructureTemplate.StructureBlockInfo> pieceBlocks = settings.getRandomPalette(template.palettes, piecePos).blocks();
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
            return new StructureTemplate.StructureBlockInfo(blockpos, VINE.defaultBlockState().setValue(property, true), blockInfo.nbt);
        }
    }

    private boolean isDirectionPossible(List<StructureTemplate.StructureBlockInfo> pieceBlocks, BlockPos pos, Direction direction){
        StructureTemplate.StructureBlockInfo tempBlock = getBlock(pieceBlocks, pos.relative(direction));
        return isFaceFull(tempBlock, direction.getOpposite());
    }

    protected StructureProcessorType<?> getType() {
        return VINES;
    }
}
