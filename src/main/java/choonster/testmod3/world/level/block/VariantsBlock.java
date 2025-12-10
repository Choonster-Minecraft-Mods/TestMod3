package choonster.testmod3.world.level.block;

import choonster.testmod3.world.level.block.variantgroup.IBlockVariantGroup;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

import java.util.Objects;
import java.util.function.Supplier;

/**
 * A block with several variants.
 * <p>
 * Test for this thread:
 * http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/modification-development/2594064-metadata-blocks-dont-have-textures
 *
 * @author Choonster
 */
public class VariantsBlock extends Block {
	public static Codec<VariantsBlock> codec(
			final Supplier<IBlockVariantGroup<EnumType, VariantsBlock>> variantGroupSupplier,
			final MapCodec<IBlockVariantGroup<EnumType, VariantsBlock>> variantGroupMapCodec
	) {
		return RecordCodecBuilder.create(instance ->
				instance.group(
						EnumType.CODEC
								.fieldOf("variant")
								.forGetter(VariantsBlock::getType),

						MapCodec.unitCodec(() -> variantGroupSupplier)
								.fieldOf("variantGroup")
								.forGetter(block -> block.variantGroup),

						MapCodec.unitCodec(() -> variantGroupMapCodec)
								.fieldOf("variantGroupMapCodec")
								.forGetter(block -> block.variantGroupMapCodec),

						propertiesCodec()
				).apply(instance, VariantsBlock::new)
		);
	}

	private final Supplier<IBlockVariantGroup<EnumType, VariantsBlock>> variantGroup;
	private final MapCodec<IBlockVariantGroup<EnumType, VariantsBlock>> variantGroupMapCodec;
	private final EnumType type;
	private final MapCodec<VariantsBlock> codec;

	public VariantsBlock(
			final EnumType type,
			final Supplier<IBlockVariantGroup<EnumType, VariantsBlock>> variantGroup,
			final MapCodec<IBlockVariantGroup<EnumType, VariantsBlock>> variantGroupMapCodec,
			final Block.Properties properties
	) {
		super(properties);
		this.type = type;
		this.variantGroup = variantGroup;
		this.variantGroupMapCodec = variantGroupMapCodec;

		codec = IBlockVariantGroup.blockMapCodec(variantGroupMapCodec, block -> block.variantGroup.get(), type);
	}

	@Override
	protected MapCodec<? extends Block> codec() {
		return codec;
	}

	public EnumType getType() {
		return type;
	}

	@Override
	protected InteractionResult useWithoutItem(final BlockState state, final Level level, final BlockPos pos, final Player player, final BlockHitResult blockHitResult) {
		final var variantGroup = this.variantGroup.get();
		final var newType = variantGroup.cycleVariant(type);
		final var newBlock = Objects.requireNonNull(variantGroup.getBlock(newType));
		final var newState = newBlock.get().defaultBlockState();

		level.setBlockAndUpdate(pos, newState);

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
