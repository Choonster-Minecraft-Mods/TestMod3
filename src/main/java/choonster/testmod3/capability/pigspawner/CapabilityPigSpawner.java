package choonster.testmod3.capability.pigspawner;

import choonster.testmod3.TestMod3;
import choonster.testmod3.api.capability.pigspawner.IPigSpawner;
import choonster.testmod3.api.capability.pigspawner.IPigSpawnerFinite;
import choonster.testmod3.api.capability.pigspawner.IPigSpawnerInteractable;
import choonster.testmod3.capability.CapabilityContainerListenerManager;
import choonster.testmod3.capability.CapabilityProviderSerializable;
import choonster.testmod3.util.DebugUtil;
import choonster.testmod3.util.LogUtil;
import net.minecraft.block.Block;
import net.minecraft.command.ICommandSource;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.INBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import javax.annotation.Nullable;
import java.util.List;
import java.util.stream.Collectors;

import static choonster.testmod3.util.InjectionUtil.Null;

/**
 * Capability for {@link IPigSpawner}.
 *
 * @author Choonster
 */
public final class CapabilityPigSpawner {
	private static final org.apache.logging.log4j.Logger LOGGER = LogManager.getLogger();

	/**
	 * The {@link Capability} instance.
	 */
	@CapabilityInject(IPigSpawner.class)
	public static final Capability<IPigSpawner> PIG_SPAWNER_CAPABILITY = Null();

	/**
	 * The default {@link EnumFacing} to use for this capability.
	 */
	public static final EnumFacing DEFAULT_FACING = null;

	/**
	 * The ID of the capability.
	 */
	public static final ResourceLocation ID = new ResourceLocation(TestMod3.MODID, "PigPower");

	public static final Marker LOG_MARKER = MarkerManager.getMarker("PIG_SPAWNER").addParents(LogUtil.MOD_MARKER);

	/**
	 * Register the capability.
	 */
	public static void register() {
		CapabilityManager.INSTANCE.register(IPigSpawner.class, new Capability.IStorage<IPigSpawner>() {
			@Override
			public INBTBase writeNBT(final Capability<IPigSpawner> capability, final IPigSpawner instance, final EnumFacing side) {
				final NBTTagCompound tagCompound = new NBTTagCompound();
				if (instance instanceof IPigSpawnerFinite) {
					tagCompound.putInt("NumPigs", ((IPigSpawnerFinite) instance).getNumPigs());
				}
				return tagCompound;
			}

			@Override
			public void readNBT(final Capability<IPigSpawner> capability, final IPigSpawner instance, final EnumFacing side, final INBTBase nbt) {
				if (instance instanceof IPigSpawnerFinite) {
					final IPigSpawnerFinite pigSpawnerFinite = (IPigSpawnerFinite) instance;
					final NBTTagCompound tagCompound = (NBTTagCompound) nbt;

					LogUtil.debug(LOGGER, LOG_MARKER, DebugUtil.getStackTrace(10), "Reading finite pig spawner from NBT: %s (Current: %d, New: %d)", instance, pigSpawnerFinite.getNumPigs(), tagCompound.getInt("NumPigs"));

					pigSpawnerFinite.setNumPigs(tagCompound.getInt("NumPigs"));
				}
			}
		}, () -> new PigSpawnerFinite(20));

		CapabilityContainerListenerManager.registerListenerFactory(ContainerListenerPigSpawnerFinite::new);
	}

	/**
	 * Get the {@link IPigSpawner} from the specified {@link ItemStack}'s capabilities, if any.
	 *
	 * @param itemStack The ItemStack
	 * @return A lazy optional containing the IPigSpawner, if amy
	 */
	public static LazyOptional<IPigSpawner> getPigSpawner(final ItemStack itemStack) {
		return itemStack.getCapability(PIG_SPAWNER_CAPABILITY, DEFAULT_FACING);
	}

	/**
	 * Create a provider for the default {@link IPigSpawner} instance.
	 *
	 * @return The provider
	 */
	public static ICapabilityProvider createProvider() {
		return new CapabilityProviderSerializable<>(PIG_SPAWNER_CAPABILITY, DEFAULT_FACING);
	}

	/**
	 * Create a provider for the specified {@link IPigSpawner} instance.
	 *
	 * @param pigSpawner The IPigSpawner
	 * @return The provider
	 */
	public static ICapabilityProvider createProvider(final IPigSpawner pigSpawner) {
		return new CapabilityProviderSerializable<>(PIG_SPAWNER_CAPABILITY, DEFAULT_FACING, pigSpawner);
	}

