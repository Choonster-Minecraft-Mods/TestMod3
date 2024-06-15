package choonster.testmod3.world.item;

import com.google.common.collect.ImmutableList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.component.Tool;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.ToolActions;

import java.util.List;
import java.util.stream.Stream;

/**
 * A tool that can function as a sword, pickaxe, axe or shovel.
 * <p>
 * Test for this thread:
 * http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/modification-development/2550421-how-to-make-a-tool-e-g-a-sword-have-the-abilities
 *
 * @author Choonster
 */
public class HarvestSwordItem extends TieredItem {
	/**
	 * The speed at which Cobwebs are harvested
	 */
	private static final float DIG_SPEED_COBWEB = 15.0f;

	/**
	 * The speed at which Sword-efficient blocks are harvested
	 */
	private static final float DIG_SPEED_SWORD = 1.5f;

	/**
	 * The speed at which blocks are harvested if this isn't their correct tool
	 */
	private static final float DIG_SPEED_DEFAULT = 1.0f;

	/**
	 * The base attack damage before the {@link Tier}'s attack damage is factored in
	 */
	private static final float BASE_ATTACK_DAMAGE = 3.0f;

	/**
	 * The attack speed
	 */
	private static final float ATTACK_SPEED = -2.4f;

	public HarvestSwordItem(final Tier tier, final Item.Properties properties) {
		super(tier, properties.component(DataComponents.TOOL, createTool(tier)));
	}

	public static ItemAttributeModifiers createAttributes(final Tier tier) {
		return ItemAttributeModifiers.builder()
				.add(
						Attributes.ATTACK_DAMAGE,
						new AttributeModifier(BASE_ATTACK_DAMAGE_ID, BASE_ATTACK_DAMAGE + tier.getAttackDamageBonus(), AttributeModifier.Operation.ADD_VALUE),
						EquipmentSlotGroup.MAINHAND
				)
				.add(
						Attributes.ATTACK_SPEED,
						new AttributeModifier(BASE_ATTACK_SPEED_ID, ATTACK_SPEED, AttributeModifier.Operation.ADD_VALUE),
						EquipmentSlotGroup.MAINHAND
				)
				.build();
	}

	@Override
	public boolean canPerformAction(final ItemStack stack, final ToolAction toolAction) {
		return Stream.of(
				ToolActions.DEFAULT_SWORD_ACTIONS,
				ToolActions.DEFAULT_PICKAXE_ACTIONS,
				ToolActions.DEFAULT_AXE_ACTIONS,
				ToolActions.DEFAULT_SHOVEL_ACTIONS
		).anyMatch(toolActions -> toolActions.contains(toolAction));
	}

	@Override
	public boolean hurtEnemy(final ItemStack itemStack, final LivingEntity target, final LivingEntity attacker) {
		// Only reduce the durability by 1 point (like swords do) instead of 2 (like tools do)
		itemStack.hurtAndBreak(1, target, EquipmentSlot.MAINHAND);
		return true;
	}

	private static Tool createTool(final Tier tier) {
		final var rules = ImmutableList.<Tool.Rule>builder()
				.add(Tool.Rule.deniesDrops(tier.getIncorrectBlocksForDrops()));

		final var minesAndDrops = Stream.of(
				BlockTags.MINEABLE_WITH_AXE,
				BlockTags.MINEABLE_WITH_HOE,
				BlockTags.MINEABLE_WITH_PICKAXE,
				BlockTags.MINEABLE_WITH_SHOVEL
		).map(tag -> Tool.Rule.minesAndDrops(BlockTags.MINEABLE_WITH_AXE, tier.getSpeed()));

		rules.addAll(minesAndDrops.iterator());

		rules.add(Tool.Rule.minesAndDrops(List.of(Blocks.COBWEB), DIG_SPEED_COBWEB));
		rules.add(Tool.Rule.overrideSpeed(BlockTags.SWORD_EFFICIENT, DIG_SPEED_SWORD));

		return new Tool(rules.build(), DIG_SPEED_DEFAULT, 1);
	}
}
