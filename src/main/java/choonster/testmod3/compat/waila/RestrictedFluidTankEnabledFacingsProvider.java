package choonster.testmod3.compat.waila;

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
public class RestrictedFluidTankEnabledFacingsProvider implements IBlockComponentProvider {
	private final ResourceLocation uid;

	public RestrictedFluidTankEnabledFacingsProvider(final ResourceLocation uid) {
		this.uid = uid;
	}

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
