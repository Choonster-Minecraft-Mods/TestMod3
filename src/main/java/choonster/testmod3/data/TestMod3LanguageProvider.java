package choonster.testmod3.data;

import choonster.testmod3.TestMod3;
import choonster.testmod3.block.PlaneBlock;
import choonster.testmod3.fluid.group.FluidGroup;
import choonster.testmod3.init.*;
import choonster.testmod3.item.ModBucketItem;
import choonster.testmod3.item.ModSpawnEggItem;
import choonster.testmod3.text.TestMod3Lang;
import choonster.testmod3.util.EnumFaceRotation;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.client.resources.I18n;
import net.minecraft.data.DataGenerator;
import net.minecraft.entity.EntityType;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.Direction;
import net.minecraft.util.IStringSerializable;
import net.minecraftforge.common.data.LanguageProvider;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Generates this mod's en-US language file.
 *
 * @author Choonster
 */
public class TestMod3LanguageProvider extends LanguageProvider {
	private final Map<EntityType<?>, String> ENTITY_TYPE_NAMES = new HashMap<>();

	public TestMod3LanguageProvider(final DataGenerator gen) {
		super(gen, TestMod3.MODID, "en_us");
	}

	@Override
	protected void addTranslations() {
		addEntities();
		addBlocks();
		addItems();
		addFluids();
		addPotions();
		addContainers();
		addCommands();
		addCapabilities();
		addKeyBindings();
		addConfig();
		addChatMessages();
		addSubtitles();
		addMisc();
	}

	@Override
	public String getName() {
		return "TestMo3 " + super.getName();
	}

	private void addBlocks() {
		addBlock(ModBlocks.WATER_GRASS, "Water Grass");
		addBlock(ModBlocks.LARGE_COLLISION_TEST, "Large Collision Test");
		addBlock(ModBlocks.RIGHT_CLICK_TEST, "Right Click Test");
		addBlock(ModBlocks.CLIENT_PLAYER_RIGHT_CLICK, "Client Player Right Click Block");
		addBlock(ModBlocks.ROTATABLE_LAMP, "Rotatable Lamp");
		addBlock(ModBlocks.ITEM_COLLISION_TEST, "Item Collision Test");

		addBlock(ModBlocks.FLUID_TANK, "Fluid Tank");
		add(TestMod3Lang.BLOCK_DESC_FLUID_TANK_FLUID, "%s (%s/%s mB)");
		add(TestMod3Lang.BLOCK_DESC_FLUID_TANK_EMPTY, "Empty");

		addBlock(ModBlocks.ITEM_DEBUGGER, "Item Debugger");
		addBlock(ModBlocks.END_PORTAL_FRAME_FULL, "End Portal Frame Full");
		addBlock(ModBlocks.POTION_EFFECT, "Potion Effect Block");
		addBlock(ModBlocks.CLIENT_PLAYER_ROTATION, "Client Player Rotation Block");
		addBlock(ModBlocks.PIG_SPAWNER_REFILLER, "Pig Spawner Refiller");

		addBlock(ModBlocks.MIRROR_PLANE, "Mirror Plane");
		add(TestMod3Lang.BLOCK_DESC_PLANE_HORIZONTAL_ROTATION, "Horizontal Rotation: %s");
		add(TestMod3Lang.BLOCK_DESC_PLANE_VERTICAL_ROTATION, "Vertical Rotation: %s");

		addBlock(ModBlocks.VANILLA_MODEL_TEST, "Vanilla Model Test");
		addBlock(ModBlocks.FULLBRIGHT, "Fullbright Block");
		addBlock(ModBlocks.NORMAL_BRIGHTNESS, "Normal Brightness Block");
		addBlock(ModBlocks.MAX_HEALTH_SETTER, "Max Health Setter");
		addBlock(ModBlocks.MAX_HEALTH_GETTER, "Max Health Getter");
		addBlock(ModBlocks.SMALL_COLLISION_TEST, "Small Collision Test");
		addBlock(ModBlocks.CHEST, "Mod Chest");
		addBlock(ModBlocks.HIDDEN, "Hidden Block");
		addBlock(ModBlocks.BASIC_PIPE, "Basic Pipe");
		addBlock(ModBlocks.FLUID_PIPE, "Fluid Pipe");
		addBlock(ModBlocks.SURVIVAL_COMMAND_BLOCK, "Survival Command Block");
		addBlock(ModBlocks.REPEATING_SURVIVAL_COMMAND_BLOCK, "Repeating Survival Command Block");
		addBlock(ModBlocks.CHAIN_SURVIVAL_COMMAND_BLOCK, "Chain Survival Command Block");
		addBlock(ModBlocks.OAK_SAPLING, "Oak TestMod3 Sapling");
		addBlock(ModBlocks.SPRUCE_SAPLING, "Spruce TestMod3 Sapling");
		addBlock(ModBlocks.BIRCH_SAPLING, "Birch TestMod3 Sapling");
		addBlock(ModBlocks.JUNGLE_SAPLING, "Jungle TestMod3 Sapling");
		addBlock(ModBlocks.ACACIA_SAPLING, "Acacia TestMod3 Sapling");
		addBlock(ModBlocks.DARK_OAK_SAPLING, "Dark Oak TestMod3 Sapling");
		addBlock(ModBlocks.INVISIBLE, "Invisible Block");

		addBlock(ModBlocks.FLUID_TANK_RESTRICTED, "Restricted Fluid Tank");
		add(TestMod3Lang.BLOCK_DESC_FLUID_TANK_RESTRICTED_ENABLED_FACINGS, "Enabled facings: %s");

		addBlock(ModBlocks.PLANKS, "TestMod3 Planks");

		ModBlocks.COLORED_ROTATABLE_BLOCKS
				.getBlocks()
				.forEach(block -> addBlock(block, String.format("%s Rotatable Block", translate(block.get().getColor()))));

		ModBlocks.COLORED_MULTI_ROTATABLE_BLOCKS
				.getBlocks()
				.forEach(block -> addBlock(block, String.format("%s Multi Rotatable Block", translate(block.get().getColor()))));

		ModBlocks.VARIANTS_BLOCKS
				.getBlocks()
				.forEach(block -> addBlock(block, String.format("Variants Block - %s", StringUtils.capitalize(block.get().getType().getSerializedName()))));

		ModBlocks.TERRACOTTA_SLABS
				.getBlocks()
				.forEach(block -> addBlock(block, String.format("%s Terracotta Slab", translate(block.get().getVariant()))));
	}

