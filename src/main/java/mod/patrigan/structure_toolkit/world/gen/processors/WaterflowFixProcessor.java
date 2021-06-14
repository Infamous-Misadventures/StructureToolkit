package mod.patrigan.structure_toolkit.world.gen.processors;

import com.mojang.serialization.Codec;
import net.minecraft.block.Blocks;
import net.minecraft.fluid.Fluids;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.feature.template.IStructureProcessorType;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.StructureProcessor;
import net.minecraft.world.gen.feature.template.Template;
import net.minecraft.world.server.ServerWorld;

import static mod.patrigan.structure_toolkit.init.ModProcessors.WATERFLOW_FIX_PROCESSOR;
import static net.minecraft.block.Blocks.WATER;
import static net.minecraft.block.FlowingFluidBlock.LEVEL;

public class WaterflowFixProcessor extends StructureProcessor {

    public static final Codec<WaterflowFixProcessor> CODEC = Codec.unit(WaterflowFixProcessor::new);
    private WaterflowFixProcessor() { }

    @Override
    public Template.BlockInfo process(IWorldReader world, BlockPos piecePos, BlockPos seedPos, Template.BlockInfo rawBlockInfo, Template.BlockInfo blockInfo, PlacementSettings settings, Template template) {
        // Workaround for water not flowing down when water has been placed earlier.
        ServerWorld serverWorld = ((IServerWorld) world).getLevel();
        if(blockInfo.state.is(WATER)) {
            //serverWorld.getLiquidTicks().scheduleTick(blockInfo.pos, Fluids.WATER, Fluids.WATER.getTickDelay(serverWorld)*20);
            /*ChunkPos currentChunkPos = new ChunkPos(blockInfo.pos);
            IChunk currentChunk = world.getChunk(currentChunkPos.x, currentChunkPos.z);
            currentChunk.setBlockState(blockInfo.pos, blockInfo.state, true);*/
            return new Template.BlockInfo(blockInfo.pos, WATER.defaultBlockState().setValue(LEVEL, 7), null);
            //((ServerWorld) world).setBlock(blockInfo.pos, blockInfo.state, 3);
        }
        /*if(rawBlockInfo.pos.getX() == 0){
            refreshFluid(world, blockInfo.pos, Direction.WEST);
        }else if(rawBlockInfo.pos.getX() == template.getSize().getX()){
            refreshFluid(world, blockInfo.pos, Direction.EAST);
        }
        if(rawBlockInfo.pos.getZ() == 0){
            refreshFluid(world, blockInfo.pos, Direction.NORTH);
        }else if(rawBlockInfo.pos.getZ() == template.getSize(BUI).getZ()){
            refreshFluid(world, blockInfo.pos, Direction.SOUTH);
        }
        if(rawBlockInfo.pos.getY() == 0){
            refreshFluid(world, blockInfo.pos, Direction.DOWN);
        }else if(rawBlockInfo.pos.getY() == template.getSize().getY()){
            refreshFluid(world, blockInfo.pos, Direction.UP);
        }*/
        return blockInfo;
    }

    private void refreshFluid(IWorldReader world, BlockPos pos, Direction direction){
        ChunkPos currentChunkPos = new ChunkPos(pos);
        IChunk currentChunk = world.getChunk(currentChunkPos.x, currentChunkPos.z);
        BlockPos.Mutable mutable = new BlockPos.Mutable();
        mutable.set(pos).move(direction);
        if (currentChunkPos.x != mutable.getX() >> 4 || currentChunkPos.z != mutable.getZ() >> 4) {
            currentChunk = world.getChunk(mutable);
        }
        if (currentChunk.getFluidState(mutable).is(FluidTags.WATER) && !currentChunk.getFluidState(mutable).isSource()) {
            currentChunk.setBlockState(mutable, Blocks.STONE.defaultBlockState(), false);
            currentChunk.setBlockState(mutable, Blocks.AIR.defaultBlockState(), false);
        }
    }

    @Override
    protected IStructureProcessorType<?> getType() {
        return WATERFLOW_FIX_PROCESSOR;
    }
}