package choonster.testmod3.init;

import choonster.testmod3.TestMod3;
import choonster.testmod3.inventory.container.ModChestContainer;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;

import static choonster.testmod3.util.InjectionUtil.Null;

/**
 * Registers this mod's {@link ContainerType}s.
 *
 * @author Choonster
 */
@ObjectHolder(TestMod3.MODID)
public class ModContainerTypes {
	public static ContainerType<ModChestContainer> CHEST = Null();

	@Mod.EventBusSubscriber(modid = TestMod3.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
	public static class RegistrationHandler {
		@SubscribeEvent
		public static void registerContainerTypes(final RegistryEvent.Register<ContainerType<?>> event) {
			event.getRegistry().register(
					new ContainerType<>(ModChestContainer::new).setRegistryName("chest")
			);
		}
	}
}
