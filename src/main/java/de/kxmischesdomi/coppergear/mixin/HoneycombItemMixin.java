package de.kxmischesdomi.coppergear.mixin;

import com.google.common.collect.BiMap;
import de.kxmischesdomi.coppergear.common.blocks.CopperGearOxidizable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.HoneycombItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
@Mixin(HoneycombItem.class)
public class HoneycombItemMixin {

	@Inject(method = "getWaxedState", at = @At("HEAD"), cancellable = true)
	private static void onGetWaxedState(BlockState state, CallbackInfoReturnable<Optional<BlockState>> cir) {
		Optional<BlockState> blockState = Optional.ofNullable((Block) ((BiMap) CopperGearOxidizable.UNWAXED_TO_WAXED_BLOCKS.get()).get(state.getBlock())).map((block) -> {
			return block.getStateWithProperties(state);
		});
		if (blockState.isPresent()) {
			System.out.println("PRESENT : " + blockState);
			cir.setReturnValue(blockState);
		} else {
			System.out.println("NOT PRESENT : " + blockState);
		}

	}

}