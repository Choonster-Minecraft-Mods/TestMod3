package choonster.testmod3.block.slab;

import choonster.testmod3.block.variantgroup.BlockVariantGroup;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
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
public class ColouredSlabBlock extends TestMod3SlabBlock<DyeColor, ColouredSlabBlock> {
	/**
	 * Create a coloured slab block.
	 *
	 * @param variant      The variant of this slab
	 * @param variantGroup The group this slab belongs to
	 * @param properties   The block properties of this slab
	 */
	public ColouredSlabBlock(final DyeColor variant, final BlockVariantGroup<DyeColor, ColouredSlabBlock> variantGroup, final Block.Properties properties) {
		super(variant, variantGroup, properties);
	}

	private boolean recolorBlock(final BlockState state, final IWorld world, final BlockPos pos, final Direction facing, final DyeColor colour) {
		final BlockState newState = variantGroup.getBlock(colour).get().getDefaultState()
				.with(TYPE, state.get(TYPE));

		return world.setBlockState(pos, newState, 3);
	}

	@SuppressWarnings("deprecation")
	@Override
	public ActionResultType onBlockActivated(final BlockState state, final World world, final BlockPos pos, final PlayerEntity player, final Hand hand, final BlockRayTraceResult rayTraceResult) {
		final ItemStack heldItem = player.getHeldItem(hand);

		if (!heldItem.isEmpty()) {

			final DyeColor dyeColour = DyeColor.getColor(heldItem);
			if (dyeColour != null) {
				final boolean success = recolorBlock(state, world, pos, rayTraceResult.getFace(), dyeColour);
				if (success) {
					heldItem.shrink(1);
					return ActionResultType.SUCCESS;
				}
			}
		}

		return ActionResultType.FAIL;
	}
}
