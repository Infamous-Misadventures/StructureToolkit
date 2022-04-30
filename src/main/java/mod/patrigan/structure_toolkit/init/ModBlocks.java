package mod.patrigan.structure_toolkit.init;

import mod.patrigan.structure_toolkit.StructureToolkit;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, StructureToolkit.MOD_ID);
    public static final List<String> BLOCK_IDS = new ArrayList<>();

}