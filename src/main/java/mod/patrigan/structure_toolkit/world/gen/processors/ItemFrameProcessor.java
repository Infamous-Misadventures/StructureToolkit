package mod.patrigan.structure_toolkit.world.gen.processors;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.patrigan.structure_toolkit.init.ModProcessors;
import mod.patrigan.structure_toolkit.util.GeneralUtils;
import mod.patrigan.structure_toolkit.util.RandomType;
import net.minecraft.entity.EntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.gen.feature.template.IStructureProcessorType;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.StructureProcessor;
import net.minecraft.world.gen.feature.template.Template;
import net.minecraft.world.server.ServerWorld;

import java.util.Random;

import static mod.patrigan.structure_toolkit.util.RandomType.RANDOM_TYPE_CODEC;

public class ItemFrameProcessor extends StructureProcessor {
    public static final Codec<ItemFrameProcessor> CODEC = RecordCodecBuilder.create(builder ->
            builder.group(
                    Codec.BOOL.optionalFieldOf("invisible", false).forGetter(processor -> processor.invisible),
                    Codec.BOOL.optionalFieldOf("random_rotation", false).forGetter(processor -> processor.randomRotation),
                    ResourceLocation.CODEC.optionalFieldOf("loot_table", new ResourceLocation("structure_toolkit:empty")).forGetter(data -> data.lootTable),
                    RANDOM_TYPE_CODEC.optionalFieldOf("random_type", RandomType.BLOCK).forGetter(processor -> processor.randomType)
            ).apply(builder, ItemFrameProcessor::new));

    private static final long SEED = 5132791L;

    private final boolean invisible;
    private final boolean randomRotation;
    private ResourceLocation lootTable;
    private final RandomType randomType;

    public ItemFrameProcessor(boolean invisible, boolean randomRotation, ResourceLocation lootTable, RandomType randomType) {
        this.invisible = invisible;
        this.randomRotation = randomRotation;
        this.lootTable = lootTable;
        this.randomType = randomType;
    }

    @Override
    public Template.EntityInfo processEntity(IWorldReader world, BlockPos structurePos, Template.EntityInfo rawEntityInfo, Template.EntityInfo entityInfo, PlacementSettings placementSettings, Template template) {
        Random random = ProcessorUtil.getRandom(randomType, entityInfo.blockPos, BlockPos.ZERO, structurePos, world, SEED);
        Vector3d pos = entityInfo.pos;
        BlockPos blockPos = entityInfo.blockPos;
        CompoundNBT nbt = entityInfo.nbt;
        if(nbt.getString("id").equals(EntityType.ITEM_FRAME.getRegistryName().toString())){
            ItemStack itemStack = getItemStack(random, world, blockPos);
            nbt.put("Item", itemStack.save(new CompoundNBT()));
            nbt.putBoolean("Invisible", invisible);
            if(randomRotation){
                nbt.putByte("ItemRotation", (byte)random.nextInt(8));
            }
            return new Template.EntityInfo(pos, blockPos, nbt);
        }
        return entityInfo;
    }

    private ItemStack getItemStack(Random random, IWorldReader world, BlockPos blockPos) {
        ServerWorld serverWorld = ((IServerWorld)world).getLevel();
        return GeneralUtils.generateItemStack(serverWorld, blockPos, lootTable, random);
    }

    protected IStructureProcessorType<?> getType() {
        return ModProcessors.ITEM_FRAMES;
    }
}