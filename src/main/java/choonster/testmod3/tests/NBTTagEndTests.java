package choonster.testmod3.tests;

import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagEnd;

import java.io.File;
import java.io.IOException;

/**
 * Testing the use of {@link NBTTagEnd} as a value in a {@link NBTTagCompound}. This isn't expected to work.
 * <p>
 * Test for this thread:
 * http://www.minecraftforge.net/forum/index.php/topic,34724.0.html
 *
 * @author Choonster
 */
public class NBTTagEndTests extends Test {
	public static final NBTTagEndTests INSTANCE = new NBTTagEndTests();

	@Override
	protected void runTest() {
		final NBTTagCompound originalCompound = new NBTTagCompound();
		originalCompound.putString("Tag1", "Test2");
		originalCompound.put("EndTag", new NBTTagEnd());
		originalCompound.putFloat("Tag2", 3.55f);

		final File file = new File("./endTagTest.dat");

		try {
			CompressedStreamTools.write(originalCompound, file);
		} catch (final IOException e) {
			exceptionThrown(e, "Failed to write to NBT file");
			return;
		}

		final NBTTagCompound readCompound;
		try {
			readCompound = CompressedStreamTools.read(file);
		} catch (final IOException e) {
			exceptionThrown(e, "Failed to read from NBT file");
			return;
		}

		assertEqual(originalCompound, readCompound);
	}
}
