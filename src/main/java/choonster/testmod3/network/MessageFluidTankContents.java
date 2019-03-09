package choonster.testmod3.network;

import choonster.testmod3.block.BlockFluidTank;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.FluidTankProperties;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * Sent by {@link BlockFluidTank} to notify the player of the tank's contents.
 * <p>
 * This is required because the Forge-provided {@link Fluid}s for Water and Lava only override {@link Fluid#getLocalizedName}
 * and not {@link Fluid#getUnlocalizedName}, so sending a {@link TextComponentTranslation} with
 * the translation key doesn't display the correct translation.
 * <p>
 * Can be replaced by an {@link ITextComponent} once this PR is merged:
 * https://github.com/MinecraftForge/MinecraftForge/pull/2948
 *
 * @author Choonster
 */
// TODO: Update/remove when fluids are re-implemented in 1.14
public class MessageFluidTankContents {
	private final IFluidTankProperties[] fluidTankProperties;

	public MessageFluidTankContents(final IFluidTankProperties[] fluidTankProperties) {
		this.fluidTankProperties = fluidTankProperties;
	}

	public static MessageFluidTankContents decode(final PacketBuffer buffer) {
		final int numProperties = buffer.readInt();
		final IFluidTankProperties[] fluidTankProperties = new IFluidTankProperties[numProperties];

		for (int i = 0; i < numProperties; i++) {
			final NBTTagCompound tagCompound = buffer.readCompoundTag();
			final FluidStack contents = FluidStack.loadFluidStackFromNBT(tagCompound);

			final int capacity = buffer.readInt();

			fluidTankProperties[i] = new FluidTankProperties(contents, capacity);
		}

		return new MessageFluidTankContents(fluidTankProperties);
	}

	public static void encode(final MessageFluidTankContents message, final PacketBuffer buffer) {
		buffer.writeInt(message.fluidTankProperties.length);

		for (final IFluidTankProperties properties : message.fluidTankProperties) {
			final FluidStack contents = properties.getContents();
			final NBTTagCompound tagCompound = new NBTTagCompound();

			if (contents != null) {
				contents.writeToNBT(tagCompound);
			}

			buffer.writeCompoundTag(tagCompound);

			buffer.writeInt(properties.getCapacity());
		}
	}

	public static void handle(final MessageFluidTankContents message, final Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> {
			final EntityPlayer player = Minecraft.getInstance().player;

			BlockFluidTank.getFluidDataForDisplay(message.fluidTankProperties).forEach(player::sendMessage);
		}));

		ctx.get().setPacketHandled(true);
	}
}
