package mod.patrigan.structure_toolkit.world.gen.processors;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.patrigan.structure_toolkit.util.RandomType;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraftforge.common.Tags;

import net.minecraft.util.RandomSource;

import static mod.patrigan.structure_toolkit.init.ModProcessors.CHESTS;
import static mod.patrigan.structure_toolkit.util.RandomType.RANDOM_TYPE_CODEC;
import static net.minecraft.world.level.block.Blocks.AIR;
import static net.minecraft.world.level.block.Blocks.BARREL;
import static net.minecraftforge.registries.ForgeRegistries.BLOCKS;

public class ChestProcessor extends StructureProcessor {

    public static final Codec<ChestProcessor> CODEC = RecordCodecBuilder.create(builder -> builder.group(
            ResourceLocation.CODEC.optionalFieldOf("loot_table", new ResourceLocation("structure_toolkit:empty")).forGetter(data -> data.lootTable),
            ResourceLocation.CODEC.optionalFieldOf("replacer", new ResourceLocation("minecraft:air")).forGetter(data -> data.replacer),
            Codec.FLOAT.optionalFieldOf("rarity", 1.0F).forGetter(data -> data.rarity),
            RANDOM_TYPE_CODEC.optionalFieldOf("random_type", RandomType.BLOCK).forGetter(processor -> processor.randomType)
    ).apply(builder, builder.stable(ChestProcessor::new)));
    private static final long SEED = 354134L;

    private final ResourceLocation lootTable;
    private final ResourceLocation replacer;
    private final float rarity;
    private final RandomType randomType;

    public ChestProcessor(ResourceLocation lootTable, ResourceLocation replacer, float rarity, RandomType randomType) {
        this.lootTable = lootTable;
        this.replacer = replacer;
        this.rarity = rarity;
        this.randomType = randomType;
    }

    @Override
    public StructureTemplate.StructureBlockInfo process(LevelReader world, BlockPos piecePos, BlockPos structurePos, StructureTemplate.StructureBlockInfo rawBlockInfo, StructureTemplate.StructureBlockInfo blockInfo, StructurePlaceSettings settings, StructureTemplate template) {
        if (((blockInfo.state.is(Tags.Blocks.CHESTS) && !blockInfo.state.is(Tags.Blocks.CHESTS_ENDER)) || blockInfo.state.getBlock().equals(BARREL)) && blockInfo.state.hasBlockEntity()) {
            BlockEntity tileEntity = ((EntityBlock) blockInfo.state.getBlock()).newBlockEntity(blockInfo.pos, blockInfo.state);
            RandomSource random = ProcessorUtil.getRandom(randomType, blockInfo.pos, piecePos, structurePos, world, SEED);
            if(tileEntity instanceof RandomizableContainerBlockEntity && random.nextFloat() < rarity) {
                ServerLevel serverWorld = ((ServerLevelAccessor) world).getLevel();
                RandomizableContainerBlockEntity randomizableContainerBlockEntity = (RandomizableContainerBlockEntity) tileEntity;
                serverWorld.setBlockEntity(randomizableContainerBlockEntity);
                CompoundTag nbt = blockInfo.nbt;
                nbt.putString("LootTable", this.lootTable.toString());
                nbt.putLong("LootTableSeed", serverWorld.random.nextLong());
                return new StructureTemplate.StructureBlockInfo(
                        blockInfo.pos,
                        blockInfo.state,
                        nbt);
            }else{
                Block newBlock = BLOCKS.getValue(replacer);
                if(newBlock == null){
                    newBlock = AIR;
                }
                return new StructureTemplate.StructureBlockInfo(
                        blockInfo.pos,
                        newBlock.defaultBlockState(),
                        null
                );
            }
        }
        return blockInfo;
    }

    @Override
    protected StructureProcessorType<?> getType() {
        return CHESTS;
    }
}