	private void addItems() {
		addItem(ModItems.WOODEN_AXE, "Wooden Cutting Axe");
		addItem(ModItems.ENTITY_TEST, "Entity Test");

		addItem(ModItems.RECORD_SOLARIS, "Music Disc");
		add(ModItems.RECORD_SOLARIS.get().getDescriptionId() + ".desc", "Solaris (Path of Exile)");

		addItem(ModItems.HEAVY, "Heavy Item");
		addItem(ModItems.ENTITY_INTERACTION_TEST, "Entity Interaction Test");
		addItem(ModItems.BLOCK_DESTROYER, "Block Destroyer");
		addItem(ModItems.SUBSCRIPTS, "Subscripts");
		addItem(ModItems.SUPERSCRIPTS, "Superscripts");
		addItem(ModItems.MODEL_TEST, "Model Test");
		addItem(ModItems.SNOWBALL_LAUNCHER, "Snowball Launcher");
		addItem(ModItems.SLINGSHOT, "Slingshot");

		addItem(ModItems.UNICODE_TOOLTIPS, "Unicode Tooltips");
		add(TestMod3Lang.ITEM_DESC_UNICODE_TOOLTIPS_1, "§a§oLine 1 (Section Characters in Lang File)§r");
		add(TestMod3Lang.ITEM_DESC_UNICODE_TOOLTIPS_2, "Line 2 (Section Characters in Code)");
		add(TestMod3Lang.ITEM_DESC_UNICODE_TOOLTIPS_3, "Line 3 (EnumChatFormatting)");

		addItem(ModItems.SWAP_TEST_A, "Swap Test A");
		addItem(ModItems.SWAP_TEST_B, "Swap Test B");
		add(TestMod3Lang.ITEM_DESC_SWAP_TEST_WITH_ITEM, "Shift-right click to convert to %s");
		add(TestMod3Lang.ITEM_DESC_SWAP_TEST_WITHOUT_ITEM, "No conversion defined for this item");

		addItem(ModItems.BLOCK_DEBUGGER, "Block Debugger");
		addItem(ModItems.WOODEN_HARVEST_SWORD, "Wooden Harvest Sword");
		addItem(ModItems.DIAMOND_HARVEST_SWORD, "Diamond Harvest Sword");
		addItem(ModItems.CLEARER, "Clearer");
		addItem(ModItems.BOW, "Mod Bow");
		addItem(ModItems.ARROW, "Mod Arrow");
		addItem(ModItems.HEIGHT_TESTER, "Height Tester");
		addItem(ModItems.PIG_SPAWNER_FINITE, "Finite Pig Spawner");
		addItem(ModItems.PIG_SPAWNER_INFINITE, "Infinite Pig Spawner");
		addItem(ModItems.CONTINUOUS_BOW, "Continuous Bow");
		addItem(ModItems.RESPAWNER, "Respawner");
		addItem(ModItems.LOOT_TABLE_TEST, "Loot Table Test");
		addItem(ModItems.MAX_HEALTH_SETTER_ITEM, "Max Health Setter");
		addItem(ModItems.MAX_HEALTH_GETTER_ITEM, "Max Health Getter");
		addItem(ModItems.GUN, "Gun");

		addItem(ModItems.DIMENSION_REPLACEMENT, "Dimension Replacement");
		add(TestMod3Lang.ITEM_DESC_DIMENSION_REPLACEMENT_REPLACEMENT, "Will be replaced with %s when crafted in this dimension");
		add(TestMod3Lang.ITEM_DESC_DIMENSION_REPLACEMENT_NO_REPLACEMENT, "Will not be replaced when crafted in this dimension");

		addItem(ModItems.SADDLE, "Mod Saddle");
		addItem(ModItems.WOODEN_SLOW_SWORD, "Slow Wooden Sword");
		addItem(ModItems.DIAMOND_SLOW_SWORD, "Slow Diamond Sword");
		addItem(ModItems.RITUAL_CHECKER, "Ritual Checker");
		addItem(ModItems.HIDDEN_BLOCK_REVEALER, "Hidden Block Revealer");
		addItem(ModItems.NO_MOD_NAME, "No Mod Name");
		addItem(ModItems.KEY, "Key");
		addItem(ModItems.BLOCK_DETECTION_ARROW, "Block Detection Arrow");
		addItem(ModItems.TRANSLUCENT_ITEM, "Translucent Item");
		addItem(ModItems.ENTITY_KILLER, "Entity Killer");
		addItem(ModItems.CHUNK_ENERGY_SETTER, "Chunk Energy Setter");
		addItem(ModItems.CHUNK_ENERGY_GETTER, "Chunk Energy Getter");
		addItem(ModItems.CHUNK_ENERGY_DISPLAY, "Chunk Energy Display");
		addItem(ModItems.BEACON_ITEM, "Beacon Item");
		addItem(ModItems.SATURATION_HELMET, "Saturation Helmet");

		addItem(ModItems.ENTITY_CHECKER, "Entity Checker");
		add(TestMod3Lang.ITEM_DESC_ENTITY_CHECKER_RADIUS, "Radius: %d");
		add(TestMod3Lang.ITEM_DESC_ENTITY_CHECKER_MODE_CORNER, "Mode: Corner");
		add(TestMod3Lang.ITEM_DESC_ENTITY_CHECKER_MODE_EDGE, "Mode: Edge");

		addItem(ModItems.RUBBER, "Rubber");

		addItem(ModItems.REPLACEMENT_HELMET, "Armour Replacement Helmet");
		addItem(ModItems.REPLACEMENT_CHESTPLATE, "Armour Replacement Chestplate");
		addItem(ModItems.REPLACEMENT_LEGGINGS, "Armour Replacement Leggings");
		addItem(ModItems.REPLACEMENT_BOOTS, "Armour Replacement Boots");
		add(TestMod3Lang.ITEM_DESC_ARMOUR_REPLACEMENT_EQUIP, "Replaces your other armour when equipped");
		add(TestMod3Lang.ITEM_DESC_ARMOUR_REPLACEMENT_UNEQUIP, "Restores your armour when unequipped");
		add(TestMod3Lang.ITEM_DESC_ARMOUR_RESTRICTED, "Disappears if unequipped");

		addItem(ModItems.FLUID_STACK_ITEM, "FluidStack Item");
		addSpawnEgg(ModItems.PLAYER_AVOIDING_CREEPER_SPAWN_EGG);
		addBucket(ModItems.WOODEN_BUCKET, "Wooden Bucket");
		addBucket(ModItems.STONE_BUCKET, "Stone Bucket");

		ModItems.VARIANTS_ITEMS
				.getItems()
				.forEach(item -> addItem(item, String.format("Variants Item - %s", StringUtils.capitalize(item.get().getType().getSerializedName()))));
	}

