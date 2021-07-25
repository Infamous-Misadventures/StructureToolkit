package mod.patrigan.structure_toolkit.datagen;

import mod.patrigan.structure_toolkit.StructureToolkit;
import mod.patrigan.structure_toolkit.init.ModTags;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;

import static net.minecraft.world.level.block.Blocks.*;

public class ModBlockTagsProvider extends BlockTagsProvider {

    public ModBlockTagsProvider(DataGenerator generatorIn, ExistingFileHelper existingFileHelper) {
        super(generatorIn, StructureToolkit.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags() {
        vanillaMushrooms();
    }

    private void vanillaMushrooms() {
        this.tag(ModTags.Blocks.MUSHROOMS).add(RED_MUSHROOM);
        this.tag(ModTags.Blocks.MUSHROOMS).add(BROWN_MUSHROOM);
        this.tag(ModTags.Blocks.MUSHROOMS).add(WARPED_FUNGUS);
        this.tag(ModTags.Blocks.MUSHROOMS).add(CRIMSON_FUNGUS);
    }

}
