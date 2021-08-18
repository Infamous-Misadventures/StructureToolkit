package mod.patrigan.structure_toolkit.world.gen.processors;

import com.mojang.serialization.Codec;
import mod.patrigan.structure_toolkit.init.ModProcessors;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.template.IStructureProcessorType;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.StructureProcessor;
import net.minecraft.world.gen.feature.template.Template;

import java.util.HashMap;
import java.util.Map;

// Courtesy of TelepathicGrunt
public class BiomeSurfaceProcessor extends StructureProcessor {
    public static final Codec<BiomeSurfaceProcessor> CODEC = Codec.unit(BiomeSurfaceProcessor::new);

    private static final Map<IWorldReader, Map<Long, Biome>> MINI_BIOMEPOS_CACHE = new HashMap<>();

    @Override
    public Template.BlockInfo process(IWorldReader world, BlockPos piecePos, BlockPos structurePos, Template.BlockInfo rawBlockInfo, Template.BlockInfo blockInfo, PlacementSettings settings, Template template) {
        BlockState structureBlock = blockInfo.state;

        if (structureBlock.is(Blocks.GRASS_BLOCK)) {
            BlockPos blockPos = blockInfo.pos;
            Biome biome = getCachedBiome(world, blockPos);

            BlockState topSurfaceBlock = biome.getGenerationSettings().getSurfaceBuilder().get().config.getTopMaterial();
            return new Template.BlockInfo(blockPos, topSurfaceBlock, blockInfo.nbt);
        }
        else if (structureBlock.is(Blocks.DIRT)) {
            BlockPos blockPos = blockInfo.pos;
            Biome biome = getCachedBiome(world, blockPos);

            BlockState underSurfaceBlock = biome.getGenerationSettings().getSurfaceBuilder().get().config.getUnderMaterial();
            return new Template.BlockInfo(blockPos, underSurfaceBlock, blockInfo.nbt);
        }
        return blockInfo;
    }

    private Biome getCachedBiome(IWorldReader worldView, BlockPos structurePos) {
        Map<Long, Biome> worldSpecificBiomes = MINI_BIOMEPOS_CACHE.computeIfAbsent(worldView, (keyPos) -> new HashMap<>());
        BlockPos biomePos = new BlockPos(structurePos.getX() >> 2, 0, structurePos.getZ() >> 2);
        Biome biome = worldSpecificBiomes.computeIfAbsent(biomePos.asLong(), (keyPos) -> worldView.getBiome(structurePos));
        if(worldSpecificBiomes.size() > 20) worldSpecificBiomes.clear();
        return biome;
    }

    @Override
    protected IStructureProcessorType<?> getType() {
        return ModProcessors.BIOME_SURFACE;
    }
}