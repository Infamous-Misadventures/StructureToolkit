package mod.patrigan.structure_toolkit.datagen;

import mod.patrigan.structure_toolkit.StructureToolkit;
import net.minecraft.world.level.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.data.LanguageProvider;

import static mod.patrigan.structure_toolkit.init.ModBlocks.BLOCK_IDS;
import static net.minecraftforge.registries.ForgeRegistries.*;

public class ModLanguageProvider extends LanguageProvider {

    public ModLanguageProvider(DataGenerator gen, String locale) {
        super(gen, StructureToolkit.MOD_ID, locale);
    }

    @Override
    protected void addTranslations() {
        BLOCK_IDS.forEach(this::addBlock);
        addConfigOptions();
        addTips();
    }

    private void addTips() {
    }

    private void addConfigOptions() {
    }

    private void addBlock(String blockId) {
        Block block = BLOCKS.getValue(new ResourceLocation(StructureToolkit.MOD_ID, blockId));
        add(block, getNameFromId(blockId));
    }

    private void addItem(String itemId) {
        Item item = ITEMS.getValue(new ResourceLocation(StructureToolkit.MOD_ID, itemId));
        add(item, getNameFromId(itemId));
    }

    private void addEntity(String entityId) {
        EntityType<?> entityType = ENTITY_TYPES.getValue(new ResourceLocation(StructureToolkit.MOD_ID, entityId));
        add(entityType, getNameFromId(entityId));
        String entitySpawnEggItem = entityId + "_spawn_egg";
        Item spawnEgg = ITEMS.getValue(new ResourceLocation(StructureToolkit.MOD_ID, entitySpawnEggItem));
        add(spawnEgg, getNameFromId(entitySpawnEggItem));
    }

    private String getNameFromId(String idString) {
        StringBuilder sb = new StringBuilder();
        for(String word : idString.toLowerCase().split("_") )
        {
            sb.append(word.substring(0,1).toUpperCase() );
            sb.append(word.substring(1) );
            sb.append(" ");
        }
        return sb.toString().trim();
    }
}
