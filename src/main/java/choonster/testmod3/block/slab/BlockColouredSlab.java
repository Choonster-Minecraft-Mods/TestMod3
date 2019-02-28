package choonster.testmod3.block.slab;

import choonster.testmod3.block.variantgroup.BlockVariantGroup;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

/**
 * A slab that uses vanilla's dye colours.
 * <p>
 * Test for this thread:
 * http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/modification-development/2597500-how-do-you-create-a-halfslab
 *
 * @author Choonster
 */
public class BlockColouredSlab extends BlockSlabTestMod3<EnumDyeColor, BlockColouredSlab> {
	/**
	 * Create a coloured slab block.
	 *
	 * @param variant      The variant of this slab
	 * @param variantGroup The group this slab belongs to
	 * @param properties   The block properties of this slab
	 */
	public BlockColouredSlab(final EnumDyeColor variant, final BlockVariantGroup<EnumDyeColor, BlockColouredSlab> variantGroup, final Block.Properties properties) {
		super(variant, variantGroup, properties);
	}

	@Override
	public boolean recolorBlock(final IBlockState state, final IWorld world, final BlockPos pos, final EnumFacing facing, final EnumDyeColor colour) {
		final IBlockState newState = variantGroup.getBlock(colour).getDefaultState()
				.with(TYPE, state.get(TYPE));

		return world.setBlockState(pos, newState, 3);
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean onBlockActivated(final IBlockState state, final World world, final BlockPos pos, final EntityPlayer player, final EnumHand hand, final EnumFacing side, final float hitX, final float hitY, final float hitZ) {
		final ItemStack heldItem = player.getHeldItem(hand);

		if (!heldItem.isEmpty()) {

			final EnumDyeColor dyeColour = EnumDyeColor.getColor(heldItem);
			if (dyeColour != null) {
				final boolean success = recolorBlock(state, world, pos, side, dyeColour);
				if (success) {
					heldItem.shrink(1);
					return true;
				}
			}
		}

		return false;
	}
}
