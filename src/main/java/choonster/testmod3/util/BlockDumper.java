package choonster.testmod3.util;

import choonster.testmod3.TestMod3;
import com.google.common.base.Charsets;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.PrintWriter;
import java.util.Objects;
import java.util.stream.StreamSupport;

/**
 * Dumps the translation keys and the output of the {@link net.minecraft.item.ItemBlock}'s {@link Object#toString()} method for all of this mod's blocks.
 * <p>
 * I wrote this because I was getting an {@link AbstractMethodError} from a lambda implementing {@link net.minecraft.client.renderer.ItemMeshDefinition} and the only the toString output was included in the crash report.
 *
 * @author Choonster
 */
public class BlockDumper {
	private static final Logger LOGGER = LogManager.getLogger();

	public static void dump() {
		try (final PrintWriter writer = new PrintWriter("TestMod3_BlockDump_" + (FMLCommonHandler.instance().getSide().isClient() ? "Client" : "Server") + ".txt", Charsets.UTF_8.toString())) {
			writer.println("Name - toString");

			StreamSupport.stream(ForgeRegistries.BLOCKS.spliterator(), false)
					.filter(block -> Objects.requireNonNull(block.getRegistryName()).getNamespace().equals(TestMod3.MODID))
					.forEach(block -> {
						final Item item = Item.getItemFromBlock(block);
						if (item != Items.AIR) {
							writer.printf("%s - %s\n", item.getTranslationKey(), item.toString());
						}
					});
		} catch (final Exception e) {
			LOGGER.fatal("Exception dumping blocks", e);
		}
	}
}
