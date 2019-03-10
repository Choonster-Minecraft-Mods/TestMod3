package choonster.testmod3.block;

import choonster.testmod3.tileentity.TileEntityFluidTankRestricted;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReaderBase;
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
public class BlockFluidTankRestricted extends BlockFluidTank<TileEntityFluidTankRestricted> {
	public BlockFluidTankRestricted(final Block.Properties properties) {
		super(properties);
	}

	@Override
	public TileEntity createTileEntity(final IBlockState state, final IBlockReader world) {
		return new TileEntityFluidTankRestricted();
	}

	@Override
	public boolean onBlockActivated(final IBlockState state, final World world, final BlockPos pos, final EntityPlayer player, final EnumHand hand, final EnumFacing side, final float hitX, final float hitY, final float hitZ) {
		final ItemStack heldItem = player.getHeldItem(hand);

		if (heldItem.getItem() == Items.STICK) {
			final TileEntityFluidTankRestricted tileEntity = getTileEntity(world, pos);
			if (tileEntity != null) {
				final boolean enabled = tileEntity.toggleFacing(side);

				if (!world.isRemote) {
					final String message = enabled ? "message.testmod3:fluid_tank_restricted.facing_enabled" : "message.testmod3:fluid_tank_restricted.facing_disabled";
					player.sendMessage(new TextComponentTranslation(message, side));
				}

				return true;
			}
		}

		return super.onBlockActivated(state, world, pos, player, hand, side, hitX, hitY, hitZ);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onBlockClicked(final IBlockState state, final World worldIn, final BlockPos pos, final EntityPlayer player) {
		if (!worldIn.isRemote) {
			final String enabledFacingsString = getEnabledFacingsString(worldIn, pos);
			player.sendMessage(new TextComponentTranslation("message.testmod3:fluid_tank_restricted.enabled_facings", enabledFacingsString));
		}
	}

	/**
	 * Get the enabled facings for the tank at the specified position as a string.
	 *
	 * @param worldIn The World
	 * @param pos     The position
	 * @return The enabled facings string
	 */
	public String getEnabledFacingsString(final IWorldReaderBase worldIn, final BlockPos pos) {
		final TileEntityFluidTankRestricted tileEntity = getTileEntity(worldIn, pos);

		if (tileEntity != null) {
			final Set<EnumFacing> enabledFacings = tileEntity.getEnabledFacings();
			return enabledFacings.stream()
					.sorted()
					.map(EnumFacing::getName)
					.collect(Collectors.joining(", "));
		}

		return "";
	}
}
