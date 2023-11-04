package choonster.testmod3.compat;

import choonster.testmod3.TestMod3;
import net.minecraft.resources.ResourceLocation;

/**
 * IDs for Jade and The One Probe providers.
 *
 * @author Choonster
 */
public enum HudProvider {
	COLORED_ROTATABLE_BLOCK_FACING("colored_rotatable_block_facing"),
	COLORED_MULTI_ROTATABLE_BLOCK_FACE_ROTATION("colored_multi_rotatable_block_face_rotation"),
	ROTATABLE_LAMP_FACING("rotatable_lamp_facing"),
	CHEST_FACING("chest_facing"),
	PLANE_HORIZONTAL_ROTATION("plane_horizontal_rotation"),
	PLANE_VERTICAL_ROTATION("plane_vertical_rotation"),
	RESTRICTED_FLUID_TANK_ENABLED_FACINGS("restricted_fluid_tank_enabled_facings"),

	;

	private final String name;
	private final ResourceLocation id;

	HudProvider(final String name) {
		this.name = name;
		id = new ResourceLocation(TestMod3.MODID, name);
	}

	public String getName() {
		return name;
	}

	public ResourceLocation getId() {
		return id;
	}
}