	/**
	 * Event handler for the {@link IPigSpawner} capability.
	 */
	@Mod.EventBusSubscriber(modid = TestMod3.MODID)
	private static class EventHandler {

		/**
		 * Attach the {@link IPigSpawner} capability to vanilla items.
		 *
		 * @param event The event
		 */
		@SubscribeEvent
		public static void attachCapabilities(final AttachCapabilitiesEvent<ItemStack> event) {
			if (event.getObject().getItem() == Items.CLAY_BALL) {
				event.addCapability(ID, createProvider());
			}
		}

		/**
		 * Spawn a pig at the specified position.
		 * <p>
		 * If there's an {@link IPigSpawnerInteractable}, call {@link IPigSpawnerInteractable#interact} on it.
		 *
		 * @param pigSpawner      The pig spawner
		 * @param world           The World
		 * @param x               The x position to spawn the pig at
		 * @param y               The y position to spawn the pig at
		 * @param z               The z position to spawn the pig at
		 * @param interactable    The IPigSpawnerInteractable, if any
		 * @param interactablePos The position of the IPigSpawnerInteractable
		 * @param commandSource   The command source, if any
		 */
		private static void trySpawnPig(final IPigSpawner pigSpawner, final World world, final double x, final double y, final double z, @Nullable final IPigSpawnerInteractable interactable, final BlockPos interactablePos, @Nullable final ICommandSource commandSource) {
			if (world.isRemote) return;

			boolean shouldSpawnPig = true;

			if (interactable != null) {
				shouldSpawnPig = !interactable.interact(pigSpawner, world, interactablePos, commandSource);
			}

			if (shouldSpawnPig && pigSpawner.canSpawnPig(world, x, y, z)) {
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
		public static void playerInteract(final PlayerInteractEvent.RightClickBlock event) {
			final EnumFacing facing = event.getFace();
			assert facing != null;

			final BlockPos spawnPos = event.getPos().offset(facing);
			final double x = spawnPos.getX() + 0.5, y = spawnPos.getY() + 0.5, z = spawnPos.getZ() + 0.5;

			final World world = event.getWorld();
			final Block block = world.getBlockState(event.getPos()).getBlock();
			final IPigSpawnerInteractable interactable = block instanceof IPigSpawnerInteractable ? (IPigSpawnerInteractable) block : null;

			final EntityPlayer player = event.getEntityPlayer();

			getPigSpawner(event.getItemStack())
					.ifPresent(pigSpawner -> trySpawnPig(pigSpawner, world, x, y, z, interactable, event.getPos(), player));
		}

		/**
		 * Spawn a pig when a player right clicks an entity with an item that has the {@link IPigSpawner} capability.
		 * <p>
		 * If the entity implements {@link IPigSpawnerInteractable}, call {@link IPigSpawnerInteractable#interact} on it.
		 *
		 * @param event The event
		 */
		@SubscribeEvent
		public static void entityInteract(final PlayerInteractEvent.EntityInteract event) {
			final World world = event.getEntityPlayer().getEntityWorld();

			final Entity target = event.getTarget();
			final double x = target.posX, y = target.posY, z = target.posZ;
			final IPigSpawnerInteractable interactable = target instanceof IPigSpawnerInteractable ? (IPigSpawnerInteractable) target : null;

			final EnumHand hand = event.getHand();

			getPigSpawner(event.getEntityPlayer().getHeldItem(hand))
					.ifPresent(pigSpawner -> trySpawnPig(pigSpawner, world, x, y, z, interactable, target.getPosition(), event.getEntityPlayer()));
		}

	}

	@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = TestMod3.MODID)
	private static class ClientEventHandler {
		/**
		 * Add the {@link IPigSpawner}'s tooltip lines to the tooltip if the item has the {@link IPigSpawner} capability
		 *
		 * @param event The event
		 */
		@SubscribeEvent
		public static void itemTooltip(final ItemTooltipEvent event) {
			getPigSpawner(event.getItemStack()).ifPresent(pigSpawner -> {
				final Style style = new Style().setColor(TextFormatting.LIGHT_PURPLE);

				final List<ITextComponent> tooltipLines = pigSpawner.getTooltipLines().stream()
						.map(iTextComponent -> iTextComponent.setStyle(style))
						.collect(Collectors.toList());

				event.getToolTip().add(new TextComponentString(""));
				event.getToolTip().addAll(tooltipLines);

			});
		}
	}
}
