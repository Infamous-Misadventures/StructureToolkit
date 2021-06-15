package mod.patrigan.structure_toolkit.world.gen.processors;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.patrigan.structure_toolkit.util.GeneralUtils;
import mod.patrigan.structure_toolkit.util.RandomType;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.LecternBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.LecternTileEntity;
import net.minecraft.tileentity.LockableLootTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.gen.feature.template.IStructureProcessorType;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.StructureProcessor;
import net.minecraft.world.gen.feature.template.Template;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.Tags;

import java.util.Random;

import static mod.patrigan.structure_toolkit.init.ModProcessors.CHESTS;
import static mod.patrigan.structure_toolkit.init.ModProcessors.LECTERNS;
import static mod.patrigan.structure_toolkit.util.RandomType.RANDOM_TYPE_CODEC;
import static net.minecraft.block.Blocks.AIR;
import static net.minecraft.block.Blocks.BARREL;
import static net.minecraft.block.LecternBlock.HAS_BOOK;
import static net.minecraft.block.LecternBlock.POWERED;
import static net.minecraft.tags.ItemTags.LECTERN_BOOKS;
import static net.minecraftforge.registries.ForgeRegistries.BLOCKS;

public class ChestProcessor extends StructureProcessor {

    public static final Codec<ChestProcessor> CODEC = RecordCodecBuilder.create(builder -> builder.group(
            ResourceLocation.CODEC.optionalFieldOf("loot_table", new ResourceLocation("structure_toolkit:empty")).forGetter(data -> data.lootTable),
            ResourceLocation.CODEC.optionalFieldOf("replacer", new ResourceLocation("minecraft:air")).forGetter(data -> data.replacer),
            Codec.FLOAT.optionalFieldOf("rarity", 1.0F).forGetter(data -> data.rarity),
            RANDOM_TYPE_CODEC.optionalFieldOf("random_type", RandomType.BLOCK).forGetter(processor -> processor.randomType)
    ).apply(builder, builder.stable(ChestProcessor::new)));
    private static final long SEED = 354134L;

    private final ResourceLocation lootTable;
    private final ResourceLocation replacer;
    private final float rarity;
    private final RandomType randomType;

    public ChestProcessor(ResourceLocation lootTable, ResourceLocation replacer, float rarity, RandomType randomType) {
        this.lootTable = lootTable;
        this.replacer = replacer;
        this.rarity = rarity;
        this.randomType = randomType;
    }

    @Override
    public Template.BlockInfo process(IWorldReader world, BlockPos piecePos, BlockPos structurePos, Template.BlockInfo rawBlockInfo, Template.BlockInfo blockInfo, PlacementSettings settings, Template template) {
        if (((blockInfo.state.is(Tags.Blocks.CHESTS) && !blockInfo.state.is(Tags.Blocks.CHESTS_ENDER)) || blockInfo.state.getBlock().is(BARREL)) && blockInfo.state.hasTileEntity()) {
            TileEntity tileEntity = blockInfo.state.createTileEntity(world);
            Random random = ProcessorUtil.getRandom(randomType, blockInfo.pos, piecePos, structurePos, world, SEED);
            if(tileEntity instanceof LockableLootTileEntity && random.nextFloat() < rarity) {
                ServerWorld serverWorld = ((IServerWorld) world).getLevel();
                LockableLootTileEntity lockableLootTileEntity = (LockableLootTileEntity) tileEntity;
                lockableLootTileEntity.setLevelAndPosition(serverWorld, blockInfo.pos);
                CompoundNBT nbt = blockInfo.nbt;
                nbt.putString("LootTable", this.lootTable.toString());
                nbt.putLong("LootTableSeed", serverWorld.random.nextLong());
                return new Template.BlockInfo(
                        blockInfo.pos,
                        blockInfo.state,
                        nbt);
            }else{
                Block newBlock = BLOCKS.getValue(replacer);
                if(newBlock == null){
                    newBlock = AIR;
                }
                return new Template.BlockInfo(
                        blockInfo.pos,
                        newBlock.defaultBlockState(),
                        null
                );
            }
        }
        return blockInfo;
    }

    @Override
    protected IStructureProcessorType<?> getType() {
        return CHESTS;
    }
}