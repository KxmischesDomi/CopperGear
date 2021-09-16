package de.kxmischesdomi.template.common.registry;

import de.kxmischesdomi.template.TemplateMod;
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

	public static void init() {}

	public static <T extends Item> T register(String name, T item) {
		Registry.register(Registry.ITEM, new Identifier(TemplateMod.MOD_ID, name), item);
		return item;
	}

}
    