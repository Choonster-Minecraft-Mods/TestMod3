package choonster.testmod3.data;

import choonster.testmod3.TestMod3;
import choonster.testmod3.init.ModBlocks;
import choonster.testmod3.init.ModTags;
import choonster.testmod3.world.level.block.variantgroup.BlockVariantGroup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.fmllegacy.RegistryObject;

import javax.annotation.Nullable;

/**
 * Generates this mod's block tags.
 *
 * @author Choonster
 */
public class TestMod3BlockTagsProvider extends BlockTagsProvider {
	public TestMod3BlockTagsProvider(final DataGenerator generatorIn, @Nullable final ExistingFileHelper existingFileHelper) {
		super(generatorIn, TestMod3.MODID, existingFileHelper);
	}

	@Override
	protected void addTags() {
		tag(BlockTags.MINEABLE_WITH_PICKAXE)
				.add(
						ModBlocks.RIGHT_CLICK_TEST.get(),
						ModBlocks.CLIENT_PLAYER_RIGHT_CLICK.get(),
						ModBlocks.ROTATABLE_LAMP.get(),
						ModBlocks.FLUID_TANK.get(),
						ModBlocks.ITEM_DEBUGGER.get(),
						ModBlocks.END_PORTAL_FRAME_FULL.get(),
						ModBlocks.POTION_EFFECT.get(),
						ModBlocks.CLIENT_PLAYER_ROTATION.get(),
						ModBlocks.PIG_SPAWNER_REFILLER.get(),
						ModBlocks.MIRROR_PLANE.get(),
						ModBlocks.VANILLA_MODEL_TEST.get(),
						ModBlocks.FULLBRIGHT.get(),
						ModBlocks.NORMAL_BRIGHTNESS.get(),
						ModBlocks.MAX_HEALTH_SETTER.get(),
						ModBlocks.MAX_HEALTH_GETTER.get(),
						ModBlocks.SMALL_COLLISION_TEST.get(),
						ModBlocks.HIDDEN.get(),
						ModBlocks.BASIC_PIPE.get(),
						ModBlocks.FLUID_PIPE.get(),
						ModBlocks.SURVIVAL_COMMAND_BLOCK.get(),
						ModBlocks.REPEATING_SURVIVAL_COMMAND_BLOCK.get(),
						ModBlocks.CHAIN_SURVIVAL_COMMAND_BLOCK.get(),
						ModBlocks.INVISIBLE.get(),
						ModBlocks.FLUID_TANK_RESTRICTED.get()
				)
				.add(getBlocks(ModBlocks.VARIANTS_BLOCKS))
				.add(getBlocks(ModBlocks.TERRACOTTA_SLABS));

		tag(BlockTags.MINEABLE_WITH_AXE)
				.add(
						ModBlocks.PLANKS.get()
				);

		tag(ModTags.Blocks.SAPLINGS)
				.add(
						ModBlocks.OAK_SAPLING.get(),
						ModBlocks.SPRUCE_SAPLING.get(),
						ModBlocks.BIRCH_SAPLING.get(),
						ModBlocks.JUNGLE_SAPLING.get(),
						ModBlocks.ACACIA_SAPLING.get(),
						ModBlocks.DARK_OAK_SAPLING.get()
				);

		tag(BlockTags.SAPLINGS)
				.addTag(ModTags.Blocks.SAPLINGS);

	}

	private Block[] getBlocks(final BlockVariantGroup<?, ?> variantGroup) {
		return variantGroup
				.getBlocks()
				.stream()
				.map(RegistryObject::get)
				.toArray(Block[]::new);
	}
}
