package com.choonster.testmod3.pigspawner;

import com.choonster.testmod3.TestMod3;
import com.choonster.testmod3.api.pigspawner.IPigSpawner;
import com.choonster.testmod3.api.pigspawner.IPigSpawnerFinite;
import com.choonster.testmod3.api.pigspawner.IPigSpawnerInteractable;
import com.choonster.testmod3.util.DebugUtil;
import net.minecraft.block.Block;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.EntityInteractEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Capability for {@link IPigSpawner}.
 *
 * @author Choonster
 */
public final class CapabilityPigSpawner {
	/**
	 * The capability instance.
	 */
	@CapabilityInject(IPigSpawner.class)
	public static final Capability<IPigSpawner> PIG_SPAWNER_CAPABILITY = null;

	/**
	 * The ID of the capability.
	 */
	public static final ResourceLocation ID = new ResourceLocation(TestMod3.MODID, "PigPower");

	public static final Marker LOG_MARKER = MarkerManager.getMarker("PIG_SPAWNER", Logger.MOD_MARKER);

	/**
	 * Register the capability.
	 */
	public static void register() {
		CapabilityManager.INSTANCE.register(IPigSpawner.class, new Capability.IStorage<IPigSpawner>() {
			@Override
			public NBTBase writeNBT(Capability<IPigSpawner> capability, IPigSpawner instance, EnumFacing side) {
				NBTTagCompound tagCompound = new NBTTagCompound();
				if (instance instanceof IPigSpawnerFinite) {
					tagCompound.setInteger("NumPigs", ((IPigSpawnerFinite) instance).getNumPigs());
				}
				return tagCompound;
			}

			@Override
			public void readNBT(Capability<IPigSpawner> capability, IPigSpawner instance, EnumFacing side, NBTBase nbt) {
				if (instance instanceof IPigSpawnerFinite) {
					final IPigSpawnerFinite pigSpawnerFinite = (IPigSpawnerFinite) instance;
					NBTTagCompound tagCompound = (NBTTagCompound) nbt;

					Logger.info(LOG_MARKER, DebugUtil.getStackTrace(10), "Reading finite pig spawner from NBT: %s (Current: %d, New: %d)", instance, pigSpawnerFinite.getNumPigs(), tagCompound.getInteger("NumPigs"));

					pigSpawnerFinite.setNumPigs(tagCompound.getInteger("NumPigs"));
				}
			}
		}, () -> new PigSpawnerFinite(20));

		MinecraftForge.EVENT_BUS.register(new EventHandler());
	}

	/**
	 * Get the {@link IPigSpawner} from the specified {@link ItemStack}'s capabilities, if any.
	 *
	 * @param itemStack The ItemStack
	 * @return The IPigSpawner, or null if there isn't one
	 */
	public static IPigSpawner getPigSpawner(ItemStack itemStack) {
		if (itemStack != null && itemStack.hasCapability(PIG_SPAWNER_CAPABILITY, EnumFacing.NORTH)) {
			return itemStack.getCapability(PIG_SPAWNER_CAPABILITY, EnumFacing.NORTH);
		}

		return null;
	}

	/**
	 * Event handler for the {@link IPigSpawner} capability.
	 */
	public static class EventHandler {

		/**
		 * Attach the {@link IPigSpawner} capability to vanilla items.
		 *
		 * @param event The event
		 */
		@SubscribeEvent
		public void attachCapabilities(AttachCapabilitiesEvent.Item event) {
			if (event.getItem() == Items.clay_ball) {
				event.addCapability(ID, new Provider());
			}
		}

		/**
		 * Spawn a pig at the specified position of the item has the {@link IPigSpawner} capability.
		 * <p>
		 * If there's an {@link IPigSpawnerInteractable}, call {@link IPigSpawnerInteractable#interact} on it.
		 *
		 * @param itemStack       The player's held item
		 * @param world           The World
		 * @param x               The x position to spawn the pig at
		 * @param y               The y position to spawn the pig at
		 * @param z               The z position to spawn the pig at
		 * @param interactable    The IPigSpawnerInteractable, if any
		 * @param interactablePos The position of the IPigSpawnerInteractable
		 * @param iCommandSender  The ICommandSender, if any
		 */
		private void trySpawnPig(ItemStack itemStack, World world, double x, double y, double z, Optional<IPigSpawnerInteractable> interactable, BlockPos interactablePos, Optional<ICommandSender> iCommandSender) {
			if (world.isRemote) return;

			final IPigSpawner pigSpawner = getPigSpawner(itemStack);
			if (pigSpawner == null) return;

			boolean spawnPig = true;
			if (interactable.isPresent()) {
				spawnPig = !interactable.get().interact(pigSpawner, world, interactablePos, iCommandSender);
			}

			if (spawnPig && pigSpawner.canSpawnPig(world, x, y, z)) {
				pigSpawner.spawnPig(world, x, y, z);
			}
		}

