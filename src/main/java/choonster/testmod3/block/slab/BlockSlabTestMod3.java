package choonster.testmod3.block.slab;

import choonster.testmod3.block.variantgroup.SlabVariantGroup;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

import java.util.Collections;
import java.util.Random;

/**
 * Base class for this mod's slab blocks.
 *
 * @param <VARIANT> The variant type
 * @param <SLAB>    The slab type
 * @author Choonster
 */
public abstract class BlockSlabTestMod3<
		VARIANT extends Enum<VARIANT> & IStringSerializable,
		SLAB extends BlockSlabTestMod3<VARIANT, SLAB>
		> extends BlockSlab {

	/**
	 * The variant of this slab.
	 */
	protected final VARIANT variant;

	/**
	 * The group this slab belongs to.
	 */
	protected final SlabVariantGroup<VARIANT, SLAB>.SlabGroup slabGroup;

	/**
	 * Create a slab block.
	 *
	 * @param variant   The variant of this slab
	 * @param material  The Material of this slab
	 * @param slabGroup The group this slab belongs to
	 */
	public BlockSlabTestMod3(final VARIANT variant, final Material material, final MapColor mapColor, final SlabVariantGroup<VARIANT, SLAB>.SlabGroup slabGroup) {
		super(material, mapColor);

		this.variant = variant;
		this.slabGroup = slabGroup;

		// Vanilla sets this for anything that extends BlockSlab in Block.registerBlocks,
		// but this is run before mods are loaded; so we need to set it manually.
		// Thanks to HappyKiller1O1 for pointing out this field to me:
		// http://www.minecraftforge.net/forum/topic/35912-solved-189-custom-slabs-have-incorrect-lighting-in-world/?tab=comments#comment-191051
		this.useNeighborBrightness = true;

		IBlockState defaultState = this.blockState.getBaseState();

		if (!isDouble()) {
			defaultState = defaultState.withProperty(HALF, EnumBlockHalf.BOTTOM);
		}

		setDefaultState(defaultState);
	}

	/**
	 * Creates a variant property for the specified variant.
	 *
	 * @param variantClass The variant class object
	 * @param variant      The variant
	 * @param <VARIANT>    The variant type
	 * @return The variant property
	 */
	public static <VARIANT extends Enum<VARIANT> & IStringSerializable> IProperty<VARIANT> createVariantProperty(final Class<VARIANT> variantClass, final VARIANT variant) {
		return PropertyEnum.create("variant", variantClass, Collections.singleton(variant));
	}

	@Override
	public Item getItemDropped(final IBlockState state, final Random rand, final int fortune) {
		return slabGroup.getItem();
	}

	@Override
	public abstract boolean isDouble();

	@Override
	public abstract IProperty<VARIANT> getVariantProperty();

	@Override
	public final VARIANT getTypeForItem(final ItemStack stack) {
		return variant;
	}

	@Override
	public final String getTranslationKey(final int meta) {
		return getTranslationKey();
	}

	@Override
	public ItemStack getPickBlock(final IBlockState state, final RayTraceResult target, final World world, final BlockPos pos, final EntityPlayer player) {
		return new ItemStack(slabGroup.getItem());
	}

	@SuppressWarnings("deprecation")
	@Override
	public IBlockState getStateFromMeta(final int meta) {
		IBlockState state = this.getDefaultState();

		if (!this.isDouble()) {
			state = state.withProperty(HALF, (meta & 8) == 0 ? EnumBlockHalf.BOTTOM : EnumBlockHalf.TOP);
		}

		return state;
	}

	@Override
	public int getMetaFromState(final IBlockState state) {
		int meta = 0;

		if (!this.isDouble() && state.getValue(HALF) == EnumBlockHalf.TOP) {
			meta |= 8;
		}

		return meta;
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return isDouble() ? new BlockStateContainer(this, getVariantProperty()) : new BlockStateContainer(this, HALF, getVariantProperty());
	}
}
