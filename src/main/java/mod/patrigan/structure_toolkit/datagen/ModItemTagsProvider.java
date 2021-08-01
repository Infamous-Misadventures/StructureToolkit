package mod.patrigan.structure_toolkit.datagen;

import mod.patrigan.structure_toolkit.StructureToolkit;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;

public class ModItemTagsProvider extends net.minecraft.data.tags.ItemTagsProvider {

    public ModItemTagsProvider(DataGenerator dataGenerator, BlockTagsProvider blockTagProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(dataGenerator, blockTagProvider, StructureToolkit.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags() {
    }
}
