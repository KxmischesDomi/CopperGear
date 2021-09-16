package de.kxmischesdomi.template.common.registry;

import de.kxmischesdomi.template.TemplateMod;
import de.kxmischesdomi.template.common.blocks.CopperGearBlock;
import de.kxmischesdomi.template.common.blocks.CopperPipeBlock;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.block.AbstractBlock.Settings;
import net.minecraft.block.Block;
import net.minecraft.block.MapColor;
import net.minecraft.block.Material;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public class ModBlocks {

	public static Block COPPER_GEAR = register("copper_gear", new CopperGearBlock(Settings.of(Material.DECORATION).breakInstantly().nonOpaque()));
	public static Block COPPER_PIPE = register("copper_pipe", new CopperPipeBlock(FabricBlockSettings.of(Material.METAL, MapColor.STONE_GRAY).breakByTool(FabricToolTags.PICKAXES).requiresTool().strength(3.0F, 4.8F).sounds(BlockSoundGroup.METAL).nonOpaque()));

	private static <T extends Block> T register(String name, T block) {
		Registry.register(Registry.BLOCK, new Identifier(TemplateMod.MOD_ID, name), block);
		return block;
	}

}
