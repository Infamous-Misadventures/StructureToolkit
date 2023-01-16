package mod.patrigan.structure_toolkit.world.gen.processors;

import mod.patrigan.structure_toolkit.util.RandomType;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.tags.Tag;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static net.minecraft.world.level.block.Blocks.*;
import static net.minecraftforge.registries.ForgeRegistries.BLOCKS;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;

public class ProcessorUtil {
    public static final String NBT_FINAL_STATE = "final_state";

    public static Random getRandom(RandomType type, BlockPos blockPos, BlockPos piecePos, BlockPos structurePos, LevelReader world, long processorSeed){
        return new Random(getRandomSeed(type, blockPos, piecePos, structurePos, world, processorSeed));
    }

    public static long getRandomSeed(RandomType type, BlockPos blockPos, BlockPos piecePos, BlockPos structurePos, LevelReader world, long processorSeed){
        switch(type){
            case BLOCK: return getRandomSeed(blockPos, processorSeed);
            case PIECE: return getRandomSeed(piecePos, processorSeed);
            case STRUCTURE: return getRandomSeed(structurePos, processorSeed);
            case WORLD: return ((WorldGenLevel) world).getSeed() + processorSeed;
            default: throw new RuntimeException("Unknown random type: " + type.toString());
        }
    }

    public static long getRandomSeed(BlockPos pos, long processorSeed) {
        return pos == null ? Util.getMillis() + processorSeed : Mth.getSeed(pos) + processorSeed;
    }

    public static Block getRandomBlockFromTag(TagKey<Block> tagKey, Random random, List<ResourceLocation> exclusionList){
        List<Block> resultList = BLOCKS.tags().getTag(tagKey).stream().filter(block -> !exclusionList.contains(block.getRegistryName())).collect(Collectors.toList());
        return resultList.get(random.nextInt(resultList.size()));
    }

    public static Item getRandomItemFromTag(Tag<Item> tag, Random random, List<ResourceLocation> exclusionList){
        List<Item> resultList = tag.getValues().stream().filter(item -> !exclusionList.contains(item.getRegistryName())).collect(Collectors.toList());
        return resultList.get(random.nextInt(resultList.size()));
    }

    public static BlockState copyStairsState(BlockState blockState, Block newBlock) {
        BlockState newBlockState = newBlock.defaultBlockState();
        newBlockState = updateProperty(blockState, newBlockState, StairBlock.FACING);
        newBlockState = updateProperty(blockState, newBlockState, StairBlock.SHAPE);
        newBlockState = updateProperty(blockState, newBlockState, StairBlock.HALF);
        newBlockState = updateProperty(blockState, newBlockState, StairBlock.WATERLOGGED);
        return newBlockState;
    }

    public static BlockState copySlabState(BlockState blockState, Block newBlock) {
        BlockState newBlockState = newBlock.defaultBlockState();
        newBlockState = updateProperty(blockState, newBlockState, SlabBlock.TYPE);
        newBlockState = updateProperty(blockState, newBlockState, SlabBlock.WATERLOGGED);
        return newBlockState;
    }

    public static BlockState copyWallState(BlockState blockState, Block newBlock) {
        BlockState newBlockState = newBlock.defaultBlockState();
        newBlockState = updateProperty(blockState, newBlockState, WallBlock.UP);
        newBlockState = updateProperty(blockState, newBlockState, WallBlock.EAST_WALL);
        newBlockState = updateProperty(blockState, newBlockState, WallBlock.NORTH_WALL);
        newBlockState = updateProperty(blockState, newBlockState, WallBlock.SOUTH_WALL);
        newBlockState = updateProperty(blockState, newBlockState, WallBlock.WEST_WALL);
        newBlockState = updateProperty(blockState, newBlockState, WallBlock.WATERLOGGED);
        return newBlockState;
    }

    private static BlockState updateProperty(BlockState state, BlockState newState, Property property) {
        if(newState.hasProperty(property)){
            return newState.setValue(property, state.getValue(property));
        }
        return newState;
    }

    public static StructureTemplate.StructureBlockInfo getBlock(List<StructureTemplate.StructureBlockInfo> pieceBlocks, BlockPos pos) {
        return pieceBlocks.stream().filter(blockInfo -> blockInfo.pos.equals(pos)).findFirst().orElse(null);
    }

    public static boolean isAir(StructureTemplate.StructureBlockInfo blockinfo){
        if(blockinfo != null && blockinfo.state.is(JIGSAW)){
            Block block = BLOCKS.getValue(new ResourceLocation(blockinfo.nbt.getString(NBT_FINAL_STATE)));
            return block == null || block.defaultBlockState().isAir() ;
        }else {
            return blockinfo == null || blockinfo.state.is(AIR) || blockinfo.state.is(CAVE_AIR);
        }
    }

    public static boolean isSolid(StructureTemplate.StructureBlockInfo blockinfo){
        if(blockinfo != null && blockinfo.state.is(JIGSAW)){
            Block block = BLOCKS.getValue(new ResourceLocation(blockinfo.nbt.getString(NBT_FINAL_STATE)));
            return block != null && !block.defaultBlockState().isAir() && !(block instanceof LiquidBlock);
        }else {
            return blockinfo != null && !blockinfo.state.is(AIR) && !blockinfo.state.is(CAVE_AIR) && !(blockinfo.state.getBlock() instanceof LiquidBlock);
        }
    }

    public static boolean isFaceFull(StructureTemplate.StructureBlockInfo blockinfo, Direction direction){
        if(blockinfo != null && blockinfo.state.is(JIGSAW)){
            Block block = BLOCKS.getValue(new ResourceLocation(blockinfo.nbt.getString(NBT_FINAL_STATE)));
            return block != null && !block.defaultBlockState().isAir() && !(block instanceof LiquidBlock) &&
                    Block.isFaceFull(block.getShape(block.defaultBlockState(), null, blockinfo.pos, CollisionContext.empty()), direction);
        }else {
            return blockinfo != null && !blockinfo.state.is(AIR) && !blockinfo.state.is(CAVE_AIR) && !(blockinfo.state.getBlock() instanceof LiquidBlock) &&
            Block.isFaceFull(blockinfo.state.getBlock().getShape(blockinfo.state, null, blockinfo.pos, CollisionContext.empty()), direction);
        }
    }
}
