package choonster.testmod3.data.models;

import choonster.testmod3.TestMod3;
import choonster.testmod3.client.renderer.item.properties.numeric.TicksSinceLastUse;
import choonster.testmod3.data.models.model.DynamicFluidContainerModelTemplate;
import choonster.testmod3.data.models.model.ModModelTemplates;
import choonster.testmod3.data.models.model.ModTextureSlots;
import choonster.testmod3.fluid.group.FluidGroup;
import choonster.testmod3.init.ModDataComponents;
import choonster.testmod3.init.ModFluids;
import choonster.testmod3.init.ModItems;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ItemModelOutput;
import net.minecraft.client.data.models.model.*;
import net.minecraft.client.renderer.item.RangeSelectItemModel;
import net.minecraft.client.renderer.item.properties.numeric.UseDuration;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Supplier;
import java.util.stream.IntStream;

/**
 * Generates this mod's item models.
 *
 * @author Choonster
 */
public class ModItemModelGenerators extends ItemModelGenerators {
	private static final ResourceLocation BUCKET_MODEL = ResourceLocation.fromNamespaceAndPath(
			"forge",
			"items/bucket"
	);

	public ModItemModelGenerators(
			final ItemModelOutput itemModelOutput,
			final BiConsumer<ResourceLocation, ModelInstance> modelOutput
	) {
		super(itemModelOutput, modelOutput);
	}

	@Override
	public void run() {
		copy(ModItems.WOODEN_AXE.get(), Items.WOODEN_AXE);

		copy(ModItems.ENTITY_TEST.get(), Items.PORKCHOP);

		copy(ModItems.MUSIC_DISC_SOLARIS.get(), Items.MUSIC_DISC_13);

		copy(ModItems.HEAVY.get(), Items.BRICK);

		copy(ModItems.ENTITY_INTERACTION_TEST.get(), Items.BEEF);

		copy(ModItems.BLOCK_DESTROYER.get(), Items.TNT_MINECART);

		generateFlatItem(ModItems.SUBSCRIPTS.get(), ModModelTemplates.SIMPLE_ITEM);

		generateFlatItem(ModItems.SUPERSCRIPTS.get(), ModModelTemplates.SIMPLE_ITEM);

		generateModelTest();

		copy(ModItems.SNOWBALL_LAUNCHER.get(), Items.FISHING_ROD);

		generateSlingshot();

		copy(ModItems.UNICODE_TOOLTIPS.get(), Items.RABBIT);

		copy(ModItems.SWAP_TEST_A.get(), Items.BRICK);

		copy(ModItems.SWAP_TEST_B.get(), Items.NETHER_BRICK);

		copy(ModItems.BLOCK_DEBUGGER.get(), Items.NETHER_STAR);

		copy(ModItems.WOODEN_HARVEST_SWORD.get(), Items.WOODEN_SWORD);

		copy(ModItems.DIAMOND_HARVEST_SWORD.get(), Items.DIAMOND_SWORD);

		copy(ModItems.CLEARER.get(), Items.NETHER_STAR);

		generateBow(ModItems.BOW.get(), Items.BOW);

		generateFlatItem(ModItems.ARROW.get(), ModelTemplates.FLAT_ITEM);

		copy(ModItems.HEIGHT_TESTER.get(), Items.COMPASS);

		copy(ModItems.PIG_SPAWNER_FINITE.get(), Items.PORKCHOP);

		copy(ModItems.PIG_SPAWNER_INFINITE.get(), Items.PORKCHOP);

		generateBow(ModItems.CONTINUOUS_BOW.get(), Items.BOW);

		copy(ModItems.RESPAWNER.get(), Items.CLOCK);

		copy(ModItems.LOOT_TABLE_TEST.get(), Items.GOLD_INGOT);

		generateFlatItem(ModItems.MAX_HEALTH_GETTER_ITEM.get(), ModelTemplates.FLAT_ITEM);

		generateFlatItem(ModItems.MAX_HEALTH_SETTER_ITEM.get(), ModelTemplates.FLAT_ITEM);

		generateFlatItem(ModItems.GUN.get(), ModModelTemplates.SIMPLE_ITEM);

		generateFlatItem(ModItems.DIMENSION_REPLACEMENT.get(), ModModelTemplates.SIMPLE_ITEM);

		copy(ModItems.SADDLE.get(), Items.SADDLE);

		copy(ModItems.WOODEN_SLOW_SWORD.get(), Items.WOODEN_SWORD);

		copy(ModItems.DIAMOND_SLOW_SWORD.get(), Items.DIAMOND_SWORD);

		generateRitualChecker();

		generateHiddenBlockRevealer();

		copy(ModItems.NO_MOD_NAME.get(), Items.BREAD);

		generateFlatItem(ModItems.KEY.get(), ModelTemplates.FLAT_HANDHELD_ITEM);

		generateFlatItem(ModItems.BLOCK_DETECTION_ARROW.get(), ModelTemplates.FLAT_ITEM);

		generateTranslucentItem();

		generateFlatItem(ModItems.ENTITY_KILLER.get(), ModelTemplates.FLAT_ITEM);

		generateFlatItem(ModItems.CHUNK_ENERGY_SETTER.get(), ModelTemplates.FLAT_ITEM);

		generateFlatItem(ModItems.CHUNK_ENERGY_GETTER.get(), ModelTemplates.FLAT_ITEM);

		generateFlatItem(ModItems.CHUNK_ENERGY_DISPLAY.get(), ModelTemplates.FLAT_ITEM);

		copy(ModItems.BEACON_ITEM.get(), Items.BEACON);

		copy(ModItems.SATURATION_HELMET.get(), Items.CHAINMAIL_HELMET);

		copy(ModItems.ENTITY_CHECKER.get(), Items.BONE);

		generateFlatItem(ModItems.RUBBER.get(), ModelTemplates.FLAT_ITEM);

		copy(ModItems.REPLACEMENT_HELMET.get(), Items.CHAINMAIL_HELMET);

		copy(ModItems.REPLACEMENT_CHESTPLATE.get(), Items.CHAINMAIL_CHESTPLATE);

		copy(ModItems.REPLACEMENT_LEGGINGS.get(), Items.CHAINMAIL_LEGGINGS);

		copy(ModItems.REPLACEMENT_BOOTS.get(), Items.CHAINMAIL_BOOTS);

		generateFlatItem(ModItems.FLUID_STACK_ITEM.get(), ModModelTemplates.EMPTY);

		generateSpawnEgg(ModItems.PLAYER_AVOIDING_CREEPER_SPAWN_EGG.get(), 0xda70b, 0);

		generateBucket(ModItems.WOODEN_BUCKET.get());

		generateBucket(ModItems.STONE_BUCKET.get());

		ModItems.VARIANTS_ITEMS
				.getItems()
				.stream()
				.map(Supplier::get)
				.forEach(item -> createFlatItemModel(item, ModelTemplates.FLAT_ITEM));

		generateBucket(ModFluids.STATIC);
		generateBucket(ModFluids.STATIC_GAS);
		generateBucket(ModFluids.NORMAL);
		generateBucket(ModFluids.NORMAL_GAS);
		generateBucket(ModFluids.PORTAL_DISPLACEMENT);
	}

