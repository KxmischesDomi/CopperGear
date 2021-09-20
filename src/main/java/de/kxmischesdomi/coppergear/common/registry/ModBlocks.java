package de.kxmischesdomi.coppergear.common.registry;

import de.kxmischesdomi.coppergear.CopperGearMod;
import de.kxmischesdomi.coppergear.common.blocks.*;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.block.AbstractBlock.Settings;
import net.minecraft.block.*;
import net.minecraft.block.Oxidizable.OxidizationLevel;
import net.minecraft.entity.EntityType;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.BlockView;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public class ModBlocks {

	public static Block COPPER_GEAR = register("copper_gear", new CopperGearBlock(Settings.of(Material.DECORATION).noCollision().breakInstantly().nonOpaque()));

	
	// COPPER PIPE
	public static Block COPPER_PIPE = register("copper_pipe", new CopperPipeBlock(OxidizationLevel.UNAFFECTED, false, FabricBlockSettings.of(Material.METAL, MapColor.ORANGE).breakByTool(FabricToolTags.PICKAXES).requiresTool().strength(3.0F, 4.8F).sounds(BlockSoundGroup.METAL).nonOpaque()));
	public static Block EXPOSED_COPPER_PIPE = register("exposed_copper_pipe", new CopperPipeBlock(OxidizationLevel.EXPOSED, false, FabricBlockSettings.of(Material.METAL, MapColor.TERRACOTTA_LIGHT_GRAY).breakByTool(FabricToolTags.PICKAXES).requiresTool().strength(3.0F, 4.8F).sounds(BlockSoundGroup.METAL).nonOpaque()));
	public static Block WEATHERED_COPPER_PIPE = register("weathered_copper_pipe", new CopperPipeBlock(OxidizationLevel.WEATHERED, false, FabricBlockSettings.of(Material.METAL, MapColor.DARK_AQUA).breakByTool(FabricToolTags.PICKAXES).requiresTool().strength(3.0F, 4.8F).sounds(BlockSoundGroup.METAL).nonOpaque()));
	public static Block OXIDIZED_COPPER_PIPE = register("oxidized_copper_pipe", new CopperPipeBlock(OxidizationLevel.OXIDIZED, false, FabricBlockSettings.of(Material.METAL, MapColor.TEAL).breakByTool(FabricToolTags.PICKAXES).requiresTool().strength(3.0F, 4.8F).sounds(BlockSoundGroup.METAL).nonOpaque()));

	// Oxidization Level is OXIDIZED to make other not oxidized block ignore this block.
	public static Block WAXED_COPPER_PIPE = register("waxed_copper_pipe", new CopperPipeBlock(OxidizationLevel.OXIDIZED , true, FabricBlockSettings.of(Material.METAL, MapColor.ORANGE).breakByTool(FabricToolTags.PICKAXES).requiresTool().strength(3.0F, 4.8F).sounds(BlockSoundGroup.METAL).nonOpaque()));
	public static Block WAXED_EXPOSED_COPPER_PIPE = register("waxed_exposed_copper_pipe", new CopperPipeBlock(OxidizationLevel.OXIDIZED, true, FabricBlockSettings.of(Material.METAL, MapColor.TERRACOTTA_LIGHT_GRAY).breakByTool(FabricToolTags.PICKAXES).requiresTool().strength(3.0F, 4.8F).sounds(BlockSoundGroup.METAL).nonOpaque()));
	public static Block WAXED_WEATHERED_COPPER_PIPE = register("waxed_weathered_copper_pipe", new CopperPipeBlock(OxidizationLevel.OXIDIZED, true, FabricBlockSettings.of(Material.METAL, MapColor.DARK_AQUA).breakByTool(FabricToolTags.PICKAXES).requiresTool().strength(3.0F, 4.8F).sounds(BlockSoundGroup.METAL).nonOpaque()));
	public static Block WAXED_OXIDIZED_COPPER_PIPE = register("waxed_oxidized_copper_pipe", new CopperPipeBlock(OxidizationLevel.OXIDIZED, true, FabricBlockSettings.of(Material.METAL, MapColor.TEAL).breakByTool(FabricToolTags.PICKAXES).requiresTool().strength(3.0F, 4.8F).sounds(BlockSoundGroup.METAL).nonOpaque()));

	
	// COPPER VACUUM
	public static Block COPPER_VACUUM = register("copper_vacuum", new CopperVacuumBlock(OxidizationLevel.UNAFFECTED, false, FabricBlockSettings.of(Material.METAL, MapColor.ORANGE).breakByTool(FabricToolTags.PICKAXES).requiresTool().strength(3.0F, 4.8F).sounds(BlockSoundGroup.METAL).nonOpaque()));
	public static Block EXPOSED_COPPER_VACUUM = register("exposed_copper_vacuum", new CopperVacuumBlock(OxidizationLevel.EXPOSED, false, FabricBlockSettings.of(Material.METAL, MapColor.TERRACOTTA_LIGHT_GRAY).breakByTool(FabricToolTags.PICKAXES).requiresTool().strength(3.0F, 4.8F).sounds(BlockSoundGroup.METAL).nonOpaque()));
	public static Block WEATHERED_COPPER_VACUUM = register("weathered_copper_vacuum", new CopperVacuumBlock(OxidizationLevel.WEATHERED, false, FabricBlockSettings.of(Material.METAL, MapColor.DARK_AQUA).breakByTool(FabricToolTags.PICKAXES).requiresTool().strength(3.0F, 4.8F).sounds(BlockSoundGroup.METAL).nonOpaque()));
	public static Block OXIDIZED_COPPER_VACUUM = register("oxidized_copper_vacuum", new CopperVacuumBlock(OxidizationLevel.OXIDIZED, false, FabricBlockSettings.of(Material.METAL, MapColor.TEAL).breakByTool(FabricToolTags.PICKAXES).requiresTool().strength(3.0F, 4.8F).sounds(BlockSoundGroup.METAL).nonOpaque()));

	// Oxidization Level is OXIDIZED to make other not oxidized block ignore this block.
	public static Block WAXED_COPPER_VACUUM = register("waxed_copper_vacuum", new CopperVacuumBlock(OxidizationLevel.OXIDIZED , true, FabricBlockSettings.of(Material.METAL, MapColor.ORANGE).breakByTool(FabricToolTags.PICKAXES).requiresTool().strength(3.0F, 4.8F).sounds(BlockSoundGroup.METAL).nonOpaque()));
	public static Block WAXED_EXPOSED_COPPER_VACUUM = register("waxed_exposed_copper_vacuum", new CopperVacuumBlock(OxidizationLevel.OXIDIZED, true, FabricBlockSettings.of(Material.METAL, MapColor.TERRACOTTA_LIGHT_GRAY).breakByTool(FabricToolTags.PICKAXES).requiresTool().strength(3.0F, 4.8F).sounds(BlockSoundGroup.METAL).nonOpaque()));
	public static Block WAXED_WEATHERED_COPPER_VACUUM = register("waxed_weathered_copper_vacuum", new CopperVacuumBlock(OxidizationLevel.OXIDIZED, true, FabricBlockSettings.of(Material.METAL, MapColor.DARK_AQUA).breakByTool(FabricToolTags.PICKAXES).requiresTool().strength(3.0F, 4.8F).sounds(BlockSoundGroup.METAL).nonOpaque()));
	public static Block WAXED_OXIDIZED_COPPER_VACUUM = register("waxed_oxidized_copper_vacuum", new CopperVacuumBlock(OxidizationLevel.OXIDIZED, true, FabricBlockSettings.of(Material.METAL, MapColor.TEAL).breakByTool(FabricToolTags.PICKAXES).requiresTool().strength(3.0F, 4.8F).sounds(BlockSoundGroup.METAL).nonOpaque()));

	
	// COPPER DOOR
	public static Block COPPER_DOOR = register("copper_door", new OxidizableDoorBlock(OxidizationLevel.UNAFFECTED, false, FabricBlockSettings.of(Material.METAL, MapColor.ORANGE).breakByTool(FabricToolTags.PICKAXES).requiresTool().strength(5.0F).sounds(BlockSoundGroup.METAL).nonOpaque()));
	public static Block EXPOSED_COPPER_DOOR = register("exposed_copper_door", new OxidizableDoorBlock(OxidizationLevel.EXPOSED, false, FabricBlockSettings.of(Material.METAL, MapColor.TERRACOTTA_LIGHT_GRAY).breakByTool(FabricToolTags.PICKAXES).requiresTool().strength(5.0F).sounds(BlockSoundGroup.METAL).nonOpaque()));
	public static Block WEATHERED_COPPER_DOOR = register("weathered_copper_door", new OxidizableDoorBlock(OxidizationLevel.WEATHERED, false, FabricBlockSettings.of(Material.METAL, MapColor.DARK_AQUA).breakByTool(FabricToolTags.PICKAXES).requiresTool().strength(5.0F).sounds(BlockSoundGroup.METAL).nonOpaque()));
	public static Block OXIDIZED_COPPER_DOOR = register("oxidized_copper_door", new OxidizableDoorBlock(OxidizationLevel.OXIDIZED, false, FabricBlockSettings.of(Material.METAL, MapColor.TEAL).breakByTool(FabricToolTags.PICKAXES).requiresTool().strength(5.0F).sounds(BlockSoundGroup.METAL).nonOpaque()));

	// Oxidization Level is OXIDIZED to make other not oxidized block ignore this block.
	public static Block WAXED_COPPER_DOOR = register("waxed_copper_door", new OxidizableDoorBlock(OxidizationLevel.OXIDIZED , true, FabricBlockSettings.of(Material.METAL, MapColor.ORANGE).breakByTool(FabricToolTags.PICKAXES).requiresTool().strength(5.0F).sounds(BlockSoundGroup.METAL).nonOpaque()));
	public static Block WAXED_EXPOSED_COPPER_DOOR = register("waxed_exposed_copper_door", new OxidizableDoorBlock(OxidizationLevel.OXIDIZED, true, FabricBlockSettings.of(Material.METAL, MapColor.TERRACOTTA_LIGHT_GRAY).breakByTool(FabricToolTags.PICKAXES).requiresTool().strength(5.0F).sounds(BlockSoundGroup.METAL).nonOpaque()));
	public static Block WAXED_WEATHERED_COPPER_DOOR = register("waxed_weathered_copper_door", new OxidizableDoorBlock(OxidizationLevel.OXIDIZED, true, FabricBlockSettings.of(Material.METAL, MapColor.DARK_AQUA).breakByTool(FabricToolTags.PICKAXES).requiresTool().strength(5.0F).sounds(BlockSoundGroup.METAL).nonOpaque()));
	public static Block WAXED_OXIDIZED_COPPER_DOOR = register("waxed_oxidized_copper_door", new OxidizableDoorBlock(OxidizationLevel.OXIDIZED, true, FabricBlockSettings.of(Material.METAL, MapColor.TEAL).breakByTool(FabricToolTags.PICKAXES).requiresTool().strength(5.0F).sounds(BlockSoundGroup.METAL).nonOpaque()));
	
	
	// COPPER TRAPDOOR
	public static Block COPPER_TRAPDOOR = register("copper_trapdoor", new OxidizableTrapdoorBlock(OxidizationLevel.UNAFFECTED, false, FabricBlockSettings.of(Material.METAL, MapColor.ORANGE).breakByTool(FabricToolTags.PICKAXES).requiresTool().strength(5.0F).sounds(BlockSoundGroup.METAL).nonOpaque().allowsSpawning(ModBlocks::never)));
	public static Block EXPOSED_COPPER_TRAPDOOR = register("exposed_copper_trapdoor", new OxidizableTrapdoorBlock(OxidizationLevel.EXPOSED, false, FabricBlockSettings.of(Material.METAL, MapColor.TERRACOTTA_LIGHT_GRAY).breakByTool(FabricToolTags.PICKAXES).requiresTool().strength(5.0F).sounds(BlockSoundGroup.METAL).nonOpaque().allowsSpawning(ModBlocks::never)));
	public static Block WEATHERED_COPPER_TRAPDOOR = register("weathered_copper_trapdoor", new OxidizableTrapdoorBlock(OxidizationLevel.WEATHERED, false, FabricBlockSettings.of(Material.METAL, MapColor.DARK_AQUA).breakByTool(FabricToolTags.PICKAXES).requiresTool().strength(5.0F).sounds(BlockSoundGroup.METAL).nonOpaque().allowsSpawning(ModBlocks::never)));
	public static Block OXIDIZED_COPPER_TRAPDOOR = register("oxidized_copper_trapdoor", new OxidizableTrapdoorBlock(OxidizationLevel.OXIDIZED, false, FabricBlockSettings.of(Material.METAL, MapColor.TEAL).breakByTool(FabricToolTags.PICKAXES).requiresTool().strength(5.0F).sounds(BlockSoundGroup.METAL).nonOpaque().allowsSpawning(ModBlocks::never)));

	// Oxidization Level is OXIDIZED to make other not oxidized block ignore this block.
	public static Block WAXED_COPPER_TRAPDOOR = register("waxed_copper_trapdoor", new OxidizableTrapdoorBlock(OxidizationLevel.OXIDIZED , true, FabricBlockSettings.of(Material.METAL, MapColor.ORANGE).breakByTool(FabricToolTags.PICKAXES).requiresTool().strength(5.0F).sounds(BlockSoundGroup.METAL).nonOpaque().allowsSpawning(ModBlocks::never)));
	public static Block WAXED_EXPOSED_COPPER_TRAPDOOR = register("waxed_exposed_copper_trapdoor", new OxidizableTrapdoorBlock(OxidizationLevel.OXIDIZED, true, FabricBlockSettings.of(Material.METAL, MapColor.TERRACOTTA_LIGHT_GRAY).breakByTool(FabricToolTags.PICKAXES).requiresTool().strength(5.0F).sounds(BlockSoundGroup.METAL).nonOpaque().allowsSpawning(ModBlocks::never)));
	public static Block WAXED_WEATHERED_COPPER_TRAPDOOR = register("waxed_weathered_copper_trapdoor", new OxidizableTrapdoorBlock(OxidizationLevel.OXIDIZED, true, FabricBlockSettings.of(Material.METAL, MapColor.DARK_AQUA).breakByTool(FabricToolTags.PICKAXES).requiresTool().strength(5.0F).sounds(BlockSoundGroup.METAL).nonOpaque().allowsSpawning(ModBlocks::never)));
	public static Block WAXED_OXIDIZED_COPPER_TRAPDOOR = register("waxed_oxidized_copper_trapdoor", new OxidizableTrapdoorBlock(OxidizationLevel.OXIDIZED, true, FabricBlockSettings.of(Material.METAL, MapColor.TEAL).breakByTool(FabricToolTags.PICKAXES).requiresTool().strength(5.0F).sounds(BlockSoundGroup.METAL).nonOpaque().allowsSpawning(ModBlocks::never)));


	private static <T extends Block> T register(String name, T block) {
		Registry.register(Registry.BLOCK, new Identifier(CopperGearMod.MOD_ID, name), block);
		return block;
	}

	/**
	 * A shortcut to always return {@code false} in a typed context predicate with an
	 * {@link EntityType}, used like {@code settings.allowSpawning(Blocks::never)}.
	 */
	private static Boolean never(BlockState state, BlockView world, BlockPos pos, EntityType<?> type) {
		return false;
	}

}
