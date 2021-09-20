package de.kxmischesdomi.coppergear.common.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.DoorBlock;
import net.minecraft.block.Oxidizable;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.WorldAccess;

import java.util.Random;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public class OxidizableDoorBlock extends DoorBlock implements CopperGearOxidizable {

	private final Oxidizable.OxidizationLevel oxidizationLevel;
	private final boolean waxed;

	public OxidizableDoorBlock(OxidizationLevel oxidizationLevel, boolean waxed, Settings settings) {
		super(settings);
		this.oxidizationLevel = oxidizationLevel;
		this.waxed = waxed;
	}

	@Override
	public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		this.tickDegradation(state, world, pos, random);
		super.randomTick(state, world, pos, random);
	}

	@Override
	public boolean hasRandomTicks(BlockState state) {
		return state.get(HALF) == DoubleBlockHalf.LOWER && CopperGearOxidizable.getIncreasedOxidationBlock(state.getBlock()).isPresent();
	}

	@Override
	public boolean isWaxed() {
		return waxed;
	}

	@Override
	public OxidizationLevel getDegradationLevel() {
		return oxidizationLevel;
	}

	public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
		DoubleBlockHalf doubleBlockHalf = state.get(HALF);

		if (direction == Direction.UP && doubleBlockHalf == DoubleBlockHalf.LOWER || direction == Direction.DOWN && doubleBlockHalf == DoubleBlockHalf.UPPER) {
			if (neighborState.getBlock() instanceof OxidizableDoorBlock && !neighborState.isOf(this)) {
				state = neighborState.with(HALF, neighborState.get(HALF) == DoubleBlockHalf.UPPER ? DoubleBlockHalf.LOWER : DoubleBlockHalf.UPPER);
			}
		}

		if (direction.getAxis() == Direction.Axis.Y && doubleBlockHalf == DoubleBlockHalf.LOWER == (direction == Direction.UP)) {
			return neighborState.isOf(state.getBlock()) && neighborState.get(HALF) != doubleBlockHalf ? state.with(FACING, neighborState.get(FACING)).with(OPEN, neighborState.get(OPEN)).with(HINGE, neighborState.get(HINGE)).with(POWERED, neighborState.get(POWERED)) : Blocks.AIR.getDefaultState();
		} else {
			return doubleBlockHalf == DoubleBlockHalf.LOWER && direction == Direction.DOWN && !state.canPlaceAt(world, pos) ? Blocks.AIR.getDefaultState() : super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
		}
	}

}
