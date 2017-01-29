package choonster.testmod3.init;

import choonster.testmod3.dispensebehavior.BehaviorDispenseDelegate;
import net.minecraft.block.BlockColored;
import net.minecraft.block.BlockDispenser;
import net.minecraft.block.state.IBlockState;
import net.minecraft.dispenser.IBehaviorDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.RegistryDefaulted;
import net.minecraft.world.World;

/**
 * Registers this mod's {@link IBehaviorDispenseItem}s.
 *
 * @author Choonster
 */
public class ModDispenseBehaviors {

	private static final RegistryDefaulted<Item, IBehaviorDispenseItem> REGISTRY = BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY;

	public static void register() {
		// Replace the dye behavior with one that causes Ink Sacs to place Black Wool and all other dyes to run the vanilla behavior.
		// http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/modification-development/2789286-override-dispenser-dispense-only-for-a-certain
		register(Items.DYE, new BehaviorDispenseDelegate(REGISTRY.getObject(Items.DYE)) {

			@Override
			protected ItemStack dispenseStack(IBlockSource source, ItemStack stack) {
				doSoundsParticles = true;
				successful = true;

				if (EnumDyeColor.byDyeDamage(stack.getMetadata()) == EnumDyeColor.BLACK) {
					final EnumFacing facing = source.getBlockState().getValue(BlockDispenser.FACING);
					final BlockPos neighbourPos = source.getBlockPos().offset(facing);
					final World world = source.getWorld();
					final IBlockState neighbourState = world.getBlockState(neighbourPos);

					successful = neighbourState.getBlock().isAir(neighbourState, world, neighbourPos) &&
							world.setBlockState(neighbourPos, Blocks.WOOL.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.BLACK));

					if (successful) {
						stack.shrink(1);
					}

					return stack;
				}

				return callDelegate(source, stack);
			}
		});
	}

	private static void register(Item item, IBehaviorDispenseItem behaviorDispenseItem) {
		REGISTRY.putObject(item, behaviorDispenseItem);
	}
}
