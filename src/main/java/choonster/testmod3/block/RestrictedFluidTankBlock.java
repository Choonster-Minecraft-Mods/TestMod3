package choonster.testmod3.block;

import choonster.testmod3.tileentity.RestrictedFluidTankTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * A Fluid Tank block that can have access enabled and disabled for each facing when right-clicked with a stick.
 * <p>
 * When left clicked, it will tell the player which sides are enabled.
 *
 * @author Choonster
 */
public class RestrictedFluidTankBlock extends FluidTankBlock<RestrictedFluidTankTileEntity> {
	public RestrictedFluidTankBlock(final Block.Properties properties) {
		super(properties);
	}

	@Override
	public TileEntity createTileEntity(final BlockState state, final IBlockReader world) {
		return new RestrictedFluidTankTileEntity();
	}

	@Override
	public boolean onBlockActivated(final BlockState state, final World world, final BlockPos pos, final PlayerEntity player, final Hand hand, final BlockRayTraceResult rayTraceResult) {
		final ItemStack heldItem = player.getHeldItem(hand);
		final Direction face = rayTraceResult.getFace();

		if (heldItem.getItem() == Items.STICK) {
			final RestrictedFluidTankTileEntity tileEntity = getTileEntity(world, pos);
			if (tileEntity != null) {
				final boolean enabled = tileEntity.toggleFacing(face);

				if (!world.isRemote) {
					final String message = enabled ? "message.testmod3.fluid_tank_restricted.facing_enabled" : "message.testmod3.fluid_tank_restricted.facing_disabled";
					player.sendMessage(new TranslationTextComponent(message, face));
				}

				return true;
			}
		}

		return super.onBlockActivated(state, world, pos, player, hand, rayTraceResult);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onBlockClicked(final BlockState state, final World worldIn, final BlockPos pos, final PlayerEntity player) {
		if (!worldIn.isRemote) {
			final String enabledFacingsString = getEnabledFacingsString(worldIn, pos);
			player.sendMessage(new TranslationTextComponent("message.testmod3.fluid_tank_restricted.enabled_facings", enabledFacingsString));
		}
	}

	/**
	 * Get the enabled facings for the tank at the specified position as a string.
	 *
	 * @param worldIn The World
	 * @param pos     The position
	 * @return The enabled facings string
	 */
	public String getEnabledFacingsString(final IWorldReader worldIn, final BlockPos pos) {
		final RestrictedFluidTankTileEntity tileEntity = getTileEntity(worldIn, pos);

		if (tileEntity != null) {
			final Set<Direction> enabledFacings = tileEntity.getEnabledFacings();
			return enabledFacings.stream()
					.sorted()
					.map(Direction::getName)
					.collect(Collectors.joining(", "));
		}

		return "";
	}
}
