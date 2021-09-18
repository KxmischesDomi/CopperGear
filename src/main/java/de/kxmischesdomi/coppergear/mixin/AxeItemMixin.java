package de.kxmischesdomi.coppergear.mixin;

import com.google.common.collect.BiMap;
import de.kxmischesdomi.coppergear.common.blocks.CopperGearOxidizable;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldEvents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;
import java.util.function.Consumer;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
@Mixin(AxeItem.class)
public class AxeItemMixin {

	@Inject(method = "useOnBlock", at = @At(value = "HEAD"), cancellable = true)
	private void onUseOnBlock(ItemUsageContext context, CallbackInfoReturnable<ActionResult> cir) {
		World world = context.getWorld();
		BlockPos blockPos = context.getBlockPos();
		PlayerEntity playerEntity = context.getPlayer();
		BlockState blockState = world.getBlockState(blockPos);

		Optional<BlockState> optional2 = CopperGearOxidizable.getDecreasedOxidationState(blockState);
		Optional<BlockState> optional3 = Optional.ofNullable((Block) ((BiMap) CopperGearOxidizable.WAXED_TO_UNWAXED_BLOCKS.get()).get(blockState.getBlock())).map((block) -> {
			return block.getStateWithProperties(blockState);
		});
		ItemStack itemStack = context.getStack();
		Optional<BlockState> optional4 = Optional.empty();

		if (optional2.isPresent()) {
			world.playSound(playerEntity, blockPos, SoundEvents.ITEM_AXE_SCRAPE, SoundCategory.BLOCKS, 1.0F, 1.0F);
			world.syncWorldEvent(playerEntity, WorldEvents.BLOCK_SCRAPED, blockPos, 0);
			optional4 = optional2;
		} else if (optional3.isPresent()) {
			world.playSound(playerEntity, blockPos, SoundEvents.ITEM_AXE_WAX_OFF, SoundCategory.BLOCKS, 1.0F, 1.0F);
			world.syncWorldEvent(playerEntity, WorldEvents.WAX_REMOVED, blockPos, 0);
			optional4 = optional3;
		}

		if (optional4.isPresent()) {
			if (playerEntity instanceof ServerPlayerEntity) {
				Criteria.ITEM_USED_ON_BLOCK.trigger((ServerPlayerEntity) playerEntity, blockPos, itemStack);
			}

			world.setBlockState(blockPos, optional4.get(), Block.NOTIFY_ALL | Block.REDRAW_ON_MAIN_THREAD);
			if (playerEntity != null) {
				itemStack.damage(1, (LivingEntity) playerEntity, (Consumer) ((p) -> {
					playerEntity.sendToolBreakStatus(context.getHand());
				}));
			}

			cir.setReturnValue(ActionResult.success(world.isClient));
		}

	}

}
