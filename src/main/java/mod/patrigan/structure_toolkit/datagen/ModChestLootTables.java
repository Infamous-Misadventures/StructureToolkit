package mod.patrigan.structure_toolkit.datagen;

import mod.patrigan.structure_toolkit.StructureToolkit;
import mod.patrigan.structure_toolkit.init.ModTags;
import net.minecraft.data.loot.ChestLoot;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.functions.EnchantWithLevelsFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.tags.ItemTags;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraftforge.common.Tags;

import java.util.function.BiConsumer;

import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootTableReference;
import net.minecraft.world.level.storage.loot.entries.TagEntry;

public class ModChestLootTables extends ChestLoot {

    @Override
    public void accept(BiConsumer<ResourceLocation, LootTable.Builder> consumer) {
        consumer.accept(new ResourceLocation(StructureToolkit.MOD_ID, "processors/saplings"),
                LootTable.lootTable().
                        withPool(LootPool.lootPool().setRolls(UniformGenerator.between(1.0F, 1.0F))
                                .add(TagEntry.expandTag(ItemTags.SAPLINGS).setWeight(6).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1))))));
        consumer.accept(new ResourceLocation(StructureToolkit.MOD_ID, "empty"),
                LootTable.lootTable());
        equipmentLootTable(consumer, "iron");
        equipmentLootTable(consumer, "golden");
        equipmentLootTable(consumer, "diamond");
        consumer.accept(new ResourceLocation(StructureToolkit.MOD_ID, "processors/helmets"),
                LootTable.lootTable().
                        withPool(LootPool.lootPool().setRolls(UniformGenerator.between(0.0F, 1.0F))
                                .add(LootItem.lootTableItem(Items.CHAINMAIL_HELMET).setWeight(40))
                                .add(LootItem.lootTableItem(Items.IRON_HELMET).setWeight(80))
                                .add(LootItem.lootTableItem(Items.GOLDEN_HELMET).setWeight(40))
                                .add(LootItem.lootTableItem(Items.DIAMOND_HELMET).setWeight(20))
                                .add(LootItem.lootTableItem(Items.CHAINMAIL_HELMET).setWeight(10).apply(EnchantWithLevelsFunction.enchantWithLevels(ConstantValue.exactly(30)).allowTreasure()))
                                .add(LootItem.lootTableItem(Items.IRON_HELMET).setWeight(20).apply(EnchantWithLevelsFunction.enchantWithLevels(ConstantValue.exactly(30)).allowTreasure()))
                                .add(LootItem.lootTableItem(Items.GOLDEN_HELMET).setWeight(10).apply(EnchantWithLevelsFunction.enchantWithLevels(ConstantValue.exactly(30)).allowTreasure()))
                                .add(LootItem.lootTableItem(Items.DIAMOND_HELMET).setWeight(5).apply(EnchantWithLevelsFunction.enchantWithLevels(ConstantValue.exactly(30)).allowTreasure()))));
        consumer.accept(new ResourceLocation(StructureToolkit.MOD_ID, "processors/chestplates"),
                LootTable.lootTable().
                        withPool(LootPool.lootPool().setRolls(UniformGenerator.between(0.0F, 1.0F))
                                .add(LootItem.lootTableItem(Items.CHAINMAIL_CHESTPLATE).setWeight(40))
                                .add(LootItem.lootTableItem(Items.IRON_CHESTPLATE).setWeight(80))
                                .add(LootItem.lootTableItem(Items.GOLDEN_CHESTPLATE).setWeight(40))
                                .add(LootItem.lootTableItem(Items.DIAMOND_CHESTPLATE).setWeight(20))
                                .add(LootItem.lootTableItem(Items.CHAINMAIL_CHESTPLATE).setWeight(10).apply(EnchantWithLevelsFunction.enchantWithLevels(ConstantValue.exactly(30)).allowTreasure()))
                                .add(LootItem.lootTableItem(Items.IRON_CHESTPLATE).setWeight(20).apply(EnchantWithLevelsFunction.enchantWithLevels(ConstantValue.exactly(30)).allowTreasure()))
                                .add(LootItem.lootTableItem(Items.GOLDEN_CHESTPLATE).setWeight(10).apply(EnchantWithLevelsFunction.enchantWithLevels(ConstantValue.exactly(30)).allowTreasure()))
                                .add(LootItem.lootTableItem(Items.DIAMOND_CHESTPLATE).setWeight(5).apply(EnchantWithLevelsFunction.enchantWithLevels(ConstantValue.exactly(30)).allowTreasure()))));
        consumer.accept(new ResourceLocation(StructureToolkit.MOD_ID, "processors/leggings"),
                LootTable.lootTable().
                        withPool(LootPool.lootPool().setRolls(UniformGenerator.between(0.0F, 1.0F))
                                .add(LootItem.lootTableItem(Items.CHAINMAIL_LEGGINGS).setWeight(40))
                                .add(LootItem.lootTableItem(Items.IRON_LEGGINGS).setWeight(80))
                                .add(LootItem.lootTableItem(Items.GOLDEN_LEGGINGS).setWeight(40))
                                .add(LootItem.lootTableItem(Items.DIAMOND_LEGGINGS).setWeight(20))
                                .add(LootItem.lootTableItem(Items.CHAINMAIL_LEGGINGS).setWeight(10).apply(EnchantWithLevelsFunction.enchantWithLevels(ConstantValue.exactly(30)).allowTreasure()))
                                .add(LootItem.lootTableItem(Items.IRON_LEGGINGS).setWeight(20).apply(EnchantWithLevelsFunction.enchantWithLevels(ConstantValue.exactly(30)).allowTreasure()))
                                .add(LootItem.lootTableItem(Items.GOLDEN_LEGGINGS).setWeight(10).apply(EnchantWithLevelsFunction.enchantWithLevels(ConstantValue.exactly(30)).allowTreasure()))
                                .add(LootItem.lootTableItem(Items.DIAMOND_LEGGINGS).setWeight(5).apply(EnchantWithLevelsFunction.enchantWithLevels(ConstantValue.exactly(30)).allowTreasure()))));
        consumer.accept(new ResourceLocation(StructureToolkit.MOD_ID, "processors/boots"),
                LootTable.lootTable().
                        withPool(LootPool.lootPool().setRolls(UniformGenerator.between(0.0F, 1.0F))
                                .add(LootItem.lootTableItem(Items.CHAINMAIL_BOOTS).setWeight(40))
                                .add(LootItem.lootTableItem(Items.IRON_BOOTS).setWeight(80))
                                .add(LootItem.lootTableItem(Items.GOLDEN_BOOTS).setWeight(40))
                                .add(LootItem.lootTableItem(Items.DIAMOND_BOOTS).setWeight(20))
                                .add(LootItem.lootTableItem(Items.CHAINMAIL_BOOTS).setWeight(10).apply(EnchantWithLevelsFunction.enchantWithLevels(ConstantValue.exactly(30)).allowTreasure()))
                                .add(LootItem.lootTableItem(Items.IRON_BOOTS).setWeight(20).apply(EnchantWithLevelsFunction.enchantWithLevels(ConstantValue.exactly(30)).allowTreasure()))
                                .add(LootItem.lootTableItem(Items.GOLDEN_BOOTS).setWeight(10).apply(EnchantWithLevelsFunction.enchantWithLevels(ConstantValue.exactly(30)).allowTreasure()))
                                .add(LootItem.lootTableItem(Items.DIAMOND_BOOTS).setWeight(5).apply(EnchantWithLevelsFunction.enchantWithLevels(ConstantValue.exactly(30)).allowTreasure()))));
        consumer.accept(new ResourceLocation(StructureToolkit.MOD_ID, "processors/fuel"),
                LootTable.lootTable().
                        withPool(LootPool.lootPool().setRolls(UniformGenerator.between(0.0F, 1.0F))
                                .add(LootItem.lootTableItem(Items.COAL).setWeight(40)).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 10.0F)))));
        consumer.accept(new ResourceLocation(StructureToolkit.MOD_ID, "processors/ore"),
                LootTable.lootTable().
                        withPool(LootPool.lootPool().setRolls(UniformGenerator.between(0.0F, 1.0F))
                                .add(TagEntry.expandTag(Tags.Items.ORES).setWeight(40)).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F)))));
        consumer.accept(new ResourceLocation(StructureToolkit.MOD_ID, "processors/ingots"),
                LootTable.lootTable().
                        withPool(LootPool.lootPool().setRolls(UniformGenerator.between(0.0F, 1.0F))
                                .add(TagEntry.expandTag(Tags.Items.INGOTS).setWeight(40)).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F)))));
    }

    private void equipmentLootTable(BiConsumer<ResourceLocation, LootTable.Builder> consumer, String material) {
        ResourceLocation armorsLoc = new ResourceLocation(StructureToolkit.MOD_ID, "processors/armors/" + material);
        consumer.accept(armorsLoc,
                LootTable.lootTable().
                        withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                                .add(TagEntry.expandTag(ModTags.Items.ARMORS.get(material)))));
        ResourceLocation weaponsLoc = new ResourceLocation(StructureToolkit.MOD_ID, "processors/weapons/" + material);
        consumer.accept(weaponsLoc,
                LootTable.lootTable().
                        withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                                .add(TagEntry.expandTag(ModTags.Items.ARMORS.get(material)))));
        ResourceLocation toolsLoc = new ResourceLocation(StructureToolkit.MOD_ID, "processors/tools/" + material);
        consumer.accept(toolsLoc,
                LootTable.lootTable().
                        withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                                .add(TagEntry.expandTag(ModTags.Items.ARMORS.get(material)))));
        consumer.accept(new ResourceLocation(StructureToolkit.MOD_ID, "processors/equipment/"+material),
                LootTable.lootTable().
                        withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                                .add(LootTableReference.lootTableReference(armorsLoc).setWeight(80))
                                .add(LootTableReference.lootTableReference(weaponsLoc).setWeight(80))
                                .add(LootTableReference.lootTableReference(toolsLoc).setWeight(80))
                                .add(LootTableReference.lootTableReference(armorsLoc).setWeight(20).apply(EnchantWithLevelsFunction.enchantWithLevels(ConstantValue.exactly(30)).allowTreasure()))
                                .add(LootTableReference.lootTableReference(weaponsLoc).setWeight(20).apply(EnchantWithLevelsFunction.enchantWithLevels(ConstantValue.exactly(30)).allowTreasure()))
                                .add(LootTableReference.lootTableReference(toolsLoc).setWeight(20).apply(EnchantWithLevelsFunction.enchantWithLevels(ConstantValue.exactly(30)).allowTreasure()))));
    }
}