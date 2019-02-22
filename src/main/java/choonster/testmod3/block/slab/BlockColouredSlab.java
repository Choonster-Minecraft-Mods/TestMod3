package choonster.testmod3.block.slab;

import choonster.testmod3.block.variantgroup.SlabVariantGroup;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.oredict.DyeUtils;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Iterator;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * A slab that uses vanilla's dye colours.
 * <p>
 * Test for this thread:
 * http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/modification-development/2597500-how-do-you-create-a-halfslab
 *
 * @author Choonster
 */
public abstract class BlockColouredSlab extends BlockSlabTestMod3<EnumDyeColor, BlockColouredSlab> {

	/**
	 * Create a coloured slab block.
	 *
	 * @param variant   The variant of this slab
	 * @param material  The Material of this slab
	 * @param slabGroup The group this slab belongs to
	 */
	public BlockColouredSlab(final EnumDyeColor variant, final Material material, final SlabVariantGroup<EnumDyeColor, BlockColouredSlab>.SlabGroup slabGroup) {
		super(variant, material, MapColor.getBlockColor(variant), slabGroup);
	}

	@Override
	public boolean recolorBlock(final World world, final BlockPos pos, final EnumFacing side, final EnumDyeColor colour) {
		final IBlockState newState;

		final SlabVariantGroup<EnumDyeColor, BlockColouredSlab>.SlabGroup newSlabGroup = slabGroup.getVariantGroup().getSlabGroup(colour);

		if (isDouble()) {
			newState = newSlabGroup.getDoubleSlab().getDefaultState();
		} else {
			final IBlockState currentState = world.getBlockState(pos);

			newState = newSlabGroup.getSingleSlab().getDefaultState().withProperty(HALF, currentState.getValue(HALF));
		}

		return world.setBlockState(pos, newState);
	}

	@Override
	public boolean onBlockActivated(final World worldIn, final BlockPos pos, final IBlockState state, final EntityPlayer playerIn, final EnumHand hand, final EnumFacing side, final float hitX, final float hitY, final float hitZ) {
		final ItemStack heldItem = playerIn.getHeldItem(hand);

		if (!heldItem.isEmpty()) {
			final Optional<EnumDyeColor> dyeColour = DyeUtils.colorFromStack(heldItem);
			if (dyeColour.isPresent()) {
				final boolean success = recolorBlock(worldIn, pos, side, dyeColour.get());
				if (success) {
					heldItem.shrink(1);
					return true;
				}
			}
		}

		return false;
	}

	/**
	 * A group of {@link EnumDyeColor} values.
	 */
	// TODO: Remove in 1.13
	@Deprecated
	public enum EnumColourGroup implements Iterable<EnumDyeColor>, IStringSerializable {
		LOW("low", colour -> colour.getMetadata() < 8, 0),
		HIGH("high", colour -> colour.getMetadata() >= 8, 8);

		/**
		 * The property with this group's colours as the allowed values
		 */
		public final IProperty<EnumDyeColor> property;

		/**
		 * The offset to subtract from each colour's metadata value
		 */
		private final int metaOffset;

		/**
		 * The name of this group
		 */
		private final String name;

		/**
		 * The colours in this group
		 */
		private final Collection<EnumDyeColor> values;

		/**
		 * Create a colour group
		 *
		 * @param name         The name of this group
		 * @param colourFilter A filter to obtain the colours in this group
		 * @param metaOffset   The offset to subtract from each colour's metadata value
		 */
		EnumColourGroup(final String name, final Predicate<EnumDyeColor> colourFilter, final int metaOffset) {
			this.name = name;
			this.property = PropertyEnum.create("colour", EnumDyeColor.class, colourFilter::test);
			this.metaOffset = metaOffset;
			this.values = this.property.getAllowedValues();
		}

		/**
		 * Get the metadata value of the specified colour with this group's offset.
		 *
		 * @param colour The colour
		 * @return The offset metadata value
		 */
		public int getOffsetMetadata(final EnumDyeColor colour) {
			return colour.getMetadata() - metaOffset;
		}

		/**
		 * Get the colour of the specified metadata value with this group's offset
		 *
		 * @param meta The offset metadata value
		 * @return The colour
		 */
		public EnumDyeColor byOffsetMetadata(final int meta) {
			return EnumDyeColor.byMetadata(meta + metaOffset);
		}

		/**
		 * Returns an iterator over elements of type {@code T}.
		 *
		 * @return an Iterator.
		 */
		@Override
		public Iterator<EnumDyeColor> iterator() {
			return values.iterator();
		}

		@Override
		public String getName() {
			return name;
		}

		/**
		 * Is the specified colour in this group?
		 *
		 * @param colour The colour
		 * @return True if the colour is in this group
		 */
		public boolean isColourInGroup(final EnumDyeColor colour) {
			return values.contains(colour);
		}

		/**
		 * Get the first group containing the specified colour.
		 *
		 * @param colour The colour
		 * @return A group containing the colour, or null if there isn't one
		 */
		@Nullable
		public static EnumColourGroup getGroupForColour(final EnumDyeColor colour) {
			for (final EnumColourGroup colourGroup : values()) {
				if (colourGroup.isColourInGroup(colour)) {
					return colourGroup;
				}
			}

			return null;
		}
	}
}
