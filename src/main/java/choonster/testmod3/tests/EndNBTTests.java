package choonster.testmod3.tests;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.EndTag;
import net.minecraft.nbt.NbtIo;

import java.io.File;
import java.io.IOException;

/**
 * Testing the use of {@link EndTag} as a value in a {@link CompoundTag}. This isn't expected to work.
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
		final CompoundTag originalCompound = new CompoundTag();
		originalCompound.putString("Tag1", "Test2");
		originalCompound.put("EndTag", EndTag.INSTANCE);
		originalCompound.putFloat("Tag2", 3.55f);

		final File file = new File("./endTagTest.dat");

		try {
			NbtIo.write(originalCompound, file);
		} catch (final IOException e) {
			exceptionThrown(e, "Failed to write to NBT file");
			return;
		}

		final CompoundTag readCompound;
		try {
			readCompound = NbtIo.read(file);
		} catch (final IOException e) {
			exceptionThrown(e, "Failed to read from NBT file");
			return;
		}

		assertEqual(originalCompound, readCompound);
	}
}