	private void addFluids() {
		addFluidGroup(ModFluids.STATIC, "Static Fluid");
		addFluidGroup(ModFluids.STATIC_GAS, "Static Gas");
		addFluidGroup(ModFluids.NORMAL, "Normal");
		addFluidGroup(ModFluids.NORMAL_GAS, "Normal Gas");
		addFluidGroup(ModFluids.PORTAL_DISPLACEMENT, "Portal Displacement Fluid");
		//addFluidGroup(ModFluids.FINITE, "Finite Fluid");
	}

	private void addEntities() {
		addEntityType(ModEntities.MOD_ARROW, "Mod Arrow");
		addEntityType(ModEntities.BLOCK_DETECTION_ARROW, "Block Detection Arrow");
		addEntityType(ModEntities.PLAYER_AVOIDING_CREEPER, "Player-Avoiding Creeper");
	}

	private void addPotions() {
		addEffect(ModEffects.TEST, "Test");
		addPotion(ModPotions.TEST, "Testing");
	}

	private void addContainers() {
		add(TestMod3Lang.CONTAINER_CHEST, "Mod Chest");
	}

	private void addCommands() {
		add(TestMod3Lang.ARGUMENT_AXIS_INVALID, "Invalid axis, expected 'x', 'y' or 'z'");

		add(TestMod3Lang.COMMAND_MAX_HEALTH_INVALID_ENTITY, "Invalid entity, only living entities (e.g. players, animals, monsters) are supported");
		add(TestMod3Lang.COMMAND_ROTATE_VECTOR_RESULT, "Rotated vector: [%s, %s, %s]");
		add(TestMod3Lang.COMMAND_RUN_TESTS_TESTS_PASSED, "All tests passed.");
		add(TestMod3Lang.COMMAND_RUN_TESTS_TESTS_FAILED, "Tests failed. See log for more details.");
	}

