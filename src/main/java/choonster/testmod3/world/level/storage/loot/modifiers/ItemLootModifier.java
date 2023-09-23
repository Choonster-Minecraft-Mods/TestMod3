package choonster.testmod3.world.level.storage.loot.modifiers;

import com.google.common.base.Suppliers;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctions;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Supplier;

/**
 * A global loot modifier that uses an {@link Item} and a list of {@link LootItemFunction}s to generate a single
 * {@link ItemStack}.
 *
 * @author Choonster
 */
public class ItemLootModifier extends LootModifier {
	public static final Supplier<Codec<ItemLootModifier>> CODEC = Suppliers.memoize(() ->
			RecordCodecBuilder.create(inst ->
					codecStart(inst)
							.and(
									ForgeRegistries.ITEMS.getCodec()
											.fieldOf("name")
											.forGetter(m -> m.item)
							)
							.and(
									LootItemFunctions.CODEC
											.listOf()
											.fieldOf("functions")
											.forGetter(m -> m.functions)
							)
							.apply(inst, ItemLootModifier::new)
			)
	);

	private final Item item;
	private final List<LootItemFunction> functions;
	private final BiFunction<ItemStack, LootContext, ItemStack> compositeFunction;

	public ItemLootModifier(final LootItemCondition[] conditions, final Item item, final List<LootItemFunction> functions) {
		super(conditions);
		this.item = item;
		this.functions = functions;
		compositeFunction = LootItemFunctions.compose(functions);
	}

	@Override
	protected ObjectArrayList<ItemStack> doApply(final ObjectArrayList<ItemStack> generatedLoot, final LootContext context) {
		final ItemStack stack = new ItemStack(item);

		compositeFunction.apply(stack, context);

		generatedLoot.add(stack);

		return generatedLoot;
	}

	@Override
	public Codec<? extends IGlobalLootModifier> codec() {
		return CODEC.get();
	}
}
