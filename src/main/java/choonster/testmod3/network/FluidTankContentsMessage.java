package choonster.testmod3.network;

import choonster.testmod3.block.FluidTankBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.FluidTankProperties;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * Sent by {@link FluidTankBlock} to notify the player of the tank's contents.
 * <p>
 * This is required because the Forge-provided {@link Fluid}s for Water and Lava only override {@link Fluid#getLocalizedName}
 * and not {@link Fluid#getUnlocalizedName}, so sending a {@link TranslationTextComponent} with
 * the translation key doesn't display the correct translation.
 * <p>
 * Can be replaced by an {@link ITextComponent} once this PR is merged:
 * https://github.com/MinecraftForge/MinecraftForge/pull/2948
 *
 * @author Choonster
 */
// TODO: Update/remove when fluids are re-implemented in 1.14
public class FluidTankContentsMessage {
	private final IFluidTankProperties[] fluidTankProperties;

	public FluidTankContentsMessage(final IFluidTankProperties[] fluidTankProperties) {
		this.fluidTankProperties = fluidTankProperties;
	}

	public static FluidTankContentsMessage decode(final PacketBuffer buffer) {
		final int numProperties = buffer.readInt();
		final IFluidTankProperties[] fluidTankProperties = new IFluidTankProperties[numProperties];

		for (int i = 0; i < numProperties; i++) {
			final CompoundNBT tagCompound = buffer.readCompoundTag();
			final FluidStack contents = FluidStack.loadFluidStackFromNBT(tagCompound);

			final int capacity = buffer.readInt();

			fluidTankProperties[i] = new FluidTankProperties(contents, capacity);
		}

		return new FluidTankContentsMessage(fluidTankProperties);
	}

	public static void encode(final FluidTankContentsMessage message, final PacketBuffer buffer) {
		buffer.writeInt(message.fluidTankProperties.length);

		for (final IFluidTankProperties properties : message.fluidTankProperties) {
			final FluidStack contents = properties.getContents();
			final CompoundNBT tagCompound = new CompoundNBT();

			if (contents != null) {
				contents.writeToNBT(tagCompound);
			}

			buffer.writeCompoundTag(tagCompound);

			buffer.writeInt(properties.getCapacity());
		}
	}

	public static void handle(final FluidTankContentsMessage message, final Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> {
			final PlayerEntity player = Minecraft.getInstance().player;

			FluidTankBlock.getFluidDataForDisplay(message.fluidTankProperties).forEach(player::sendMessage);
		}));

		ctx.get().setPacketHandled(true);
	}
}
