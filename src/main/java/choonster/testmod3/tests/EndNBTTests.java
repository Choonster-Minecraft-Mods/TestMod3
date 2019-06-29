package choonster.testmod3.tests;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.EndNBT;

import java.io.File;
import java.io.IOException;

/**
 * Testing the use of {@link EndNBT} as a value in a {@link CompoundNBT}. This isn't expected to work.
 * <p>
 * Test for this thread:
 * http://www.minecraftforge.net/forum/index.php/topic,34724.0.html
 *
 * @author Choonster
 */
public class EndNBTTests extends Test {
	public static final EndNBTTests INSTANCE = new EndNBTTests();

	@Override
	protected void runTest() {
		final CompoundNBT originalCompound = new CompoundNBT();
		originalCompound.putString("Tag1", "Test2");
		originalCompound.put("EndTag", new EndNBT());
		originalCompound.putFloat("Tag2", 3.55f);

		final File file = new File("./endTagTest.dat");

		try {
			CompressedStreamTools.write(originalCompound, file);
		} catch (final IOException e) {
			exceptionThrown(e, "Failed to write to NBT file");
			return;
		}

		final CompoundNBT readCompound;
		try {
			readCompound = CompressedStreamTools.read(file);
		} catch (final IOException e) {
			exceptionThrown(e, "Failed to read from NBT file");
			return;
		}

		assertEqual(originalCompound, readCompound);
	}
}
