package choonster.testmod3.init;

import choonster.testmod3.TestMod3;
import choonster.testmod3.command.arguments.AxisArgument;
import com.mojang.brigadier.arguments.ArgumentType;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.commands.synchronization.ArgumentTypeInfos;
import net.minecraft.commands.synchronization.SingletonArgumentInfo;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

/**
 * Registers this mod's {@link ArgumentType}s.
 *
 * @author Choonster
 */
@Mod.EventBusSubscriber(modid = TestMod3.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModArgumentTypes {
	private static final DeferredRegister<ArgumentTypeInfo<?, ?>> COMMAND_ARGUMENT_TYPES = DeferredRegister.create(ForgeRegistries.COMMAND_ARGUMENT_TYPES, TestMod3.MODID);

	private static boolean isInitialised = false;

	public static final RegistryObject<ArgumentTypeInfo<?, ?>> AXIS = COMMAND_ARGUMENT_TYPES.register("axis",
			() -> ArgumentTypeInfos.registerByClass(AxisArgument.class, SingletonArgumentInfo.contextFree(AxisArgument::axis))
	);

	/**
	 * Registers the {@link DeferredRegister} instance with the mod event bus.
	 * <p>
	 * This should be called during mod construction.
	 *
	 * @param modEventBus The mod event bus
	 */
	public static void initialise(final IEventBus modEventBus) {
		if (isInitialised) {
			throw new IllegalStateException("Already initialised");
		}

		COMMAND_ARGUMENT_TYPES.register(modEventBus);

		isInitialised = true;
	}
}
