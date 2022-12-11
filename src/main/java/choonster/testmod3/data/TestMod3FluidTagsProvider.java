package choonster.testmod3.data;

import choonster.testmod3.TestMod3;
import choonster.testmod3.init.ModFluids;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.FluidTagsProvider;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

/**
 * Generates this mod's fluid tags.
 *
 * @author Choonster
 */
public class TestMod3FluidTagsProvider extends FluidTagsProvider {
	public TestMod3FluidTagsProvider(
			final PackOutput output,
			final CompletableFuture<HolderLookup.Provider> holderLookup,
			@Nullable final ExistingFileHelper existingFileHelper
	) {
		super(output, holderLookup, TestMod3.MODID, existingFileHelper);
	}

	@Override
	protected void addTags(final HolderLookup.Provider p_256366_) {
		tag(Tags.Fluids.GASEOUS).add(
				ModFluids.STATIC_GAS.getStill().get(),
				ModFluids.STATIC_GAS.getFlowing().get(),
				ModFluids.NORMAL_GAS.getStill().get(),
				ModFluids.NORMAL_GAS.getFlowing().get()
		);
	}
}
