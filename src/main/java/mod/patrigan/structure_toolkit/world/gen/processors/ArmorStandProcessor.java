package mod.patrigan.structure_toolkit.world.gen.processors;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.patrigan.structure_toolkit.init.ModProcessors;
import mod.patrigan.structure_toolkit.util.GeneralUtils;
import mod.patrigan.structure_toolkit.util.RandomType;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Rotations;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.server.level.ServerLevel;

import java.util.Random;

import static mod.patrigan.structure_toolkit.util.RandomType.RANDOM_TYPE_CODEC;

public class ArmorStandProcessor extends StructureProcessor {
    public static final Codec<ArmorStandProcessor> CODEC = RecordCodecBuilder.create(builder ->
            builder.group(
                    Codec.BOOL.optionalFieldOf("invisible", false).forGetter(processor -> processor.invisible),
                    Codec.BOOL.optionalFieldOf("no_base_plate", false).forGetter(processor -> processor.noBasePlate),
                    Codec.BOOL.optionalFieldOf("show_arms", false).forGetter(processor -> processor.showArms),
                    Codec.BOOL.optionalFieldOf("small", false).forGetter(processor -> processor.small),
                    ResourceLocation.CODEC.optionalFieldOf("head_loot_table", new ResourceLocation("structure_toolkit:empty")).forGetter(data -> data.headLootTable),
                    ResourceLocation.CODEC.optionalFieldOf("chest_loot_table", new ResourceLocation("structure_toolkit:empty")).forGetter(data -> data.chestLootTable),
                    ResourceLocation.CODEC.optionalFieldOf("legs_loot_table", new ResourceLocation("structure_toolkit:empty")).forGetter(data -> data.legsLootTable),
                    ResourceLocation.CODEC.optionalFieldOf("feet_loot_table", new ResourceLocation("structure_toolkit:empty")).forGetter(data -> data.feetLootTable),
                    RANDOM_TYPE_CODEC.optionalFieldOf("random_type", RandomType.BLOCK).forGetter(processor -> processor.randomType)
            ).apply(builder, ArmorStandProcessor::new));

    private static final long SEED = 5132791L;

    private final boolean invisible;
    private final boolean noBasePlate;
    private final boolean showArms;
    private final boolean small;
    private final ResourceLocation headLootTable;
    private final ResourceLocation chestLootTable;
    private final ResourceLocation legsLootTable;
    private final ResourceLocation feetLootTable;
    private final RandomType randomType;

    public ArmorStandProcessor(boolean invisible, boolean noBasePlate, boolean showArms, boolean small, ResourceLocation headLootTable, ResourceLocation chestLootTable, ResourceLocation legsLootTable, ResourceLocation feetLootTable, RandomType randomType) {
        this.invisible = invisible;
        this.noBasePlate = noBasePlate;
        this.showArms = showArms;
        this.small = small;
        this.headLootTable = headLootTable;
        this.chestLootTable = chestLootTable;
        this.legsLootTable = legsLootTable;
        this.feetLootTable = feetLootTable;
        this.randomType = randomType;
    }

    @Override
    public StructureTemplate.StructureEntityInfo processEntity(LevelReader world, BlockPos structurePos, StructureTemplate.StructureEntityInfo rawEntityInfo, StructureTemplate.StructureEntityInfo entityInfo, StructurePlaceSettings placementSettings, StructureTemplate template) {
        Random random = ProcessorUtil.getRandom(randomType, entityInfo.blockPos, BlockPos.ZERO, structurePos, world, SEED);
        Vec3 pos = entityInfo.pos;
        BlockPos blockPos = entityInfo.blockPos;
        CompoundTag nbt = entityInfo.nbt;
        if(nbt.getString("id").equals(EntityType.ARMOR_STAND.getRegistryName().toString())){
            nbt.put("ArmorItems", writeArmorItems(random, world, blockPos));
            nbt.put("Pose", this.writePose(random));
            nbt.putBoolean("Invisible", invisible);
            nbt.putBoolean("Small", small);
            nbt.putBoolean("ShowArms", showArms);
            nbt.putBoolean("NoBasePlate", noBasePlate);
            return new StructureTemplate.StructureEntityInfo(pos, blockPos, nbt);
        }
        return entityInfo;
    }

