package choonster.testmod3.block;

import choonster.testmod3.TestMod3;
import choonster.testmod3.util.Constants;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemSlab;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Base class for this mod's slab blocks.
 *
 * @param <VARIANT>  The variant type
 * @param <VARIANTS> The variant collection type
 * @param <SLAB>     The slab type
 * @author Choonster
 */
public abstract class BlockSlabTestMod3<
		VARIANT extends Enum<VARIANT> & IStringSerializable,
		VARIANTS extends Iterable<VARIANT> & IStringSerializable,
		SLAB extends BlockSlabTestMod3<VARIANT, VARIANTS, SLAB>
		> extends BlockSlab {

	/**
	 * The group this slab belongs to
	 */
	protected final SlabGroup<VARIANT, VARIANTS, SLAB> slabGroup;

	/**
	 * The variants of this slab
	 */
	protected final VARIANTS variants;

	/**
	 * Create a slab block.
	 *
	 * @param material  The Material of this slab
	 * @param slabGroup The group this slab belongs to
	 * @param variants  The variants of this slab
	 */
	public BlockSlabTestMod3(Material material, SlabGroup<VARIANT, VARIANTS, SLAB> slabGroup, VARIANTS variants) {
		super(material);
		this.slabGroup = slabGroup;
		this.variants = variants;

		// Vanilla sets this for anything that extends BlockSlab in Block.registerBlocks,
		// but this is run before mods are loaded; so we need to set it manually.
		// Thanks to HappyKiller1O1 for pointing out this field to me:
		// http://www.minecraftforge.net/forum/index.php/topic,36125.msg190252.html#msg190252
		this.useNeighborBrightness = true;

		String name = slabGroup.groupName + StringUtils.capitalize(variants.getName());
		if (isDouble()) {
			name += "Double";
		}

		setRegistryName(name);
		setUnlocalizedName(Constants.RESOURCE_PREFIX + slabGroup.groupName);

		IBlockState iblockstate = this.blockState.getBaseState();

		if (!isDouble()) {
			iblockstate = iblockstate.withProperty(HALF, BlockSlab.EnumBlockHalf.BOTTOM);
		}

		final Optional<VARIANT> defaultVariant = getVariantProperty().getAllowedValues().stream().findFirst();
		if (defaultVariant.isPresent()) {
			iblockstate = iblockstate.withProperty(getVariantProperty(), defaultVariant.get());
		}

		setDefaultState(iblockstate);

		setCreativeTab(TestMod3.creativeTab);
	}

	/**
	 * Get the metadata value for the specified variant
	 *
	 * @param variant The variant
	 * @return The metadata value
	 */
	protected abstract int getMetadata(VARIANT variant);

	/**
	 * Get the variant for the specified metadata value
	 *
	 * @param meta The metadata value
	 * @return The variant
	 */
	protected abstract VARIANT getVariant(int meta);

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return slabGroup.getItem();
	}

	@Override
	public abstract IProperty<VARIANT> getVariantProperty();

	@SideOnly(Side.CLIENT)
	@Override
	public void getSubBlocks(Item item, CreativeTabs tab, List<ItemStack> list) {
		list.addAll(getVariantProperty().getAllowedValues().stream()
				.map(variant -> new ItemStack(item, 1, getMetadata(variant)))
				.collect(Collectors.toList()));
	}

	@SuppressWarnings("deprecation")
	@Override
	public IBlockState getStateFromMeta(int meta) {
		final VARIANT variant = getVariant(meta & 7);
		IBlockState state = this.getDefaultState().withProperty(getVariantProperty(), variant);

		if (!this.isDouble()) {
			state = state.withProperty(HALF, (meta & 8) == 0 ? EnumBlockHalf.BOTTOM : EnumBlockHalf.TOP);
		}

		return state;
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		int meta = getMetadata(state.getValue(getVariantProperty()));

		if (!this.isDouble() && state.getValue(HALF) == EnumBlockHalf.TOP) {
			meta |= 8;
		}

		return meta;
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return isDouble() ? new BlockStateContainer(this, getVariantProperty()) : new BlockStateContainer(this, HALF, getVariantProperty());
	}

	@Override
	public int damageDropped(IBlockState state) {
		return getMetadata(state.getValue(getVariantProperty()));
	}

	/**
	 * A group consisting of a single and a double slab.
	 *
	 * @param <VARIANT>  The variant type
	 * @param <VARIANTS> The variant collection type
	 * @param <SLAB>     The slab type
	 */
	public static abstract class SlabGroup<
			VARIANT extends Enum<VARIANT> & IStringSerializable,
			VARIANTS extends Iterable<VARIANT> & IStringSerializable,
			SLAB extends BlockSlabTestMod3<VARIANT, VARIANTS, SLAB>
			> {

		public final SLAB singleSlab;
		public final SLAB doubleSlab;
		public final String groupName;
		private ItemSlab item;

		/**
		 * Create a slab group.
		 *
		 * @param groupName The group's name
		 * @param material  The Material of the slabs
		 * @param variants  The variants of the slabs
		 */
		public SlabGroup(String groupName, Material material, VARIANTS variants) {
			this.groupName = groupName;
			this.singleSlab = createSlab(material, false, variants);
			this.doubleSlab = createSlab(material, true, variants);
		}

		/**
		 * Create a slab block.
		 *
		 * @param material The Material
		 * @param isDouble Is this a double slab?
		 * @param variants The variants
		 * @return The slab block
		 */
		public abstract SLAB createSlab(Material material, boolean isDouble, VARIANTS variants);

		public ItemSlab getItem() {
			return item;
		}

		public void setItem(ItemSlab item) {
			this.item = item;
		}
	}
}
