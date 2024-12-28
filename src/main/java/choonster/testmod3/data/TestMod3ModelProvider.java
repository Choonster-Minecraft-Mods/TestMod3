package choonster.testmod3.data;

import choonster.testmod3.data.models.ModBlockModelGenerators;
import choonster.testmod3.data.models.ModItemModelGenerators;
import choonster.testmod3.util.RegistryUtil;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.stream.Stream;

/**
 * Generates this mod's block and item models.
 *
 * @author Choonster
 */
public class TestMod3ModelProvider extends ModelProvider {
	public TestMod3ModelProvider(final PackOutput output) {
		super(output);
	}

	@Override
	protected Stream<Block> getKnownBlocks() {
		return RegistryUtil.getModRegistryEntriesStream(ForgeRegistries.BLOCKS);
	}

	@Override
	protected Stream<Item> getKnownItems() {
		return RegistryUtil.getModRegistryEntriesStream(ForgeRegistries.ITEMS);
	}

	@Override
	protected BlockModelGenerators getBlockModelGenerators(final BlockStateGeneratorCollector blocks, final ItemInfoCollector items, final SimpleModelCollector models) {
		return new ModBlockModelGenerators(blocks, items, models);
	}

	@Override
	protected ItemModelGenerators getItemModelGenerators(final ItemInfoCollector items, final SimpleModelCollector models) {
		return new ModItemModelGenerators(items, models);
	}
}
