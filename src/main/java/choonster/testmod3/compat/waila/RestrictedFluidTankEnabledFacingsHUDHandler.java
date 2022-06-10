package choonster.testmod3.compat.waila;

import choonster.testmod3.TestMod3;
import choonster.testmod3.text.TestMod3Lang;
import choonster.testmod3.world.level.block.RestrictedFluidTankBlock;
import choonster.testmod3.world.level.block.entity.RestrictedFluidTankBlockEntity;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.TooltipPosition;
import snownee.jade.api.config.IPluginConfig;

/**
 * Adds a line to the Waila tooltip body displaying the enabled facings of a {@link RestrictedFluidTankBlockEntity}.
 *
 * @author Choonster
 */
public class RestrictedFluidTankEnabledFacingsHUDHandler implements IBlockComponentProvider {
	private final ResourceLocation uid = new ResourceLocation(TestMod3.MODID, "restricted_fluid_tank_enabled_facings");

	@Override
	public ResourceLocation getUid() {
		return uid;
	}

	@Override
	public int getDefaultPriority() {
		return TooltipPosition.BODY;
	}

	@Override
	public void appendTooltip(final ITooltip tooltip, final BlockAccessor accessor, final IPluginConfig config) {
		final BlockEntity blockEntity = accessor.getBlockEntity();

		if (blockEntity instanceof RestrictedFluidTankBlockEntity) {
			final String enabledFacingsString = ((RestrictedFluidTankBlock) accessor.getBlock())
					.getEnabledFacingsString(accessor.getLevel(), accessor.getPosition());

			tooltip.add(Component.translatable(TestMod3Lang.BLOCK_DESC_FLUID_TANK_RESTRICTED_ENABLED_FACINGS.getTranslationKey(), enabledFacingsString));
		}
	}
}
