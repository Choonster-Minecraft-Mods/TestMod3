package choonster.testmod3.world.level.block.slab;

import choonster.testmod3.world.level.block.variantgroup.BlockVariantGroup;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

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

	private boolean recolorBlock(final BlockState state, final LevelAccessor world, final BlockPos pos, final Direction facing, final DyeColor colour) {
		final BlockState newState = variantGroup.getBlock(colour).get().defaultBlockState()
				.setValue(TYPE, state.getValue(TYPE));

		return world.setBlock(pos, newState, 3);
	}

	@SuppressWarnings("deprecation")
	@Override
	public InteractionResult use(final BlockState state, final Level world, final BlockPos pos, final Player player, final InteractionHand hand, final BlockHitResult rayTraceResult) {
		final ItemStack heldItem = player.getItemInHand(hand);

		if (!heldItem.isEmpty()) {

			final DyeColor dyeColour = DyeColor.getColor(heldItem);
			if (dyeColour != null) {
				final boolean success = recolorBlock(state, world, pos, rayTraceResult.getDirection(), dyeColour);
				if (success) {
					heldItem.shrink(1);
					return InteractionResult.SUCCESS;
				}
			}
		}

		return InteractionResult.FAIL;
	}
}
