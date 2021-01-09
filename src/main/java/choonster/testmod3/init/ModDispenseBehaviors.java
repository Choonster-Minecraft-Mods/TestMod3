package choonster.testmod3.init;

import choonster.testmod3.TestMod3;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.DispenserBlock;
import net.minecraft.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.dispenser.IDispenseItemBehavior;
import net.minecraft.dispenser.OptionalDispenseBehavior;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

import java.util.function.Supplier;

/**
 * Registers this mod's {@link IDispenseItemBehavior}s.
 *
 * @author Choonster
 */
@Mod.EventBusSubscriber(modid = TestMod3.MODID, bus = Bus.MOD)
public class ModDispenseBehaviors {
	/**
	 * Register this mod's {@link IDispenseItemBehavior}s.
	 *
	 * @param event The common setup event
	 */
	@SubscribeEvent
	public static void registerDispenseBehaviors(final FMLCommonSetupEvent event) {
		event.enqueueWork(() -> {
			// Add a dispense behaviour that causes Black Dye to place Black Wool.
			// http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/modification-development/2789286-override-dispenser-dispense-only-for-a-certain
			DispenserBlock.registerDispenseBehavior(Items.BLACK_DYE, new OptionalDispenseBehavior() {
				@Override
				protected ItemStack dispenseStack(final IBlockSource source, final ItemStack stack) {
					setSuccessful(true);

					final Direction facing = source.getBlockState().get(DispenserBlock.FACING);
					final BlockPos neighbourPos = source.getBlockPos().offset(facing);
					final World world = source.getWorld();
					final BlockState neighbourState = world.getBlockState(neighbourPos);

					setSuccessful(
							neighbourState.getBlock().isAir(neighbourState, world, neighbourPos) &&
									world.setBlockState(neighbourPos, Blocks.BLACK_WOOL.getDefaultState())
					);

					if (isSuccessful()) {
						stack.shrink(1);
					}

					return stack;
				}
			});

			// Add a copy of the Vanilla spawn egg dispense behaviour for all our spawn eggs
			final DefaultDispenseItemBehavior spawnEggDispenseBehavior = new DefaultDispenseItemBehavior() {
				@Override
				public ItemStack dispenseStack(final IBlockSource source, final ItemStack stack) {
					final Direction direction = source.getBlockState().get(DispenserBlock.FACING);
					final EntityType<?> entityType = ((SpawnEggItem) stack.getItem()).getType(stack.getTag());

					entityType.spawn(
							source.getWorld(), stack, null,
							source.getBlockPos().offset(direction), SpawnReason.DISPENSER,
							direction != Direction.UP, false
					);

					stack.shrink(1);

					return stack;
				}
			};

			ModItems.getSpawnEggs()
					.stream()
					.map(Supplier::get)
					.forEach(egg -> DispenserBlock.registerDispenseBehavior(egg, spawnEggDispenseBehavior));
		});
	}
}
