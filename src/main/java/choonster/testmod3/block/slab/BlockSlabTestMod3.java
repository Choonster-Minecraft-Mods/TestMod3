package choonster.testmod3.block.slab;

import choonster.testmod3.TestMod3;
import choonster.testmod3.util.Constants;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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
	public BlockSlabTestMod3(final Material material, final SlabGroup<VARIANT, VARIANTS, SLAB> slabGroup, final VARIANTS variants) {
		super(material);
		this.slabGroup = slabGroup;
		this.variants = variants;

		// Vanilla sets this for anything that extends BlockSlab in Block.registerBlocks,
		// but this is run before mods are loaded; so we need to set it manually.
		// Thanks to HappyKiller1O1 for pointing out this field to me:
		// http://www.minecraftforge.net/forum/index.php/topic,36125.msg190252.html#msg190252
		this.useNeighborBrightness = true;

		String name = slabGroup.groupName + "_" + variants.getName();
		if (isDouble()) {
			name = "double_" + name;
		}

		setRegistryName(TestMod3.MODID, name);
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
	public abstract int getMetadata(VARIANT variant);

	/**
	 * Get the variant for the specified metadata value
	 *
	 * @param meta The metadata value
	 * @return The variant
	 */
	protected abstract VARIANT getVariant(int meta);

	@Override
	public Item getItemDropped(final IBlockState state, final Random rand, final int fortune) {
		return slabGroup.item;
	}

	@Override
	public abstract IProperty<VARIANT> getVariantProperty();

	@SideOnly(Side.CLIENT)
	@Override
	public void getSubBlocks(final CreativeTabs tab, final NonNullList<ItemStack> list) {
		list.addAll(getVariantProperty().getAllowedValues().stream()
				.map(variant -> new ItemStack(this, 1, getMetadata(variant)))
				.collect(Collectors.toList()));
	}

	@SuppressWarnings("deprecation")
	@Override
	public IBlockState getStateFromMeta(final int meta) {
		final VARIANT variant = getVariant(meta & 7);
		IBlockState state = this.getDefaultState().withProperty(getVariantProperty(), variant);

		if (!this.isDouble()) {
			state = state.withProperty(HALF, (meta & 8) == 0 ? EnumBlockHalf.BOTTOM : EnumBlockHalf.TOP);
		}

		return state;
	}

	@Override
	public int getMetaFromState(final IBlockState state) {
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
	public int damageDropped(final IBlockState state) {
		return getMetadata(state.getValue(getVariantProperty()));
	}

	@Override
	public abstract VARIANT getTypeForItem(final ItemStack stack);
}
