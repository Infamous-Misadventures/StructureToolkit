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

public class ModTags {

    public static class Items {

        private static ITag.INamedTag<Item> tag(String id) {
            return ItemTags.bind(StructureToolkit.MOD_ID + ":" + id);
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