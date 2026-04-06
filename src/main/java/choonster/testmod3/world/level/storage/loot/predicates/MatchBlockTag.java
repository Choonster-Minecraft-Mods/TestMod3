package choonster.testmod3.world.level.storage.loot.predicates;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

/**
 * A condition that matches when the {@link Block} is in the specified tag.
 *
 * @author Choonster
 */
public record MatchBlockTag(TagKey<Block> tag) implements LootItemCondition {
	public static final MapCodec<MatchBlockTag> CODEC = RecordCodecBuilder.mapCodec(builder ->
			builder.group(

					TagKey.codec(Registries.BLOCK)
							.fieldOf("tag")
							.forGetter(MatchBlockTag::tag)

			).apply(builder, MatchBlockTag::new)
	);

	@Override
	public MapCodec<? extends LootItemCondition> codec() {
		return CODEC;
	}

	@Override
	public boolean test(final LootContext lootContext) {
		final BlockState state = lootContext.getOptionalParameter(LootContextParams.BLOCK_STATE);

		return state != null && state.is(tag);
	}

	public static Builder builder(final TagKey<Block> tag) {
		return () -> new MatchBlockTag(tag);
	}
}
