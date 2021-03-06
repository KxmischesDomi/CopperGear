package de.kxmischesdomi.coppergear.common.registry;

import de.kxmischesdomi.coppergear.CopperGearMod;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public class ModEntities {

	private static <T extends Entity> EntityType<T> register(String id, FabricEntityTypeBuilder<T> builder) {
		return Registry.register(Registry.ENTITY_TYPE, new Identifier(CopperGearMod.MOD_ID, id), builder.build());
	}

}
