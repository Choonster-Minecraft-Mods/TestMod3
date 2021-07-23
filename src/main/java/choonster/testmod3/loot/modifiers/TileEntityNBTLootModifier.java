package choonster.testmod3.loot.modifiers;

import com.google.gson.JsonObject;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;

import java.util.List;

/**
 * A global loot modifier that adds an ItemStack of the harvested block's item with the TileEntity's NBT stored in the
 * "BlockEntityTag" tag.
 *
 * @author Choonster
 */
public class TileEntityNBTLootModifier extends LootModifier {
	public TileEntityNBTLootModifier(final ILootCondition[] conditionsIn) {
		super(conditionsIn);
	}

	@Override
	protected List<ItemStack> doApply(final List<ItemStack> generatedLoot, final LootContext context) {
		final BlockState state = context.getParamOrNull(LootParameters.BLOCK_STATE);
		final TileEntity tileEntity = context.getParamOrNull(LootParameters.BLOCK_ENTITY);

		if (state != null && tileEntity != null) {
			final ItemStack stack = new ItemStack(state.getBlock());

			// Write the TileEntity to NBT
			final CompoundNBT tileData = tileEntity.serializeNBT();

			// Remove the coordinate tags so items of the same type from different positions stack
			tileData.remove("x");
			tileData.remove("y");
			tileData.remove("z");

			// Store the TileEntity data in the ItemStack
			stack.addTagElement("BlockEntityTag", tileData);

			generatedLoot.add(stack);
		}

		return generatedLoot;
	}

	public static class Serializer extends GlobalLootModifierSerializer<TileEntityNBTLootModifier> {
		@Override
		public TileEntityNBTLootModifier read(final ResourceLocation location, final JsonObject object, final ILootCondition[] conditions) {
			return new TileEntityNBTLootModifier(conditions);
		}

		@Override
		public JsonObject write(final TileEntityNBTLootModifier instance) {
			return makeConditions(instance.conditions);
		}
	}
}
