package de.kxmischesdomi.coppergear.common.blocks;

import com.google.common.base.Suppliers;
import de.kxmischesdomi.coppergear.utils.Utils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Oxidizable;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.function.Supplier;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public interface CopperGearOxidizable extends Oxidizable {

	Map<Block, Block> OXIDATION_LEVEL_INCREASES = new HashMap<>();

	Supplier<Map<Block, Block>> OXIDATION_LEVEL_DECREASES = () -> Utils.inverseMap(OXIDATION_LEVEL_INCREASES);

	Map<Block, Block> UNWAXED_TO_WAXED_BLOCKS = new HashMap<>();

	Supplier<Map<Block, Block>> WAXED_TO_UNWAXED_BLOCKS = Suppliers.memoize(() -> Utils.inverseMap(UNWAXED_TO_WAXED_BLOCKS));

	static Optional<Block> getDecreasedOxidationBlock(Block block) {
		return Optional.ofNullable(OXIDATION_LEVEL_DECREASES.get().get(block));
	}

	static Block getUnaffectedOxidationBlock(Block block) {
		Block block2 = block;

		for(Block block3 = OXIDATION_LEVEL_DECREASES.get().get(block); block3 != null; block3 = OXIDATION_LEVEL_DECREASES.get().get(block3)) {
			block2 = block3;
		}

		return block2;
	}

	static Optional<BlockState> getDecreasedOxidationState(BlockState state) {
		return getDecreasedOxidationBlock(state.getBlock()).map((block) -> block.getStateWithProperties(state));
	}

	static Optional<Block> getIncreasedOxidationBlock(Block block) {
		return Optional.ofNullable((OXIDATION_LEVEL_INCREASES).get(block));
	}

	static BlockState getUnaffectedOxidationState(BlockState state) {
		return getUnaffectedOxidationBlock(state.getBlock()).getStateWithProperties(state);
	}

	@Override
	default void tickDegradation(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		if (!isWaxed()) {
			Oxidizable.super.tickDegradation(state, world, pos, random);
		}
	}

	default Optional<BlockState> getDegradationResult(BlockState state) {
		return getIncreasedOxidationBlock(state.getBlock()).map((block) -> block.getStateWithProperties(state));
	}

	boolean isWaxed();

}
