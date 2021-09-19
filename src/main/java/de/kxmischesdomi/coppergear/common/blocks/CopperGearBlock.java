package de.kxmischesdomi.coppergear.common.blocks;

import net.minecraft.block.*;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public class CopperGearBlock extends Block {

	public static final IntProperty POWER;
	private boolean wiresGivePower = true;

	public static final DirectionProperty FACING;
	protected static final VoxelShape EAST_SHAPE;
	protected static final VoxelShape WEST_SHAPE;
	protected static final VoxelShape SOUTH_SHAPE;
	protected static final VoxelShape NORTH_SHAPE;

	public CopperGearBlock(Settings settings) {
		super(settings);
		this.setDefaultState(this.stateManager.getDefaultState().with(FACING, Direction.NORTH).with(POWER, 0));
	}

	@Override
	public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
		int receiving = world.getReceivedRedstonePower(pos);
		world.setBlockState(pos, state.with(POWER, receiving), NOTIFY_NEIGHBORS);
	}

	public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
		if (!moved) {
			Direction[] var6 = Direction.values();
			int var7 = var6.length;

			for(int var8 = 0; var8 < var7; ++var8) {
				Direction direction = var6[var8];
				world.updateNeighborsAlways(pos.offset(direction), this);
			}

		}
	}

	@Override
	public int getStrongRedstonePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
		return state.get(POWER) - 1;
	}

	@Override
	public int getWeakRedstonePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
		return state.get(POWER) - 1;
	}

	@Override
	public void neighborUpdate(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean notify) {
		int receiving = world.getReceivedRedstonePower(pos);
		Direction direction = Direction.fromVector(fromPos.subtract(pos));
		int emitted = world.getEmittedRedstonePower(fromPos, direction);
//		if (state.get(POWER) != receiving || (receiving > 0 && receiving > emitted)) {
			world.setBlockState(pos, state.with(POWER, receiving), NOTIFY_NEIGHBORS);
//		}

	}

	@Override
	public boolean emitsRedstonePower(BlockState state) {
		return wiresGivePower;
	}

	private boolean canPlaceOn(BlockView world, BlockPos pos, Direction side) {
		BlockState blockState = world.getBlockState(pos);
		return blockState.isSideSolidFullSquare(world, pos, side);
	}

	public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
		Direction direction = state.get(FACING);
		return this.canPlaceOn(world, pos.offset(direction.getOpposite()), direction);
	}

	public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
		if (direction.getOpposite() == state.get(FACING) && !state.canPlaceAt(world, pos)) {
			return Blocks.AIR.getDefaultState();
		} else {
//			if ((Boolean)state.get(WATERLOGGED)) {
//				world.getFluidTickScheduler().schedule(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
//			}

			return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
		}
	}

	@Nullable
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		BlockState blockState2;
		if (!ctx.canReplaceExisting()) {
			blockState2 = ctx.getWorld().getBlockState(ctx.getBlockPos().offset(ctx.getSide().getOpposite()));
			if (blockState2.isOf(this) && blockState2.get(FACING) == ctx.getSide()) {
				return null;
			}
		}

		blockState2 = this.getDefaultState();
		WorldView worldView = ctx.getWorld();
		BlockPos blockPos = ctx.getBlockPos();
//		FluidState fluidState = ctx.getWorld().getFluidState(ctx.getBlockPos());
		Direction[] var6 = ctx.getPlacementDirections();
		int var7 = var6.length;

		for(int var8 = 0; var8 < var7; ++var8) {
			Direction direction = var6[var8];
			if (direction.getAxis().isHorizontal()) {
				blockState2 = blockState2.with(FACING, direction.getOpposite());
				if (blockState2.canPlaceAt(worldView, blockPos)) {
					return blockState2;
//					return blockState2.with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
				}
			}
		}

		return null;
	}

	public BlockState rotate(BlockState state, BlockRotation rotation) {
		return state.with(FACING, rotation.rotate(state.get(FACING)));
	}

	public BlockState mirror(BlockState state, BlockMirror mirror) {
		return state.rotate(mirror.getRotation(state.get(FACING)));
	}

	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(FACING, POWER);
//		builder.add(FACING, WATERLOGGED);
	}

	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		switch(state.get(FACING)) {
			case NORTH:
				return NORTH_SHAPE;
			case SOUTH:
				return SOUTH_SHAPE;
			case WEST:
				return WEST_SHAPE;
			case EAST:
			default:
				return EAST_SHAPE;
		}
	}

	static {
		FACING = HorizontalFacingBlock.FACING;
		POWER = Properties.POWER;
//		WATERLOGGED = Properties.WATERLOGGED;
		EAST_SHAPE = Block.createCuboidShape(0.0D, 0.0D, 0.0D, 3.0D, 16.0D, 16.0D);
		WEST_SHAPE = Block.createCuboidShape(13.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D);
		SOUTH_SHAPE = Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 3.0D);
		NORTH_SHAPE = Block.createCuboidShape(0.0D, 0.0D, 13.0D, 16.0D, 16.0D, 16.0D);
	}

}
