package mod.patrigan.structure_toolkit.world.gen.processors;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.patrigan.structure_toolkit.util.GeneralUtils;
import mod.patrigan.structure_toolkit.util.RandomType;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.LecternBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.LecternBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

import net.minecraft.util.RandomSource;

import static mod.patrigan.structure_toolkit.init.ModProcessors.LECTERNS;
import static mod.patrigan.structure_toolkit.util.RandomType.RANDOM_TYPE_CODEC;
import static net.minecraft.tags.ItemTags.LECTERN_BOOKS;
import static net.minecraft.world.level.block.LecternBlock.HAS_BOOK;
import static net.minecraft.world.level.block.LecternBlock.POWERED;
import static net.minecraftforge.registries.ForgeRegistries.BLOCKS;
import static net.minecraftforge.registries.ForgeRegistries.ITEMS;

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
    public StructureTemplate.StructureBlockInfo process(LevelReader world, BlockPos piecePos, BlockPos structurePos, StructureTemplate.StructureBlockInfo rawBlockInfo, StructureTemplate.StructureBlockInfo blockInfo, StructurePlaceSettings settings, StructureTemplate template) {
        if (blockInfo.state.getBlock() instanceof LecternBlock && blockInfo.state.hasBlockEntity()) {
            BlockEntity blockEntity = ((LecternBlock) blockInfo.state.getBlock()).newBlockEntity(blockInfo.pos, blockInfo.state);
            if(blockEntity instanceof LecternBlockEntity) {
                RandomSource random = ProcessorUtil.getRandom(randomType, blockInfo.pos, piecePos, structurePos, world, SEED);
                ServerLevel serverWorld = ((ServerLevelAccessor) world).getLevel();
                ItemStack itemStack = getItem(random, serverWorld, blockInfo.pos, lootTable);
                if(!itemStack.isEmpty()){
                    LecternBlockEntity lecternBlockEntity = (LecternBlockEntity) blockEntity;
                    serverWorld.setBlockEntity(lecternBlockEntity);
                    CompoundTag newNbt = setLecternBook(blockInfo, lecternBlockEntity, itemStack);
                    BlockState newBlockState = blockInfo.state.setValue(POWERED, Boolean.FALSE).setValue(HAS_BOOK, Boolean.TRUE);
                    return new StructureTemplate.StructureBlockInfo(
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
    private CompoundTag setLecternBook(StructureTemplate.StructureBlockInfo blockInfo, LecternBlockEntity lecternTileEntity, ItemStack itemStack) {
        CompoundTag nbt = blockInfo.nbt;
        lecternTileEntity.load(nbt);
        lecternTileEntity.setBook(itemStack.split(1));
        return lecternTileEntity.saveWithId();
    }


    private ItemStack getItem(RandomSource random, ServerLevel serverWorld, BlockPos blockPos, ResourceLocation lootTable){
        ItemStack itemStack = GeneralUtils.generateItemStack(serverWorld, blockPos, lootTable, random);
        if (!itemStack.isEmpty() && ITEMS.tags().getTag(LECTERN_BOOKS).contains(itemStack.getItem())) {
            return itemStack;
        }else{
            return ItemStack.EMPTY;
        }
    }


    @Override
    protected StructureProcessorType<?> getType() {
        return LECTERNS;
    }
}