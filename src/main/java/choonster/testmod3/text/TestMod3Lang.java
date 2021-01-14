package choonster.testmod3.text;

import choonster.testmod3.TestMod3;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;

/**
 * Adapted from Mekanism's APILang under the following license:
 * <p>
 * MIT License
 * <p>
 * Copyright (c) 2017-2020 Aidan C. Brady
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
public enum TestMod3Lang {
	// Block descriptions
	BLOCK_DESC_FLUID_TANK_EMPTY("block", "fluid_tank.empty.desc"),
	BLOCK_DESC_FLUID_TANK_FLUID("block", "fluid_tank.fluid.desc"),
	BLOCK_DESC_FLUID_TANK_RESTRICTED_ENABLED_FACINGS("block", "fluid_tank_restricted.enabled_facings"),
	BLOCK_DESC_PLANE_HORIZONTAL_ROTATION("block", "plane.horizontal_rotation.desc"),
	BLOCK_DESC_PLANE_VERTICAL_ROTATION("block", "plane.vertical_rotation.desc"),

	// Commands
	ARGUMENT_AXIS_INVALID("arguments", "axis.invalid"),
	COMMAND_MAX_HEALTH_INVALID_ENTITY("commands", "maxhealth.invalid_entity"),
	COMMAND_ROTATE_VECTOR_RESULT("commands", "rotatevector.result"),
	COMMAND_RUN_TESTS_TESTS_FAILED("commands", "runtests.tests_failed"),
	COMMAND_RUN_TESTS_TESTS_PASSED("commands", "runtests.tests_passed"),

	// Containers
	CONTAINER_CHEST("container", "chest"),

	// HUD descriptions
	DESC_MULTI_ROTATABLE_FACE_ROTATION(prefix() + "multi_rotatable.face_rotation.desc"),
	DESC_ROTATABLE_FACING(prefix() + "rotatable.facing.desc"),

	// Item descriptions
	ITEM_DESC_ARMOUR_REPLACEMENT_EQUIP("item", "armour_replacement.equip.desc"),
	ITEM_DESC_ARMOUR_REPLACEMENT_UNEQUIP("item", "armour_replacement.unequip.desc"),
	ITEM_DESC_ARMOUR_RESTRICTED("item", "armour_restricted.desc"),
	ITEM_DESC_DIMENSION_REPLACEMENT_NO_REPLACEMENT("item", "dimension_replacement.no_replacement.desc"),
	ITEM_DESC_DIMENSION_REPLACEMENT_REPLACEMENT("item", "dimension_replacement.replacement.desc"),
	ITEM_DESC_ENTITY_CHECKER_MODE_CORNER("item", "entity_checker.mode.corner.desc"),
	ITEM_DESC_ENTITY_CHECKER_MODE_EDGE("item", "entity_checker.mode.edge.desc"),
	ITEM_DESC_ENTITY_CHECKER_RADIUS("item", "entity_checker.radius.desc"),
	ITEM_DESC_SWAP_TEST_WITHOUT_ITEM("item", "swap_test.without_item.desc"),
	ITEM_DESC_SWAP_TEST_WITH_ITEM("item", "swap_test.with_item.desc"),
	ITEM_DESC_UNICODE_TOOLTIPS_1("item", "unicode_tooltips.1.desc"),
	ITEM_DESC_UNICODE_TOOLTIPS_2("item", "unicode_tooltips.2.desc"),
	ITEM_DESC_UNICODE_TOOLTIPS_3("item", "unicode_tooltips.3.desc"),

	// Key bindings
	KEY_CATEGORY_GENERAL("key.category", "general"),
	KEY_PLACE_HELD_BLOCK("key", "place_held_block"),
	KEY_PRINT_POTIONS("key", "print_potions"),

	// Capabilities
	CHUNK_ENERGY_HUD(prefix() + "chunk_energy.hud"),
	LOCK_ALREADY_LOCKED(prefix() + "lock.already_locked"),
	LOCK_LOCK_CODE(prefix() + "lock.lock_code"),
	LOCK_SET_LOCK_CODE(prefix() + "lock.set_lock_code"),
	PIG_SPAWNER_FINITE_DESC(prefix() + "pig_spawner.finite.desc"),
	PIG_SPAWNER_INFINITE_DESC(prefix() + "pig_spawner.infinite.desc"),

	// Chat messages
	MESSAGE_BLOCK_DESTROYER_DESTROY("message", "block_destroyer.destroy"),
	MESSAGE_CHUNK_ENERGY_ADD("message", "chunk_energy.add"),
	MESSAGE_CHUNK_ENERGY_GET("message", "chunk_energy.get"),
	MESSAGE_CHUNK_ENERGY_NOT_FOUND("message", "chunk_energy.not_found"),
	MESSAGE_CHUNK_ENERGY_REMOVE("message", "chunk_energy.remove"),
	MESSAGE_CLEARER_CLEARED("message", "clearer.cleared"),
	MESSAGE_CLEARER_CLEARING("message", "clearer.clearing"),
	MESSAGE_CLEARER_MODE_S("message." + prefix() + "clearer.mode.%s"),
	MESSAGE_CLIENT_PLAYER_RIGHT_CLICK_RIGHT_CLICK("message", "client_player_right_click.right_click"),
	MESSAGE_DEATH_COORDINATES("message", "death.coordinates"),
	MESSAGE_ENTITY_CHECKER_MODE_CORNER("message", "entity_checker.mode.corner"),
	MESSAGE_ENTITY_CHECKER_MODE_EDGE("message", "entity_checker.mode.edge"),
	MESSAGE_ENTITY_CHECKER_RADIUS("message", "entity_checker.radius"),
	MESSAGE_ENTITY_CHECKER_RESULTS("message", "entity_checker.results"),
	MESSAGE_ENTITY_INTERACT_COUNT("message", "entity_interact.count"),
	MESSAGE_FLUID_TANK_RESTRICTED_ENABLED_FACINGS("message", "fluid_tank_restricted.enabled_facings"),
	MESSAGE_FLUID_TANK_RESTRICTED_FACING_DISABLED("message", "fluid_tank_restricted.facing_disabled"),
	MESSAGE_FLUID_TANK_RESTRICTED_FACING_ENABLED("message", "fluid_tank_restricted.facing_enabled"),
	MESSAGE_HEIGHT_TESTER_HEIGHT("message", "height_tester.height"),
	MESSAGE_HIDDEN_BLOCK_REVEALER_HIDE("message", "hidden_block_revealer.hide"),
	MESSAGE_HIDDEN_BLOCK_REVEALER_REVEAL("message", "hidden_block_revealer.reveal"),
	MESSAGE_LOGIN_ALREADY_RECEIVED("message", "login.already_received"),
	MESSAGE_LOGIN_FREE_APPLE("message", "login.free_apple"),
	MESSAGE_MAX_HEALTH_ADD("message", "max_health.add"),
	MESSAGE_MAX_HEALTH_GET("message", "max_health.get"),
	MESSAGE_MAX_HEALTH_SET("message", "max_health.set"),
	MESSAGE_PIG_SPAWNER_REFILLER_REFILLED("message", "pig_spawner_refiller.refilled"),
	MESSAGE_PLAYER_RECEIVED_LOOT_BASE("message", "player_received_loot.base"),
	MESSAGE_PLAYER_RECEIVED_LOOT_ITEM("message", "player_received_loot.item"),
	MESSAGE_PLAYER_RECEIVED_LOOT_NO_LOOT("message", "player_received_loot.no_loot"),
	MESSAGE_PRINT_POTIONS_NOT_LIVING("message", "print_potions.not_living"),
	MESSAGE_PRINT_POTIONS_NO_ENTITY("message", "print_potions.no_entity"),
	MESSAGE_PRINT_POTIONS_NO_POTIONS("message", "print_potions.no_potions"),
	MESSAGE_PRINT_POTIONS_POTIONS("message", "print_potions.potions"),
	MESSAGE_RESPAWNER_NO_SPAWN_LOCATION("message", "respawner.no_spawn_location"),
	MESSAGE_RESPAWNER_TELEPORTING("message", "respawner.teleporting"),
	MESSAGE_RITUAL_CHECKER_FAILURE("message", "ritual_checker.failure"),
	MESSAGE_RITUAL_CHECKER_SUCCESS("message", "ritual_checker.success"),
	MESSAGE_SCRIPTS_RIGHT_CLICK("message." + prefix() + "%s.right_click"),

	// Enum prefixes
	PREFIX_FACE_ROTATION(prefix() + "face_rotation"),
	PREFIX_FACING(prefix() + "facing"),
	PREFIX_VERTICAL_ROTATION(prefix() + "vertical_rotation"),

	// Subtitles
	SUBTITLE_ACTION_SADDLE("subtitles.action", "saddle"),
	SUBTITLE_ITEM_GUN_FIRE("subtitles.item", "gun.fire"),

	;

	private final String key;

	TestMod3Lang(final String type, final String path) {
		this(Util.makeTranslationKey(type, new ResourceLocation(TestMod3.MODID, path)));
	}

	TestMod3Lang(final String key) {
		this.key = key;
	}

	public String getTranslationKey() {
		return key;
	}

	private static String prefix() {
		return TestMod3.MODID + ".";
	}


}
