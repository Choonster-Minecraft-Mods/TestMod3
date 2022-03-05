package choonster.testmod3.world.level.storage.loot.predicates;

import choonster.testmod3.init.ModLootConditionTypes;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.Serializer;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;

/**
 * A condition that matches when the {@link Block} is in the specified tag.
 *
 * @author Choonster
 */
public class MatchBlockTag implements LootItemCondition {
	private final TagKey<Block> tag;

	public MatchBlockTag(final TagKey<Block> tag) {
		this.tag = tag;
	}

	@Override
	public LootItemConditionType getType() {
		return ModLootConditionTypes.MATCH_BLOCK_TAG.get();
	}

	@Override
	public boolean test(final LootContext lootContext) {
		final BlockState state = lootContext.getParamOrNull(LootContextParams.BLOCK_STATE);

		return state != null && state.is(tag);
	}

	public static LootItemCondition.Builder builder(final TagKey<Block> tag) {
		return () -> new MatchBlockTag(tag);
	}

	public static class ConditionSerializer implements Serializer<MatchBlockTag> {
		@Override
		public void serialize(final JsonObject object, final MatchBlockTag instance, final JsonSerializationContext context) {
			object.addProperty("tag", instance.tag.location().toString());
		}

		@Override
		public MatchBlockTag deserialize(final JsonObject object, final JsonDeserializationContext context) {
			final ResourceLocation tagName = new ResourceLocation(GsonHelper.getAsString(object, "tag"));
			final TagKey<Block> tag = BlockTags.create(tagName);
			return new MatchBlockTag(tag);
		}
	}
}
