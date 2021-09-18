package de.kxmischesdomi.coppergear.common.blocks;

import com.google.common.base.Suppliers;
import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import de.kxmischesdomi.coppergear.common.registry.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Oxidizable;

import java.util.Optional;
import java.util.function.Supplier;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public interface CopperGearOxidizable extends Oxidizable {

	Supplier<BiMap<Block, Block>> OXIDATION_LEVEL_INCREASES = Suppliers.memoize(() -> {
		return ImmutableBiMap.<Block, Block>builder()
				.put(ModBlocks.COPPER_PIPE, ModBlocks.EXPOSED_COPPER_PIPE)
				.put(ModBlocks.EXPOSED_COPPER_PIPE, ModBlocks.WEATHERED_COPPER_PIPE)
				.put(ModBlocks.WEATHERED_COPPER_PIPE, ModBlocks.OXIDIZED_COPPER_PIPE)
				.build();
	});
	Supplier<BiMap<Block, Block>> OXIDATION_LEVEL_DECREASES = Suppliers.memoize(() -> {
		return ((BiMap)OXIDATION_LEVEL_INCREASES.get()).inverse();
	});

	Supplier<BiMap<Block, Block>> UNWAXED_TO_WAXED_BLOCKS = Suppliers.memoize(() -> {
		return ImmutableBiMap.<Block, Block>builder().put(ModBlocks.COPPER_PIPE, ModBlocks.WAXED_COPPER_PIPE).put(ModBlocks.EXPOSED_COPPER_PIPE, ModBlocks.WAXED_EXPOSED_COPPER_PIPE).put(ModBlocks.WEATHERED_COPPER_PIPE, ModBlocks.WAXED_WEATHERED_COPPER_PIPE).put(ModBlocks.OXIDIZED_COPPER_PIPE, ModBlocks.WAXED_OXIDIZED_COPPER_PIPE).build();
	});
	Supplier<BiMap<Block, Block>> WAXED_TO_UNWAXED_BLOCKS = Suppliers.memoize(() -> {
		return ((BiMap)UNWAXED_TO_WAXED_BLOCKS.get()).inverse();
	});

	static Optional<Block> getDecreasedOxidationBlock(Block block) {
		return Optional.ofNullable((Block)((BiMap)OXIDATION_LEVEL_DECREASES.get()).get(block));
	}

	static Block getUnaffectedOxidationBlock(Block block) {
		Block block2 = block;

		for(Block block3 = (Block)((BiMap)OXIDATION_LEVEL_DECREASES.get()).get(block); block3 != null; block3 = (Block)((BiMap)OXIDATION_LEVEL_DECREASES.get()).get(block3)) {
			block2 = block3;
		}

		return block2;
	}

	static Optional<BlockState> getDecreasedOxidationState(BlockState state) {
		return getDecreasedOxidationBlock(state.getBlock()).map((block) -> {
			return block.getStateWithProperties(state);
		});
	}

	static Optional<Block> getIncreasedOxidationBlock(Block block) {
		return Optional.ofNullable((Block)((BiMap)OXIDATION_LEVEL_INCREASES.get()).get(block));
	}

	static BlockState getUnaffectedOxidationState(BlockState state) {
		return getUnaffectedOxidationBlock(state.getBlock()).getStateWithProperties(state);
	}

	default Optional<BlockState> getDegradationResult(BlockState state) {
		return getIncreasedOxidationBlock(state.getBlock()).map((block) -> {
			return block.getStateWithProperties(state);
		});
	}


}
