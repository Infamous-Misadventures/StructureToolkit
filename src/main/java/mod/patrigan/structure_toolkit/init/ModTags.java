package mod.patrigan.structure_toolkit.init;

import mod.patrigan.structure_toolkit.StructureToolkit;
import net.minecraft.tags.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.versions.forge.ForgeVersion;

import java.util.HashMap;
import java.util.Map;

public class ModTags {

    public static class Items {

        public static Map<String, TagKey<Item>> ARMORS = new HashMap<>();
        public static Map<String, TagKey<Item>> WEAPONS = new HashMap<>();
        public static Map<String, TagKey<Item>> TOOLS = new HashMap<>();
        public static final TagKey<Item> IRON_ARMOR = addArmorTag("iron");
        public static final TagKey<Item> IRON_WEAPONS = addWeaponsTag("iron");
        public static final TagKey<Item> IRON_TOOLS = addToolsTag("iron");
        public static final TagKey<Item> GOLD_ARMOR = addArmorTag("golden");
        public static final TagKey<Item> GOLD_WEAPONS = addWeaponsTag("golden");
        public static final TagKey<Item> GOLD_TOOLS = addToolsTag("golden");
        public static final TagKey<Item> DIAMOND_ARMOR = addArmorTag("diamond");
        public static final TagKey<Item> DIAMOND_WEAPONS = addWeaponsTag("diamond");
        public static final TagKey<Item> DIAMOND_TOOLS = addToolsTag("diamond");

        private static TagKey<Item> tag(String id) {
            return ItemTags.create(new ResourceLocation(StructureToolkit.MOD_ID + ":" + id));
        }
        private static TagKey<Item> forgeTag(String id) {
            return ItemTags.create(new ResourceLocation(ForgeVersion.MOD_ID + ":" + id));
        }

        private static TagKey<Item> addArmorTag(String material){
            TagKey<Item> tag = forgeTag("armor/" + material);
            ARMORS.put(material, tag);
            return tag;
        }
        private static TagKey<Item> addWeaponsTag(String material){
            TagKey<Item> tag = forgeTag("weapons/" + material);
            WEAPONS.put(material, tag);
            return tag;
        }
        private static TagKey<Item> addToolsTag(String material){
            TagKey<Item> tag = forgeTag("tools/" + material);
            TOOLS.put(material, tag);
            return tag;
        }
    }

//    public static class EntityTypes {
//
//        private static TagKey<EntityType<?>> forgeTag(String id) {
//            return EntityTypeTags.create(ForgeVersion.MOD_ID + ":" + id);
//        }
//    }

    public static class Blocks {

        public static final TagKey<Block> MUSHROOMS = forgeTag("mushrooms");

        private static TagKey<Block> forgeTag(String id) {
            return BlockTags.create(new ResourceLocation(ForgeVersion.MOD_ID + ":" + id));
        }
    }
}