package de.kxmischesdomi.coppergear.common.registry;

import de.kxmischesdomi.coppergear.CopperGearMod;
import de.kxmischesdomi.coppergear.common.blocks.*;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.block.*;
import net.minecraft.block.Oxidizable.OxidizationLevel;
import net.minecraft.data.client.model.BlockStateVariantMap;
import net.minecraft.entity.EntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.TallBlockItem;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.BlockView;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public class ModBlocks {

//	public static Block COPPER_GEAR = register("copper_gear", new CopperGearBlock(Settings.of(Material.DECORATION).noCollision().breakInstantly().nonOpaque()));

	public static List<Block> BLOCKS = new LinkedList<>();

	public static void init() {
		registerCopperBlock("pipe", 3, 4.8F, block -> new BlockItem(block, new Item.Settings().group(ModItems.GROUP)), CopperPipeBlock::new);
		registerCopperBlock("vacuum", 3, 4.8F, block -> new BlockItem(block, new Item.Settings().group(ModItems.GROUP)), CopperVacuumBlock::new);
		registerCopperBlock("trapdoor", 5, 5, block -> new BlockItem(block, new Item.Settings().group(ModItems.GROUP)), OxidizableTrapdoorBlock::new);
		registerCopperBlock("door", 5, 5, block -> new TallBlockItem(block, new Item.Settings().group(ModItems.GROUP)), OxidizableDoorBlock::new);
	}

	private static <T extends Block> void registerCopperBlock(String name, float hardness, float resistance, Function<Block, Item> itemFunction, BlockStateVariantMap.TriFunction<OxidizationLevel, Boolean, AbstractBlock.Settings, T> instanceFunction) {
		Block copper = registerWithItem(String.format("copper_%s", name), itemFunction, instanceFunction.apply(OxidizationLevel.UNAFFECTED, false, FabricBlockSettings.of(Material.METAL, MapColor.ORANGE).breakByTool(FabricToolTags.PICKAXES).requiresTool().strength(hardness, resistance).sounds(BlockSoundGroup.COPPER).nonOpaque()));
		Block exposed = registerWithItem(String.format("exposed_copper_%s", name), itemFunction, instanceFunction.apply(OxidizationLevel.EXPOSED, false, FabricBlockSettings.of(Material.METAL, MapColor.TERRACOTTA_LIGHT_GRAY).breakByTool(FabricToolTags.PICKAXES).requiresTool().strength(hardness, resistance).sounds(BlockSoundGroup.COPPER).nonOpaque()));
		Block weathered = registerWithItem(String.format("weathered_copper_%s", name), itemFunction, instanceFunction.apply(OxidizationLevel.WEATHERED, false, FabricBlockSettings.of(Material.METAL, MapColor.DARK_AQUA).breakByTool(FabricToolTags.PICKAXES).requiresTool().strength(hardness, resistance).sounds(BlockSoundGroup.COPPER).nonOpaque()));
		Block oxidized = registerWithItem(String.format("oxidized_copper_%s", name), itemFunction, instanceFunction.apply(OxidizationLevel.OXIDIZED, false, FabricBlockSettings.of(Material.METAL, MapColor.TEAL).breakByTool(FabricToolTags.PICKAXES).requiresTool().strength(hardness, resistance).sounds(BlockSoundGroup.COPPER).nonOpaque()));

		CopperGearOxidizable.OXIDATION_LEVEL_INCREASES.put(copper, exposed);
		CopperGearOxidizable.OXIDATION_LEVEL_INCREASES.put(exposed, weathered);
		CopperGearOxidizable.OXIDATION_LEVEL_INCREASES.put(weathered, oxidized);

		// Oxidization Level is OXIDIZED to make other not oxidized block ignore this block when checking for other not oxidized copper blocks.
		Block waxedCopper = registerWithItem(String.format("waxed_copper_%s", name), itemFunction, instanceFunction.apply(OxidizationLevel.OXIDIZED, true, FabricBlockSettings.of(Material.METAL, MapColor.ORANGE).breakByTool(FabricToolTags.PICKAXES).requiresTool().strength(hardness, resistance).sounds(BlockSoundGroup.COPPER).nonOpaque()));
		Block waxedExposed = registerWithItem(String.format("waxed_exposed_copper_%s", name), itemFunction, instanceFunction.apply(OxidizationLevel.OXIDIZED, true, FabricBlockSettings.of(Material.METAL, MapColor.TERRACOTTA_LIGHT_GRAY).breakByTool(FabricToolTags.PICKAXES).requiresTool().strength(hardness, resistance).sounds(BlockSoundGroup.COPPER).nonOpaque()));
		Block waxedWeathered = registerWithItem(String.format("waxed_weathered_copper_%s", name), itemFunction, instanceFunction.apply(OxidizationLevel.OXIDIZED, true, FabricBlockSettings.of(Material.METAL, MapColor.DARK_AQUA).breakByTool(FabricToolTags.PICKAXES).requiresTool().strength(hardness, resistance).sounds(BlockSoundGroup.COPPER).nonOpaque()));
		Block waxedOxidized = registerWithItem(String.format("waxed_oxidized_copper_%s", name), itemFunction, instanceFunction.apply(OxidizationLevel.OXIDIZED, true, FabricBlockSettings.of(Material.METAL, MapColor.TEAL).breakByTool(FabricToolTags.PICKAXES).requiresTool().strength(hardness, resistance).sounds(BlockSoundGroup.COPPER).nonOpaque()));

		CopperGearOxidizable.UNWAXED_TO_WAXED_BLOCKS.put(copper, waxedCopper);
		CopperGearOxidizable.UNWAXED_TO_WAXED_BLOCKS.put(exposed, waxedExposed);
		CopperGearOxidizable.UNWAXED_TO_WAXED_BLOCKS.put(weathered, waxedWeathered);
		CopperGearOxidizable.UNWAXED_TO_WAXED_BLOCKS.put(oxidized, waxedOxidized);
	}

	private static <T extends Block> T registerWithItem(String name, Function<Block, Item> itemFunction, T block) {
		ModItems.register(name, itemFunction.apply(block));
		return register(name, block);
	}

	private static <T extends Block> T register(String name, T block) {
		Registry.register(Registry.BLOCK, new Identifier(CopperGearMod.MOD_ID, name), block);
		BLOCKS.add(block);
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
