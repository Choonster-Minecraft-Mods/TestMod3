package choonster.testmod3.util;

import choonster.testmod3.TestMod3;
import com.google.common.collect.ImmutableSet;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldWriter;
import net.minecraft.world.World;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Constants {
	public static final String RESOURCE_PREFIX = TestMod3.MODID + ":";

	/**
	 * The armour equipment slots.
	 */
	public static final Set<EquipmentSlotType> ARMOUR_SLOTS = ImmutableSet.copyOf(
			Stream.of(EquipmentSlotType.values())
					.filter(equipmentSlot -> equipmentSlot.getSlotType() == EquipmentSlotType.Group.ARMOR)
					.collect(Collectors.toList())
	);

	public static class BlockFlags {
		/**
		 * When this flag is set, {@link World#notifyNeighborsOfStateChange(BlockPos, Block)} and
		 * {@link World#updateComparatorOutputLevel(BlockPos, Block)} are called.
		 */
		public static final int NOTIFY_NEIGHBOURS = 1;

		/**
		 * When this flag is set, {@link World#notifyBlockUpdate(BlockPos, BlockState, BlockState, int)} is called.
		 */
		public static final int NOTIFY_BLOCK_UPDATE = 2;

		/**
		 * When this flag is set on the client, the chunk is marked for immediate re-rendering on the main thread.
		 */
		public static final int RE_RENDER_ON_MAIN_THREAD = 8;

		/**
		 * Default flags for {@link IWorldWriter#setBlockState(BlockPos, BlockState, int)}
		 */
		public static final int DEFAULT_FLAGS = NOTIFY_NEIGHBOURS | NOTIFY_BLOCK_UPDATE;
	}
}
