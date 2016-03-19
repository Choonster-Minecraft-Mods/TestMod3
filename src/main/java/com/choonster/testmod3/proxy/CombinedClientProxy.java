package com.choonster.testmod3.proxy;


import com.choonster.testmod3.client.cape.CapeEventHandler;
import com.choonster.testmod3.client.event.ClientEventHandler;
import com.choonster.testmod3.client.model.ModModelManager;
import com.choonster.testmod3.client.render.entity.RenderModArrow;
import com.choonster.testmod3.entity.EntityModArrow;
import com.choonster.testmod3.init.ModBlocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.ColorizerGrass;
import net.minecraft.world.biome.BiomeColorHelper;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class CombinedClientProxy implements IProxy {

	private final Minecraft minecraft = Minecraft.getMinecraft();

	@Override
	public void preInit() {
		ModModelManager.INSTANCE.registerAllModels();
		MinecraftForge.EVENT_BUS.register(new CapeEventHandler());
		MinecraftForge.EVENT_BUS.register(new ClientEventHandler());

		RenderingRegistry.registerEntityRenderingHandler(EntityModArrow.class, RenderModArrow::new);
	}

	@Override
	public void init() {
		final BlockColors blockColors = minecraft.getBlockColors();

		blockColors.registerBlockColorHandler(
				(state, blockAccess, pos, tintIndex) ->
						blockAccess != null && pos != null ?
								BiomeColorHelper.getGrassColorAtPos(blockAccess, pos) :
								ColorizerGrass.getGrassColor(0.5D, 1.0D),
				ModBlocks.waterGrass
		);

	}

	@Override
	public void postInit() {

	}

	@Override
	public void doClientRightClick() {
		// Press the Use Item keybinding
		KeyBinding.onTick(minecraft.gameSettings.keyBindUseItem.getKeyCode());
	}

	@Override
	public EntityPlayer getClientPlayer() {
		return minecraft.thePlayer;
	}
}
