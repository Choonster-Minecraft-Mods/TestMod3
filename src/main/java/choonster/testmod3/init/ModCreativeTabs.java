package choonster.testmod3.init;

import choonster.testmod3.TestMod3;
import choonster.testmod3.util.SwordUpgrades;
import choonster.testmod3.world.item.block.FluidTankItem;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.Collection;
import java.util.function.Supplier;

/**
 * Registers this mod's {@link CreativeModeTab}s.
 *
 * @author Choonster
 */
public class ModCreativeTabs {
	private static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, TestMod3.MODID);

	private static boolean isInitialised = false;

	public static RegistryObject<CreativeModeTab> TAB = CREATIVE_MODE_TABS.register("tab", () -> {
				final Supplier<ItemStack> sword = () -> SwordUpgrades.upgradeSword(Items.STONE_SWORD);

				return CreativeModeTab.builder()
						.title(Component.translatable("itemGroup.testmod3"))
						.icon(sword)
						.displayItems((parameters, output) -> {
							output.accept(sword.get());

							add(output, ModBlocks.orderedItems());

							add(output, ModItems.orderedItems());

							add(output, ModFluids.orderedItems());
						})
						.build();
			}
	);

	/**
	 * Registers the {@link DeferredRegister} instances with the mod event bus.
	 * <p>
	 * This should be called during mod construction.
	 *
	 * @param modEventBus The mod event bus
	 */
	public static void initialise(final IEventBus modEventBus) {
		if (isInitialised) {
			throw new IllegalStateException("Already initialised");
		}

		CREATIVE_MODE_TABS.register(modEventBus);

		isInitialised = true;
	}

	private static void add(final CreativeModeTab.Output output, final Collection<RegistryObject<Item>> items) {
		items.stream()
				.map(RegistryObject::get)
				.forEach(output::accept);
	}

	@Mod.EventBusSubscriber(modid = TestMod3.MODID, bus = Bus.MOD)
	public static class RegistrationHandler {
		@SubscribeEvent
		public static void buildContents(final BuildCreativeModeTabContentsEvent event) {
			if (event.getTab() != TAB.get()) {
				return;
			}

			final var entries = event.getEntries();

			if (ModBlocks.FLUID_TANK.get().asItem() instanceof final FluidTankItem item) {
				item.fillCreativeModeTab(entries);
			}

			if (ModBlocks.FLUID_TANK_RESTRICTED.get().asItem() instanceof final FluidTankItem item) {
				item.fillCreativeModeTab(entries);
			}

			ModItems.WOODEN_BUCKET.get().fillCreativeModeTab(entries);
			ModItems.STONE_BUCKET.get().fillCreativeModeTab(entries);
		}
	}
}
