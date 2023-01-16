package mod.patrigan.structure_toolkit.world.gen.processors;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.patrigan.structure_toolkit.util.GeneralUtils;
import mod.patrigan.structure_toolkit.util.RandomType;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

import net.minecraft.util.RandomSource;

import static mod.patrigan.structure_toolkit.init.ModProcessors.FURNACES;
import static mod.patrigan.structure_toolkit.util.RandomType.RANDOM_TYPE_CODEC;

public class FurnaceProcessor extends StructureProcessor {

    public static final Codec<FurnaceProcessor> CODEC = RecordCodecBuilder.create(builder -> builder.group(
            ResourceLocation.CODEC.optionalFieldOf("smeltable_loot_table", new ResourceLocation("structure_toolkit:empty")).forGetter(data -> data.smeltableLootTable),
            ResourceLocation.CODEC.optionalFieldOf("fuel_loot_table", new ResourceLocation("structure_toolkit:empty")).forGetter(data -> data.fuelLootTable),
            ResourceLocation.CODEC.optionalFieldOf("result_loot_table", new ResourceLocation("structure_toolkit:empty")).forGetter(data -> data.resultLootTable),
            RANDOM_TYPE_CODEC.optionalFieldOf("random_type", RandomType.BLOCK).forGetter(processor -> processor.randomType)
    ).apply(builder, builder.stable(FurnaceProcessor::new)));
    private static final long SEED = 31782163L;
    private static final int SMELTABLE_ITEM_IDX = 0;
    private static final int FUEL_ITEM_IDX = 1;
    private static final int RESULT_ITEM_IDX = 2;

    private final ResourceLocation smeltableLootTable;
    private final ResourceLocation fuelLootTable;
    private final ResourceLocation resultLootTable;
    private final RandomType randomType;

    public FurnaceProcessor(ResourceLocation smeltableLootTable, ResourceLocation fuelLootTable, ResourceLocation resultLootTable, RandomType randomType) {
        this.smeltableLootTable = smeltableLootTable;
        this.fuelLootTable = fuelLootTable;
        this.resultLootTable = resultLootTable;
        this.randomType = randomType;
    }

    @Override
    public StructureTemplate.StructureBlockInfo process(LevelReader world, BlockPos piecePos, BlockPos structurePos, StructureTemplate.StructureBlockInfo rawBlockInfo, StructureTemplate.StructureBlockInfo blockInfo, StructurePlaceSettings settings, StructureTemplate template) {
        if (blockInfo.state.getBlock() instanceof AbstractFurnaceBlock && blockInfo.state.hasBlockEntity()) {
            BlockEntity blockEntity = ((AbstractFurnaceBlock) blockInfo.state.getBlock()).newBlockEntity(blockInfo.pos, blockInfo.state);
            if(blockEntity instanceof AbstractFurnaceBlockEntity) {
                AbstractFurnaceBlockEntity furnaceBlockEntity = (AbstractFurnaceBlockEntity) blockEntity;
                ServerLevel serverWorld = ((ServerLevelAccessor) world).getLevel();
                serverWorld.setBlockEntity(furnaceBlockEntity);
                RandomSource random = ProcessorUtil.getRandom(randomType, blockInfo.pos, piecePos, structurePos, world, SEED);
                return new StructureTemplate.StructureBlockInfo(
                        blockInfo.pos,
                        blockInfo.state,
                        setAbstractFurnaceEntity(random, serverWorld, blockInfo.pos, blockInfo, furnaceBlockEntity));
            }
        }
        return blockInfo;
    }

    /**
     * Makes the given block entity now have the loottable items
     */
    private CompoundTag setAbstractFurnaceEntity(RandomSource random, ServerLevel serverWorld, BlockPos blockPos, StructureTemplate.StructureBlockInfo blockInfo, AbstractFurnaceBlockEntity furnaceTileEntity) {
        CompoundTag nbt = blockInfo.nbt;
        furnaceTileEntity.load(nbt);
        furnaceTileEntity.setItem(SMELTABLE_ITEM_IDX, getItem(random, serverWorld, blockPos, smeltableLootTable));
        furnaceTileEntity.setItem(FUEL_ITEM_IDX, getItem(random, serverWorld, blockPos, fuelLootTable));
        furnaceTileEntity.setItem(RESULT_ITEM_IDX, getItem(random, serverWorld, blockPos, resultLootTable));
        return furnaceTileEntity.saveWithId();
    }


    private ItemStack getItem(RandomSource random, ServerLevel serverWorld, BlockPos blockPos, ResourceLocation lootTable){
        ItemStack itemStack = GeneralUtils.generateItemStack(serverWorld, blockPos, lootTable, random);
        if (!itemStack.isEmpty()) {
            return itemStack;
        }else{
            return ItemStack.EMPTY;
        }
    }


    @Override
    protected StructureProcessorType<?> getType() {
        return FURNACES;
    }
}