	// Single item model generation
	private void generateModelTest() {
		final var item = ModItems.MODEL_TEST.get();

		// Create the parent model
		final var standby = ItemModelUtils.plainModel(
				createFlatItemModel(item, "_standby", ModModelTemplates.SIMPLE_ITEM)
		);

		final var entries = new ArrayList<RangeSelectItemModel.Entry>();

		// Create three child models and add them as overrides that display when the ticks since last use is >= index * 20
		IntStream.range(0, 3)
				.mapToObj(index -> {
					final var model = ItemModelUtils.plainModel(
							createFlatItemModel(item, "_" + index, ModModelTemplates.SIMPLE_ITEM)
					);

					return ItemModelUtils.override(model, index * 20);
				})
				.forEach(entries::add);

		// Add the parent as a fallback that displays when the ticks since last use is >= 60
		entries.add(ItemModelUtils.override(standby, 60));

		itemModelOutput.accept(item, ItemModelUtils.rangeSelect(new TicksSinceLastUse(), standby, entries));
	}

	private void generateSlingshot() {
		final var item = ModItems.SLINGSHOT.get();

		// Create the parent model
		final var standby = ItemModelUtils.plainModel(createFlatItemModel(item, ModModelTemplates.SIMPLE_ITEM));

		// Create the child model
		final var pulled = ItemModelUtils.plainModel(
				createFlatItemModel(item, "_pulled", ModModelTemplates.SIMPLE_ITEM)
		);

		// Add the child as an override that displays when the ticks since last use is >= 0 and < 20
		// Add the parent as a fallback that displays when the ticks since last use is >= 20
		final var entries = List.of(
				ItemModelUtils.override(pulled, 0),
				ItemModelUtils.override(standby, 20)
		);

		itemModelOutput.accept(
				item,
				ItemModelUtils.rangeSelect(new TicksSinceLastUse(), standby, entries)
		);
	}

