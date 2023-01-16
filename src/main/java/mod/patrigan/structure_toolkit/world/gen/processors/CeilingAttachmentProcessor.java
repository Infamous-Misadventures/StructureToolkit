package mod.patrigan.structure_toolkit.world.gen.processors;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.patrigan.structure_toolkit.util.RandomType;
import net.minecraft.world.level.block.state.BlockState;
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

import static mod.patrigan.structure_toolkit.init.ModProcessors.CEILING_ATTACHMENT;
import static mod.patrigan.structure_toolkit.util.RandomType.RANDOM_TYPE_CODEC;
import static mod.patrigan.structure_toolkit.world.gen.processors.ProcessorUtil.getBlock;
import static mod.patrigan.structure_toolkit.world.gen.processors.ProcessorUtil.isSolid;
import static net.minecraftforge.registries.ForgeRegistries.BLOCKS;

public class CeilingAttachmentProcessor extends StructureProcessor {
    public static final Codec<CeilingAttachmentProcessor> CODEC = RecordCodecBuilder.create(builder ->
            builder.group(
                    ResourceLocation.CODEC.fieldOf("block").forGetter(data -> data.block),
                    Codec.BOOL.optionalFieldOf("needs_wall", false).forGetter(processor -> processor.needsWall),
                    Codec.FLOAT.fieldOf("rarity").forGetter(processor -> processor.rarity),
                    RANDOM_TYPE_CODEC.optionalFieldOf("random_type", RandomType.BLOCK).forGetter(processor -> processor.randomType)
            ).apply(builder, CeilingAttachmentProcessor::new));
    private static final long SEED = 7645816L;

    private final ResourceLocation block;
    private final boolean needsWall;
    private final float rarity;
    private final RandomType randomType;

    public CeilingAttachmentProcessor(ResourceLocation block, boolean needsWall, float rarity, RandomType randomType) {
        this.block = block;
        this.needsWall = needsWall;
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
            if(hasCeiling(pieceBlocks, rawBlockInfo.pos)) {
                if(needsWall) {
                    if(hasWall(pieceBlocks, rawBlockInfo.pos)){
                        return new StructureTemplate.StructureBlockInfo(blockpos, BLOCKS.getValue(block).defaultBlockState(), blockInfo.nbt);
                    }
                } else {
                    return new StructureTemplate.StructureBlockInfo(blockpos, BLOCKS.getValue(block).defaultBlockState(), blockInfo.nbt);
                }
            }
        }
        return blockInfo;
    }

    private boolean hasWall(List<StructureTemplate.StructureBlockInfo> pieceBlocks, BlockPos pos) {
        List<StructureTemplate.StructureBlockInfo> neighbours = getSideNeighboursBlockInfo(pieceBlocks, pos, true);
        long count = neighbours.stream().filter(ProcessorUtil::isSolid).count();
        return count > 0;
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

    private boolean hasCeiling(List<StructureTemplate.StructureBlockInfo> pieceBlocks, BlockPos pos) {
        return isSolid(getBlock(pieceBlocks, pos.above()));
    }

    protected StructureProcessorType<?> getType() {
        return CEILING_ATTACHMENT;
    }
}