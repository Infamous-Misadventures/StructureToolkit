package mod.patrigan.structure_toolkit.world.gen.processors;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.patrigan.structure_toolkit.util.RandomType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.minecraft.util.RandomSource;

import static mod.patrigan.structure_toolkit.init.ModProcessors.ATTACHMENT;
import static mod.patrigan.structure_toolkit.init.ModProcessors.CEILING_ATTACHMENT;
import static mod.patrigan.structure_toolkit.util.RandomType.RANDOM_TYPE_CODEC;
import static mod.patrigan.structure_toolkit.world.gen.processors.ProcessorUtil.*;
import static net.minecraftforge.registries.ForgeRegistries.BLOCKS;

public class AttachmentProcessor extends StructureProcessor {
    public static final Codec<AttachmentProcessor> CODEC = RecordCodecBuilder.create(builder ->
            builder.group(
                    ResourceLocation.CODEC.fieldOf("block").forGetter(data -> data.block),
                    Codec.INT.optionalFieldOf("requires_sides", 0).forGetter(processor -> processor.requiresSides),
                    Codec.BOOL.optionalFieldOf("requires_up", false).forGetter(processor -> processor.requiresUp),
                    Codec.BOOL.optionalFieldOf("requires_down", false).forGetter(processor -> processor.requiresDown),
                    Codec.FLOAT.fieldOf("rarity").forGetter(processor -> processor.rarity),
                    RANDOM_TYPE_CODEC.optionalFieldOf("random_type", RandomType.BLOCK).forGetter(processor -> processor.randomType)
            ).apply(builder, AttachmentProcessor::new));
    private static final long SEED = 7645816L;

    private final ResourceLocation block;
    private final int requiresSides;
    private final boolean requiresUp;
    private final boolean requiresDown;
    private final float rarity;
    private final RandomType randomType;

    public AttachmentProcessor(ResourceLocation block, int requiresSides, boolean requiresUp, boolean requiresDown, float rarity, RandomType randomType) {
        this.block = block;
        this.requiresSides = requiresSides;
        this.requiresUp = requiresUp;
        this.requiresDown = requiresDown;
        this.rarity = rarity;
        this.randomType = randomType;
    }

    @Override
    public StructureTemplate.StructureBlockInfo process(LevelReader world, BlockPos piecePos, BlockPos structurePos, StructureTemplate.StructureBlockInfo rawBlockInfo, StructureTemplate.StructureBlockInfo blockInfo, StructurePlaceSettings settings, StructureTemplate template) {
        RandomSource random = ProcessorUtil.getRandom(randomType, blockInfo.pos, piecePos, structurePos, world, SEED);
        BlockState blockstate = blockInfo.state;
        BlockPos blockpos = blockInfo.pos;
        if(blockstate.isAir() && random.nextFloat() <= rarity){
            List<StructureTemplate.StructureBlockInfo> pieceBlocks = settings.getRandomPalette(template.palettes, piecePos).blocks();
            boolean hasSides = requiresSides == 0 || hasSides(pieceBlocks, rawBlockInfo.pos) >= requiresSides;
            boolean hasUp = !requiresUp || hasDirection(pieceBlocks, rawBlockInfo.pos, Direction.UP);
            boolean hasDown = !requiresDown || hasDirection(pieceBlocks, rawBlockInfo.pos, Direction.DOWN);
            if(hasSides && hasUp && hasDown){
                return new StructureTemplate.StructureBlockInfo(blockpos, BLOCKS.getValue(block).defaultBlockState(), blockInfo.nbt);
            }
        }
        return blockInfo;
    }

    private int hasSides(List<StructureTemplate.StructureBlockInfo> pieceBlocks, BlockPos pos) {
        List<StructureTemplate.StructureBlockInfo> neighbours = getSideNeighboursBlockInfo(pieceBlocks, pos, false);
        return (int) neighbours.stream().filter(ProcessorUtil::isSolid).count();
    }

    private List<StructureTemplate.StructureBlockInfo> getSideNeighboursBlockInfo(List<StructureTemplate.StructureBlockInfo> pieceBlocks, BlockPos pos, boolean diagonal) {
        List<StructureTemplate.StructureBlockInfo> neighbours = new ArrayList<>(Arrays.asList(getBlock(pieceBlocks, pos.north()), getBlock(pieceBlocks, pos.south()), getBlock(pieceBlocks, pos.west()), getBlock(pieceBlocks, pos.east())));
        if(diagonal){
            neighbours.addAll(Arrays.asList(
                    getBlock(pieceBlocks, pos.north().east()),
                    getBlock(pieceBlocks, pos.east().south()),
                    getBlock(pieceBlocks, pos.south().west()),
                    getBlock(pieceBlocks, pos.west().north())
            ));
        }
        return neighbours;
    }

    private boolean hasDirection(List<StructureTemplate.StructureBlockInfo> pieceBlocks, BlockPos pos, Direction direction) {
        StructureTemplate.StructureBlockInfo block = getBlock(pieceBlocks, pos.mutable().move(direction));
        return isSolid(block) && isFaceFull(block, direction.getOpposite());
    }

    protected StructureProcessorType<?> getType() {
        return ATTACHMENT;
    }
}