package choonster.testmod3.block;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

/**
 * A rotatable lamp block.
 * <p>
 * Example for this thread:
 * http://www.minecraftforge.net/forum/index.php/topic,33716.0.html
 *
 * @author Choonster
 */
public class BlockRotatableLamp extends BlockTestMod3 {
	public static final IProperty<EnumFacing> FACING = PropertyDirection.create("facing");
	public static final IProperty<Boolean> LIT = PropertyBool.create("lit");

	public BlockRotatableLamp() {
		super(Material.REDSTONE_LIGHT, "rotatable_lamp");
		setDefaultState(getBlockState().getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(LIT, false));
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, FACING, LIT);
	}

	@Override
	public int getMetaFromState(final IBlockState state) {
		final int facingIndex = state.getValue(FACING).getIndex(); // Convert the EnumFacing to its index
		final int lit = state.getValue(LIT) ? 1 : 0; // Convert the lit state boolean to 1 or 0

		// Shift lit left three bits so it occupies the highest bit then OR it with the facing index (which occupies the lowest three bits)
		return lit << 3 | facingIndex;
	}

	@SuppressWarnings("deprecation")
	@Override
	public IBlockState getStateFromMeta(final int meta) {
		final int facingIndex = meta & 7; // Extract the facing index (lowest three bits)
		final EnumFacing facing = EnumFacing.byIndex(facingIndex); // Convert it to the corresponding EnumFacing

		final boolean lit = (meta & 8) != 0; // Extract the lit state (highest bit)

		return getDefaultState().withProperty(FACING, facing).withProperty(LIT, lit);
	}

	@Override
	public IBlockState getStateForPlacement(final World world, final BlockPos pos, final EnumFacing facing, final float hitX, final float hitY, final float hitZ, final int meta, final EntityLivingBase placer, final EnumHand hand) {
		final EnumFacing newFacing = EnumFacing.getDirectionFromEntityLiving(pos, placer);

		return super.getStateForPlacement(world, pos, facing, hitX, hitY, hitZ, meta, placer, hand).withProperty(FACING, newFacing);
	}

	@Override
	public boolean onBlockActivated(final World worldIn, final BlockPos pos, final IBlockState state, final EntityPlayer playerIn, final EnumHand hand, final EnumFacing side, final float hitX, final float hitY, final float hitZ) {
		final IBlockState newState;

		if (playerIn.isSneaking()) {
			newState = state.cycleProperty(FACING); // Cycle the facing (down -> up -> north -> south -> west -> east -> down)
		} else {
			newState = state.cycleProperty(LIT); // Cycle the lit state (true -> false -> true)
		}

		worldIn.setBlockState(pos, newState);

		return true;
	}

	@Override
	public int getLightValue(final IBlockState state, final IBlockAccess world, final BlockPos pos) {
		return state.getValue(LIT) ? 15 : 0;
	}
}
