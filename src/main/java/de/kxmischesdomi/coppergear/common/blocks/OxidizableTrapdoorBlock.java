package de.kxmischesdomi.coppergear.common.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.block.TrapdoorBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

import java.util.Random;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public class OxidizableTrapdoorBlock extends TrapdoorBlock implements CopperGearOxidizable {

	private final OxidizationLevel oxidizationLevel;
	private final boolean waxed;

	public OxidizableTrapdoorBlock(OxidizationLevel oxidizationLevel, boolean waxed, Settings settings) {
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

}
