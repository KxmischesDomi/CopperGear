package de.kxmischesdomi.coppergear.common.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Indicates a block that can be connected with the copper pipe
 *
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public interface CopperPipeConnectable {

	default boolean canConnectTop() {
		return true;
	}

	default boolean canConnectBottom() {
		return true;
	}

	default boolean isEnabled(World world, BlockPos pos, BlockState state) {
		return true;
	}

	/**
	 * @return if the item should pass this pipe
	 */
	default boolean onItemPass(World world, BlockPos blockPos, BlockState state, ItemStack itemStack) {
		return true;
	}

}
