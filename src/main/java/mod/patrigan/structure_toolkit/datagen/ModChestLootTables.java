package mod.patrigan.structure_toolkit.datagen;

import mod.patrigan.structure_toolkit.StructureToolkit;
import mod.patrigan.structure_toolkit.init.ModTags;
import net.minecraft.data.loot.ChestLootTables;
import net.minecraft.item.Items;
import net.minecraft.loot.*;
import net.minecraft.loot.functions.EnchantWithLevels;
import net.minecraft.loot.functions.SetCount;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;

import java.util.function.BiConsumer;

public class ModChestLootTables extends ChestLootTables {

    @Override
    public void accept(BiConsumer<ResourceLocation, LootTable.Builder> consumer) {
        consumer.accept(new ResourceLocation(StructureToolkit.MOD_ID, "processors/saplings"),
                LootTable.lootTable().
                        withPool(LootPool.lootPool().setRolls(RandomValueRange.between(1.0F, 1.0F))
                                .add(TagLootEntry.expandTag(ItemTags.SAPLINGS).setWeight(6).apply(SetCount.setCount(ConstantRange.exactly(1))))));
        consumer.accept(new ResourceLocation(StructureToolkit.MOD_ID, "empty"),
                LootTable.lootTable());
        equipmentLootTable(consumer, "iron");
        consumer.accept(new ResourceLocation(StructureToolkit.MOD_ID, "processors/helmets"),
                LootTable.lootTable().
                        withPool(LootPool.lootPool().setRolls(RandomValueRange.between(0.0F, 1.0F))
                                .add(ItemLootEntry.lootTableItem(Items.CHAINMAIL_HELMET).setWeight(40))
                                .add(ItemLootEntry.lootTableItem(Items.IRON_HELMET).setWeight(80))
                                .add(ItemLootEntry.lootTableItem(Items.GOLDEN_HELMET).setWeight(40))
                                .add(ItemLootEntry.lootTableItem(Items.DIAMOND_HELMET).setWeight(20))
                                .add(ItemLootEntry.lootTableItem(Items.CHAINMAIL_HELMET).setWeight(10).apply(EnchantWithLevels.enchantWithLevels(ConstantRange.exactly(30)).allowTreasure()))
                                .add(ItemLootEntry.lootTableItem(Items.IRON_HELMET).setWeight(20).apply(EnchantWithLevels.enchantWithLevels(ConstantRange.exactly(30)).allowTreasure()))
                                .add(ItemLootEntry.lootTableItem(Items.GOLDEN_HELMET).setWeight(10).apply(EnchantWithLevels.enchantWithLevels(ConstantRange.exactly(30)).allowTreasure()))
                                .add(ItemLootEntry.lootTableItem(Items.DIAMOND_HELMET).setWeight(5).apply(EnchantWithLevels.enchantWithLevels(ConstantRange.exactly(30)).allowTreasure()))));
        consumer.accept(new ResourceLocation(StructureToolkit.MOD_ID, "processors/chestplates"),
                LootTable.lootTable().
                        withPool(LootPool.lootPool().setRolls(RandomValueRange.between(0.0F, 1.0F))
                                .add(ItemLootEntry.lootTableItem(Items.CHAINMAIL_CHESTPLATE).setWeight(40))
                                .add(ItemLootEntry.lootTableItem(Items.IRON_CHESTPLATE).setWeight(80))
                                .add(ItemLootEntry.lootTableItem(Items.GOLDEN_CHESTPLATE).setWeight(40))
                                .add(ItemLootEntry.lootTableItem(Items.DIAMOND_CHESTPLATE).setWeight(20))
                                .add(ItemLootEntry.lootTableItem(Items.CHAINMAIL_CHESTPLATE).setWeight(10).apply(EnchantWithLevels.enchantWithLevels(ConstantRange.exactly(30)).allowTreasure()))
                                .add(ItemLootEntry.lootTableItem(Items.IRON_CHESTPLATE).setWeight(20).apply(EnchantWithLevels.enchantWithLevels(ConstantRange.exactly(30)).allowTreasure()))
                                .add(ItemLootEntry.lootTableItem(Items.GOLDEN_CHESTPLATE).setWeight(10).apply(EnchantWithLevels.enchantWithLevels(ConstantRange.exactly(30)).allowTreasure()))
                                .add(ItemLootEntry.lootTableItem(Items.DIAMOND_CHESTPLATE).setWeight(5).apply(EnchantWithLevels.enchantWithLevels(ConstantRange.exactly(30)).allowTreasure()))));
        consumer.accept(new ResourceLocation(StructureToolkit.MOD_ID, "processors/leggings"),
                LootTable.lootTable().
                        withPool(LootPool.lootPool().setRolls(RandomValueRange.between(0.0F, 1.0F))
                                .add(ItemLootEntry.lootTableItem(Items.CHAINMAIL_LEGGINGS).setWeight(40))
                                .add(ItemLootEntry.lootTableItem(Items.IRON_LEGGINGS).setWeight(80))
                                .add(ItemLootEntry.lootTableItem(Items.GOLDEN_LEGGINGS).setWeight(40))
                                .add(ItemLootEntry.lootTableItem(Items.DIAMOND_LEGGINGS).setWeight(20))
                                .add(ItemLootEntry.lootTableItem(Items.CHAINMAIL_LEGGINGS).setWeight(10).apply(EnchantWithLevels.enchantWithLevels(ConstantRange.exactly(30)).allowTreasure()))
                                .add(ItemLootEntry.lootTableItem(Items.IRON_LEGGINGS).setWeight(20).apply(EnchantWithLevels.enchantWithLevels(ConstantRange.exactly(30)).allowTreasure()))
                                .add(ItemLootEntry.lootTableItem(Items.GOLDEN_LEGGINGS).setWeight(10).apply(EnchantWithLevels.enchantWithLevels(ConstantRange.exactly(30)).allowTreasure()))
                                .add(ItemLootEntry.lootTableItem(Items.DIAMOND_LEGGINGS).setWeight(5).apply(EnchantWithLevels.enchantWithLevels(ConstantRange.exactly(30)).allowTreasure()))));
        consumer.accept(new ResourceLocation(StructureToolkit.MOD_ID, "processors/boots"),
                LootTable.lootTable().
                        withPool(LootPool.lootPool().setRolls(RandomValueRange.between(0.0F, 1.0F))
                                .add(ItemLootEntry.lootTableItem(Items.CHAINMAIL_BOOTS).setWeight(40))
                                .add(ItemLootEntry.lootTableItem(Items.IRON_BOOTS).setWeight(80))
                                .add(ItemLootEntry.lootTableItem(Items.GOLDEN_BOOTS).setWeight(40))
                                .add(ItemLootEntry.lootTableItem(Items.DIAMOND_BOOTS).setWeight(20))
                                .add(ItemLootEntry.lootTableItem(Items.CHAINMAIL_BOOTS).setWeight(10).apply(EnchantWithLevels.enchantWithLevels(ConstantRange.exactly(30)).allowTreasure()))
                                .add(ItemLootEntry.lootTableItem(Items.IRON_BOOTS).setWeight(20).apply(EnchantWithLevels.enchantWithLevels(ConstantRange.exactly(30)).allowTreasure()))
                                .add(ItemLootEntry.lootTableItem(Items.GOLDEN_BOOTS).setWeight(10).apply(EnchantWithLevels.enchantWithLevels(ConstantRange.exactly(30)).allowTreasure()))
                                .add(ItemLootEntry.lootTableItem(Items.DIAMOND_BOOTS).setWeight(5).apply(EnchantWithLevels.enchantWithLevels(ConstantRange.exactly(30)).allowTreasure()))));
        consumer.accept(new ResourceLocation(StructureToolkit.MOD_ID, "processors/fuel"),
                LootTable.lootTable().
                        withPool(LootPool.lootPool().setRolls(RandomValueRange.between(0.0F, 1.0F))
                                .add(ItemLootEntry.lootTableItem(Items.COAL).setWeight(40)).apply(SetCount.setCount(RandomValueRange.between(1.0F, 10.0F)))));
        consumer.accept(new ResourceLocation(StructureToolkit.MOD_ID, "processors/ore"),
                LootTable.lootTable().
                        withPool(LootPool.lootPool().setRolls(RandomValueRange.between(0.0F, 1.0F))
                                .add(TagLootEntry.expandTag(Tags.Items.ORES).setWeight(40)).apply(SetCount.setCount(RandomValueRange.between(1.0F, 3.0F)))));
        consumer.accept(new ResourceLocation(StructureToolkit.MOD_ID, "processors/ingots"),
                LootTable.lootTable().
                        withPool(LootPool.lootPool().setRolls(RandomValueRange.between(0.0F, 1.0F))
                                .add(TagLootEntry.expandTag(Tags.Items.INGOTS).setWeight(40)).apply(SetCount.setCount(RandomValueRange.between(1.0F, 3.0F)))));
    }

