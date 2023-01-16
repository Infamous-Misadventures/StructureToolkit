package mod.patrigan.structure_toolkit.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraft.world.item.Item;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Arrays;
import java.util.List;

import static net.minecraft.world.item.Items.*;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators {

    public static final List<Item> DYE_ITEMS = Arrays.asList(WHITE_DYE, LIGHT_GRAY_DYE, GRAY_DYE, BLACK_DYE, RED_DYE, ORANGE_DYE, YELLOW_DYE, LIME_DYE, GREEN_DYE, LIGHT_BLUE_DYE, CYAN_DYE, BLUE_DYE, PURPLE_DYE, MAGENTA_DYE, PINK_DYE, BROWN_DYE);

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        generator.addProvider(event.includeClient(), new ModLanguageProvider(generator, "en_us"));
        ModBlockTagsProvider modBlockTagsProvider = new ModBlockTagsProvider(generator, event.getExistingFileHelper());
        generator.addProvider(event.includeServer(), modBlockTagsProvider);
        generator.addProvider(event.includeServer(), new ModEntityTypeTagsProvider(generator, event.getExistingFileHelper()));
        generator.addProvider(event.includeServer(), new ModChestLootTablesProvider(generator));
    }
}