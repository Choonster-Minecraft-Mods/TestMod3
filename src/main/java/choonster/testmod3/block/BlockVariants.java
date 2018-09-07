package choonster.testmod3.block;

import choonster.testmod3.util.IVariant;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Comparator;
import java.util.stream.Stream;

/**
 * A block with several variants.
 * <p>
 * Test for this thread:
 * http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/modification-development/2594064-metadata-blocks-dont-have-textures
 *
 * @author Choonster
 */
public class BlockVariants extends BlockTestMod3 {
	public static final IProperty<EnumType> VARIANT = PropertyEnum.create("variant", EnumType.class);

	public BlockVariants(final Material materialIn) {
		super(materialIn, "variants");
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, VARIANT);
	}

	@SuppressWarnings("deprecation")
	@Override
	public IBlockState getStateFromMeta(final int meta) {
		return getDefaultState().withProperty(VARIANT, EnumType.byMetadata(meta));
	}

	@Override
	public int getMetaFromState(final IBlockState state) {
		return state.getValue(VARIANT).getMeta();
	}

	@Override
	public int damageDropped(final IBlockState state) {
		return getMetaFromState(state);
	}

	@Override
	public boolean onBlockActivated(final World worldIn, final BlockPos pos, final IBlockState state, final EntityPlayer playerIn, final EnumHand hand, final EnumFacing facing, final float hitX, final float hitY, final float hitZ) {
		return worldIn.setBlockState(pos, state.cycleProperty(VARIANT));
	}

	@Override
	public void getSubBlocks(final CreativeTabs tab, final NonNullList<ItemStack> list) {
		for (final EnumType enumType : EnumType.values()) {
			list.add(new ItemStack(this, 1, enumType.getMeta()));
		}
	}

	/**
	 * Get the translation key suffix for the specified {@link ItemStack}.
	 *
	 * @param stack The ItemStack
	 * @return The translation key suffix
	 */
	public String getName(final ItemStack stack) {
		final int metadata = stack.getMetadata();

		return EnumType.byMetadata(metadata).getName();
	}

	public enum EnumType implements IVariant {
		VARIANT_A(0, "a"),
		VARIANT_B(1, "b");

		private static final EnumType[] META_LOOKUP = Stream.of(values()).sorted(Comparator.comparing(EnumType::getMeta)).toArray(EnumType[]::new);

		private final int meta;
		private final String name;

		EnumType(final int meta, final String name) {
			this.meta = meta;
			this.name = name;
		}

		@Override
		public int getMeta() {
			return meta;
		}

		@Override
		public String getName() {
			return name;
		}

		public static EnumType byMetadata(int meta) {
			if (meta < 0 || meta >= META_LOOKUP.length) {
				meta = 0;
			}

			return META_LOOKUP[meta];
		}
	}
}
