package mod.patrigan.structure_toolkit.world.gen.processors;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.patrigan.structure_toolkit.StructureToolkit;
import mod.patrigan.structure_toolkit.util.GeneralUtils;
import mod.patrigan.structure_toolkit.util.RandomType;
import net.minecraft.nbt.IntTag;
import net.minecraft.world.level.block.SpawnerBlock;
import net.minecraft.world.entity.EntityType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

import java.util.List;
import net.minecraft.util.RandomSource;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static mod.patrigan.structure_toolkit.init.ModProcessors.SPAWNER_RANDOMIZER_PROCESSOR;
import static mod.patrigan.structure_toolkit.util.RandomType.RANDOM_TYPE_CODEC;

public class SpawnerRandomizerProcessor extends StructureProcessor {

    public static final Codec<SpawnerRandomizerProcessor> CODEC = RecordCodecBuilder.create(builder -> builder.group(
            Codec.mapPair(Registry.ENTITY_TYPE.byNameCodec().fieldOf("resourcelocation"), Codec.intRange(1, Integer.MAX_VALUE).fieldOf("weight")).codec().listOf().fieldOf("spawner_mob_entries").forGetter(processor -> processor.entityWeightMap),
            Codec.SHORT.optionalFieldOf("delay", (short) 20).forGetter(processor -> processor.delay),
            Codec.SHORT.optionalFieldOf("min_spawn_delay", (short) 200).forGetter(processor -> processor.minSpawnDelay),
            Codec.SHORT.optionalFieldOf("max_spawn_delay", (short) 800).forGetter(processor -> processor.maxSpawnDelay),
            Codec.SHORT.optionalFieldOf("spawn_count", (short) 4).forGetter(processor -> processor.spawnCount),
            Codec.SHORT.optionalFieldOf("max_nearby_entities", (short) 6).forGetter(processor -> processor.maxNearbyEntities),
            Codec.SHORT.optionalFieldOf("required_player_range", (short) 16).forGetter(processor -> processor.requiredPlayerRange),
            Codec.SHORT.optionalFieldOf("spawn_range", (short) 4).forGetter(processor -> processor.spawnRange),
            RANDOM_TYPE_CODEC.optionalFieldOf("random_type", RandomType.BLOCK).forGetter(processor -> processor.randomType)
    ).apply(builder, builder.stable(SpawnerRandomizerProcessor::new)));
    private static final long SEED = 531498L;

    public final List<Pair<EntityType<?>, Integer>> entityWeightMap;
    public final Short delay;
    public final Short minSpawnDelay;
    public final Short maxSpawnDelay;
    public final Short spawnCount;
    public final Short maxNearbyEntities;
    public final Short requiredPlayerRange;
    public final Short spawnRange;
    private final RandomType randomType;

    public SpawnerRandomizerProcessor(List<Pair<EntityType<?>, Integer>> entityWeightMap, Short delay, Short minSpawnDelay, Short maxSpawnDelay, Short spawnCount, Short maxNearbyEntities, Short requiredPlayerRange, Short spawnRange, RandomType randomType) {
        this.entityWeightMap = entityWeightMap;
        this.delay = delay;
        this.minSpawnDelay = minSpawnDelay;
        this.maxSpawnDelay = maxSpawnDelay;
        this.spawnCount = spawnCount;
        this.maxNearbyEntities = maxNearbyEntities;
        this.requiredPlayerRange = requiredPlayerRange;
        this.spawnRange = spawnRange;
        this.randomType = randomType;
    }

    @Override
    public StructureTemplate.StructureBlockInfo process(LevelReader world, BlockPos piecePos, BlockPos structurePos, StructureTemplate.StructureBlockInfo rawBlockInfo, StructureTemplate.StructureBlockInfo blockInfo, StructurePlaceSettings settings, StructureTemplate template) {
        if (blockInfo.state.getBlock() instanceof SpawnerBlock) {
            RandomSource random = ProcessorUtil.getRandom(randomType, blockInfo.pos, piecePos, structurePos, world, SEED);
            return new StructureTemplate.StructureBlockInfo(
                    blockInfo.pos,
                    blockInfo.state,
                    setMobSpawnerEntity(random, blockInfo.nbt));
        }
        return blockInfo;
    }

    /**
     * Makes the given block entity now have the correct spawner mob
     */
    private CompoundTag setMobSpawnerEntity(RandomSource random, CompoundTag nbt) {
        EntityType<?> entity = GeneralUtils.getRandomEntry(entityWeightMap, random);
        if (entity != null) {
            CompoundTag compound = new CompoundTag();
            compound.putShort("Delay", delay);
            compound.putShort("MinSpawnDelay", minSpawnDelay);
            compound.putShort("MaxSpawnDelay", maxSpawnDelay);
            compound.putShort("SpawnCount", spawnCount);
            compound.putShort("MaxNearbyEntities", maxNearbyEntities);
            compound.putShort("RequiredPlayerRange", requiredPlayerRange);
            compound.putShort("SpawnRange", spawnRange);

            CompoundTag entityTag = new CompoundTag();
            entityTag.putString("id", Registry.ENTITY_TYPE.getKey(entity).toString());
            CompoundTag spawnData = new CompoundTag();
            spawnData.put("entity", entityTag);
            compound.put("SpawnData", spawnData);

            CompoundTag entityData = new CompoundTag();
            entityData.putString("id", Registry.ENTITY_TYPE.getKey(entity).toString());

            ListTag listnbt = new ListTag();

            CompoundTag spawnEntityDataTag = new CompoundTag();
            spawnEntityDataTag.putString("id", Registry.ENTITY_TYPE.getKey(entity).toString());
            CompoundTag spawnPotentialDataEntryTag = new CompoundTag();
            spawnPotentialDataEntryTag.put("entity", spawnEntityDataTag);
            CompoundTag spawnPotentialEntryTag = new CompoundTag();
            spawnPotentialEntryTag.put("data", spawnPotentialDataEntryTag);
            spawnPotentialEntryTag.put("weight", IntTag.valueOf(1));
            listnbt.add(spawnPotentialEntryTag);

            compound.put("SpawnPotentials", listnbt);

            return compound;
        }
        else {
            StructureToolkit.LOGGER.warn("EntityType in a dungeon does not exist in registry! : {}", entityWeightMap);
        }
        return nbt;
    }

    @Override
    protected StructureProcessorType<?> getType() {
        return SPAWNER_RANDOMIZER_PROCESSOR;
    }
}