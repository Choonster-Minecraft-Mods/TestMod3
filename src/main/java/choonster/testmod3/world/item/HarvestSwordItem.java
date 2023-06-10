package choonster.testmod3.world.item;

import choonster.testmod3.init.ModTags;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.TierSortingRegistry;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.ToolActions;

import java.util.stream.Stream;

/**
 * A tool that can function as a sword, pickaxe, axe or shovel.
 * <p>
 * Test for this thread:
 * http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/modification-development/2550421-how-to-make-a-tool-e-g-a-sword-have-the-abilities
 *
 * @author Choonster
 */
public class HarvestSwordItem extends DiggerItem {
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
	private static final float BASE_DAMAGE = 3.0f;

	/**
	 * The attack speed
	 */
	private static final float ATTACK_SPEED = -2.4f;

	public HarvestSwordItem(final Tier tier, final Item.Properties properties) {
		super(BASE_DAMAGE, ATTACK_SPEED, tier, ModTags.Blocks.EMPTY, properties);
	}

	@Override
	public boolean isCorrectToolForDrops(final ItemStack stack, final BlockState state) {
		if (state.is(Blocks.COBWEB)) {
			return true;
		}

		return isMineable(state) && TierSortingRegistry.isCorrectTierForDrops(getTier(), state);
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
	public float getDestroySpeed(final ItemStack stack, final BlockState state) {
		if (state.is(Blocks.COBWEB)) {
			return DIG_SPEED_COBWEB;
		}

		if (isMineable(state)) {
			return speed;
		}

		if (state.is(BlockTags.SWORD_EFFICIENT)) {
			return DIG_SPEED_SWORD;
		}

		return DIG_SPEED_DEFAULT;
	}

	@Override
	public boolean hurtEnemy(final ItemStack itemStack, final LivingEntity target, final LivingEntity attacker) {
		// Only reduce the durability by 1 point (like swords do) instead of 2 (like tools do)
		itemStack.hurtAndBreak(1, attacker, (entity) -> entity.broadcastBreakEvent(InteractionHand.MAIN_HAND));
		return true;
	}

	private boolean isMineable(final BlockState state) {
		return Stream.of(
				BlockTags.MINEABLE_WITH_AXE,
				BlockTags.MINEABLE_WITH_HOE,
				BlockTags.MINEABLE_WITH_PICKAXE,
				BlockTags.MINEABLE_WITH_SHOVEL
		).anyMatch(state::is);
	}
}
