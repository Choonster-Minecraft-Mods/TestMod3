package choonster.testmod3.init;

import choonster.testmod3.TestMod3;
import choonster.testmod3.command.arguments.AxisArgument;
import com.mojang.brigadier.arguments.ArgumentType;
import net.minecraft.command.arguments.ArgumentSerializer;
import net.minecraft.command.arguments.ArgumentTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

/**
 * Registers this mod's {@link ArgumentType}s.
 *
 * @author Choonster
 */
@Mod.EventBusSubscriber(modid = TestMod3.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModArgumentTypes {
	/**
	 * Register the command argument types.
	 *
	 * @param event The common setup event
	 */
	@SubscribeEvent
	public static void registerArgumentTypes(final FMLCommonSetupEvent event) {
		ArgumentTypes.register(new ResourceLocation(TestMod3.MODID, "axis").toString(), AxisArgument.class, new ArgumentSerializer<>(AxisArgument::axis));
	}
}
