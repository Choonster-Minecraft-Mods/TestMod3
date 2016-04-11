package com.choonster.testmod3.block;

import com.choonster.testmod3.TestMod3;
import net.minecraft.block.BlockGlass;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;

/**
 * A Block that changes state when right clicked with an Eye of Ender.
 * <p>
 * Test for this thread:
 * http://www.minecraftforge.net/forum/index.php/topic,32685.0.html
 *
 * @author Choonster
 */
public class BlockRightClickTest extends BlockGlass {
	public static final IProperty<Boolean> HAS_ENDER_EYE = PropertyBool.create("has_ender_eye");

	public BlockRightClickTest() {
		super(Material.GLASS, false);
		setDefaultState(blockState.getBaseState().withProperty(HAS_ENDER_EYE, false));
		BlockTestMod3.setBlockName(this, "rightClickTest");
		setCreativeTab(TestMod3.creativeTab);
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, HAS_ENDER_EYE);
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(HAS_ENDER_EYE, meta > 0);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(HAS_ENDER_EYE) ? 1 : 0;
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
		if (heldItem != null && heldItem.getItem() == Items.ENDER_EYE && !state.getValue(HAS_ENDER_EYE)) {
			if (!playerIn.capabilities.isCreativeMode) {
				heldItem.stackSize--;
			}

			worldIn.setBlockState(pos, state.withProperty(HAS_ENDER_EYE, true));
			return true;
		}

		return super.onBlockActivated(worldIn, pos, state, playerIn, hand, heldItem, side, hitX, hitY, hitZ);
	}

	@Override
	public int quantityDropped(Random random) {
		return 1;
	}

	@Override
	public int damageDropped(IBlockState state) {
		return 0;
	}

	@Override
	public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
		List<ItemStack> drops = super.getDrops(world, pos, state, fortune);

		if (state.getValue(HAS_ENDER_EYE)) {
			drops.add(new ItemStack(Items.ENDER_EYE));
		}

		return drops;
	}
}
