package choonster.testmod3.network;

import choonster.testmod3.TestMod3;
import choonster.testmod3.block.BlockFluidTank;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.FluidTankProperties;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * Sent by {@link BlockFluidTank} to notify the player of the tank's contents.
 * <p>
 * This is required because the Forge-provided {@link Fluid}s for Water and Lava only override {@link Fluid#getLocalizedName}
 * and not {@link Fluid#getUnlocalizedName}, so sending a {@link TextComponentTranslation} with
 * the unlocalised name doesn't display the correct translation.
 * <p>
 * Can be replaced by an {@link ITextComponent} once this PR is merged:
 * https://github.com/MinecraftForge/MinecraftForge/pull/2948
 *
 * @author Choonster
 */
public class MessageFluidTankContents implements IMessage {

	private IFluidTankProperties[] fluidTankProperties;

	@SuppressWarnings("unused")
	public MessageFluidTankContents() {
	}

	public MessageFluidTankContents(IFluidTankProperties[] fluidTankProperties) {
		this.fluidTankProperties = fluidTankProperties;
	}


	/**
	 * Convert from the supplied buffer into your specific message type
	 *
	 * @param buf The buffer
	 */
	@Override
	public void fromBytes(ByteBuf buf) {
		final int numProperties = buf.readInt();
		fluidTankProperties = new IFluidTankProperties[numProperties];

		for (int i = 0; i < numProperties; i++) {
			final NBTTagCompound tagCompound = ByteBufUtils.readTag(buf);
			final FluidStack contents = FluidStack.loadFluidStackFromNBT(tagCompound);

			final int capacity = buf.readInt();

			fluidTankProperties[i] = new FluidTankProperties(contents, capacity);
		}
	}

	/**
	 * Deconstruct your message into the supplied byte buffer
	 *
	 * @param buf The buffer
	 */
	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(fluidTankProperties.length);

		for (IFluidTankProperties properties : fluidTankProperties) {
			final FluidStack contents = properties.getContents();
			final NBTTagCompound tagCompound = new NBTTagCompound();

			if (contents != null) {
				contents.writeToNBT(tagCompound);
			}

			ByteBufUtils.writeTag(buf, tagCompound);

			buf.writeInt(properties.getCapacity());
		}
	}

	public static class Handler implements IMessageHandler<MessageFluidTankContents, IMessage> {

		/**
		 * Called when a message is received of the appropriate type. You can optionally return a reply message, or null if no reply
		 * is needed.
		 *
		 * @param message The message
		 * @param ctx     The context
		 * @return an optional return message
		 */
		@Override
		public IMessage onMessage(MessageFluidTankContents message, MessageContext ctx) {
			Minecraft.getMinecraft().addScheduledTask(() -> {
				final EntityPlayer player = TestMod3.proxy.getClientPlayer();

				BlockFluidTank.getFluidDataForDisplay(message.fluidTankProperties).forEach(player::addChatComponentMessage);
			});

			return null;
		}
	}
}
