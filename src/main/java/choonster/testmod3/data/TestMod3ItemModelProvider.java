package choonster.testmod3.data;

import choonster.testmod3.TestMod3;
import choonster.testmod3.client.item.RevealHiddenBlocksItemPropertyFunction;
import choonster.testmod3.client.item.TicksSinceLastUseItemPropertyFunction;
import choonster.testmod3.fluid.group.FluidGroup;
import choonster.testmod3.init.ModFluids;
import choonster.testmod3.init.ModItems;
import choonster.testmod3.util.RegistryUtil;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.client.model.generators.loaders.DynamicFluidContainerModelBuilder;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.registries.RegistryObject;
import org.apache.commons.lang3.tuple.Pair;

import java.util.function.Supplier;
import java.util.stream.IntStream;

/**
 * Generates this mod's item models.
 *
 * @author Choonster
 */
public class TestMod3ItemModelProvider extends ItemModelProvider {
	private static final String LAYER_0 = "layer0";

	/**
	 * A model that extends item/generated and uses the same transforms as the Vanilla bow.
	 */
	private final Supplier<ModelFile> simpleModel = Lazy.of(() ->
			withGeneratedParent("simple_model")
					.transforms()

					.transform(ItemDisplayContext.THIRD_PERSON_RIGHT_HAND)
					.rotation(-80, 260, -40)
					.translation(-1, -2, 2.5f)
					.scale(0.9f, 0.9f, 0.9f)
					.end()

					.transform(ItemDisplayContext.THIRD_PERSON_LEFT_HAND)
					.rotation(-80, -280, 40)
					.translation(-1, -2, 2.5f)
					.scale(0.9f, 0.9f, 0.9f)
					.end()

					.transform(ItemDisplayContext.FIRST_PERSON_RIGHT_HAND)
					.rotation(0, -90, 25)
					.translation(1.13f, 3.2f, 1.13f)
					.scale(0.68f, 0.68f, 0.68f)
					.end()

					.transform(ItemDisplayContext.FIRST_PERSON_LEFT_HAND)
					.rotation(0, 90, -25)
					.translation(1.13f, 3.2f, 1.13f)
					.scale(0.68f, 0.68f, 0.68f)
					.end()

					.end()
	);


	public TestMod3ItemModelProvider(final PackOutput output, final ExistingFileHelper existingFileHelper) {
		super(output, TestMod3.MODID, existingFileHelper);
	}

	/**
	 * Gets a name for this provider, to use in logging.
	 */
	@Override
	public String getName() {
		return "TestMod3ItemModels";
	}

