package mod.patrigan.structure_toolkit.world.gen.processors;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.patrigan.structure_toolkit.util.GeneralUtils;
import mod.patrigan.structure_toolkit.util.RandomType;
import net.minecraft.block.AbstractFurnaceBlock;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.AbstractCookingRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.AbstractFurnaceTileEntity;
import net.minecraft.tileentity.FurnaceTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.gen.feature.template.IStructureProcessorType;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.StructureProcessor;
import net.minecraft.world.gen.feature.template.Template;
import net.minecraft.world.server.ServerWorld;

import java.util.Random;

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
    public Template.BlockInfo process(IWorldReader world, BlockPos piecePos, BlockPos structurePos, Template.BlockInfo rawBlockInfo, Template.BlockInfo blockInfo, PlacementSettings settings, Template template) {
        if (blockInfo.state.getBlock() instanceof AbstractFurnaceBlock && blockInfo.state.hasTileEntity()) {
            TileEntity tileEntity = blockInfo.state.createTileEntity(world);
            if(tileEntity instanceof AbstractFurnaceTileEntity) {
                AbstractFurnaceTileEntity furnaceTileEntity = (AbstractFurnaceTileEntity) tileEntity;
                ServerWorld serverWorld = ((IServerWorld) world).getLevel();
                furnaceTileEntity.setLevelAndPosition(serverWorld, blockInfo.pos);
                Random random = ProcessorUtil.getRandom(randomType, blockInfo.pos, piecePos, structurePos, world, SEED);
                return new Template.BlockInfo(
                        blockInfo.pos,
                        blockInfo.state,
                        setAbstractFurnaceEntity(random, serverWorld, blockInfo.pos, blockInfo, furnaceTileEntity));
            }
        }
        return blockInfo;
    }

    /**
     * Makes the given block entity now have the loottable items
     */
    private CompoundNBT setAbstractFurnaceEntity(Random random, ServerWorld serverWorld, BlockPos blockPos, Template.BlockInfo blockInfo, AbstractFurnaceTileEntity furnaceTileEntity) {
        CompoundNBT nbt = blockInfo.nbt;
        furnaceTileEntity.load(blockInfo.state, nbt);
        furnaceTileEntity.setItem(SMELTABLE_ITEM_IDX, getItem(random, serverWorld, blockPos, smeltableLootTable));
        furnaceTileEntity.setItem(FUEL_ITEM_IDX, getItem(random, serverWorld, blockPos, fuelLootTable));
        furnaceTileEntity.setItem(RESULT_ITEM_IDX, getItem(random, serverWorld, blockPos, resultLootTable));
        return furnaceTileEntity.save(nbt);
    }


    private ItemStack getItem(Random random, ServerWorld serverWorld, BlockPos blockPos, ResourceLocation lootTable){
        ItemStack itemStack = GeneralUtils.generateItemStack(serverWorld, blockPos, lootTable, random);
        if (!itemStack.isEmpty()) {
            return itemStack;
        }else{
            return ItemStack.EMPTY;
        }
    }


    @Override
    protected IStructureProcessorType<?> getType() {
        return FURNACES;
    }
}