package de.kxmischesdomi.coppergear.common.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Random;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public class CopperVacuumBlock extends Block implements CopperGearOxidizable, CopperPipeConnectable {

	public static final BooleanProperty ENABLED;

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
		this.setDefaultState((this.stateManager.getDefaultState()).with(ENABLED, true));
	}

	@Override
	public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		world.getBlockTickScheduler().schedule(pos, this, 2);

		if (!state.get(ENABLED)) {
			return;
		}

		List<ItemEntity> entitiesInPullRange = world.getEntitiesByType(EntityType.ITEM, new Box(pos.getX() + 1, pos.getY(), pos.getZ() + 1, pos.getX(), pos.getY() - 0.5, pos.getZ()), itemEntity -> true);
		for (ItemEntity itemEntity : entitiesInPullRange) {
			CopperPipeBlock.transportItem(world, pos, itemEntity.getStack(), itemEntity::setStack);
		}

		List<ItemEntity> entitiesInVelocityRange = world.getEntitiesByType(EntityType.ITEM, new Box(pos.getX() + 1, pos.getY(), pos.getZ() + 1, pos.getX(), pos.getY() - 3, pos.getZ()), itemEntity -> true);
		for (ItemEntity itemEntity : entitiesInVelocityRange) {
			itemEntity.addVelocity(0, 0.5, 0);
		}

	}

	@Override
	public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
		world.getBlockTickScheduler().schedule(pos, this, 2);
	}

	public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
		if (!oldState.isOf(state.getBlock())) {
			this.updateState(world, pos, state);
		}
	}

	public BlockState getPlacementState(ItemPlacementContext ctx) {
		BlockState shape = getState(ctx.getWorld(), ctx.getBlockPos(), getDefaultState());
		return shape.with(ENABLED, true);
	}

	public void neighborUpdate(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean notify) {
		this.updateState(world, pos, state);
	}

	private void updateState(World world, BlockPos pos, BlockState state) {
		world.setBlockState(pos, getState(world, pos, state));
	}

	private BlockState getState(World world, BlockPos pos, BlockState state) {

		boolean bl = !world.isReceivingRedstonePower(pos);
		if (bl != state.get(ENABLED)) {
			return state.with(ENABLED, bl);
		}

		return state;
	}

	public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {

	}

	@Override
	public boolean canPathfindThrough(BlockState state, BlockView world, BlockPos pos, NavigationType type) {
		return false;
	}

	@Override
	public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		this.tickDegradation(state, world, pos, random);
		super.randomTick(state, world, pos, random);
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
		builder.add(ENABLED);
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return COMBINED_SHAPE;
	}

	static {
		ENABLED = Properties.ENABLED;

		double pixelSize = 0.0625;
		double bottomMiddleThickness = pixelSize * 6;
		BOTTOM_SHAPE = VoxelShapes.cuboid(0, 0, 0, 1, bottomMiddleThickness, 1);
		MIDDLE_SHAPE = VoxelShapes.cuboid(pixelSize, bottomMiddleThickness, pixelSize, 1 - pixelSize, bottomMiddleThickness * 2, 1 - pixelSize);
		TOP_SHAPE = VoxelShapes.cuboid(pixelSize*2, bottomMiddleThickness*2, pixelSize*2, 1-pixelSize*2, bottomMiddleThickness*2+pixelSize*4, 1-pixelSize*2);
		COMBINED_SHAPE = VoxelShapes.union(TOP_SHAPE, BOTTOM_SHAPE, MIDDLE_SHAPE);
	}

}
