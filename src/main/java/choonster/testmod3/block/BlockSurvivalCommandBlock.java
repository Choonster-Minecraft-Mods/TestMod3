package choonster.testmod3.block;

import choonster.testmod3.TestMod3;
import choonster.testmod3.tileentity.TileEntitySurvivalCommandBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCommandBlock;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityCommandBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;
import java.util.Random;

/**
 * A Command Block that's accessible outside of Creative Mode.
 * <p>
 * Test for this thread:
 * http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/modification-development/2540671-command-block-replica-issue
 *
 * @author Choonster
 */
public class BlockSurvivalCommandBlock extends BlockCommandBlock {
	private final TileEntityCommandBlock.Mode commandBlockMode;

	public BlockSurvivalCommandBlock(TileEntityCommandBlock.Mode commandBlockMode, String name) {
		super(MapColor.BROWN);
		this.commandBlockMode = commandBlockMode;
		setCreativeTab(TestMod3.creativeTab);
		BlockTestMod3.setBlockName(this, name);
		setHardness(5);
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		final TileEntitySurvivalCommandBlock tileEntity = new TileEntitySurvivalCommandBlock();
		tileEntity.setAuto(getCommandBlockMode() == TileEntityCommandBlock.Mode.SEQUENCE);
		return tileEntity;
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, @Nullable ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
		final TileEntity tileEntity = worldIn.getTileEntity(pos);
		return tileEntity instanceof TileEntitySurvivalCommandBlock && ((TileEntitySurvivalCommandBlock) tileEntity).getCommandBlockLogic().tryOpenEditCommandBlock(playerIn);
	}

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		final TileEntity tileEntity = worldIn.getTileEntity(pos);
		if (tileEntity instanceof TileEntitySurvivalCommandBlock && !worldIn.isRemote) {
			final TileEntitySurvivalCommandBlock tileEntitySurvivalCommandBlock = (TileEntitySurvivalCommandBlock) tileEntity;

			final NBTTagCompound tagCompound = stack.getTagCompound();
			if (tagCompound == null || !tagCompound.hasKey("BlockEntityTag", Constants.NBT.TAG_COMPOUND)) {
				tileEntitySurvivalCommandBlock.setAuto(getCommandBlockMode() == TileEntityCommandBlock.Mode.SEQUENCE);
			}
		}

		super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
	}

	@Override
	public int quantityDropped(Random random) {
		return 1;
	}

	/**
	 * Called when a Command Block in Impulse/Repeat commandBlockMode is triggered to propagate the update to chained Command Blocks.
	 * <p>
	 * Uses {@link TileEntityCommandBlock#getMode()} instead of comparing instances to support both Vanilla and Survival Command Blocks.
	 *
	 * @param worldIn The Command Block's World
	 * @param pos     The Command Block's position
	 */
	@Override
	public void propagateUpdate(World worldIn, BlockPos pos) {
		final IBlockState iblockstate = worldIn.getBlockState(pos);

		final TileEntity tileEntity = worldIn.getTileEntity(pos);
		if (!(tileEntity instanceof TileEntityCommandBlock)) return;

		final TileEntityCommandBlock tileEntityCommandBlock = (TileEntityCommandBlock) tileEntity;
		if (tileEntityCommandBlock.getMode() != TileEntityCommandBlock.Mode.REDSTONE && tileEntityCommandBlock.getMode() != TileEntityCommandBlock.Mode.AUTO)
			return;

		final BlockPos.MutableBlockPos neighbourPos = new BlockPos.MutableBlockPos(pos);
		neighbourPos.move(iblockstate.getValue(FACING));

		for (TileEntity neighbourTileEntity = worldIn.getTileEntity(neighbourPos); neighbourTileEntity instanceof TileEntityCommandBlock; neighbourTileEntity = worldIn.getTileEntity(neighbourPos)) {
			final TileEntityCommandBlock neighbourTileEntityCommandBlock = (TileEntityCommandBlock) neighbourTileEntity;

			if (neighbourTileEntityCommandBlock.getMode() != TileEntityCommandBlock.Mode.SEQUENCE) {
				break;
			}

			final IBlockState neighbourState = worldIn.getBlockState(neighbourPos);
			final Block neighbourBlock = neighbourState.getBlock();

			if (worldIn.isUpdateScheduled(neighbourPos, neighbourBlock)) {
				break;
			}

			worldIn.scheduleUpdate(neighbourPos.toImmutable(), neighbourBlock, tickRate(worldIn));
			neighbourPos.move(neighbourState.getValue(FACING));
		}
	}

	public TileEntityCommandBlock.Mode getCommandBlockMode() {
		return commandBlockMode;
	}
}
