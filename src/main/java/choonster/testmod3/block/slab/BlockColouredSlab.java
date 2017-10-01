package choonster.testmod3.block.slab;

import com.google.common.collect.ImmutableList;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.oredict.DyeUtils;
import net.minecraftforge.registries.IForgeRegistry;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
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
public abstract class BlockColouredSlab extends BlockSlabTestMod3<EnumDyeColor, BlockColouredSlab.EnumColourGroup, BlockColouredSlab> {

	/**
	 * Create a coloured slab block.
	 *
	 * @param material    The Material of this slab
	 * @param colourGroup This slab's colour group
	 * @param slabGroup   The group this slab belongs to
	 */
	public BlockColouredSlab(final Material material, final EnumColourGroup colourGroup, final SlabGroup<EnumDyeColor, EnumColourGroup, BlockColouredSlab> slabGroup) {
		super(material, slabGroup, colourGroup);
	}

	@SuppressWarnings("deprecation")
	@Override
	public MapColor getMapColor(final IBlockState state, final IBlockAccess world, final BlockPos pos) {
		return MapColor.getBlockColor(state.getValue(getVariantProperty()));
	}

	@Override
	public String getUnlocalizedName(final int meta) {
		return getUnlocalizedName() + "." + getVariant(meta).getUnlocalizedName();
	}

	/**
	 * Get the metadata value for the specified colour
	 *
	 * @param colour The colour
	 * @return The metadata value
	 */
	@Override
	public int getMetadata(final EnumDyeColor colour) {
		return variants.getOffsetMetadata(colour);
	}

	/**
	 * Get the colour for the specified metadata value
	 *
	 * @param meta The metadata value
	 * @return The colour
	 */
	@Override
	protected EnumDyeColor getVariant(final int meta) {
		return variants.byOffsetMetadata(meta);
	}

	@Override
	public boolean recolorBlock(final World world, final BlockPos pos, final EnumFacing side, final EnumDyeColor colour) {
		final BlockSlabTestMod3<EnumDyeColor, EnumColourGroup, BlockColouredSlab> slabBlock;
		final IBlockState baseState;

		if (variants.isColourInGroup(colour)) { // If the new colour is in this Block's colour group, use the current state as the base state
			slabBlock = this;
			baseState = world.getBlockState(pos);

			if (baseState.getValue(getVariantProperty()) == colour) { // If the new colour is the same as the current colour, do nothing
				return false;
			}
		} else { // If the new colour isn't in this Block's colour group, get the appropriate Block from the slab group container
			final SlabGroup<EnumDyeColor, EnumColourGroup, BlockColouredSlab> slabGroup = this.slabGroup.getParentContainer().getSlabGroupForVariant(colour);
			slabBlock = isDouble() ? slabGroup.getDoubleSlab() : slabGroup.getSingleSlab();
			baseState = slabBlock.getDefaultState();
		}

		return world.setBlockState(pos, baseState.withProperty(slabBlock.getVariantProperty(), colour));
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

	@Override
	public EnumDyeColor getTypeForItem(final ItemStack stack) {
		return variants.byOffsetMetadata(stack.getMetadata());
	}

	/**
	 * A group of {@link EnumDyeColor} values.
	 */
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

	public static class ColouredSlabGroupContainer implements ISlabGroupContainer<EnumDyeColor, EnumColourGroup, BlockColouredSlab> {
		public final SlabGroup<EnumDyeColor, EnumColourGroup, BlockColouredSlab> low;
		public final SlabGroup<EnumDyeColor, EnumColourGroup, BlockColouredSlab> high;

		/**
		 * Create a coloured slab group container.
		 *
		 * @param groupName The group's name
		 * @param material  The Material of the slabs
		 */
		public ColouredSlabGroupContainer(final String groupName, final Material material) {
			low = createGroup(groupName, material, EnumColourGroup.LOW);
			high = createGroup(groupName, material, EnumColourGroup.HIGH);
		}

		/**
		 * Create a slab group.
		 *
		 * @param groupName   The group's name
		 * @param material    The Material of the slabs
		 * @param colourGroup The colour group
		 * @return The slab group
		 */
		private SlabGroup<EnumDyeColor, EnumColourGroup, BlockColouredSlab> createGroup(final String groupName, final Material material, final EnumColourGroup colourGroup) {
			return new SlabGroup<EnumDyeColor, EnumColourGroup, BlockColouredSlab>(groupName, material, colourGroup, this) {
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
		 * Get the slab group containing the specified variant.
		 *
		 * @param enumDyeColor The variant
		 * @return The slab group
		 */
		@Override
		public SlabGroup<EnumDyeColor, EnumColourGroup, BlockColouredSlab> getSlabGroupForVariant(final EnumDyeColor enumDyeColor) {
			return EnumColourGroup.LOW.isColourInGroup(enumDyeColor) ? low : high;
		}

		/**
		 * Register this container's {@link Block}s.
		 *
		 * @param registry The Block registry
		 * @throws IllegalStateException If the Blocks have already been registered
		 */
		@Override
		public void registerBlocks(final IForgeRegistry<Block> registry) {
			low.registerBlocks(registry);
			high.registerBlocks(registry);
		}

		/**
		 * Register this container's {@link Item}s.
		 *
		 * @param registry The Item registry
		 * @return The registered Items
		 * @throws IllegalStateException If the Items have already been registered
		 */
		@Override
		public List<ItemBlock> registerItems(final IForgeRegistry<Item> registry) {
			low.registerItem(registry);
			high.registerItem(registry);
			return ImmutableList.of(low.getItem(), high.getItem());
		}
	}
}
