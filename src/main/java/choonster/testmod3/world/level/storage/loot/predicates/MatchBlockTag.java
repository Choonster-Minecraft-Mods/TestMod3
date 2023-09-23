package choonster.testmod3.world.level.storage.loot.predicates;

import choonster.testmod3.init.ModLootConditionTypes;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;

/**
 * A condition that matches when the {@link Block} is in the specified tag.
 *
 * @author Choonster
 */
public record MatchBlockTag(TagKey<Block> tag) implements LootItemCondition {
	public static final Codec<MatchBlockTag> CODEC = RecordCodecBuilder.create(builder ->
			builder.group(

					TagKey.codec(Registries.BLOCK)
							.fieldOf("tag")
							.forGetter(MatchBlockTag::tag)

			).apply(builder, MatchBlockTag::new)
	);

	@Override
	public LootItemConditionType getType() {
		return ModLootConditionTypes.MATCH_BLOCK_TAG.get();
	}

	@Override
	public boolean test(final LootContext lootContext) {
		final BlockState state = lootContext.getParamOrNull(LootContextParams.BLOCK_STATE);

		return state != null && state.is(tag);
	}

	public static Builder builder(final TagKey<Block> tag) {
		return () -> new MatchBlockTag(tag);
	}
}
