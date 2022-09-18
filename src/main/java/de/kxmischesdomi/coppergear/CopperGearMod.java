package de.kxmischesdomi.coppergear;

import de.kxmischesdomi.coppergear.common.registry.ModBlocks;
import de.kxmischesdomi.coppergear.common.registry.ModItems;
import net.fabricmc.api.ModInitializer;

public class CopperGearMod implements ModInitializer {

	public static final String MOD_ID = "coppergear";

	@Override
	public void onInitialize() {
		ModBlocks.init();
		ModItems.init();
	}

	private void initializeSpawning() {

	}

}