	private void addCapabilities() {
		add(TestMod3Lang.CHUNK_ENERGY_HUD, "Chunk Energy: %d/%d");

		add(TestMod3Lang.LOCK_ALREADY_LOCKED, "Can't lock container that's already locked.");
		add(TestMod3Lang.LOCK_LOCK_CODE, "Lock Code");
		add(TestMod3Lang.LOCK_SET_LOCK_CODE, "Set Lock Code");

		add(TestMod3Lang.PIG_SPAWNER_FINITE_DESC, "Spawnable Pigs: %s/%s");
		add(TestMod3Lang.PIG_SPAWNER_INFINITE_DESC, "Spawnable Pigs: ∞");
	}

	private void addChatMessages() {
		add(TestMod3Lang.MESSAGE_ENTITY_INTERACT_COUNT, "Interact count: %s");
		add(TestMod3Lang.MESSAGE_BLOCK_DESTROYER_DESTROY, "Destroyed Wheat with Age >= 6");
		add(String.format(TestMod3Lang.MESSAGE_SCRIPTS_RIGHT_CLICK.getTranslationKey(), ModItems.SUBSCRIPTS.get().getDescriptionId()), "Subscripts: %s");
		add(String.format(TestMod3Lang.MESSAGE_SCRIPTS_RIGHT_CLICK.getTranslationKey(), ModItems.SUPERSCRIPTS.get().getDescriptionId()), "Superscripts: %s");
		add(TestMod3Lang.MESSAGE_CLEARER_CLEARING, "Clearing the chunk at %s, %s");
		add(TestMod3Lang.MESSAGE_CLEARER_CLEARED, "Chunk cleared");
		add(String.format(TestMod3Lang.MESSAGE_CLEARER_MODE_S.getTranslationKey(), 0), "Switched to Whitelist Mode");
		add(String.format(TestMod3Lang.MESSAGE_CLEARER_MODE_S.getTranslationKey(), 1), "Switched to All Mode");
		add(TestMod3Lang.MESSAGE_HEIGHT_TESTER_HEIGHT, "Height at %s,%s is %s");
		add(TestMod3Lang.MESSAGE_LOGIN_FREE_APPLE, "Congratulations! You have received a free apple for logging into the game.");
		add(TestMod3Lang.MESSAGE_LOGIN_ALREADY_RECEIVED, "You've already received your free apple, you won't be getting any more.");
		add(TestMod3Lang.MESSAGE_PIG_SPAWNER_REFILLER_REFILLED, "Refilled held pig spawner.");
		add(TestMod3Lang.MESSAGE_RESPAWNER_NO_SPAWN_LOCATION, "No spawn location set");
		add(TestMod3Lang.MESSAGE_RESPAWNER_TELEPORTING, "Teleporting to %d,%d,%d in dimension %d");
		add(TestMod3Lang.MESSAGE_CLIENT_PLAYER_RIGHT_CLICK_RIGHT_CLICK, "Right click!!");
		add(TestMod3Lang.MESSAGE_DEATH_COORDINATES, "You died at %d %d %d in dimension %s!");
		add(TestMod3Lang.MESSAGE_PLAYER_RECEIVED_LOOT_BASE, "Received Loot: %s");
		add(TestMod3Lang.MESSAGE_PLAYER_RECEIVED_LOOT_ITEM, "%d x %s");
		add(TestMod3Lang.MESSAGE_PLAYER_RECEIVED_LOOT_NO_LOOT, "No loot received.");
		add(TestMod3Lang.MESSAGE_MAX_HEALTH_ADD, "Added %2$s bonus max health to %1$s");
		add(TestMod3Lang.MESSAGE_MAX_HEALTH_SET, "Set bonus max health of %s to %s");
		add(TestMod3Lang.MESSAGE_MAX_HEALTH_GET, "Max health of %s is %s (%s bonus)");
		add(TestMod3Lang.MESSAGE_RITUAL_CHECKER_SUCCESS, "Ritual pattern is valid!");
		add(TestMod3Lang.MESSAGE_RITUAL_CHECKER_FAILURE, "Ritual pattern is invalid at %d,%d,%d!");
		add(TestMod3Lang.MESSAGE_HIDDEN_BLOCK_REVEALER_REVEAL, "Revealing hidden blocks.");
		add(TestMod3Lang.MESSAGE_HIDDEN_BLOCK_REVEALER_HIDE, "Hiding hidden blocks.");
		add(TestMod3Lang.MESSAGE_CHUNK_ENERGY_ADD, "Added %d energy to chunk %s");
		add(TestMod3Lang.MESSAGE_CHUNK_ENERGY_REMOVE, "Removed %d energy from chunk %s");
		add(TestMod3Lang.MESSAGE_CHUNK_ENERGY_GET, "Chunk %s contains %d energy");
		add(TestMod3Lang.MESSAGE_CHUNK_ENERGY_NOT_FOUND, "No chunk energy found for chunk %s");
		add(TestMod3Lang.MESSAGE_PRINT_POTIONS_NO_POTIONS, "No active potions on %s");
		add(TestMod3Lang.MESSAGE_PRINT_POTIONS_POTIONS, "Active potions on %s:");
		add(TestMod3Lang.MESSAGE_PRINT_POTIONS_NOT_LIVING, "%s isn't a living entity");
		add(TestMod3Lang.MESSAGE_PRINT_POTIONS_NO_ENTITY, "You must look at an entity");
		add(TestMod3Lang.MESSAGE_FLUID_TANK_RESTRICTED_FACING_ENABLED, "Enabled access from facing %s");
		add(TestMod3Lang.MESSAGE_FLUID_TANK_RESTRICTED_FACING_DISABLED, "Disabled access from facing %s");
		add(TestMod3Lang.MESSAGE_FLUID_TANK_RESTRICTED_ENABLED_FACINGS, "Enabled facings: %s");
		add(TestMod3Lang.MESSAGE_ENTITY_CHECKER_RESULTS, "%d entities found");
		add(TestMod3Lang.MESSAGE_ENTITY_CHECKER_RADIUS, "Radius set to %d");
		add(TestMod3Lang.MESSAGE_ENTITY_CHECKER_MODE_CORNER, "Mode set to Corner");
		add(TestMod3Lang.MESSAGE_ENTITY_CHECKER_MODE_EDGE, "Mode set to Edge");
	}

