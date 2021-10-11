package choonster.testmod3.init;

import choonster.testmod3.TestMod3;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockSource;
import net.minecraft.core.Direction;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.core.dispenser.OptionalDispenseItemBehavior;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.state.BlockState;
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

					final Direction facing = source.getBlockState().getValue(DispenserBlock.FACING);
					final BlockPos neighbourPos = source.getPos().relative(facing);
					final Level world = source.getLevel();
					final BlockState neighbourState = world.getBlockState(neighbourPos);

					setSuccess(
							neighbourState.isAir() &&
									world.setBlockAndUpdate(neighbourPos, Blocks.BLACK_WOOL.defaultBlockState())
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
