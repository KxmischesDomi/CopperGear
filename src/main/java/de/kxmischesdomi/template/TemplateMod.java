package de.kxmischesdomi.template;

import de.kxmischesdomi.template.common.registry.ModItems;
import net.fabricmc.api.ModInitializer;

public class TemplateMod implements ModInitializer {

	public static final String MOD_ID = "coppergear";

	@Override
	public void onInitialize() {
		ModItems.init();
	}

	private void initializeSpawning() {

	}

}
