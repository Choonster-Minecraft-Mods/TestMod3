package choonster.testmod3.init;

import choonster.testmod3.TestMod3;
import net.minecraft.block.BlockDispenser;
import net.minecraft.block.state.IBlockState;
import net.minecraft.dispenser.IBehaviorDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.init.Blocks;
import net.minecraft.init.Bootstrap;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

/**
 * Registers this mod's {@link IBehaviorDispenseItem}s.
 *
 * @author Choonster
 */
@Mod.EventBusSubscriber(modid = TestMod3.MODID, bus = Bus.MOD)
public class ModDispenseBehaviors {
	/**
	 * Register this mod's {@link IBehaviorDispenseItem}s.
	 */
	@SubscribeEvent
	public static void registerDispenseBehaviors(final FMLCommonSetupEvent event) {
		// Add a dispense behaviour that causes Ink Sacs to place Black Wool.
		// http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/modification-development/2789286-override-dispenser-dispense-only-for-a-certain
		BlockDispenser.registerDispenseBehavior(Items.INK_SAC, new Bootstrap.BehaviorDispenseOptional() {
			@Override
			protected ItemStack dispenseStack(final IBlockSource source, final ItemStack stack) {
				successful = true;

				final EnumFacing facing = source.getBlockState().get(BlockDispenser.FACING);
				final BlockPos neighbourPos = source.getBlockPos().offset(facing);
				final World world = source.getWorld();
				final IBlockState neighbourState = world.getBlockState(neighbourPos);

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
