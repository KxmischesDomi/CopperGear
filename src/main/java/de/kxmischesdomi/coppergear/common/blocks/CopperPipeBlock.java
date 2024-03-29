package de.kxmischesdomi.coppergear.common.blocks;

import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.block.entity.HopperBlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.server.world.ServerWorld;
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
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Random;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.IntStream;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public class CopperPipeBlock extends Block implements CopperGearOxidizable, CopperPipeConnectable, Waterloggable {

	public static final BooleanProperty BOTTOM;
	public static final BooleanProperty TOP;
	public static final BooleanProperty ENABLED;
	public static final BooleanProperty WATERLOGGED;

	public static final VoxelShape BOTH_SHAPE;
	public static final VoxelShape NONE_SHAPE;
	public static final VoxelShape TOP_SHAPE;
	public static final VoxelShape BOTTOM_SHAPE;

	private final Oxidizable.OxidizationLevel oxidizationLevel;
	private final boolean waxed;

	public CopperPipeBlock(Oxidizable.OxidizationLevel oxidizationLevel, boolean waxed, Settings settings) {
		super(settings);
		this.waxed = waxed;
		this.oxidizationLevel = oxidizationLevel;
		this.setDefaultState((this.stateManager.getDefaultState()).with(BOTTOM, false).with(TOP, false).with(ENABLED, true).with(WATERLOGGED, false));
	}

	@Override
	public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		this.tickDegradation(state, world, pos, random);
	}

	public boolean hasRandomTicks(BlockState state) {
		return CopperGearOxidizable.getIncreasedOxidationBlock(state.getBlock()).isPresent();
	}

	public Oxidizable.OxidizationLevel getDegradationLevel() {
		return this.oxidizationLevel;
	}

	@Override
	public boolean isWaxed() {
		return waxed;
	}

	@Override
	public boolean isEnabled(World world, BlockPos pos, BlockState state) {
		return state.get(ENABLED);
	}

	@Override
	public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		world.getBlockTickScheduler().schedule(pos, this, 8);

		if (!state.get(ENABLED) || state.get(BOTTOM)) {
			return;
		}

		Inventory inputInventory = getInputInventory(world, pos);

		if (inputInventory != null) {
			if (!isInventoryEmpty(inputInventory, Direction.UP)) {

					int slot = -1;
					for (int i : getAvailableSlots(inputInventory, Direction.DOWN).toArray()) {
						ItemStack itemStack = inputInventory.getStack(i);
						if (!itemStack.isEmpty()) {
							slot = i;
							break;
						}

					}

					transportItemFromInventory(world, pos, inputInventory, slot, itemStack -> {});

			}
		}

	}

	public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
		world.getBlockTickScheduler().schedule(pos, this, 8);
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
		BlockState state1 = state;

		Block topBlock = world.getBlockState(pos.up()).getBlock();
		if (topBlock instanceof CopperPipeConnectable && ((CopperPipeConnectable) topBlock).canConnectBottom()) {
			state1 = state1.with(TOP, true);
		} else {
			state1 = state1.with(TOP, false);
		}
		Block bottomBlock = world.getBlockState(pos.down()).getBlock();
		if (bottomBlock instanceof CopperPipeConnectable && ((CopperPipeConnectable) bottomBlock).canConnectTop()) {
			state1 = state1.with(BOTTOM, true);
		} else {
			state1 = state1.with(BOTTOM, false);
		}

		boolean bl = !world.isReceivingRedstonePower(pos);
		if (bl != state.get(ENABLED)) {
			state1 = state1.with(ENABLED, bl);
		}

		return state1;
	}

	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(BOTTOM, TOP, ENABLED, WATERLOGGED);
	}

	public boolean canPathfindThrough(BlockState state, BlockView world, BlockPos pos, NavigationType type) {
		return false;
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		Boolean bottom = state.get(BOTTOM);
		Boolean top = state.get(TOP);

		if (bottom) {
			if (top) {
				return NONE_SHAPE;
			} else {
				return TOP_SHAPE;
			}
		} else {
			if (top) {
				return BOTTOM_SHAPE;
			} else {
				return BOTH_SHAPE;
			}
		}
	}

	@Nullable
	private static Inventory getInputInventory(World world, BlockPos pos) {
		return getInventoryAt(world, pos.down());
	}

	@Nullable
	public static Inventory getInventoryAt(World world, BlockPos pos) {
		return getInventoryAt(world, (double)pos.getX() + 0.5D, (double)pos.getY() + 0.5D, (double)pos.getZ() + 0.5D);
	}

	@Nullable
	private static Inventory getInventoryAt(World world, double x, double y, double z) {
		Inventory inventory = null;
		BlockPos blockPos = new BlockPos(x, y, z);
		BlockState blockState = world.getBlockState(blockPos);
		Block block = blockState.getBlock();
		if (block instanceof InventoryProvider) {
			inventory = ((InventoryProvider)block).getInventory(blockState, world, blockPos);
		} else if (blockState.hasBlockEntity()) {
			BlockEntity blockEntity = world.getBlockEntity(blockPos);
			if (blockEntity instanceof Inventory) {
				inventory = (Inventory)blockEntity;
				if (inventory instanceof ChestBlockEntity && block instanceof ChestBlock) {
					inventory = ChestBlock.getInventory((ChestBlock)block, blockState, world, blockPos, true);
				}
			}
		}

		if (inventory == null) {
			List<Entity> list = world.getOtherEntities(null, new Box(x - 0.5D, y - 0.5D, z - 0.5D, x + 0.5D, y + 0.5D, z + 0.5D), EntityPredicates.VALID_INVENTORIES);
			if (!list.isEmpty()) {
				inventory = (Inventory)list.get(world.random.nextInt(list.size()));
			}
		}

		return inventory;
	}

	private static IntStream getAvailableSlots(Inventory inventory, Direction side) {
		return inventory instanceof SidedInventory ? IntStream.of(((SidedInventory)inventory).getAvailableSlots(side)) : IntStream.range(0, inventory.size());
	}

	private static boolean isInventoryFull(Inventory inventory, Direction direction) {
		return getAvailableSlots(inventory, direction).allMatch((i) -> {
			ItemStack itemStack = inventory.getStack(i);
			return itemStack.getCount() >= itemStack.getMaxCount();
		});
	}

	private static boolean isInventoryEmpty(Inventory inv, Direction facing) {
		return getAvailableSlots(inv, facing).allMatch((i) -> {
			return inv.getStack(i).isEmpty();
		});
	}

	public static void transportItemFromInventory(World world, BlockPos pos, Inventory inputInventory, int slot, Consumer<ItemStack> result) {

		transportItem(world, pos.up(), inputInventory.getStack(slot), (out, inventory) -> {

			if (inventory != null) {
				result.accept(HopperBlockEntity.transfer(inputInventory, inventory, inputInventory.removeStack(slot, 1), Direction.DOWN));
			}

		});

	}

	public static void transportItem(World world, BlockPos pos, ItemStack itemStack, Consumer<ItemStack> result) {

		transportItem(world, pos.up(), itemStack, (out, inventory) -> {

			if (inventory != null) {
				result.accept(HopperBlockEntity.transfer(null, inventory, itemStack, Direction.DOWN));
			}

		});

	}

	public static void transportItem(World world, BlockPos pos, ItemStack itemStack, BiConsumer<BlockPos, Inventory> pipeOutput) {
		BlockState state = world.getBlockState(pos);
		Block block = state.getBlock();
		if (block instanceof CopperPipeConnectable connectable && ((CopperPipeConnectable) block).canConnectBottom()) {
			if (state.getEntries().containsKey(Properties.ENABLED) && !state.get(Properties.ENABLED)) {
				return;
			}

			if (connectable.isEnabled(world, pos, state)) {
				if (connectable.onItemPass(world, pos, state, itemStack)) {
					transportItem(world, pos.up(), itemStack, pipeOutput);
				}
			}
		} else {
			Inventory outputInventory = getInventoryAt(world, pos);

			if (outputInventory != null) {
				pipeOutput.accept(pos, outputInventory);
			}

		}

	}

	static {
		BOTTOM = Properties.BOTTOM;
		TOP = BooleanProperty.of("top");
		ENABLED = Properties.ENABLED;
		WATERLOGGED = Properties.WATERLOGGED;

		VoxelShape bottomPiece = Block.createCuboidShape(0, 0, 0, 16, 3, 16);
		VoxelShape topPiece = Block.createCuboidShape(0, 13, 0, 16, 16, 16);

		NONE_SHAPE = Block.createCuboidShape(2, 0, 2, 14, 16, 14);
		BOTH_SHAPE = VoxelShapes.union(NONE_SHAPE, bottomPiece, topPiece);
		BOTTOM_SHAPE = VoxelShapes.union(NONE_SHAPE, bottomPiece);
		TOP_SHAPE = VoxelShapes.union(NONE_SHAPE, topPiece);
	}

}
