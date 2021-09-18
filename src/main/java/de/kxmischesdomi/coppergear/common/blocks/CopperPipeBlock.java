package de.kxmischesdomi.coppergear.common.blocks;

import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.block.entity.HopperBlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.pathing.NavigationType;
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
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public class CopperPipeBlock extends Block implements CopperGearOxidizable {

	public static final BooleanProperty BOTTOM;
	public static final BooleanProperty TOP;
	public static final BooleanProperty ENABLED;
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
		this.setDefaultState((this.stateManager.getDefaultState()).with(BOTTOM, false).with(TOP, false).with(ENABLED, true));
	}

	@Override
	public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		this.tickDegradation(state, world, pos, random);
		super.randomTick(state, world, pos, random);
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
	public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
		this.updateState(world, pos, state);
		world.getBlockTickScheduler().schedule(pos, this, 8);
	}

	@Override
	public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		world.getBlockTickScheduler().schedule(pos, this, 8);

		if (!state.get(ENABLED)) {
			return;
		}

		Inventory inputInventory = getInputInventory(world, pos);

		if (inputInventory != null) {
			if (!isInventoryEmpty(inputInventory, Direction.UP)) {
//				Inventory outputInventory = getOutputInventory(world, pos);
//				if ((outputInventory != null && !isInventoryFull(outputInventory, Direction.DOWN)) || world.getBlockState(pos.up()).isOf(ModBlocks.COPPER_PIPE)) {

					ItemStack stack = ItemStack.EMPTY;
					int slot = -1;
					for (int i : getAvailableSlots(inputInventory, Direction.DOWN).toArray()) {
						ItemStack itemStack = inputInventory.getStack(i);
						if (!itemStack.isEmpty()) {
							stack = itemStack;
							slot = i;
							break;
						}

					}

					if (!stack.isEmpty()) {
						transferItem(world, pos, inputInventory, slot, stack);
					}

//				}

			}
		}

	}

	public void transferItem(World world, BlockPos pos, Inventory inputInventory, int slot, ItemStack stack) {
		Inventory outputInventory = getOutputInventory(world, pos);

		if (outputInventory != null) {
			HopperBlockEntity.transfer(inputInventory, outputInventory, inputInventory.removeStack(slot, 1), Direction.DOWN);
		} else if (world.getBlockState(pos).getBlock() instanceof CopperPipeBlock) {
			transferItem(world, pos.up(), inputInventory, slot, stack);
		} else {
//			stack.decrement(1);
//			ItemStack copy = stack.copy();
//			copy.setCount(1);
//			ItemEntity itemEntity = new ItemEntity(world, pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, copy);
//			world.spawnEntity(itemEntity);
		}

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
		BlockState state1 = state;
		if (world.getBlockState(pos.up()).getBlock() instanceof CopperPipeBlock) {
			state1 = state1.with(TOP, true);
		} else {
			state1 = state1.with(TOP, false);
		}
		if (world.getBlockState(pos.down()).getBlock() instanceof CopperPipeBlock) {
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
		builder.add(BOTTOM, TOP, ENABLED);
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

	private static boolean canInsert(Inventory inventory, ItemStack stack, int slot, @Nullable Direction side) {
		if (!inventory.isValid(slot, stack)) {
			return false;
		} else {
			return !(inventory instanceof SidedInventory) || ((SidedInventory)inventory).canInsert(slot, stack, side);
		}
	}

	@Nullable
	private static Inventory getOutputInventory(World world, BlockPos pos) {
		return getInventoryAt(world, pos.up());
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

	static {
		BOTTOM = Properties.BOTTOM;
		TOP = BooleanProperty.of("top");
		ENABLED = Properties.ENABLED;

		VoxelShape bottomPiece = VoxelShapes.cuboid(0, 0, 0, 1, 0.1875, 1);
		VoxelShape topPiece = VoxelShapes.cuboid(0, 0.8125, 0, 1, 1, 1);

		NONE_SHAPE = VoxelShapes.cuboid(0.125, 0, 0.125, 0.875, 1, 0.875);
		BOTH_SHAPE = VoxelShapes.union(NONE_SHAPE, bottomPiece, topPiece);
		BOTTOM_SHAPE = VoxelShapes.union(NONE_SHAPE, bottomPiece);
		TOP_SHAPE = VoxelShapes.union(NONE_SHAPE, topPiece);
	}

}
