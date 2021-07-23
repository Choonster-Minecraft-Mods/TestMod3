package choonster.testmod3.loot.conditions;

import choonster.testmod3.init.ModLootConditionTypes;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.loot.ILootSerializer;
import net.minecraft.loot.LootConditionType;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ITag;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;

/**
 * A condition that matches when the {@link Block} is in the specified tag.
 *
 * @author Choonster
 */
public class MatchBlockTag implements ILootCondition {
	private final ITag.INamedTag<Block> tag;

	public MatchBlockTag(final ITag.INamedTag<Block> tag) {
		this.tag = tag;
	}

	@Override
	public LootConditionType getType() {
		return ModLootConditionTypes.MATCH_BLOCK_TAG;
	}

	@Override
	public boolean test(final LootContext lootContext) {
		final BlockState state = lootContext.getParamOrNull(LootParameters.BLOCK_STATE);

		return state != null && tag.contains(state.getBlock());
	}

	public static ILootCondition.IBuilder builder(final ITag.INamedTag<Block> tag) {
		return () -> new MatchBlockTag(tag);
	}

	public static class Serializer implements ILootSerializer<MatchBlockTag> {
		@Override
		public void serialize(final JsonObject object, final MatchBlockTag instance, final JsonSerializationContext context) {
			object.addProperty("tag", instance.tag.getName().toString());
		}

		@Override
		public MatchBlockTag deserialize(final JsonObject object, final JsonDeserializationContext context) {
			final ResourceLocation tagName = new ResourceLocation(JSONUtils.getAsString(object, "tag"));
			final Tags.IOptionalNamedTag<Block> tag = BlockTags.createOptional(tagName);
			return new MatchBlockTag(tag);
		}
	}
}
