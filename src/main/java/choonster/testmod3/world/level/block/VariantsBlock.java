package choonster.testmod3.world.level.block;

import choonster.testmod3.world.level.block.variantgroup.BlockVariantGroup;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

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
	private final MapCodec<VariantsBlock> codec;

	public VariantsBlock(final EnumType type, final BlockVariantGroup<EnumType, ? extends VariantsBlock> variantGroup, final Block.Properties properties) {
		super(properties);
		this.type = type;
		this.variantGroup = variantGroup;

		codec = RecordCodecBuilder.mapCodec(instance -> instance.group(

				EnumType.CODEC
						.fieldOf("variant")
						.forGetter(VariantsBlock::getType),

				variantGroup.codec(),

				propertiesCodec()

		).apply(instance, VariantsBlock::new));
	}

	@Override
	protected MapCodec<? extends Block> codec() {
		return codec;
	}

	public EnumType getType() {
		return type;
	}

	@SuppressWarnings("deprecation")
	@Override
	public InteractionResult use(final BlockState state, final Level world, final BlockPos pos, final Player player, final InteractionHand hand, final BlockHitResult rayTraceResult) {
		final EnumType newType = variantGroup.cycleVariant(type);
		final BlockState newState = variantGroup.getBlock(newType).get().defaultBlockState();

		world.setBlockAndUpdate(pos, newState);

		return InteractionResult.SUCCESS;
	}

	public enum EnumType implements StringRepresentable {
		VARIANT_A("a"),
		VARIANT_B("b");

		public static final Codec<EnumType> CODEC = StringRepresentable.fromEnum(EnumType::values);

		private final String name;

		EnumType(final String name) {
			this.name = name;
		}

		@Override
		public String getSerializedName() {
			return name;
		}
	}
}
