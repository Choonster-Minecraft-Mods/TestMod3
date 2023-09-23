package choonster.testmod3.init;

import choonster.testmod3.TestMod3;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.core.dispenser.OptionalDispenseItemBehavior;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

/**
 * Registers this mod's {@link DispenseItemBehavior}s.
 *
 * @author Choonster
 */
@Mod.EventBusSubscriber(modid = TestMod3.MODID, bus = Bus.MOD)
public class ModDispenseBehaviors {
	/**
	 * Register this mod's {@link DispenseItemBehavior}s.
	 *
	 * @param event The common setup event
	 */
	@SubscribeEvent
	public static void registerDispenseBehaviors(final FMLCommonSetupEvent event) {
		event.enqueueWork(() -> {
			// Add a dispense behaviour that causes Black Dye to place Black Wool.
			// http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/modification-development/2789286-override-dispenser-dispense-only-for-a-certain
			DispenserBlock.registerBehavior(Items.BLACK_DYE, new OptionalDispenseItemBehavior() {
				@Override
				protected ItemStack execute(final BlockSource source, final ItemStack stack) {
					setSuccess(true);

					final var facing = source.state().getValue(DispenserBlock.FACING);
					final var neighbourPos = source.pos().relative(facing);
					final var level = source.level();
					final var neighbourState = level.getBlockState(neighbourPos);

					setSuccess(
							neighbourState.isAir() &&
									level.setBlockAndUpdate(neighbourPos, Blocks.BLACK_WOOL.defaultBlockState())
					);

					if (isSuccess()) {
						stack.shrink(1);
					}

					return stack;
				}
			});
		});
	}
}
