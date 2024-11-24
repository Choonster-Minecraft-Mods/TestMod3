package choonster.testmod3.world.level.block.variantgroup;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.*;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.Block;
import org.apache.commons.lang3.mutable.MutableObject;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * A {@link MapCodec} implementation that can encode any {@link IBlockVariantGroup} and
 * decode to {@link SimpleBlockVariantGroup}.
 *
 * @author Choonster
 */
class BlockVariantGroupMapCodec<VARIANT extends Enum<VARIANT> & StringRepresentable, BLOCK extends Block>
		extends MapCodec<IBlockVariantGroup<VARIANT, BLOCK>> {
	private static final String BLOCKS_KEY = "blocks";

	private final List<VARIANT> variants;
	private final Codec<VARIANT> variantCodec;
	private final IBlockCodecFactory<VARIANT, BLOCK> blockCodecFactory;
	private final MapCodec<String> groupNameCodec;
	private final MapCodec<List<VARIANT>> variantsListCodec;

	public BlockVariantGroupMapCodec(
			final List<VARIANT> variants,
			final Codec<VARIANT> variantCodec,
			final IBlockCodecFactory<VARIANT, BLOCK> blockCodecFactory
	) {
		this.variants = variants;
		this.variantCodec = variantCodec;
		this.blockCodecFactory = blockCodecFactory;

		groupNameCodec = Codec.STRING
				.fieldOf("groupName");

		variantsListCodec = variantCodec
				.listOf()
				.fieldOf("variants");
	}

	@Override
	public <T> Stream<T> keys(final DynamicOps<T> ops) {
		return Stream.of(
				groupNameCodec.keys(ops),
				variantsListCodec.keys(ops),
				Stream.of(ops.createString(BLOCKS_KEY))
		).flatMap(Function.identity());
	}

	@Override
	public <T> DataResult<IBlockVariantGroup<VARIANT, BLOCK>> decode(final DynamicOps<T> ops, final MapLike<T> input) {
		final var variantGroupObject = new MutableObject<IBlockVariantGroup<VARIANT, BLOCK>>();

		final var blocksMapCodec = getBlocksMapCodec(variantGroupObject::getValue);

		final var groupName = groupNameCodec.decode(ops, input);
		final var variants = variantsListCodec.decode(ops, input);

		final var blocks = blocksMapCodec.decode(ops, input);

		final var result = DataResult.<IBlockVariantGroup<VARIANT, BLOCK>>unbox(
				DataResult.instance().ap3(
						DataResult.success(SimpleBlockVariantGroup<VARIANT, BLOCK>::new),
						groupName,
						variants,
						blocks
				)
		);

		result.ifSuccess(variantGroupObject::setValue);

		return result;
	}

	@Override
	public <T> RecordBuilder<T> encode(
			final IBlockVariantGroup<VARIANT, BLOCK> input,
			final DynamicOps<T> ops,
			final RecordBuilder<T> prefix
	) {
		final var blocksMapCodec = getBlocksMapCodec(() -> input);

		final var blocksMap = input.getBlocksMap()
				.entrySet()
				.stream()
				.map(entry ->
						Pair.of(
								entry.getKey(),
								entry.getValue().get()
						)
				)
				.collect(Collectors.toMap(Pair::getFirst, Pair::getSecond));

		groupNameCodec.encode(input.getGroupName(), ops, prefix);
		variantsListCodec.encode(input.getVariants(), ops, prefix);
		blocksMapCodec.encode(blocksMap, ops, prefix);

		return prefix;
	}

	private MapCodec<Map<VARIANT, BLOCK>> getBlocksMapCodec(
			final Supplier<IBlockVariantGroup<VARIANT, BLOCK>> variantGroupSupplier
	) {
		final var blockCodec = blockCodecFactory.getBlockCodec(variantGroupSupplier, this);

		return Codec.simpleMap(
				variantCodec,
				blockCodec,
				StringRepresentable.keys(variants.toArray(StringRepresentable[]::new))
		).fieldOf(BLOCKS_KEY);
	}

}
