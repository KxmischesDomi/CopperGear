package de.kxmischesdomi.coppergear.common.registry;

import de.kxmischesdomi.coppergear.TemplateMod;
import de.kxmischesdomi.coppergear.common.blocks.CopperGearBlock;
import de.kxmischesdomi.coppergear.common.blocks.CopperPipeBlock;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.block.AbstractBlock.Settings;
import net.minecraft.block.Block;
import net.minecraft.block.MapColor;
import net.minecraft.block.Material;
import net.minecraft.block.Oxidizable.OxidizationLevel;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public class ModBlocks {

	public static Block COPPER_GEAR = register("copper_gear", new CopperGearBlock(Settings.of(Material.DECORATION).noCollision().breakInstantly().nonOpaque()));
	public static Block COPPER_PIPE = register("copper_pipe", new CopperPipeBlock(OxidizationLevel.UNAFFECTED, FabricBlockSettings.of(Material.METAL, MapColor.STONE_GRAY).breakByTool(FabricToolTags.PICKAXES).requiresTool().strength(3.0F, 4.8F).sounds(BlockSoundGroup.METAL).nonOpaque()));
	public static Block EXPOSED_COPPER_PIPE = register("exposed_copper_pipe", new CopperPipeBlock(OxidizationLevel.EXPOSED, FabricBlockSettings.of(Material.METAL, MapColor.STONE_GRAY).breakByTool(FabricToolTags.PICKAXES).requiresTool().strength(3.0F, 4.8F).sounds(BlockSoundGroup.METAL).nonOpaque()));
	public static Block WEATHERED_COPPER_PIPE = register("weathered_copper_pipe", new CopperPipeBlock(OxidizationLevel.WEATHERED, FabricBlockSettings.of(Material.METAL, MapColor.STONE_GRAY).breakByTool(FabricToolTags.PICKAXES).requiresTool().strength(3.0F, 4.8F).sounds(BlockSoundGroup.METAL).nonOpaque()));
	public static Block OXIDIZED_COPPER_PIPE = register("oxidized_copper_pipe", new CopperPipeBlock(OxidizationLevel.OXIDIZED, FabricBlockSettings.of(Material.METAL, MapColor.STONE_GRAY).breakByTool(FabricToolTags.PICKAXES).requiresTool().strength(3.0F, 4.8F).sounds(BlockSoundGroup.METAL).nonOpaque()));
	public static Block WAXED_COPPER_PIPE = register("waxed_copper_pipe", new CopperPipeBlock(null, FabricBlockSettings.of(Material.METAL, MapColor.STONE_GRAY).breakByTool(FabricToolTags.PICKAXES).requiresTool().strength(3.0F, 4.8F).sounds(BlockSoundGroup.METAL).nonOpaque()));
	public static Block WAXED_EXPOSED_COPPER_PIPE = register("waxed_exposed_copper_pipe", new CopperPipeBlock(null, FabricBlockSettings.of(Material.METAL, MapColor.STONE_GRAY).breakByTool(FabricToolTags.PICKAXES).requiresTool().strength(3.0F, 4.8F).sounds(BlockSoundGroup.METAL).nonOpaque()));
	public static Block WAXED_WEATHERED_COPPER_PIPE = register("waxed_weathered_copper_pipe", new CopperPipeBlock(null, FabricBlockSettings.of(Material.METAL, MapColor.STONE_GRAY).breakByTool(FabricToolTags.PICKAXES).requiresTool().strength(3.0F, 4.8F).sounds(BlockSoundGroup.METAL).nonOpaque()));
	public static Block WAXED_OXIDIZED_COPPER_PIPE = register("waxed_oxidized_copper_pipe", new CopperPipeBlock(null, FabricBlockSettings.of(Material.METAL, MapColor.STONE_GRAY).breakByTool(FabricToolTags.PICKAXES).requiresTool().strength(3.0F, 4.8F).sounds(BlockSoundGroup.METAL).nonOpaque()));

	private static <T extends Block> T register(String name, T block) {
		Registry.register(Registry.BLOCK, new Identifier(TemplateMod.MOD_ID, name), block);
		return block;
	}

}
