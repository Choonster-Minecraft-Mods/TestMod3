package choonster.testmod3.block;

import choonster.testmod3.TestMod3;
import choonster.testmod3.network.MessageFluidTankContents;
import choonster.testmod3.tileentity.TileEntityFluidTank;
import choonster.testmod3.util.CapabilityUtils;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidActionResult;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * A Fluid Tank block.
 *
 * @author Choonster
 */
public class BlockFluidTank<TE extends TileEntityFluidTank> extends BlockTileEntity<TE> {
	public BlockFluidTank(final String blockName) {
		super(Material.GLASS, blockName, true);
		setSoundType(SoundType.GLASS);
		setHardness(0.3f);
	}

	@Override
	public List<ItemStack> getDrops(final IBlockAccess world, final BlockPos pos, final IBlockState state, final int fortune) {
		final List<ItemStack> drops = new ArrayList<>();

		final IFluidHandler fluidHandler = getFluidHandler(world, pos);

		if (fluidHandler != null) {
			final FluidActionResult fluidActionResult = FluidUtil.tryFillContainer(new ItemStack(this), fluidHandler, Integer.MAX_VALUE, null, true);

			if (fluidActionResult.isSuccess()) {
				drops.add(fluidActionResult.getResult());
			}
		}

		return drops;
	}

	/**
	 * Get the {@link IFluidHandler} from the {@link TileEntity} at the specified position.
	 *
	 * @param world The world
	 * @param pos   The position
	 * @return The IFluidHandler
	 */
	@Nullable
	private IFluidHandler getFluidHandler(final IBlockAccess world, final BlockPos pos) {
		return CapabilityUtils.getCapability(getTileEntity(world, pos), CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null);
	}

	@Override
	public void onBlockPlacedBy(final World worldIn, final BlockPos pos, final IBlockState state, final EntityLivingBase placer, final ItemStack stack) {
		final IFluidHandler fluidHandler = getFluidHandler(worldIn, pos);
		if (fluidHandler != null) {
			FluidUtil.tryEmptyContainer(stack, fluidHandler, Integer.MAX_VALUE, null, true);
		}
	}

	@Override
	public TileEntity createTileEntity(final World world, final IBlockState state) {
		return new TileEntityFluidTank();
	}

	public static List<ITextComponent> getFluidDataForDisplay(final IFluidTankProperties[] fluidTankProperties) {
		final List<ITextComponent> data = new ArrayList<>();

		boolean hasFluid = false;

		for (final IFluidTankProperties properties : fluidTankProperties) {
			final FluidStack fluidStack = properties.getContents();

			if (fluidStack != null && fluidStack.amount > 0) {
				hasFluid = true;
				data.add(new TextComponentTranslation("tile.testmod3:fluid_tank.fluid.desc", fluidStack.getLocalizedName(), fluidStack.amount, properties.getCapacity()));
			}
		}

		if (!hasFluid) {
			data.add(new TextComponentTranslation("tile.testmod3:fluid_tank.empty.desc"));
		}

		return data;
	}

	@Override
	public boolean onBlockActivated(final World worldIn, final BlockPos pos, final IBlockState state, final EntityPlayer playerIn, final EnumHand hand, final EnumFacing side, final float hitX, final float hitY, final float hitZ) {
		final ItemStack heldItem = playerIn.getHeldItem(hand);
		final IFluidHandler fluidHandler = getFluidHandler(worldIn, pos);

		if (fluidHandler != null) {
			// Try fill/empty the held fluid container from the tank
			final boolean success = FluidUtil.interactWithFluidHandler(playerIn, hand, worldIn, pos, side);

			// If the contents changed or this is the off hand, send a chat message to the player
			if (!worldIn.isRemote && (success || hand == EnumHand.OFF_HAND)) {
				TestMod3.network.sendTo(new MessageFluidTankContents(fluidHandler.getTankProperties()), (EntityPlayerMP) playerIn);
			}

			// If the held item is a fluid container, stop processing here so it doesn't try to place its contents
			return heldItem.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null);
		}

		return false;
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean isOpaqueCube(final IBlockState state) {
		return false;
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean isFullCube(final IBlockState state) {
		return false;
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean isTopSolid(final IBlockState state) {
		return false;
	}

	@SuppressWarnings("deprecation")
	@SideOnly(Side.CLIENT)
	@Override
	public boolean shouldSideBeRendered(final IBlockState blockState, final IBlockAccess blockAccess, final BlockPos pos, final EnumFacing side) {
		return true;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.CUTOUT;
	}
}
