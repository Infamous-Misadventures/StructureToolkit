package mod.patrigan.structure_toolkit.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        if (event.includeClient()) {
            generator.addProvider(new ModLanguageProvider(generator, "en_us"));
        }
        if (event.includeServer()) {
            ModBlockTagsProvider modBlockTagsProvider = new ModBlockTagsProvider(generator, event.getExistingFileHelper());
            generator.addProvider(modBlockTagsProvider);
            generator.addProvider(new ModEntityTypeTagsProvider(generator, event.getExistingFileHelper()));
            generator.addProvider(new ModChestLootTablesProvider(generator));
        }
    }
}