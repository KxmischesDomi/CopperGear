package de.kxmischesdomi.coppergear.common.registry;

import de.kxmischesdomi.coppergear.CopperGearMod;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
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

	public static final ItemGroup GROUP = FabricItemGroupBuilder.build(new Identifier(CopperGearMod.MOD_ID, "items"), () -> ModItems.COPPER_GEAR_ITEM.getDefaultStack());

	public static Item COPPER_GEAR_ITEM = register("copper_gear", new Item(new Settings()));
	
	public static void init() {}

	public static <T extends Item> T register(String name, T item) {
		Registry.register(Registry.ITEM, new Identifier(CopperGearMod.MOD_ID, name), item);
		return item;
	}

}
    