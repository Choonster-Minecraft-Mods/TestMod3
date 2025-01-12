package choonster.testmod3.client.init;

import choonster.testmod3.TestMod3;
import choonster.testmod3.init.ModBlocks;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import java.util.function.Supplier;

/**
 * Sets the {@link RenderType} for this mod's blocks.
 *
 * @author Choonster
 */
// TODO: Remove if/when https://github.com/MinecraftForge/MinecraftForge/issues/10294 is resolved
@Mod.EventBusSubscriber(modid = TestMod3.MODID, bus = Bus.MOD, value = Dist.CLIENT)
public class ModItemBlockRenderTypes {
	@SubscribeEvent
	public static void setRenderTypes(final FMLClientSetupEvent event) {
		setCutout(ModBlocks.WATER_GRASS);
		setCutout(ModBlocks.FLUID_TANK);
		setCutout(ModBlocks.FLUID_TANK_RESTRICTED);
		setCutout(ModBlocks.MIRROR_PLANE);
		setCutout(ModBlocks.FLUID_PIPE);
		setCutout(ModBlocks.OAK_SAPLING);
		setCutout(ModBlocks.SPRUCE_SAPLING);
		setCutout(ModBlocks.BIRCH_SAPLING);
		setCutout(ModBlocks.JUNGLE_SAPLING);
		setCutout(ModBlocks.ACACIA_SAPLING);
		setCutout(ModBlocks.DARK_OAK_SAPLING);
	}

	@SuppressWarnings("deprecation")
	private static void setCutout(final Supplier<? extends Block> block) {
		ItemBlockRenderTypes.setRenderLayer(block.get(), RenderType.cutout());
	}
}
