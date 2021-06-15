package mod.patrigan.structure_toolkit.world.gen.processors;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.patrigan.structure_toolkit.util.GeneralUtils;
import mod.patrigan.structure_toolkit.util.RandomType;
import net.minecraft.block.AbstractFurnaceBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.LecternBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.WrittenBookItem;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.AbstractFurnaceTileEntity;
import net.minecraft.tileentity.LecternTileEntity;
import net.minecraft.tileentity.TileEntity;
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
import static mod.patrigan.structure_toolkit.init.ModProcessors.LECTERNS;
import static mod.patrigan.structure_toolkit.util.RandomType.RANDOM_TYPE_CODEC;
import static net.minecraft.block.LecternBlock.HAS_BOOK;
import static net.minecraft.block.LecternBlock.POWERED;
import static net.minecraft.tags.ItemTags.LECTERN_BOOKS;

public class LecternProcessor extends StructureProcessor {

    public static final Codec<LecternProcessor> CODEC = RecordCodecBuilder.create(builder -> builder.group(
            ResourceLocation.CODEC.optionalFieldOf("loot_table", new ResourceLocation("structure_toolkit:empty")).forGetter(data -> data.lootTable),
            RANDOM_TYPE_CODEC.optionalFieldOf("random_type", RandomType.BLOCK).forGetter(processor -> processor.randomType)
    ).apply(builder, builder.stable(LecternProcessor::new)));
    private static final long SEED = 678152L;

    private final ResourceLocation lootTable;
    private final RandomType randomType;

    public LecternProcessor(ResourceLocation lootTable, RandomType randomType) {
        this.lootTable = lootTable;
        this.randomType = randomType;
    }

    @Override
    public Template.BlockInfo process(IWorldReader world, BlockPos piecePos, BlockPos structurePos, Template.BlockInfo rawBlockInfo, Template.BlockInfo blockInfo, PlacementSettings settings, Template template) {
        if (blockInfo.state.getBlock() instanceof LecternBlock && blockInfo.state.hasTileEntity()) {
            TileEntity tileEntity = blockInfo.state.createTileEntity(world);
            if(tileEntity instanceof LecternTileEntity) {
                Random random = ProcessorUtil.getRandom(randomType, blockInfo.pos, piecePos, structurePos, world, SEED);
                ServerWorld serverWorld = ((IServerWorld) world).getLevel();
                ItemStack itemStack = getItem(random, serverWorld, blockInfo.pos, lootTable);
                if(!itemStack.isEmpty()){
                    LecternTileEntity lecternTileEntity = (LecternTileEntity) tileEntity;
                    lecternTileEntity.setLevelAndPosition(serverWorld, blockInfo.pos);
                    CompoundNBT newNbt = setLecternBook(blockInfo, lecternTileEntity, itemStack);
                    BlockState newBlockState = blockInfo.state.setValue(POWERED, Boolean.FALSE).setValue(HAS_BOOK, Boolean.TRUE);
                    return new Template.BlockInfo(
                            blockInfo.pos,
                            newBlockState,
                            newNbt);
                }
            }
        }
        return blockInfo;
    }

    /**
     * Makes the given block entity now have the loottable items
     */
    private CompoundNBT setLecternBook(Template.BlockInfo blockInfo, LecternTileEntity lecternTileEntity, ItemStack itemStack) {
        CompoundNBT nbt = blockInfo.nbt;
        lecternTileEntity.load(blockInfo.state, nbt);
        lecternTileEntity.setBook(itemStack.split(1));
        return lecternTileEntity.save(nbt);
    }


    private ItemStack getItem(Random random, ServerWorld serverWorld, BlockPos blockPos, ResourceLocation lootTable){
        ItemStack itemStack = GeneralUtils.generateItemStack(serverWorld, blockPos, lootTable, random);
        if (!itemStack.isEmpty() && itemStack.getItem().is(LECTERN_BOOKS)) {
            return itemStack;
        }else{
            return ItemStack.EMPTY;
        }
    }


    @Override
    protected IStructureProcessorType<?> getType() {
        return LECTERNS;
    }
}