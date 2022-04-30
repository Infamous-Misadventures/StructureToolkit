package mod.patrigan.structure_toolkit.init;

import com.mojang.serialization.Codec;
import mod.patrigan.structure_toolkit.world.gen.processors.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.Registry;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;

import static mod.patrigan.structure_toolkit.StructureToolkit.MOD_ID;

public class ModProcessors {
    // Replacers
    public static StructureProcessorType<AirRetainerProcessor> AIR_RETAINER;
    public static StructureProcessorType<BiomeSurfaceProcessor> BIOME_SURFACE;
    public static StructureProcessorType<BlockMossifyProcessor> BLOCK_MOSSIFY;
    public static StructureProcessorType<GradientReplaceProcessor> GRADIENT_SPOT_REPLACE;
    public static StructureProcessorType<SpawnerRandomizerProcessor> SPAWNER_RANDOMIZER_PROCESSOR;
    // Adders
    public static StructureProcessorType<AttachmentProcessor> ATTACHMENT;
    public static StructureProcessorType<CeilingAttachmentProcessor> CEILING_ATTACHMENT;
    public static StructureProcessorType<LilyPadProcessor> LILY_PADS;
    public static StructureProcessorType<MushroomProcessor> MUSHROOMS;
    public static StructureProcessorType<SnowProcessor> SNOW;
    public static StructureProcessorType<VineProcessor> VINES;
    // Decorators
    public static StructureProcessorType<ArmorStandProcessor> ARMOR_STANDS;
    public static StructureProcessorType<FlowerPotProcessor> FLOWER_POTS;
    public static StructureProcessorType<FurnaceProcessor> FURNACES;
    public static StructureProcessorType<ItemFrameProcessor> ITEM_FRAMES;
    public static StructureProcessorType<LecternProcessor> LECTERNS;
    public static StructureProcessorType<ChestProcessor> CHESTS;
    // Util
    public static StructureProcessorType<WaterloggingFixProcessor> WATERLOGGING_FIX_PROCESSOR;
    public static StructureProcessorType<WaterflowFixProcessor> WATERFLOW_FIX_PROCESSOR;


    public static void init(){
        // Replacers
        AIR_RETAINER = register("air_retainer", AirRetainerProcessor.CODEC);
        BIOME_SURFACE = register("biome_surface", BiomeSurfaceProcessor.CODEC);
        BLOCK_MOSSIFY = register("block_mossify", BlockMossifyProcessor.CODEC);
        GRADIENT_SPOT_REPLACE = register("gradient_replace", GradientReplaceProcessor.CODEC);
        SPAWNER_RANDOMIZER_PROCESSOR = register("spawner_randomizer", SpawnerRandomizerProcessor.CODEC);
        // Adders
        ATTACHMENT = register("attachment", AttachmentProcessor.CODEC);
        CEILING_ATTACHMENT = register("ceiling_attachment", CeilingAttachmentProcessor.CODEC);
        LILY_PADS = register("lily_pads", LilyPadProcessor.CODEC);
        MUSHROOMS = register("mushrooms", MushroomProcessor.CODEC);
        SNOW = register("snow", SnowProcessor.CODEC);
        VINES = register("vines", VineProcessor.CODEC);
        // Decorators
        ARMOR_STANDS = register("armor_stands", ArmorStandProcessor.CODEC);
        CHESTS = register("chests", ChestProcessor.CODEC);
        FLOWER_POTS = register("flower_pots", FlowerPotProcessor.CODEC);
        FURNACES = register("furnaces", FurnaceProcessor.CODEC);
        ITEM_FRAMES = register("item_frames", ItemFrameProcessor.CODEC);
        LECTERNS = register("lecterns", LecternProcessor.CODEC);
        // Util
        WATERLOGGING_FIX_PROCESSOR = register("waterlogging_fix", WaterloggingFixProcessor.CODEC);
        WATERFLOW_FIX_PROCESSOR = register("waterflow_fix", WaterflowFixProcessor.CODEC);
    }

    static <P extends StructureProcessor> StructureProcessorType<P> register(String name, Codec<P> codec) {
        return Registry.register(Registry.STRUCTURE_PROCESSOR, new ResourceLocation(MOD_ID, name), () -> {
            return codec;
        });
    }
}
