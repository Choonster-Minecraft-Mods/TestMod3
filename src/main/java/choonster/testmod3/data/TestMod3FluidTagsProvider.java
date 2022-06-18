package choonster.testmod3.data;

import choonster.testmod3.TestMod3;
import choonster.testmod3.init.ModFluids;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.FluidTagsProvider;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

/**
 * Generates this mod's fluid tags.
 *
 * @author Choonster
 */
public class TestMod3FluidTagsProvider extends FluidTagsProvider {
	public TestMod3FluidTagsProvider(final DataGenerator dataGenerator, @Nullable final ExistingFileHelper existingFileHelper) {
		super(dataGenerator, TestMod3.MODID, existingFileHelper);
	}

	@Override
	protected void addTags() {
		tag(Tags.Fluids.GASEOUS).add(
				ModFluids.STATIC_GAS.getStill().get(),
				ModFluids.STATIC_GAS.getFlowing().get(),
				ModFluids.NORMAL_GAS.getStill().get(),
				ModFluids.NORMAL_GAS.getFlowing().get()
		);
	}
}
