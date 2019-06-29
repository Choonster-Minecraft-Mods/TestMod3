package choonster.testmod3.init;

import choonster.testmod3.TestMod3;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.DispenserBlock;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.dispenser.IDispenseItemBehavior;
import net.minecraft.dispenser.OptionalDispenseBehavior;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

/**
 * Registers this mod's {@link IDispenseItemBehavior}s.
 *
 * @author Choonster
 */
@Mod.EventBusSubscriber(modid = TestMod3.MODID, bus = Bus.MOD)
public class ModDispenseBehaviors {
	/**
	 * Register this mod's {@link IDispenseItemBehavior}s.
	 */
	@SubscribeEvent
	public static void registerDispenseBehaviors(final FMLCommonSetupEvent event) {
		// Add a dispense behaviour that causes Black Dye to place Black Wool.
		// http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/modification-development/2789286-override-dispenser-dispense-only-for-a-certain
		DispenserBlock.registerDispenseBehavior(Items.BLACK_DYE, new OptionalDispenseBehavior() {
			@Override
			protected ItemStack dispenseStack(final IBlockSource source, final ItemStack stack) {
				successful = true;

				final Direction facing = source.getBlockState().get(DispenserBlock.FACING);
				final BlockPos neighbourPos = source.getBlockPos().offset(facing);
				final World world = source.getWorld();
				final BlockState neighbourState = world.getBlockState(neighbourPos);

				successful = neighbourState.getBlock().isAir(neighbourState, world, neighbourPos) &&
						world.setBlockState(neighbourPos, Blocks.BLACK_WOOL.getDefaultState());

				if (successful) {
					stack.shrink(1);
				}

				return stack;
			}
		});
	}
}
