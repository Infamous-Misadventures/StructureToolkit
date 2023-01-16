package mod.patrigan.structure_toolkit.world.gen.processors;

import com.mojang.serialization.Codec;
import mod.patrigan.structure_toolkit.init.ModProcessors;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

import java.util.HashMap;
import java.util.Map;

// Courtesy of TelepathicGrunt
// TODO: Rewrite.
public class BiomeSurfaceProcessor extends StructureProcessor {
    public static final Codec<BiomeSurfaceProcessor> CODEC = Codec.unit(BiomeSurfaceProcessor::new);

    private static final Map<LevelReader, Map<Long, Biome>> MINI_BIOMEPOS_CACHE = new HashMap<>();

    @Override
    public StructureTemplate.StructureBlockInfo process(LevelReader world, BlockPos piecePos, BlockPos structurePos, StructureTemplate.StructureBlockInfo rawBlockInfo, StructureTemplate.StructureBlockInfo blockInfo, StructurePlaceSettings settings, StructureTemplate template) {
        BlockState structureBlock = blockInfo.state;

        return blockInfo;
    }

    private Biome getCachedBiome(LevelReader worldView, BlockPos structurePos) {
        Map<Long, Biome> worldSpecificBiomes = MINI_BIOMEPOS_CACHE.computeIfAbsent(worldView, (keyPos) -> new HashMap<>());
        BlockPos biomePos = new BlockPos(structurePos.getX() >> 2, 0, structurePos.getZ() >> 2);
        Biome biome = worldSpecificBiomes.computeIfAbsent(biomePos.asLong(), (keyPos) -> worldView.getBiome(structurePos).value());
        if(worldSpecificBiomes.size() > 20) worldSpecificBiomes.clear();
        return biome;
    }

    @Override
    protected StructureProcessorType<?> getType() {
        return ModProcessors.BIOME_SURFACE;
    }
}