package mod.patrigan.structure_toolkit.init;

import mod.patrigan.structure_toolkit.StructureToolkit;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.versions.forge.ForgeVersion;

import java.util.HashMap;
import java.util.Map;

public class ModTags {

    public static class Items {

        public static Map<String, ITag.INamedTag<Item>> ARMORS = new HashMap<>();
        public static Map<String, ITag.INamedTag<Item>> WEAPONS = new HashMap<>();
        public static Map<String, ITag.INamedTag<Item>> TOOLS = new HashMap<>();
        public static final ITag.INamedTag<Item> IRON_ARMOR = addArmorTag("iron"); ;
        public static final ITag.INamedTag<Item> IRON_WEAPONS = addWeaponsTag("iron");
        public static final ITag.INamedTag<Item> IRON_TOOLS = addToolsTag("iron");

        private static ITag.INamedTag<Item> tag(String id) {
            return ItemTags.bind(StructureToolkit.MOD_ID + ":" + id);
        }
        private static ITag.INamedTag<Item> forgeTag(String id) {
            return ItemTags.createOptional(new ResourceLocation(ForgeVersion.MOD_ID + ":" + id));
        }

        private static ITag.INamedTag<Item> addArmorTag(String material){
            ITag.INamedTag<Item> tag = forgeTag("armor/" + material);
            ARMORS.put(material, tag);
            return tag;
        }
        private static ITag.INamedTag<Item> addWeaponsTag(String material){
            ITag.INamedTag<Item> tag = forgeTag("weapons/" + material);
            WEAPONS.put(material, tag);
            return tag;
        }
        private static ITag.INamedTag<Item> addToolsTag(String material){
            ITag.INamedTag<Item> tag = forgeTag("tools/" + material);
            TOOLS.put(material, tag);
            return tag;
        }
    }

    public static class EntityTypes {

        private static ITag.INamedTag<EntityType<?>> forgeTag(String id) {
            return EntityTypeTags.createOptional(new ResourceLocation(ForgeVersion.MOD_ID + ":" + id));
        }
    }

    public static class Blocks {

        public static final ITag.INamedTag<Block> MUSHROOMS = forgeTag("mushrooms");

        private static ITag.INamedTag<Block> forgeTag(String id) {
            return BlockTags.createOptional(new ResourceLocation(ForgeVersion.MOD_ID + ":" + id));
        }
    }
}