	@Override
	protected void registerModels() {
		withExistingParent(ModItems.WOODEN_AXE.get(), Items.WOODEN_AXE);

		withExistingParent(ModItems.ENTITY_TEST.get(), Items.PORKCHOP);

		withExistingParent(ModItems.RECORD_SOLARIS.get(), Items.MUSIC_DISC_13);

		withExistingParent(ModItems.HEAVY.get(), Items.BRICK);

		withExistingParent(ModItems.ENTITY_INTERACTION_TEST.get(), Items.BEEF);

		withExistingParent(ModItems.BLOCK_DESTROYER.get(), Items.TNT_MINECART);

		withSimpleParentAndDefaultTexture(ModItems.SUBSCRIPTS.get());

		withSimpleParentAndDefaultTexture(ModItems.SUPERSCRIPTS.get());


		// Create the parent model
		final var modelTest = withSimpleParent(ModItems.MODEL_TEST.get(), itemTexture(ModItems.MODEL_TEST.get()) + "_standby");

		// Create three child models and add them as overrides that display when the ticks since last use is >= index * 20
		IntStream.range(0, 3)
				.mapToObj(index -> {
					final var model = withSimpleParent(name(ModItems.MODEL_TEST.get()) + "_" + index)
							.texture(LAYER_0, itemTexture(ModItems.MODEL_TEST.get()) + "_" + index);

					return Pair.of(index, model);
				})
				.forEachOrdered(child ->
						modelTest
								.override()
								.predicate(TicksSinceLastUseItemPropertyFunction.ID, child.getKey() * 20)
								.model(child.getValue())
								.end()
				);

		// Add the parent as a fallback that displays when the ticks since last use is >= 60
		modelTest
				.override()
				.predicate(TicksSinceLastUseItemPropertyFunction.ID, 60)
				.model(modelTest)
				.end();


		withExistingParent(ModItems.SNOWBALL_LAUNCHER.get(), Items.FISHING_ROD);


		// Create the parent model
		final var slingshot = withSimpleParentAndDefaultTexture(ModItems.SLINGSHOT.get());

		// Create the child model
		final var slingshotPulled = getBuilder(name(ModItems.SLINGSHOT.get()) + "_pulled")
				.parent(slingshot)
				.texture(LAYER_0, itemTexture(ModItems.SLINGSHOT.get()) + "_pulled");

		// Add the child as an override that displays when the ticks since last use is >= 0 and < 20
		slingshot
				.override()
				.predicate(TicksSinceLastUseItemPropertyFunction.ID, 0)
				.model(slingshotPulled)
				.end();

		// Add the parent as a fallback that displays when the ticks since last use is >= 20
		slingshot
				.override()
				.predicate(TicksSinceLastUseItemPropertyFunction.ID, 1)
				.model(slingshot)
				.end();


		withExistingParent(ModItems.UNICODE_TOOLTIPS.get(), Items.RABBIT);

		withExistingParent(ModItems.SWAP_TEST_A.get(), Items.BRICK);

		withExistingParent(ModItems.SWAP_TEST_B.get(), Items.NETHER_BRICK);

		withExistingParent(ModItems.BLOCK_DEBUGGER.get(), Items.NETHER_STAR);

		withExistingParent(ModItems.WOODEN_HARVEST_SWORD.get(), Items.WOODEN_SWORD);

		withExistingParent(ModItems.DIAMOND_HARVEST_SWORD.get(), Items.DIAMOND_SWORD);

		withExistingParent(ModItems.CLEARER.get(), Items.NETHER_STAR);

		bowItem(ModItems.BOW.get());

		withGeneratedParentAndDefaultTexture(ModItems.ARROW.get());

		withExistingParent(ModItems.HEIGHT_TESTER.get(), Items.COMPASS);

		withExistingParent(ModItems.PIG_SPAWNER_FINITE.get(), Items.PORKCHOP);

		withExistingParent(ModItems.PIG_SPAWNER_INFINITE.get(), Items.PORKCHOP);

		bowItem(ModItems.CONTINUOUS_BOW.get());

		withExistingParent(ModItems.RESPAWNER.get(), Items.CLOCK);

		withExistingParent(ModItems.LOOT_TABLE_TEST.get(), Items.GOLD_INGOT);

		withGeneratedParentAndDefaultTexture(ModItems.MAX_HEALTH_GETTER_ITEM.get());

		withGeneratedParentAndDefaultTexture(ModItems.MAX_HEALTH_SETTER_ITEM.get());

		withSimpleParentAndDefaultTexture(ModItems.GUN.get());

		withSimpleParentAndDefaultTexture(ModItems.DIMENSION_REPLACEMENT.get());

		withExistingParent(ModItems.SADDLE.get(), Items.SADDLE);

		withExistingParent(ModItems.WOODEN_SLOW_SWORD.get(), Items.WOODEN_SWORD);

		withExistingParent(ModItems.DIAMOND_SLOW_SWORD.get(), Items.DIAMOND_SWORD);

		withExistingParent(name(ModItems.RITUAL_CHECKER.get()), mcLoc("handheld"))
				.texture(LAYER_0, "item/banner_base");


		// Create the parent model
		final var hiddenBlockRevealer = withGeneratedParentAndDefaultTexture(ModItems.HIDDEN_BLOCK_REVEALER.get());

		// Create the child model and add it as an override that's displayed when hidden blocks are being revealed
		final var hiddenBlockRevealerActive = getBuilder(name(ModItems.HIDDEN_BLOCK_REVEALER.get()) + "_active")
				.parent(hiddenBlockRevealer)
				.texture(LAYER_0, itemTexture(ModItems.HIDDEN_BLOCK_REVEALER.get()) + "_active");

		hiddenBlockRevealer
				.override()
				.predicate(RevealHiddenBlocksItemPropertyFunction.ID, 1)
				.model(hiddenBlockRevealerActive)
				.end();

		withExistingParent(ModItems.NO_MOD_NAME.get(), Items.BREAD);

		withExistingParent(name(ModItems.KEY.get()), "handheld")
				.texture(LAYER_0, itemTexture(ModItems.KEY.get()));

		withGeneratedParentAndDefaultTexture(ModItems.BLOCK_DETECTION_ARROW.get());

		withGeneratedParent(name(ModItems.TRANSLUCENT_ITEM.get()))
				.texture(LAYER_0, mcLoc("block/ice"));

		withGeneratedParentAndDefaultTexture(ModItems.ENTITY_KILLER.get());

		withGeneratedParentAndDefaultTexture(ModItems.CHUNK_ENERGY_SETTER.get());

		withGeneratedParentAndDefaultTexture(ModItems.CHUNK_ENERGY_GETTER.get());

		withGeneratedParentAndDefaultTexture(ModItems.CHUNK_ENERGY_DISPLAY.get());

		withExistingParent(ModItems.BEACON_ITEM.get(), Items.BEACON);

		withExistingParent(ModItems.SATURATION_HELMET.get(), Items.CHAINMAIL_HELMET);

		withExistingParent(ModItems.ENTITY_CHECKER.get(), Items.BONE);

		withGeneratedParentAndDefaultTexture(ModItems.RUBBER.get());

		withExistingParent(ModItems.REPLACEMENT_HELMET.get(), Items.CHAINMAIL_HELMET);

		withExistingParent(ModItems.REPLACEMENT_CHESTPLATE.get(), Items.CHAINMAIL_CHESTPLATE);

		withExistingParent(ModItems.REPLACEMENT_LEGGINGS.get(), Items.CHAINMAIL_LEGGINGS);

		withExistingParent(ModItems.REPLACEMENT_BOOTS.get(), Items.CHAINMAIL_BOOTS);

		getBuilder(name(ModItems.FLUID_STACK_ITEM.get()));

		spawnEggItem(ModItems.PLAYER_AVOIDING_CREEPER_SPAWN_EGG.get());

		bucketItem(ModItems.WOODEN_BUCKET.get());

		bucketItem(ModItems.STONE_BUCKET.get());

		ModItems.VARIANTS_ITEMS
				.getItems()
				.stream()
				.map(RegistryObject::get)
				.forEach(this::withGeneratedParentAndDefaultTexture);

		bucketItem(ModFluids.STATIC);
		bucketItem(ModFluids.STATIC_GAS);
		bucketItem(ModFluids.NORMAL);
		bucketItem(ModFluids.NORMAL_GAS);
		bucketItem(ModFluids.PORTAL_DISPLACEMENT);
	}