	private void addKeyBindings() {
		add(TestMod3Lang.KEY_CATEGORY_GENERAL, "TestMod3");
		add(TestMod3Lang.KEY_PLACE_HELD_BLOCK, "Place Held Block");
		add(TestMod3Lang.KEY_PRINT_POTIONS, "Print Potions");
	}

	private void addConfig() {
		add("testmod3.config.common.fooBar", "Foo Bar");
		add("testmod3.config.common.exampleEnumProperty", "Example Enum Property");
		add("testmod3.config.common.exampleMapField", "Example Map Field");
		add("testmod3.config.client.baz", "Baz");
		add("testmod3.config.client.subcategory.exampleSubcategoryEnumProperty", "Example Subcategory Enum Property");
		add("testmod3.config.client.exampleNestedEnumProperty", "Example Nested Enum Property");
		add("testmod3.config.client.chunkEnergyHUDPos", "Chunk Energy HUD Position");
	}

	private void addSubtitles() {
		add(TestMod3Lang.SUBTITLE_ITEM_GUN_FIRE, "Gun fires");
		add(TestMod3Lang.SUBTITLE_ACTION_SADDLE, "Mod saddle equips");
	}

	private void addMisc() {
		add("itemGroup." + TestMod3.MODID, "TestMod3");

		add(TestMod3Lang.DESC_ROTATABLE_FACING, "Facing: %s");
		add(TestMod3Lang.DESC_MULTI_ROTATABLE_FACE_ROTATION, "Face Rotation: %s");

		for (final Direction direction : Direction.values()) {
			add(TestMod3Lang.PREFIX_FACING, direction, StringUtils.capitalize(direction.getSerializedName()));
		}

		for (final EnumFaceRotation faceRotation : EnumFaceRotation.values()) {
			add(TestMod3Lang.PREFIX_FACE_ROTATION, faceRotation, StringUtils.capitalize(faceRotation.getSerializedName()));
		}

		for (final PlaneBlock.VerticalRotation verticalRotation : PlaneBlock.VerticalRotation.values()) {
			add(TestMod3Lang.PREFIX_VERTICAL_ROTATION, verticalRotation, StringUtils.capitalize(verticalRotation.getSerializedName()));
		}
	}

