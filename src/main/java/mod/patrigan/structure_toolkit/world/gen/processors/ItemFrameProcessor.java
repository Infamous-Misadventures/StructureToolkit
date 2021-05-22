package mod.patrigan.structure_toolkit.world.gen.processors;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.patrigan.structure_toolkit.StructureToolkit;
import mod.patrigan.structure_toolkit.init.ModProcessors;
import mod.patrigan.structure_toolkit.util.RandomType;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tags.ITag;
import net.minecraft.tags.TagCollectionManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.gen.feature.template.*;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;
import static mod.patrigan.structure_toolkit.util.Codecs.iTagCodec;
import static mod.patrigan.structure_toolkit.util.RandomType.RANDOM_TYPE_CODEC;
import static mod.patrigan.structure_toolkit.world.gen.processors.ProcessorUtil.getRandomBlockFromTag;
import static mod.patrigan.structure_toolkit.world.gen.processors.ProcessorUtil.getRandomItemFromTag;

public class ItemFrameProcessor extends StructureProcessor {
    public static final Codec<ItemFrameProcessor> CODEC = RecordCodecBuilder.create(builder ->
            builder.group(
                    Codec.BOOL.optionalFieldOf("invisible", false).forGetter(processor -> processor.invisible),
                    iTagCodec(() -> TagCollectionManager.getInstance().getItems()).optionalFieldOf("item_tag", null).forGetter(data -> data.itemTag),
                    iTagCodec(() -> TagCollectionManager.getInstance().getBlocks()).optionalFieldOf("block_tag", null).forGetter(data -> data.blockTag),
                    ResourceLocation.CODEC.listOf().optionalFieldOf("exclusion_list", emptyList()).forGetter(data -> data.exclusionList),
                    RANDOM_TYPE_CODEC.optionalFieldOf("random_type", RandomType.BLOCK).forGetter(processor -> processor.randomType)
            ).apply(builder, ItemFrameProcessor::new));

    private static final long SEED = 5132791L;
    private final boolean invisible;
    private ITag<Item> itemTag;
    private ITag<Block> blockTag;
    private final List<ResourceLocation> exclusionList;
    private final RandomType randomType;

    public ItemFrameProcessor(boolean invisible, ITag<Item> itemTag, ITag<Block> blockTag, List<ResourceLocation> exclusionList, RandomType randomType) {
        this.invisible = invisible;
        this.itemTag = itemTag;
        this.blockTag = blockTag;
        this.exclusionList = exclusionList;
        this.randomType = randomType;
    }

    @Override
    public Template.EntityInfo processEntity(IWorldReader world, BlockPos structurePos, Template.EntityInfo rawEntityInfo, Template.EntityInfo entityInfo, PlacementSettings placementSettings, Template template) {
        Random random = ProcessorUtil.getRandom(randomType, entityInfo.blockPos, BlockPos.ZERO, structurePos, world, SEED);
        Vector3d pos = entityInfo.pos;
        BlockPos blockPos = entityInfo.blockPos;
        CompoundNBT nbt = entityInfo.nbt;
        if(itemTag == null && blockTag == null){
            return entityInfo;
        }
        StructureToolkit.LOGGER.info(nbt.getString("id") + " equals " + EntityType.ITEM_FRAME.getRegistryName().toString());
        if(nbt.getString("id").equals(EntityType.ITEM_FRAME.getRegistryName().toString())){
            StructureToolkit.LOGGER.info("Starting");
            Item randomItemFromTag = itemTag != null ? getRandomItemFromTag(itemTag, random, exclusionList) : getRandomBlockFromTag(blockTag, random, exclusionList).asItem();
            nbt.put("Item", new ItemStack(randomItemFromTag).save(new CompoundNBT()));
            nbt.putBoolean("Invisible", invisible);
            return new Template.EntityInfo(pos, blockPos, nbt);
        }
        StructureToolkit.LOGGER.info(nbt.toString());
        return entityInfo;
    }

    private boolean objectFilter(Item item) {
        return !exclusionList.contains(item.getRegistryName());
    }

    protected IStructureProcessorType<?> getType() {
        return ModProcessors.ITEM_FRAMES;
    }
}