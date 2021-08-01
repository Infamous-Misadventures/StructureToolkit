package mod.patrigan.structure_toolkit.init;

import mod.patrigan.structure_toolkit.StructureToolkit;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.Tag;
import net.minecraft.tags.ItemTags;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.versions.forge.ForgeVersion;

import java.util.HashMap;
import java.util.Map;

public class ModTags {

    public static class Items {

        public static Map<String, Tag.Named<Item>> ARMORS = new HashMap<>();
        public static Map<String, Tag.Named<Item>> WEAPONS = new HashMap<>();
        public static Map<String, Tag.Named<Item>> TOOLS = new HashMap<>();
        public static final Tag.Named<Item> IRON_ARMOR = addArmorTag("iron"); ;
        public static final Tag.Named<Item> IRON_WEAPONS = addWeaponsTag("iron");
        public static final Tag.Named<Item> IRON_TOOLS = addToolsTag("iron");
        public static final Tag.Named<Item> GOLD_ARMOR = addArmorTag("golden"); ;
        public static final Tag.Named<Item> GOLD_WEAPONS = addWeaponsTag("golden");
        public static final Tag.Named<Item> GOLD_TOOLS = addToolsTag("golden");
        public static final Tag.Named<Item> DIAMOND_ARMOR = addArmorTag("diamond"); ;
        public static final Tag.Named<Item> DIAMOND_WEAPONS = addWeaponsTag("diamond");
        public static final Tag.Named<Item> DIAMOND_TOOLS = addToolsTag("diamond");

        private static Tag.Named<Item> tag(String id) {
            return ItemTags.bind(StructureToolkit.MOD_ID + ":" + id);
        }
        private static Tag.Named<Item> forgeTag(String id) {
            return ItemTags.createOptional(new ResourceLocation(ForgeVersion.MOD_ID + ":" + id));
        }

        private static Tag.Named<Item> addArmorTag(String material){
            Tag.Named<Item> tag = forgeTag("armor/" + material);
            ARMORS.put(material, tag);
            return tag;
        }
        private static Tag.Named<Item> addWeaponsTag(String material){
            Tag.Named<Item> tag = forgeTag("weapons/" + material);
            WEAPONS.put(material, tag);
            return tag;
        }
        private static Tag.Named<Item> addToolsTag(String material){
            Tag.Named<Item> tag = forgeTag("tools/" + material);
            TOOLS.put(material, tag);
            return tag;
        }
    }

    public static class EntityTypes {

        private static Tag.Named<EntityType<?>> forgeTag(String id) {
            return EntityTypeTags.createOptional(new ResourceLocation(ForgeVersion.MOD_ID + ":" + id));
        }
    }

    public static class Blocks {

        public static final Tag.Named<Block> MUSHROOMS = forgeTag("mushrooms");

        private static Tag.Named<Block> forgeTag(String id) {
            return BlockTags.createOptional(new ResourceLocation(ForgeVersion.MOD_ID + ":" + id));
        }
    }
}