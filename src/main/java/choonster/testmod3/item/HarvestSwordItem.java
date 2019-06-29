package choonster.testmod3.item;

import com.google.common.collect.ImmutableSet;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.IItemTier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolItem;
import net.minecraft.util.Hand;
import net.minecraftforge.common.ToolType;

import java.util.Collections;
import java.util.Set;

/**
 * A tool that can function as a sword, pickaxe, axe or shovel.
 * <p>
 * Test for this thread:
 * http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/modification-development/2550421-how-to-make-a-tool-e-g-a-sword-have-the-abilities
 *
 * @author Choonster
 */
public class HarvestSwordItem extends ToolItem {

	/**
	 * The speed at which Cobwebs are harvested
	 */
	private static final float DIG_SPEED_COBWEB = 15.0f;

	/**
	 * The speed at which Sword-effective {@link Material}s are harvested
	 */
	private static final float DIG_SPEED_SWORD = 1.5f;

	/**
	 * The speed at which blocks are harvested if this isn't their correct tool
	 */
	private static final float DIG_SPEED_DEFAULT = 1.0f;

	/**
	 * The base attack damage before the {@link IItemTier}'s attack damage is factored in
	 */
	private static final float BASE_DAMAGE = 3.0f;

	/**
	 * The attack speed
	 */
	private static final float ATTACK_SPEED = -2.4f;

	public HarvestSwordItem(final IItemTier itemTier, final Item.Properties properties) {
		super(BASE_DAMAGE, ATTACK_SPEED, itemTier, Collections.emptySet(), properties);
	}

	/**
	 * Add the pickaxe, axe, shovel and sword tool types to the item properties,
	 * using the {@link IItemTier}'s harvest level for each tool.
	 *
	 * @param itemTier   The item tier
	 * @param properties The item properties to add the tool types to
	 * @return The item properties with the tool types added
	 */
	public static Item.Properties addToolTypes(final IItemTier itemTier, final Item.Properties properties) {
		return properties
				.addToolType(ToolType.PICKAXE, itemTier.getHarvestLevel())
				.addToolType(ToolType.AXE, itemTier.getHarvestLevel())
				.addToolType(ToolType.SHOVEL, itemTier.getHarvestLevel())
				.addToolType(ToolType.get("sword"), itemTier.getHarvestLevel()); // Waila Harvestability sets the harvest tool of Cobwebs to "sword"
	}

	/**
	 * The {@link Material}s that this tool is effective on.
	 */
	private static final Set<Material> EFFECTIVE_MATERIALS = ImmutableSet.of(
			// Pickaxe
			Material.ROCK, Material.IRON, Material.IRON, Material.GLASS, Material.PISTON, Material.ANVIL, Material.MISCELLANEOUS,

			// Axe
			Material.WOOD, Material.GOURD, Material.PLANTS, Material.TALL_PLANTS,

			// Shovel
			Material.ORGANIC, Material.EARTH, Material.SAND, Material.SNOW, Material.SNOW_BLOCK, Material.CLAY
	);

	/**
	 * The {@link Material}s that Swords are effective on.
	 */
	private static final Set<Material> SWORD_MATERIALS = ImmutableSet.of(
			Material.PLANTS, Material.TALL_PLANTS, Material.CORAL, Material.LEAVES, Material.GOURD
	);

	@Override
	public boolean canHarvestBlock(final BlockState blockIn) {
		return super.canHarvestBlock(blockIn);
	}

	@Override
	public boolean canHarvestBlock(final ItemStack stack, final BlockState state) {
		return EFFECTIVE_MATERIALS.contains(state.getMaterial()) || state.getBlock() == Blocks.COBWEB;
	}

	@Override
	public float getDestroySpeed(final ItemStack stack, final BlockState state) {
		if (state.getBlock() == Blocks.COBWEB) {
			return DIG_SPEED_COBWEB;
		}

		for (final ToolType type : getToolTypes(stack)) {
			if (state.isToolEffective(type)) {
				return efficiency;
			}
		}

		// Not all blocks have a harvest tool/level set, so we need to fall back to checking the Material like the vanilla tools do
		if (EFFECTIVE_MATERIALS.contains(state.getMaterial())) {
			return efficiency;
		}

		if (SWORD_MATERIALS.contains(state.getMaterial())) {
			return DIG_SPEED_SWORD;
		}

		return DIG_SPEED_DEFAULT;
	}

	@Override
	public boolean hitEntity(final ItemStack itemStack, final LivingEntity target, final LivingEntity attacker) {
		// Only reduce the durability by 1 point (like swords do) instead of 2 (like tools do)
		itemStack.damageItem(1, attacker, (entity) -> entity.sendBreakAnimation(Hand.MAIN_HAND));
		return true;
	}
}
