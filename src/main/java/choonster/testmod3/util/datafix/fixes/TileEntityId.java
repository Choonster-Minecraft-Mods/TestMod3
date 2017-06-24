package choonster.testmod3.util.datafix.fixes;

import choonster.testmod3.TestMod3;
import com.google.common.collect.ImmutableMap;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.datafix.IFixableData;

import java.util.Map;

/**
 * Remap mixed-case {@link TileEntity} IDs from 1.10.x and earlier to lower-case IDs from 1.11.x+.
 *
 * @author Choonster
 */
public class TileEntityId implements IFixableData {
	private static final Map<String, String> OLD_TO_NEW_ID_MAP = new ImmutableMap.Builder<String, String>()
			.put("SurvivalCommandBlock", "survival_command_block")
			.put("FluidTank", "fluid_tank")
			.put("ColoredRotatable", "colored_rotatable")
			.put("ColoredMultiRotatable", "colored_multi_rotatable")
			.put("PotionEffect", "potion_effect")
			.put("ModChest", "mod_chest")
			.build();


	@Override
	public int getFixVersion() {
		return 1;
	}

	@Override
	public NBTTagCompound fixTagCompound(final NBTTagCompound compound) {
		final ResourceLocation oldID = new ResourceLocation(compound.getString("id"));

		if (oldID.getResourceDomain().equals(TestMod3.MODID)) {
			final String newID = OLD_TO_NEW_ID_MAP.get(oldID.getResourcePath());

			if (newID != null) {
				compound.setString("id", new ResourceLocation(TestMod3.MODID, newID).toString());
			}
		}

		return compound;
	}
}
