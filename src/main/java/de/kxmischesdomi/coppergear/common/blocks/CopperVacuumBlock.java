package de.kxmischesdomi.coppergear.common.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.Waterloggable;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public class CopperVacuumBlock extends Block implements CopperGearOxidizable, CopperPipeConnectable, Waterloggable {

	public static final BooleanProperty ENABLED;
	public static final BooleanProperty WATERLOGGED;

	private static final VoxelShape BOTTOM_SHAPE;
	private static final VoxelShape MIDDLE_SHAPE;
	private static final VoxelShape TOP_SHAPE;
	private static final VoxelShape COMBINED_SHAPE;

	private final OxidizationLevel oxidizationLevel;
	private final boolean waxed;

	public CopperVacuumBlock(OxidizationLevel oxidizationLevel, boolean waxed, Settings settings) {
		super(settings);
		this.oxidizationLevel = oxidizationLevel;
		this.waxed = waxed;
		this.setDefaultState((this.stateManager.getDefaultState()).with(ENABLED, true).with(WATERLOGGED, false));
	}

	@Override
	public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		world.getBlockTickScheduler().schedule(pos, this, 2);

		if (!state.get(ENABLED)) {
			return;
		}

		List<ItemEntity> entitiesInPullRange = world.getEntitiesByType(EntityType.ITEM, new Box(pos.getX() + 1, pos.getY(), pos.getZ() + 1, pos.getX(), pos.getY() - 0.5, pos.getZ()), itemEntity -> true);
		Collections.shuffle(entitiesInPullRange);
		for (ItemEntity itemEntity : entitiesInPullRange) {
			ItemStack copy = itemEntity.getStack().copy();
			copy.setCount(1);
			CopperPipeBlock.transportItem(world, pos, copy, itemStack -> {
				if (itemStack.isEmpty()) {
					itemEntity.getStack().decrement(1);
					world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.BLOCKS, 0.2F, (random.nextFloat() - random.nextFloat() * 0.2F + 1.0F) * 2.0F);
				}
			});
			break;
		}

		for (int i = 0; i < 3; i++) {
			BlockPos pos1 = pos.down(i+1);
			BlockState blockState = world.getBlockState(pos1);
			if (blockState.getBlock().isShapeFullCube(blockState, world, pos)) {
				break;
			}

			List<ItemEntity> entitiesInVelocityRange = world.getEntitiesByType(EntityType.ITEM, new Box(pos.getX() + 1, pos.getY() - i, pos.getZ() + 1, pos.getX(), pos.getY() - i - 1, pos.getZ()), itemEntity -> true);
			for (ItemEntity itemEntity : entitiesInVelocityRange) {
				itemEntity.addVelocity(0, 0.1, 0);
			}

		}

	}

	public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
		world.getBlockTickScheduler().schedule(pos, this, 2);
		if (!oldState.isOf(state.getBlock())) {
			this.updateState(world, pos, state);
		}
	}

	public BlockState getPlacementState(ItemPlacementContext ctx) {
		BlockState shape = getState(ctx.getWorld(), ctx.getBlockPos(), getDefaultState());
		FluidState fluidState = ctx.getWorld().getFluidState(ctx.getBlockPos());
		return shape.with(ENABLED, true).with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
	}

	public void neighborUpdate(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean notify) {
		this.updateState(world, pos, state);
	}

	private void updateState(World world, BlockPos pos, BlockState state) {
		world.setBlockState(pos, getState(world, pos, state));
	}

	public FluidState getFluidState(BlockState state) {
		return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
	}

	@Override
	public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
		if (state.get(WATERLOGGED)) {
			world.getFluidTickScheduler().schedule(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
		}

		return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
	}

	private BlockState getState(World world, BlockPos pos, BlockState state) {

		boolean bl = !world.isReceivingRedstonePower(pos);
		if (bl != state.get(ENABLED)) {
			return state.with(ENABLED, bl);
		}

		return state;
	}

	@Override
	public boolean canPathfindThrough(BlockState state, BlockView world, BlockPos pos, NavigationType type) {
		return false;
	}

	@Override
	public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		this.tickDegradation(state, world, pos, random);
	}

	public boolean hasRandomTicks(BlockState state) {
		return CopperGearOxidizable.getIncreasedOxidationBlock(state.getBlock()).isPresent();
	}

	@Override
	public boolean isWaxed() {
		return waxed;
	}

	@Override
	public OxidizationLevel getDegradationLevel() {
		return oxidizationLevel;
	}

	@Override
	public boolean canConnectBottom() {
		return false;
	}

	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(ENABLED, WATERLOGGED);
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return COMBINED_SHAPE;
	}

	static {
		ENABLED = Properties.ENABLED;
		BOTTOM_SHAPE = Block.createCuboidShape(0, 0, 0, 16, 6, 16);
		MIDDLE_SHAPE = Block.createCuboidShape(1, 6, 1, 15, 12, 15);
		TOP_SHAPE = Block.createCuboidShape(2, 12, 2, 14, 16, 14);
		COMBINED_SHAPE = VoxelShapes.union(TOP_SHAPE, BOTTOM_SHAPE, MIDDLE_SHAPE);
		WATERLOGGED = Properties.WATERLOGGED;
	}

}
