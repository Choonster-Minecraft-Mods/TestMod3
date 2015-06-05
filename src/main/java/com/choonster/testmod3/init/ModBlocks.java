package com.choonster.testmod3.init;

import com.choonster.testmod3.block.BlockFluidStatic;
import net.minecraft.block.Block;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModBlocks {
	public static BlockFluidStatic fluidStatic;

	public static void registerBlocks() {
		fluidStatic = registerBlock(new BlockFluidStatic(ModFluids.staticFluid));
	}

	private static <T extends Block> T registerBlock(T block) {
		GameRegistry.registerBlock(block, block.getUnlocalizedName().substring(5));
		return block;
	}
}
