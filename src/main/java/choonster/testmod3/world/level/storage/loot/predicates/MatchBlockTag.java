package choonster.testmod3.world.level.storage.loot.predicates;

import choonster.testmod3.init.ModLootConditionTypes;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.Serializer;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import net.minecraftforge.common.Tags;

/**
 * A condition that matches when the {@link Block} is in the specified tag.
 *
 * @author Choonster
 */
public class MatchBlockTag implements LootItemCondition {
	private final Tag.Named<Block> tag;

	public MatchBlockTag(final Tag.Named<Block> tag) {
		this.tag = tag;
	}

	@Override
	public LootItemConditionType getType() {
		return ModLootConditionTypes.MATCH_BLOCK_TAG;
	}

	@Override
	public boolean test(final LootContext lootContext) {
		final BlockState state = lootContext.getParamOrNull(LootContextParams.BLOCK_STATE);

		return state != null && tag.contains(state.getBlock());
	}

	public static LootItemCondition.Builder builder(final Tag.Named<Block> tag) {
		return () -> new MatchBlockTag(tag);
	}

	public static class ConditionSerializer implements Serializer<MatchBlockTag> {
		@Override
		public void serialize(final JsonObject object, final MatchBlockTag instance, final JsonSerializationContext context) {
			object.addProperty("tag", instance.tag.getName().toString());
		}

		@Override
		public MatchBlockTag deserialize(final JsonObject object, final JsonDeserializationContext context) {
			final ResourceLocation tagName = new ResourceLocation(GsonHelper.getAsString(object, "tag"));
			final Tags.IOptionalNamedTag<Block> tag = BlockTags.createOptional(tagName);
			return new MatchBlockTag(tag);
		}
	}
}
