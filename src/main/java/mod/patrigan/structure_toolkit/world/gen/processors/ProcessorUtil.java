package mod.patrigan.structure_toolkit.world.gen.processors;

import mod.patrigan.structure_toolkit.util.RandomType;
import net.minecraft.block.*;
import net.minecraft.item.Item;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.Property;
import net.minecraft.tags.ITag;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.gen.feature.template.Template;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static net.minecraft.block.Blocks.*;
import static net.minecraftforge.registries.ForgeRegistries.BLOCKS;

public class ProcessorUtil {
    public static final String NBT_FINAL_STATE = "final_state";

    public static Random getRandom(RandomType type, BlockPos blockPos, BlockPos piecePos, BlockPos structurePos, IWorldReader world, long processorSeed){
        return new Random(getRandomSeed(type, blockPos, piecePos, structurePos, world, processorSeed));
    }

    public static long getRandomSeed(RandomType type, BlockPos blockPos, BlockPos piecePos, BlockPos structurePos, IWorldReader world, long processorSeed){
        switch(type){
            case BLOCK: return getRandomSeed(blockPos, processorSeed);
            case PIECE: return getRandomSeed(piecePos, processorSeed);
            case STRUCTURE: return getRandomSeed(structurePos, processorSeed);
            case WORLD: return ((ISeedReader) world).getSeed() + processorSeed;
            default: throw new RuntimeException("Unknown random type: " + type.toString());
        }
    }

    public static long getRandomSeed(BlockPos pos, long processorSeed) {
        return pos == null ? Util.getMillis() + processorSeed : MathHelper.getSeed(pos) + processorSeed;
    }

    public static Block getRandomBlockFromTag(ITag<Block> tag, Random random, List<ResourceLocation> exclusionList){
        List<Block> resultList = tag.getValues().stream().filter(block -> !exclusionList.contains(block.getRegistryName())).collect(Collectors.toList());
        return resultList.get(random.nextInt(resultList.size()));
    }

    public static Item getRandomItemFromTag(ITag<Item> tag, Random random, List<ResourceLocation> exclusionList){
        List<Item> resultList = tag.getValues().stream().filter(item -> !exclusionList.contains(item.getRegistryName())).collect(Collectors.toList());
        return resultList.get(random.nextInt(resultList.size()));
    }

    public static BlockState copyStairsState(BlockState blockState, Block newBlock) {
        BlockState newBlockState = newBlock.defaultBlockState();
        newBlockState = updateProperty(blockState, newBlockState, StairsBlock.FACING);
        newBlockState = updateProperty(blockState, newBlockState, StairsBlock.SHAPE);
        newBlockState = updateProperty(blockState, newBlockState, StairsBlock.HALF);
        newBlockState = updateProperty(blockState, newBlockState, StairsBlock.WATERLOGGED);
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

    public static Template.BlockInfo getBlock(List<Template.BlockInfo> pieceBlocks, BlockPos pos) {
        return pieceBlocks.stream().filter(blockInfo -> blockInfo.pos.equals(pos)).findFirst().orElse(null);
    }

    public static boolean isAir(Template.BlockInfo blockinfo){
        if(blockinfo != null && blockinfo.state.is(JIGSAW)){
            Block block = BLOCKS.getValue(new ResourceLocation(blockinfo.nbt.getString(NBT_FINAL_STATE)));
            return block == null || block.is(AIR) || block.is(CAVE_AIR);
        }else {
            return blockinfo == null || blockinfo.state.is(AIR) || blockinfo.state.is(CAVE_AIR);
        }
    }

    public static boolean isSolid(Template.BlockInfo blockinfo){
        if(blockinfo != null && blockinfo.state.is(JIGSAW)){
            Block block = BLOCKS.getValue(new ResourceLocation(blockinfo.nbt.getString(NBT_FINAL_STATE)));
            return block != null && !block.is(AIR) && !block.is(CAVE_AIR) && !(block instanceof FlowingFluidBlock);
        }else {
            return blockinfo != null && !blockinfo.state.is(AIR) && !blockinfo.state.is(CAVE_AIR) && !(blockinfo.state.getBlock() instanceof FlowingFluidBlock);
        }
    }

    public static boolean isFaceFull(Template.BlockInfo blockinfo, Direction direction){
        if(blockinfo != null && blockinfo.state.is(JIGSAW)){
            Block block = BLOCKS.getValue(new ResourceLocation(blockinfo.nbt.getString(NBT_FINAL_STATE)));
            return block != null && !block.is(AIR) && !block.is(CAVE_AIR) && !(block instanceof FlowingFluidBlock) &&
                    Block.isFaceFull(block.getShape(block.defaultBlockState(), null, blockinfo.pos, ISelectionContext.empty()), direction);
        }else {
            return blockinfo != null && !blockinfo.state.is(AIR) && !blockinfo.state.is(CAVE_AIR) && !(blockinfo.state.getBlock() instanceof FlowingFluidBlock) &&
            Block.isFaceFull(blockinfo.state.getBlock().getShape(blockinfo.state, null, blockinfo.pos, ISelectionContext.empty()), direction);
        }
    }
}
