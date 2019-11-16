package choonster.testmod3.block;

import choonster.testmod3.block.variantgroup.BlockVariantGroup;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;

/**
 * A block with several variants.
 * <p>
 * Test for this thread:
 * http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/modification-development/2594064-metadata-blocks-dont-have-textures
 *
 * @author Choonster
 */
public class VariantsBlock extends Block {
	private final BlockVariantGroup<EnumType, ? extends VariantsBlock> variantGroup;
	private final EnumType type;

	public VariantsBlock(final EnumType type, final BlockVariantGroup<EnumType, ? extends VariantsBlock> variantGroup, final Block.Properties properties) {
		super(properties);
		this.type = type;
		this.variantGroup = variantGroup;
	}

	public EnumType getType() {
		return type;
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean onBlockActivated(final BlockState state, final World world, final BlockPos pos, final PlayerEntity player, final Hand hand, final BlockRayTraceResult rayTraceResult) {
		final EnumType newType = variantGroup.cycleVariant(type);
		final BlockState newState = variantGroup.getBlock(newType).getDefaultState();

		world.setBlockState(pos, newState);

		return true;
	}

	public enum EnumType implements IStringSerializable {
		VARIANT_A("a"),
		VARIANT_B("b");

		private final String name;

		EnumType(final String name) {
			this.name = name;
		}

		@Override
		public String getName() {
			return name;
		}
	}
}