    private CompoundTag writePose(Random random) {
        CompoundTag compoundnbt = new CompoundTag();
        compoundnbt.put("Head", generateRandomRotations(-45, 45, -65, 65, -45, 45, random).save());
        compoundnbt.put("Body", generateRandomRotations(0, 1, 0, 1, 0, 1, random).save());
        compoundnbt.put("LeftArm", generateRandomRotations(-180, 40, 0, 0, -180, 180, random).save());
        compoundnbt.put("RightArm", generateRandomRotations(-180, 180, -20, 20, -170, 85, random).save());
        compoundnbt.put("LeftLeg", generateRandomRotations(-50, 50, -10, 10, -30, 30, random).save());
        compoundnbt.put("RightLeg", generateRandomRotations(-50, 50, -10, 10, -30, 30, random).save());
        return compoundnbt;
    }

    private ListTag writeArmorItems(Random random, LevelReader world, BlockPos blockPos){
        NonNullList<ItemStack> armorItems = getArmorItems(random, (ServerLevelAccessor) world, blockPos);
        ListTag listnbt = new ListTag();
        for(ItemStack itemstack : armorItems) {
            CompoundTag compoundnbt = new CompoundTag();
            if (!itemstack.isEmpty()) {
                itemstack.save(compoundnbt);
            }
            listnbt.add(compoundnbt);
        }
        return listnbt;
    }

    private NonNullList<ItemStack> getArmorItems(Random random, ServerLevelAccessor world, BlockPos blockPos) {
        ServerLevel serverWorld = world.getLevel();
        NonNullList<ItemStack> armorItems = NonNullList.withSize(4, ItemStack.EMPTY);
        armorItems.set(EquipmentSlot.HEAD.getIndex(), getArmorItem(random, serverWorld, blockPos, headLootTable, EquipmentSlot.HEAD));
        armorItems.set(EquipmentSlot.CHEST.getIndex(), getArmorItem(random, serverWorld, blockPos, chestLootTable, EquipmentSlot.CHEST));
        armorItems.set(EquipmentSlot.LEGS.getIndex(), getArmorItem(random, serverWorld, blockPos, legsLootTable, EquipmentSlot.LEGS));
        armorItems.set(EquipmentSlot.FEET.getIndex(), getArmorItem(random, serverWorld, blockPos, feetLootTable, EquipmentSlot.FEET));
        return armorItems;
    }

    private ItemStack getArmorItem(Random random, ServerLevel serverWorld, BlockPos blockPos, ResourceLocation lootTable, EquipmentSlot equipmentSlotType){
        ItemStack itemStack = GeneralUtils.generateItemStack(serverWorld, blockPos, lootTable, random);
        if (!itemStack.isEmpty() && equipmentSlotType.equals(Mob.getEquipmentSlotForItem(itemStack))) {
            return itemStack;
        }else{
            return ItemStack.EMPTY;
        }
    }

    private Rotations generateRandomRotations(int minX, int maxX, int minY, int maxY, int minZ, int maxZ, Random random) {
        return new Rotations(getRandomValue(minX, maxX, random), getRandomValue(minY, maxY, random), getRandomValue(minZ, maxZ, random));
    }
    private int getRandomValue(int min, int max, Random random){
        int bound = Math.abs(min) + Math.abs(max);
        if(bound == 0){
            return 0;
        }
        int step1 = random.nextInt(bound);
        int step2 = step1+min;
        if(step2 < 0){
            return step2 + 360;
        }else{
            return step2;
        }
    }

    protected StructureProcessorType<?> getType() {
        return ModProcessors.ARMOR_STANDS;
    }
}