package choonster.testmod3.world.level.block.variantgroup;

import com.mojang.datafixers.util.Pair;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.Block;
import org.jspecify.annotations.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * An implementation of {@link IBlockVariantGroup} that can be encoded and decoded without the blocks being registered.
 *
 * @author Choonster
 */
class SimpleBlockVariantGroup<VARIANT extends Enum<VARIANT> & StringRepresentable, BLOCK extends Block>
		implements IBlockVariantGroup<VARIANT, BLOCK> {
	private final String groupName;
	private final List<VARIANT> variants;

	private final Map<VARIANT, Supplier<BLOCK>> blocks;

	SimpleBlockVariantGroup(final String groupName, final List<VARIANT> variants, final Map<VARIANT, BLOCK> blocks) {
		this.groupName = groupName;
		this.variants = variants;

		this.blocks = blocks.entrySet()
				.stream()
				.map(entry -> Pair.of(
						entry.getKey(),
						(Supplier<BLOCK>) entry::getValue
				))
				.collect(Collectors.toMap(Pair::getFirst, Pair::getSecond));
	}

	/**
	 * Gets the name of this group.
	 *
	 * @return The group name
	 */
	@Override
	public String getGroupName() {
		return groupName;
	}

	/**
	 * Gets this group's variants.
	 *
	 * @return The variants
	 */
	@Override
	public List<VARIANT> getVariants() {
		return variants;
	}

	/**
	 * Gets this group's blocks.
	 *
	 * @return The blocks
	 */
	@Override
	public Collection<Supplier<BLOCK>> getEntries() {
		return getBlocksMap().values();
	}

	/**
	 * Gets this group's blocks and their corresponding variants.
	 *
	 * @return The blocks map
	 */
	@Override
	public Map<VARIANT, Supplier<BLOCK>> getBlocksMap() {
		return blocks;
	}

	/**
	 * Gets the block for the specified variant.
	 *
	 * @param variant The variant
	 * @return The block
	 */
	@Nullable
	@Override
	public Supplier<BLOCK> getBlock(final VARIANT variant) {
		return blocks.get(variant);
	}
}
