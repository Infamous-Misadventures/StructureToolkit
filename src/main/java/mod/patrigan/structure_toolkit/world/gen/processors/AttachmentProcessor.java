package mod.patrigan.structure_toolkit.world.gen.processors;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.patrigan.structure_toolkit.util.RandomType;
import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
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

import static mod.patrigan.structure_toolkit.init.ModProcessors.ATTACHMENT;
import static mod.patrigan.structure_toolkit.init.ModProcessors.CEILING_ATTACHMENT;
import static mod.patrigan.structure_toolkit.util.RandomType.RANDOM_TYPE_CODEC;
import static mod.patrigan.structure_toolkit.world.gen.processors.ProcessorUtil.*;
import static net.minecraft.block.Blocks.AIR;
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
    public Template.BlockInfo process(IWorldReader world, BlockPos piecePos, BlockPos structurePos, Template.BlockInfo rawBlockInfo, Template.BlockInfo blockInfo, PlacementSettings settings, Template template) {
        Random random = ProcessorUtil.getRandom(randomType, blockInfo.pos, piecePos, structurePos, world, SEED);
        BlockState blockstate = blockInfo.state;
        BlockPos blockpos = blockInfo.pos;
        if(blockstate.isAir() && random.nextFloat() <= rarity){
            List<Template.BlockInfo> pieceBlocks = settings.getRandomPalette(template.palettes, piecePos).blocks();
            boolean hasSides = requiresSides == 0 || hasSides(pieceBlocks, rawBlockInfo.pos) >= requiresSides;
            boolean hasUp = !requiresUp || hasDirection(pieceBlocks, rawBlockInfo.pos, Direction.UP);
            boolean hasDown = !requiresDown || hasDirection(pieceBlocks, rawBlockInfo.pos, Direction.DOWN);
            if(hasSides && hasUp && hasDown){
                return new Template.BlockInfo(blockpos, BLOCKS.getValue(block).defaultBlockState(), blockInfo.nbt);
            }
        }
        return blockInfo;
    }

    private int hasSides(List<Template.BlockInfo> pieceBlocks, BlockPos pos) {
        List<Template.BlockInfo> neighbours = getSideNeighboursBlockInfo(pieceBlocks, pos, false);
        return (int) neighbours.stream().filter(ProcessorUtil::isSolid).count();
    }

    private List<Template.BlockInfo> getSideNeighboursBlockInfo(List<Template.BlockInfo> pieceBlocks, BlockPos pos, boolean diagonal) {
        List<Template.BlockInfo> neighbours = new ArrayList<>(Arrays.asList(getBlock(pieceBlocks, pos.north()), getBlock(pieceBlocks, pos.south()), getBlock(pieceBlocks, pos.west()), getBlock(pieceBlocks, pos.east())));
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

    private boolean hasDirection(List<Template.BlockInfo> pieceBlocks, BlockPos pos, Direction direction) {
        Template.BlockInfo block = getBlock(pieceBlocks, pos.mutable().move(direction));
        return isSolid(block) && isFaceFull(block, direction.getOpposite());
    }

    protected IStructureProcessorType<?> getType() {
        return ATTACHMENT;
    }
}