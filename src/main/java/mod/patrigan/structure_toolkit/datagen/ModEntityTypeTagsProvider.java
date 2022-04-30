package mod.patrigan.structure_toolkit.datagen;

import mod.patrigan.structure_toolkit.init.ModTags;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;

import static mod.patrigan.structure_toolkit.StructureToolkit.MOD_ID;

public class ModEntityTypeTagsProvider extends net.minecraft.data.tags.EntityTypeTagsProvider {


    public ModEntityTypeTagsProvider(DataGenerator dataGenerator, @Nullable ExistingFileHelper existingFileHelper) {
        super(dataGenerator, MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags() {
    }
}