	private void generateRitualChecker() {
		generateFlatItem(
				ModItems.RITUAL_CHECKER.get(),
				ModelTemplates.FLAT_HANDHELD_ITEM,
				ResourceLocation.fromNamespaceAndPath(TestMod3.MODID, "item/banner_base")
		);
	}

	private void generateHiddenBlockRevealer() {
		final var item = ModItems.HIDDEN_BLOCK_REVEALER.get();

		// Create the parent model
		final var standby = ItemModelUtils.plainModel(createFlatItemModel(item, ModelTemplates.FLAT_ITEM));

		// Create the child model and add it as an override that's displayed when hidden blocks are being revealed
		final var active = ItemModelUtils.plainModel(
				createFlatItemModel(item, "_active", ModelTemplates.FLAT_ITEM)
		);

		itemModelOutput.accept(
				item,
				ItemModelUtils.conditional(
						ItemModelUtils.hasComponent(ModDataComponents.REVEAL_HIDDEN_BLOCKS.get()),
						active,
						standby
				)
		);
	}

	private void generateTranslucentItem() {
		generateFlatItem(
				ModItems.TRANSLUCENT_ITEM.get(),
				ModelTemplates.FLAT_ITEM,
				TextureMapping.getBlockTexture(Blocks.ICE)
		);
	}

	// Generic item model generation
	protected void generateBow(final Item item, final Item textureItem) {
		final var standby = ItemModelUtils.plainModel(
				createFlatItemModel(item, textureItem, ModModelTemplates.SIMPLE_ITEM)
		);

		final var pulling0 = ItemModelUtils.plainModel(
				createFlatItemModel(item, textureItem, "_pulling_0", ModModelTemplates.SIMPLE_ITEM)
		);

		final var pulling1 = ItemModelUtils.plainModel(
				createFlatItemModel(item, textureItem, "_pulling_1", ModModelTemplates.SIMPLE_ITEM)
		);

		final var pulling2 = ItemModelUtils.plainModel(
				createFlatItemModel(item, textureItem, "_pulling_2", ModModelTemplates.SIMPLE_ITEM)
		);

		itemModelOutput
				.accept(
						item,
						ItemModelUtils.conditional(
								ItemModelUtils.isUsingItem(),
								ItemModelUtils.rangeSelect(
										new UseDuration(false),
										0.05f,
										pulling0,
										ItemModelUtils.override(pulling1, 0.65f),
										ItemModelUtils.override(pulling2, 0.9f)
								),
								standby
						)
				);
	}

	private void copy(final Item item, final Item parent) {
		itemModelOutput.copy(parent, item);
	}

	private void generateFlatItem(final Item item, final ModelTemplate modelTemplate, final ResourceLocation texture) {
		final var model = modelTemplate.create(item, TextureMapping.layer0(texture), modelOutput);
		itemModelOutput.accept(item, ItemModelUtils.plainModel(model));
	}

	private void generateBucket(final FluidGroup<?, ?, ?, ?, ?> fluidGroup) {
		final var item = fluidGroup.getBucket().get();
		final var fluid = item instanceof BucketItem ? ((BucketItem) item).getFluid() : Fluids.EMPTY;

		final var model = createBucketModel(item, fluid, new TextureMapping());
		itemModelOutput.accept(item, ItemModelUtils.plainModel(model));
	}

	private void generateBucket(final Item item) {
		final var baseTexture = TextureMapping.getItemTexture(item, "_base");

		final var textureMapping = new TextureMapping()
				.put(ModTextureSlots.BASE, baseTexture)
				.put(TextureSlot.PARTICLE, baseTexture);

		final var model = createBucketModel(item, Fluids.EMPTY, textureMapping);
		itemModelOutput.accept(item, ItemModelUtils.plainModel(model));
	}

	// Item model creation
	private ResourceLocation createFlatItemModel(
			final Item item,
			final Item textureItem,
			final String suffix,
			final ModelTemplate modelTemplate
	) {
		return modelTemplate.create(
				ModelLocationUtils.getModelLocation(item, suffix),
				TextureMapping.layer0(TextureMapping.getItemTexture(textureItem, suffix)),
				modelOutput
		);
	}

	private ResourceLocation createBucketModel(
			final Item item,
			final Fluid fluid,
			final TextureMapping textureMapping
	) {
		final var modelTemplate = new DynamicFluidContainerModelTemplate(
				Optional.of(BUCKET_MODEL),
				Optional.empty(),
				fluid,
				true
		);

		return modelTemplate.create(
				ModelLocationUtils.getModelLocation(item),
				textureMapping,
				modelOutput
		);
	}
}
