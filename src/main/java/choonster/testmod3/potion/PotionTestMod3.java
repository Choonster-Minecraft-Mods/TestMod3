package choonster.testmod3.potion;

import choonster.testmod3.TestMod3;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.awt.*;


/**
 * A base class for this mod's potions.
 *
 * @author Choonster
 */
public class PotionTestMod3 extends Potion {
	/**
	 * The icon texture to use in the HUD and inventory GUI.
	 */
	private final ResourceLocation iconTexture;

	public PotionTestMod3(boolean isBadEffect, int liquidColor, String name) {
		super(isBadEffect, liquidColor);
		setPotionName(this, name);
		iconTexture = new ResourceLocation(TestMod3.MODID, "textures/potions/" + name + ".png");
	}

	public PotionTestMod3(boolean isBadEffect, int liquidR, int liquidG, int liquidB, String name) {
		this(isBadEffect, new Color(liquidR, liquidG, liquidB).getRGB(), name);
	}

	/**
	 * Set the registry name of {@code potion} to {@code potionName} and the unlocalised name to the full registry name.
	 *
	 * @param potion     The potion
	 * @param potionName The potion's name
	 */
	public static void setPotionName(Potion potion, String potionName) {
		potion.setRegistryName(TestMod3.MODID, potionName);
		potion.setPotionName("effect." + potion.getRegistryName().toString());
	}

	@Override
	public boolean hasStatusIcon() {
		return false;
	}

	/**
	 * Called to draw the this Potion onto the player's inventory when it's active.
	 * This can be used to e.g. render Potion icons from your own texture.
	 *
	 * @param x      the x coordinate
	 * @param y      the y coordinate
	 * @param effect the active PotionEffect
	 * @param mc     the Minecraft instance, for convenience
	 */
	@SideOnly(Side.CLIENT)
	@Override
	public void renderInventoryEffect(int x, int y, PotionEffect effect, Minecraft mc) {
		if (mc.currentScreen != null) {
			mc.getTextureManager().bindTexture(iconTexture);
			Gui.drawModalRectWithCustomSizedTexture(x + 6, y + 7, 0, 0, 18, 18, 18, 18);
		}
	}

	/**
	 * Called to draw the this Potion onto the player's ingame HUD when it's active.
	 * This can be used to e.g. render Potion icons from your own texture.
	 *
	 * @param x      the x coordinate
	 * @param y      the y coordinate
	 * @param effect the active PotionEffect
	 * @param mc     the Minecraft instance, for convenience
	 * @param alpha  the alpha value, blinks when the potion is about to run out
	 */
	@SideOnly(Side.CLIENT)
	@Override
	public void renderHUDEffect(int x, int y, PotionEffect effect, Minecraft mc, float alpha) {
		mc.getTextureManager().bindTexture(iconTexture);
		Gui.drawModalRectWithCustomSizedTexture(x + 3, y + 3, 0, 0, 18, 18, 18, 18);
	}
}
