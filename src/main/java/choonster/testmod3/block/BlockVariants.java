package choonster.testmod3.block;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.List;
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

	public BlockVariants(Material materialIn) {
		super(materialIn, "variants");
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, VARIANT);
	}

	@SuppressWarnings("deprecation")
	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(VARIANT, EnumType.byMetadata(meta));
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(VARIANT).getMeta();
	}

	@Override
	public int damageDropped(IBlockState state) {
		return getMetaFromState(state);
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, @Nullable ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
		return worldIn.setBlockState(pos, state.cycleProperty(VARIANT));
	}

	@Override
	public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list) {
		for (final EnumType enumType : EnumType.values()) {
			list.add(new ItemStack(this, 1, enumType.getMeta()));
		}
	}

	public enum EnumType implements IStringSerializable {
		VARIANT_A(0, "a"),
		VARIANT_B(1, "b");

		private static final EnumType[] META_LOOKUP = Stream.of(values()).sorted(Comparator.comparing(EnumType::getMeta)).toArray(EnumType[]::new);

		private final int meta;
		private final String name;

		EnumType(int meta, String name) {
			this.meta = meta;
			this.name = name;
		}

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

		public static String[] getNames() {
			return Stream.of(META_LOOKUP).map(EnumType::getName).toArray(String[]::new);
		}
	}
}