	@Override
	public void addEntityType(final Supplier<? extends EntityType<?>> key, final String name) {
		super.addEntityType(key, name);
		ENTITY_TYPE_NAMES.put(key.get(), name);
	}

	private void addSpawnEgg(final Supplier<? extends ModSpawnEggItem> spawnEggItem) {
		final ModSpawnEggItem item = spawnEggItem.get();
		final EntityType<?> entityType = item.getType(null);
		add(item, String.format("%s Spawn Egg", ENTITY_TYPE_NAMES.get(entityType)));
	}

	private void addBucket(final Supplier<? extends ModBucketItem> bucketItem, final String name) {
		final ModBucketItem item = bucketItem.get();

		add(item, name);
		add(item.getDescriptionId() + ".filled", "%s " + name);
	}

	private <
			STILL extends Fluid, FLOWING extends Fluid,
			BLOCK extends FlowingFluidBlock, BUCKET extends Item,
			GROUP extends FluidGroup<STILL, FLOWING, BLOCK, BUCKET>
			>
	void addFluidGroup(final GROUP group, final String name) {
		add(group.getStill().get().getAttributes().getTranslationKey(), name);
		add(group.getFlowing().get().getAttributes().getTranslationKey(), String.format("Flowing %s", name));
		addBlock(group.getBlock(), name);
		addItem(group.getBucket(), String.format("%s Bucket", name));
	}

	private void addPotion(final Supplier<? extends Potion> potion, final String name) {
		add(getPotionItemTranslationKey(potion, Items.TIPPED_ARROW), String.format("Arrow of %s", name));
		add(getPotionItemTranslationKey(potion, Items.POTION), String.format("Potion of %s", name));
		add(getPotionItemTranslationKey(potion, Items.SPLASH_POTION), String.format("Splash Potion of %s", name));
		add(getPotionItemTranslationKey(potion, Items.LINGERING_POTION), String.format("Lingering Potion of %s", name));
	}

	private String getPotionItemTranslationKey(final Supplier<? extends Potion> potion, final Item item) {
		final ItemStack stack = PotionUtils.setPotion(new ItemStack(item), potion.get());
		return stack.getItem().getDescriptionId(stack);
	}

	private void add(final TestMod3Lang lang, final String value) {
		add(lang.getTranslationKey(), value);
	}

	private void add(final TestMod3Lang prefix, final IStringSerializable enumValue, final String name) {
		add(prefix.getTranslationKey() + "." + enumValue.getSerializedName(), name);
	}

	private String translate(final DyeColor colour) {
		return translate("color.minecraft." + colour.getName());
	}

	private String translate(final String key) {
		return I18n.get(key);
	}
}