		/**
		 * Spawn a pig when a player right clicks a block with an item that has the {@link IPigSpawner} capability.
		 * <p>
		 * If the block implements {@link IPigSpawnerInteractable}, call {@link IPigSpawnerInteractable#interact} on it.
		 *
		 * @param event The event
		 */
		@SubscribeEvent
		public void playerInteract(PlayerInteractEvent event) {
			if (event.action != PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK) return;

			final BlockPos spawnPos = event.pos.offset(event.face);
			final double x = spawnPos.getX() + 0.5, y = spawnPos.getY() + 0.5, z = spawnPos.getZ() + 0.5;

			final World world = event.world;
			final Block block = world.getBlockState(event.pos).getBlock();
			final Optional<IPigSpawnerInteractable> interactable = block instanceof IPigSpawnerInteractable ? Optional.of((IPigSpawnerInteractable) block) : Optional.empty();

			trySpawnPig(event.entityPlayer.getHeldItem(), world, x, y, z, interactable, event.pos, Optional.of(event.entityPlayer));
		}

		/**
		 * Spawn a pig when a player right clicks an entity with an item that has the {@link IPigSpawner} capability.
		 * <p>
		 * If the entity implements {@link IPigSpawnerInteractable}, call {@link IPigSpawnerInteractable#interact} on it.
		 *
		 * @param event The event
		 */
		@SubscribeEvent
		public void entityInteract(EntityInteractEvent event) {
			final World world = event.entityPlayer.getEntityWorld();

			final Entity target = event.target;
			final double x = target.posX, y = target.posY, z = target.posZ;
			final Optional<IPigSpawnerInteractable> interactable = target instanceof IPigSpawnerInteractable ? Optional.of((IPigSpawnerInteractable) target) : Optional.empty();

			trySpawnPig(event.entityPlayer.getHeldItem(), world, x, y, z, interactable, target.getPosition(), Optional.of(event.entityPlayer));
		}

		/**
		 * Add the {@link IPigSpawner}'s tooltip lines to the tooltip if the item has the {@link IPigSpawner} capability
		 *
		 * @param event The event
		 */
		@SubscribeEvent
		public void itemTooltip(ItemTooltipEvent event) {
			IPigSpawner pigSpawner = getPigSpawner(event.itemStack);
			if (pigSpawner == null) return;

			final ChatStyle chatStyle = new ChatStyle().setColor(EnumChatFormatting.LIGHT_PURPLE);

			final List<IChatComponent> chatComponents = pigSpawner.getTooltipLines();
			final List<String> tooltipLines = chatComponents.stream()
					.map(iChatComponent -> iChatComponent.setChatStyle(chatStyle))
					.map(IChatComponent::getFormattedText)
					.collect(Collectors.toList());

			event.toolTip.add("");
			event.toolTip.addAll(tooltipLines);
		}
	}

	/**
	 * Provider for the {@link IPigSpawner} capability.
	 */
	public static class Provider implements ICapabilitySerializable<NBTTagCompound> {
		private final IPigSpawner pigSpawner;

		/**
		 * Create a provider using the default {@link IPigSpawner} instance.
		 */
		public Provider() {
			this(new PigSpawnerFinite(20));
		}

		/**
		 * Create a provider using the specified {@link IPigSpawner} instance.
		 *
		 * @param pigSpawner The {@link IPigSpawner} instance
		 */
		public Provider(IPigSpawner pigSpawner) {
			this.pigSpawner = pigSpawner;
		}

		@Override
		public NBTTagCompound serializeNBT() {
			return (NBTTagCompound) PIG_SPAWNER_CAPABILITY.getStorage().writeNBT(PIG_SPAWNER_CAPABILITY, pigSpawner, EnumFacing.NORTH);
		}

		@Override
		public void deserializeNBT(NBTTagCompound nbt) {
			PIG_SPAWNER_CAPABILITY.getStorage().readNBT(PIG_SPAWNER_CAPABILITY, pigSpawner, EnumFacing.NORTH, nbt);
		}

		/**
		 * Determines if this object has support for the capability in question on the specific side.
		 * The return value of this MIGHT change during runtime if this object gains or looses support
		 * for a capability.
		 * <p>
		 * Example:
		 * A Pipe getting a cover placed on one side causing it loose the Inventory attachment function for that side.
		 * <p>
		 * This is a light weight version of getCapability, intended for metadata uses.
		 *
		 * @param capability The capability to check
		 * @param facing     The Side to check from:
		 *                   CAN BE NULL. Null is defined to represent 'internal' or 'self'
		 * @return True if this object supports the capability.
		 */
		@Override
		public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
			return capability == PIG_SPAWNER_CAPABILITY;
		}

		/**
		 * Retrieves the handler for the capability requested on the specific side.
		 * The return value CAN be null if the object does not support the capability.
		 * The return value CAN be the same for multiple faces.
		 *
		 * @param capability The capability to check
		 * @param facing     The Side to check from:
		 *                   CAN BE NULL. Null is defined to represent 'internal' or 'self'
		 * @return True if this object supports the capability.
		 */
		@SuppressWarnings("unchecked")
		@Override
		public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
			if (capability == PIG_SPAWNER_CAPABILITY) {
				return (T) pigSpawner;
			}

			return null;
		}
	}
}
