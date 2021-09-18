package de.kxmischesdomi.coppergear.common.registry;

import de.kxmischesdomi.coppergear.TemplateMod;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.Item.Settings;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public class ModItems {

	public static Item COPPER_GEAR_ITEM = register("copper_gear", new BlockItem(ModBlocks.COPPER_GEAR, new Settings().group(ItemGroup.REDSTONE)));
	public static Item COPPER_PIPE_ITEM = register("copper_pipe", new BlockItem(ModBlocks.COPPER_PIPE, new Settings().group(ItemGroup.REDSTONE)));
	public static Item EXPOSED_COPPER_PIPE_ITEM = register("exposed_copper_pipe", new BlockItem(ModBlocks.EXPOSED_COPPER_PIPE, new Settings().group(ItemGroup.REDSTONE)));
	public static Item WEATHERED_COPPER_PIPE_ITEM = register("weathered_copper_pipe", new BlockItem(ModBlocks.WEATHERED_COPPER_PIPE, new Settings().group(ItemGroup.REDSTONE)));
	public static Item OXIDIZED_COPPER_PIPE_ITEM = register("oxidized_copper_pipe", new BlockItem(ModBlocks.OXIDIZED_COPPER_PIPE, new Settings().group(ItemGroup.REDSTONE)));
	public static Item WAXED_COPPER_PIPE_ITEM = register("waxed_copper_pipe", new BlockItem(ModBlocks.WAXED_COPPER_PIPE, new Settings().group(ItemGroup.REDSTONE)));
	public static Item WAXED_EXPOSED_COPPER_PIPE_ITEM = register("waxed_exposed_copper_pipe", new BlockItem(ModBlocks.WAXED_EXPOSED_COPPER_PIPE, new Settings().group(ItemGroup.REDSTONE)));
	public static Item WAXED_WEATHERED_COPPER_PIPE_ITEM = register("waxed_weathered_copper_pipe", new BlockItem(ModBlocks.WAXED_WEATHERED_COPPER_PIPE, new Settings().group(ItemGroup.REDSTONE)));
	public static Item WAXED_OXIDIZED_COPPER_PIPE_ITEM = register("waxed_oxidized_copper_pipe", new BlockItem(ModBlocks.WAXED_OXIDIZED_COPPER_PIPE, new Settings().group(ItemGroup.REDSTONE)));

	public static void init() {}

	public static <T extends Item> T register(String name, T item) {
		Registry.register(Registry.ITEM, new Identifier(TemplateMod.MOD_ID, name), item);
		return item;
	}

}
    