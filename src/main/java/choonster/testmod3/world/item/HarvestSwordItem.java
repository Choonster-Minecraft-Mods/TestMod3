package choonster.testmod3.world.item;

import com.google.common.collect.ImmutableList;
import net.minecraft.core.HolderSet;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.component.Tool;
import net.minecraft.world.level.block.Blocks;
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
public class HarvestSwordItem extends Item {
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
	 * The base attack damage before the {@link ToolMaterial}'s attack damage is factored in
	 */
	private static final float BASE_ATTACK_DAMAGE = 3.0f;

	/**
	 * The attack speed
	 */
	private static final float ATTACK_SPEED = -2.4f;

	public HarvestSwordItem(final ToolMaterial toolMaterial, final Item.Properties properties) {
		super(applyToolProperties(toolMaterial, properties));
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
	public boolean hurtEnemy(final ItemStack p_40994_, final LivingEntity p_40995_, final LivingEntity p_40996_) {
		return true;
	}

	@Override
	public void postHurtEnemy(final ItemStack p_345276_, final LivingEntity p_342379_, final LivingEntity p_342949_) {
		// Only reduce the durability by 1 point (like swords do) instead of 2 (like tools do)
		p_345276_.hurtAndBreak(1, p_342949_, EquipmentSlot.MAINHAND);
	}

	private static Item.Properties applyToolProperties(final ToolMaterial toolMaterial, final Item.Properties properties) {
		return toolMaterial
				.applyToolProperties(properties, BlockTags.MINEABLE_WITH_PICKAXE, BASE_ATTACK_DAMAGE, ATTACK_SPEED)
				.component(DataComponents.TOOL, createToolProperties(toolMaterial));
	}

	@SuppressWarnings("deprecation")
	private static Tool createToolProperties(final ToolMaterial toolMaterial) {
		final var holderGetter = BuiltInRegistries.acquireBootstrapRegistrationLookup(BuiltInRegistries.BLOCK);

		final var rules = ImmutableList.<Tool.Rule>builder()
				.add(Tool.Rule.deniesDrops(holderGetter.getOrThrow(toolMaterial.incorrectBlocksForDrops())));

		final var minesAndDrops = Stream.of(
				BlockTags.MINEABLE_WITH_AXE,
				BlockTags.MINEABLE_WITH_HOE,
				BlockTags.MINEABLE_WITH_PICKAXE,
				BlockTags.MINEABLE_WITH_SHOVEL
		).map(tag -> Tool.Rule.minesAndDrops(holderGetter.getOrThrow(BlockTags.MINEABLE_WITH_AXE), toolMaterial.speed()));

		rules.addAll(minesAndDrops.iterator());

		rules.add(Tool.Rule.minesAndDrops(HolderSet.direct(Blocks.COBWEB.builtInRegistryHolder()), DIG_SPEED_COBWEB));
		rules.add(Tool.Rule.overrideSpeed(holderGetter.getOrThrow(BlockTags.SWORD_EFFICIENT), DIG_SPEED_SWORD));

		return new Tool(rules.build(), DIG_SPEED_DEFAULT, 1);
	}
}
