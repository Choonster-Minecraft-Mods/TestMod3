package com.choonster.testmod3.block;

import com.google.common.base.Predicate;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.world.World;

import java.util.Collection;
import java.util.Iterator;

/**
 * A slab that uses vanilla's dye colours.
 * <p>
 * Test for this thread:
 * http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/modification-development/2597500-how-do-you-create-a-halfslab
 *
 * @author Choonster
 */
public abstract class BlockColouredSlab extends BlockSlabTestMod3<EnumDyeColor, BlockColouredSlab.EnumColourGroup, BlockColouredSlab> {

	/**
	 * Create a coloured slab block.
	 *
	 * @param material    The Material of this slab
	 * @param colourGroup This slab's colour group
	 * @param slabGroup   The group this slab belongs to
	 */
	public BlockColouredSlab(Material material, EnumColourGroup colourGroup, SlabGroup<EnumDyeColor, EnumColourGroup, BlockColouredSlab> slabGroup) {
		super(material, slabGroup, colourGroup);
	}

	@Override
	public MapColor getMapColor(IBlockState state) {
		return state.getValue(getVariantProperty()).getMapColor();
	}

	@Override
	public String getUnlocalizedName(int meta) {
		return getUnlocalizedName() + "." + getVariant(meta).getUnlocalizedName();
	}

	/**
	 * Get the metadata value for the specified colour
	 *
	 * @param colour The colour
	 * @return The metadata value
	 */
	@Override
	protected int getMetadata(EnumDyeColor colour) {
		return variants.getOffsetMetadata(colour);
	}

	/**
	 * Get the colour for the specified metadata value
	 *
	 * @param meta The metadata value
	 * @return The colour
	 */
	@Override
	protected EnumDyeColor getVariant(int meta) {
		return variants.byOffsetMetadata(meta);
	}

	@Override
	public boolean recolorBlock(World world, BlockPos pos, EnumFacing side, EnumDyeColor colour) {
		return variants.isColourInGroup(colour) && world.setBlockState(pos, world.getBlockState(pos).withProperty(getVariantProperty(), colour));
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ) {
		final ItemStack heldItem = playerIn.getHeldItem();
		return heldItem != null && heldItem.getItem() == Items.dye && recolorBlock(worldIn, pos, side, EnumDyeColor.byDyeDamage(heldItem.getMetadata()));
	}

	/**
	 * A group of {@link EnumDyeColor} values.
	 */
	public enum EnumColourGroup implements Iterable<EnumDyeColor>, IStringSerializable {
		LOW("Low", colour -> colour.getMetadata() < 8, 0),
		HIGH("High", colour -> colour.getMetadata() >= 8, 8);

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
		EnumColourGroup(String name, Predicate<EnumDyeColor> colourFilter, int metaOffset) {
			this.name = name;
			this.property = PropertyEnum.create("colour", EnumDyeColor.class, colourFilter);
			this.metaOffset = metaOffset;
			this.values = this.property.getAllowedValues();
		}

		/**
		 * Get the metadata value of the specified colour with this group's offset.
		 *
		 * @param colour The colour
		 * @return The offset metadata value
		 */
		public int getOffsetMetadata(EnumDyeColor colour) {
			return colour.getMetadata() - metaOffset;
		}

		/**
		 * Get the colour of the specified metadata value with this group's offset
		 *
		 * @param meta The offset metadata value
		 * @return The colour
		 */
		public EnumDyeColor byOffsetMetadata(int meta) {
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
		public boolean isColourInGroup(EnumDyeColor colour){
			return values.contains(colour);
		}

		/**
		 * Get the first group containing the specified colour.
		 *
		 * @param colour The colour
		 * @return A group containing the colour, or null if there isn't one
		 */
		public static EnumColourGroup getGroupForColour(EnumDyeColor colour) {
			for (EnumColourGroup colourGroup : values()) {
				if (colourGroup.isColourInGroup(colour)) {
					return colourGroup;
				}
			}

			return null;
		}
	}

	public static class ColouredSlabGroup {
		public final SlabGroup<EnumDyeColor, EnumColourGroup, BlockColouredSlab> low;
		public final SlabGroup<EnumDyeColor, EnumColourGroup, BlockColouredSlab> high;

		/**
		 * Create a coloured slab group.
		 *
		 * @param groupName   The group's name
		 * @param material    The Material of the slabs
		 */
		public ColouredSlabGroup(String groupName, Material material) {
			low = createGroup(groupName, material, EnumColourGroup.LOW);
			high = createGroup(groupName, material, EnumColourGroup.HIGH);
		}

		/**
		 * Create a slab group.
		 *
		 * @param groupName The group's name
		 * @param material The Material of the slabs
		 * @param colourGroup The colour group
		 * @return The slab group
		 */
		private SlabGroup<EnumDyeColor, EnumColourGroup, BlockColouredSlab> createGroup(String groupName, Material material, EnumColourGroup colourGroup) {
			return new SlabGroup<EnumDyeColor, EnumColourGroup, BlockColouredSlab>(groupName, material, colourGroup) {
				@Override
				public BlockColouredSlab createSlab(final Material material, final boolean isDouble, final EnumColourGroup colourGroup) {
					return new BlockColouredSlab(material, colourGroup, this) {
						@Override
						public boolean isDouble() {
							return isDouble;
						}

						@Override
						public IProperty<EnumDyeColor> getVariantProperty() {
							return colourGroup.property;
						}
					};
				}
			};
		}

		/**
		 * Get the slab group for the specified colour group.
		 *
		 * @param colourGroup The colour group
		 * @return The slab group
		 */
		public SlabGroup<EnumDyeColor, EnumColourGroup, BlockColouredSlab> getSlabGroupByColourGroup(EnumColourGroup colourGroup) {
			return colourGroup == EnumColourGroup.LOW ? low : high;
		}
	}
}
