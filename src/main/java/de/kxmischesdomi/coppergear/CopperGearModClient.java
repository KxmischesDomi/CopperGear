package de.kxmischesdomi.coppergear;

import de.kxmischesdomi.coppergear.common.registry.ModBlocks;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.block.Block;
import net.minecraft.client.render.RenderLayer;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
@Environment(EnvType.CLIENT)
public class CopperGearModClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {

		initCutoutBlocks(
				ModBlocks.COPPER_GEAR,


				ModBlocks.COPPER_DOOR,
				ModBlocks.EXPOSED_COPPER_DOOR,
				ModBlocks.WEATHERED_COPPER_DOOR,
				ModBlocks.OXIDIZED_COPPER_DOOR,

				ModBlocks.WAXED_COPPER_DOOR,
				ModBlocks.WAXED_EXPOSED_COPPER_DOOR,
				ModBlocks.WAXED_WEATHERED_COPPER_DOOR,
				ModBlocks.WAXED_OXIDIZED_COPPER_DOOR,


				ModBlocks.COPPER_TRAPDOOR,
				ModBlocks.EXPOSED_COPPER_TRAPDOOR,
				ModBlocks.WEATHERED_COPPER_TRAPDOOR,
				ModBlocks.OXIDIZED_COPPER_TRAPDOOR,

				ModBlocks.WAXED_COPPER_TRAPDOOR,
				ModBlocks.WAXED_EXPOSED_COPPER_TRAPDOOR,
				ModBlocks.WAXED_WEATHERED_COPPER_TRAPDOOR,
				ModBlocks.WAXED_OXIDIZED_COPPER_TRAPDOOR
		);
	}

	public void initCutoutBlocks(Block... blocks) {
		BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutout(), blocks);
	}

}
