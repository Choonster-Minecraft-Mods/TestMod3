package choonster.testmod3.world.level.storage.loot.modifiers;

import com.google.gson.JsonObject;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;

import java.util.List;

/**
 * A global loot modifier that adds an ItemStack of the harvested block's item with the BlockEntity's NBT stored in the
 * "BlockEntityTag" tag.
 *
 * @author Choonster
 */
public class BlockEntityNBTLootModifier extends LootModifier {
	public BlockEntityNBTLootModifier(final LootItemCondition[] conditionsIn) {
		super(conditionsIn);
	}

	@Override
	protected List<ItemStack> doApply(final List<ItemStack> generatedLoot, final LootContext context) {
		final BlockState state = context.getParamOrNull(LootContextParams.BLOCK_STATE);
		final BlockEntity blockEntity = context.getParamOrNull(LootContextParams.BLOCK_ENTITY);

		if (state != null && blockEntity != null) {
			final ItemStack stack = new ItemStack(state.getBlock());

			// Write the BlockEntity to NBT
			final CompoundTag blockEntityTag = blockEntity.serializeNBT();

			// Remove the coordinate tags so items of the same type from different positions stack
			blockEntityTag.remove("x");
			blockEntityTag.remove("y");
			blockEntityTag.remove("z");

			// Store the BlockEntity data in the ItemStack
			stack.addTagElement("BlockEntityTag", blockEntityTag);

			generatedLoot.add(stack);
		}

		return generatedLoot;
	}

	public static class Serializer extends GlobalLootModifierSerializer<BlockEntityNBTLootModifier> {
		@Override
		public BlockEntityNBTLootModifier read(final ResourceLocation location, final JsonObject object, final LootItemCondition[] conditions) {
			return new BlockEntityNBTLootModifier(conditions);
		}

		@Override
		public JsonObject write(final BlockEntityNBTLootModifier instance) {
			return makeConditions(instance.conditions);
		}
	}
}