    private void equipmentLootTable(BiConsumer<ResourceLocation, LootTable.Builder> consumer, String material) {
        ResourceLocation armorsLoc = new ResourceLocation(StructureToolkit.MOD_ID, "processors/armors/" + material);
        consumer.accept(armorsLoc,
                LootTable.lootTable().
                        withPool(LootPool.lootPool().setRolls(ConstantRange.exactly(1))
                                .add(TagLootEntry.expandTag(ModTags.Items.ARMORS.get(material)))));
        ResourceLocation weaponsLoc = new ResourceLocation(StructureToolkit.MOD_ID, "processors/weapons/" + material);
        consumer.accept(weaponsLoc,
                LootTable.lootTable().
                        withPool(LootPool.lootPool().setRolls(ConstantRange.exactly(1))
                                .add(TagLootEntry.expandTag(ModTags.Items.ARMORS.get(material)))));
        ResourceLocation toolsLoc = new ResourceLocation(StructureToolkit.MOD_ID, "processors/tools/" + material);
        consumer.accept(toolsLoc,
                LootTable.lootTable().
                        withPool(LootPool.lootPool().setRolls(ConstantRange.exactly(1))
                                .add(TagLootEntry.expandTag(ModTags.Items.ARMORS.get(material)))));
        consumer.accept(new ResourceLocation(StructureToolkit.MOD_ID, "processors/equipment/"+material),
                LootTable.lootTable().
                        withPool(LootPool.lootPool().setRolls(ConstantRange.exactly(1))
                                .add(TableLootEntry.lootTableReference(armorsLoc).setWeight(80))
                                .add(TableLootEntry.lootTableReference(weaponsLoc).setWeight(80))
                                .add(TableLootEntry.lootTableReference(toolsLoc).setWeight(80))
                                .add(TableLootEntry.lootTableReference(armorsLoc).setWeight(20).apply(EnchantWithLevels.enchantWithLevels(ConstantRange.exactly(30)).allowTreasure()))
                                .add(TableLootEntry.lootTableReference(weaponsLoc).setWeight(20).apply(EnchantWithLevels.enchantWithLevels(ConstantRange.exactly(30)).allowTreasure()))
                                .add(TableLootEntry.lootTableReference(toolsLoc).setWeight(20).apply(EnchantWithLevels.enchantWithLevels(ConstantRange.exactly(30)).allowTreasure()))));
    }
}