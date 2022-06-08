package choonster.testmod3.util;

import choonster.testmod3.TestMod3;
import com.google.common.base.Charsets;
import com.mojang.logging.LogUtils;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.registries.ForgeRegistries;
import org.slf4j.Logger;

import java.io.PrintWriter;

/**
 * Dumps the translation keys and the output of the {@link BlockItem}'s {@link Object#toString()} method for all of this mod's blocks.
 * <p>
 * I wrote this because I was getting an {@link AbstractMethodError} from a lambda implementing ItemMeshDefinition and only the toString output was included in the crash report.
 *
 * @author Choonster
 */
public class BlockDumper {
	private static final Logger LOGGER = LogUtils.getLogger();

	public static void dump() {
		try (final PrintWriter writer = new PrintWriter("TestMod3_BlockDump_" + (FMLEnvironment.dist.isClient() ? "Client" : "Server") + ".txt", Charsets.UTF_8.toString())) {
			writer.println("Name - toString");

			RegistryUtil.stream(ForgeRegistries.BLOCKS)
					.filter(block -> RegistryUtil.getKey(block).getNamespace().equals(TestMod3.MODID))
					.forEach(block -> {
						final Item item = block.asItem();
						if (item != Items.AIR) {
							writer.printf("%s - %s\n", item.getDescriptionId(), item);
						}
					});
		} catch (final Exception e) {
			LOGGER.error(LogUtils.FATAL_MARKER, "Exception dumping blocks", e);
		}
	}
}
