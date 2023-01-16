package mod.patrigan.structure_toolkit.world.gen.processors;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.patrigan.structure_toolkit.init.ModProcessors;
import mod.patrigan.structure_toolkit.util.GeneralUtils;
import mod.patrigan.structure_toolkit.util.RandomType;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.registries.ForgeRegistries;

import net.minecraft.util.RandomSource;

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
    public StructureTemplate.StructureEntityInfo processEntity(LevelReader world, BlockPos structurePos, StructureTemplate.StructureEntityInfo rawEntityInfo, StructureTemplate.StructureEntityInfo entityInfo, StructurePlaceSettings placementSettings, StructureTemplate template) {
        RandomSource random = ProcessorUtil.getRandom(randomType, entityInfo.blockPos, BlockPos.ZERO, structurePos, world, SEED);
        Vec3 pos = entityInfo.pos;
        BlockPos blockPos = entityInfo.blockPos;
        CompoundTag nbt = entityInfo.nbt;
        if(nbt.getString("id").equals(ForgeRegistries.ENTITY_TYPES.getKey(EntityType.ITEM_FRAME).toString())){
            ItemStack itemStack = getItemStack(random, world, blockPos);
            nbt.put("Item", itemStack.save(new CompoundTag()));
            nbt.putBoolean("Invisible", invisible);
            if(randomRotation){
                nbt.putByte("ItemRotation", (byte)random.nextInt(8));
            }
            return new StructureTemplate.StructureEntityInfo(pos, blockPos, nbt);
        }
        return entityInfo;
    }

    private ItemStack getItemStack(RandomSource random, LevelReader world, BlockPos blockPos) {
        ServerLevel serverWorld = ((ServerLevelAccessor)world).getLevel();
        return GeneralUtils.generateItemStack(serverWorld, blockPos, lootTable, random);
    }

    protected StructureProcessorType<?> getType() {
        return ModProcessors.ITEM_FRAMES;
    }
}