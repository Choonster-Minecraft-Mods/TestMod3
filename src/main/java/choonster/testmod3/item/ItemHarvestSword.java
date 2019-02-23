package choonster.testmod3.item;

import com.google.common.collect.ImmutableSet;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.ForgeHooks;

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
public class ItemHarvestSword extends ItemTool {

	/**
	 * The speed at which Cobwebs are harvested
	 */
	private static final float DIG_SPEED_WEB = 15.0f;

	/**
	 * The speed at which Sword-effective {@link Material}s are harvested
	 */
	private static final float DIG_SPEED_SWORD = 1.5f;

	/**
	 * The speed at which blocks are harvested if this isn't their correct tool
	 */
	private static final float DIG_SPEED_DEFAULT = 1.0f;

	/**
	 * The base attack damage before the {@link ToolMaterial}'s attack damage is factored in
	 */
	private static final float BASE_DAMAGE = 3.0f;

	/**
	 * The attack speed
	 */
	private static final float ATTACK_SPEED = -2.4f;

	public ItemHarvestSword(final ToolMaterial toolMaterial) {
		super(BASE_DAMAGE, ATTACK_SPEED, toolMaterial, Collections.emptySet());

		setHarvestLevel("pickaxe", toolMaterial.getHarvestLevel());
		setHarvestLevel("axe", toolMaterial.getHarvestLevel());
		setHarvestLevel("shovel", toolMaterial.getHarvestLevel());

		// Waila Harvestability sets the harvest tool of Cobwebs to "sword"
		setHarvestLevel("sword", toolMaterial.getHarvestLevel());
	}

	/**
	 * The {@link Material}s that this tool is effective on.
	 */
	private static final Set<Material> EFFECTIVE_MATERIALS = ImmutableSet.of(
			// Pickaxe
			Material.ROCK, Material.IRON, Material.IRON, Material.GLASS, Material.PISTON, Material.ANVIL, Material.CIRCUITS,

			// Axe
			Material.WOOD, Material.GOURD, Material.PLANTS, Material.VINE,

			// Shovel
			Material.GRASS, Material.GROUND, Material.SAND, Material.SNOW, Material.CRAFTED_SNOW, Material.CLAY
	);

	/**
	 * The {@link Material}s that Swords are effective on.
	 */
	private static final Set<Material> SWORD_MATERIALS = ImmutableSet.of(
			Material.PLANTS, Material.VINE, Material.CORAL, Material.LEAVES, Material.GOURD
	);

	/**
	 * Can this tool harvest the {@link IBlockState}?
	 * <p>
	 * This should only be used by {@link ForgeHooks#canHarvestBlock(Block, EntityPlayer, IBlockAccess, BlockPos)},
	 * use the tool class/harvest level system where possible.
	 *
	 * @param state The IBlockState
	 * @param stack The tool
	 * @return Is this tool effective on the {@link IBlockState}'s {@link Material}?
	 */
	@Override
	public boolean canHarvestBlock(final IBlockState state, final ItemStack stack) {
		return EFFECTIVE_MATERIALS.contains(state.getMaterial()) || state.getBlock() == Blocks.WEB;
	}

	@Override
	public float getDestroySpeed(final ItemStack stack, final IBlockState state) {
		if (state.getBlock() == Blocks.WEB) {
			return DIG_SPEED_WEB;
		}

		for (final String type : getToolClasses(stack)) {
			if (state.getBlock().isToolEffective(type, state))
				return efficiency;
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
	public boolean hitEntity(final ItemStack itemStack, final EntityLivingBase target, final EntityLivingBase attacker) {
		itemStack.damageItem(1, attacker); // Only reduce the durability by 1 point (like swords do) instead of 2 (like tools do)
		return true;
	}
}
