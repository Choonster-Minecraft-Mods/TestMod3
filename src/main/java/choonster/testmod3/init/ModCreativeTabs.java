package choonster.testmod3.init;

import choonster.testmod3.TestMod3;
import choonster.testmod3.util.SwordUpgrades;
import choonster.testmod3.world.item.block.FluidTankItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

/**
 * Registers this mod's {@link CreativeModeTab}s.
 *
 * @author Choonster
 */
@Mod.EventBusSubscriber(modid = TestMod3.MODID, bus = Bus.MOD)
public class ModCreativeTabs {
	private static CreativeModeTab TAB;

	@SubscribeEvent
	public static void register(final CreativeModeTabEvent.Register event) {
		final Supplier<ItemStack> sword = () -> SwordUpgrades.upgradeSword(Items.STONE_SWORD);

		TAB = event.registerCreativeModeTab(
				new ResourceLocation(TestMod3.MODID, "tab"),
				builder -> builder.icon(sword)
						.displayItems((enabledFeatures, output, hasPermissions) -> {
							output.accept(sword.get());

							ModItems.orderedItems()
									.stream()
									.map(RegistryObject::get)
									.forEach(output::accept);
						})
		);
	}

	@SubscribeEvent
	public static void buildContents(final CreativeModeTabEvent.BuildContents event) {
		if (event.getTab() != TAB) {
			return;
		}

		final var entries = event.getEntries();

		if (ModBlocks.FLUID_TANK.get().asItem() instanceof FluidTankItem item) {
			item.fillCreativeModeTab(entries);
		}

		if (ModBlocks.FLUID_TANK_RESTRICTED.get().asItem() instanceof FluidTankItem item) {
			item.fillCreativeModeTab(entries);
		}

		ModItems.WOODEN_BUCKET.get().fillCreativeModeTab(entries);
		ModItems.STONE_BUCKET.get().fillCreativeModeTab(entries);
	}
}