	private ResourceLocation key(final Item item) {
		return RegistryUtil.getKey(item);
	}

	private String name(final Item item) {
		return key(item).getPath();
	}

	private ResourceLocation itemTexture(final Item item) {
		final var name = key(item);
		return new ResourceLocation(name.getNamespace(), ITEM_FOLDER + "/" + name.getPath());
	}

	private ItemModelBuilder withExistingParent(final Item item, final Item modelItem) {
		return withExistingParent(name(item), key(modelItem));
	}

	private ItemModelBuilder withGeneratedParentAndDefaultTexture(final Item item) {
		return withGeneratedParent(name(item))
				.texture(LAYER_0, itemTexture(item));
	}

	private ItemModelBuilder withGeneratedParent(final String name) {
		return withExistingParent(name, mcLoc("generated"));
	}

	private ItemModelBuilder withSimpleParentAndDefaultTexture(final Item item) {
		return withSimpleParent(item, itemTexture(item));
	}

	private ItemModelBuilder withSimpleParent(final Item item, final ResourceLocation texture) {
		return withSimpleParent(item, texture.toString());
	}

	private ItemModelBuilder withSimpleParent(final Item item, final String texture) {
		return withSimpleParent(name(item))
				.texture(LAYER_0, texture);
	}

	private ItemModelBuilder withSimpleParent(final String name) {
		return getBuilder(name)
				.parent(simpleModel.get());
	}

	private void bowItem(final Item item) {
		// Create the parent model
		final var bow = withSimpleParent(item, itemTexture(Items.BOW));

		// Create three child models
		final var bowPullingModels = IntStream.range(0, 3)
				.mapToObj(index ->
						getBuilder(name(item) + "_pulling_" + index)
								.parent(bow)
								.texture(LAYER_0, itemTexture(Items.BOW) + "_pulling_" + index)
				)
				.toList();

		// Add the child models as overrides that display when the bow is pulled back >= 0%, >= 65% and >= 90% respectively
		bow
				.override()
				.predicate(mcLoc("pulling"), 1)
				.model(bowPullingModels.get(0))
				.end()

				.override()
				.predicate(mcLoc("pulling"), 1)
				.predicate(mcLoc("pull"), 0.65f)
				.model(bowPullingModels.get(1))
				.end()

				.override()
				.predicate(mcLoc("pulling"), 1)
				.predicate(mcLoc("pull"), 0.9f)
				.model(bowPullingModels.get(2))
				.end();
	}

	private void spawnEggItem(final Item item) {
		withExistingParent(name(item), mcLoc("template_spawn_egg"));
	}

	private void bucketItem(final FluidGroup<?, ?, ?, ?, ?> fluidGroup) {
		final var item = fluidGroup.getBucket().get();
		final var fluid = item instanceof BucketItem ? ((BucketItem) item).getFluid() : Fluids.EMPTY;

		getBuilder(name(item))
				.parent(getExistingFile(new ResourceLocation("forge", "bucket")))
				.customLoader(DynamicFluidContainerModelBuilder::begin)
				.fluid(fluid)
				.flipGas(true)
				.end();
	}

	private void bucketItem(final Item item) {
		final var baseTexture = itemTexture(item) + "_base";

		getBuilder(name(item))
				.parent(getExistingFile(new ResourceLocation("forge", "bucket")))
				.texture("base", baseTexture)
				.texture("particle", baseTexture)
				.customLoader(DynamicFluidContainerModelBuilder::begin)
				.fluid(Fluids.EMPTY)
				.flipGas(true)
				.end();
	}
}
