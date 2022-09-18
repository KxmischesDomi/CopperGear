package de.kxmischesdomi.coppergear;

import de.kxmischesdomi.coppergear.common.registry.ModBlocks;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.block.Block;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.shape.VoxelShapes;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
@Environment(EnvType.CLIENT)
public class CopperGearModClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {

		for (Block block : ModBlocks.BLOCKS) {
			if (!block.getOutlineShape(block.getDefaultState(), null, null, null).equals(VoxelShapes.fullCube())) {
				initCutoutBlocks(block);
			}
		}

	}

	public void initCutoutBlocks(Block... blocks) {
		BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutout(), blocks);
	}

}
