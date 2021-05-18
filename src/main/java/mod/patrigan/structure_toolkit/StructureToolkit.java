package mod.patrigan.structure_toolkit;

import mod.patrigan.structure_toolkit.datagen.DataGenerators;
import mod.patrigan.structure_toolkit.init.ModBlocks;
import mod.patrigan.structure_toolkit.init.ModItems;
import mod.patrigan.structure_toolkit.init.ModProcessors;
import mod.patrigan.structure_toolkit.init.config.CommonConfigs.CommonConfigValues;
import mod.patrigan.structure_toolkit.util.ConfigHelper;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(StructureToolkit.MOD_ID)
public class StructureToolkit {

    public static final String MOD_ID = "structure_toolkit";

    public static final Logger LOGGER = LogManager.getLogger();

    public static CommonConfigValues MAIN_CONFIG = null;

    public StructureToolkit() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        IEventBus forgeBus = MinecraftForge.EVENT_BUS;
        modEventBus.addListener(this::setup);
        modEventBus.addListener(DataGenerators::gatherData);
        MAIN_CONFIG = ConfigHelper.register(ModConfig.Type.SERVER, CommonConfigValues::new, "structure_toolkit-main.toml");

        ModBlocks.BLOCKS.register(modEventBus);
        ModItems.ITEMS.register(modEventBus);

        forgeBus.register(this);

        LOGGER.log(Level.INFO, "Structure Toolkit Loaded.");
    }

    private void setup(final FMLCommonSetupEvent event)
    {
        event.enqueueWork(() -> {
            //Register Features
            ModProcessors.init();
        });
    }
}
