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
import java.util.Objects;


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

	public PotionTestMod3(final boolean isBadEffect, final int liquidColor, final String name) {
		super(isBadEffect, liquidColor);
		setPotionName(this, name);
		iconTexture = new ResourceLocation(TestMod3.MODID, "textures/potions/" + name + ".png");
	}

	public PotionTestMod3(final boolean isBadEffect, final int liquidR, final int liquidG, final int liquidB, final String name) {
		this(isBadEffect, new Color(liquidR, liquidG, liquidB).getRGB(), name);
	}

	/**
	 * Set the registry name of {@code potion} to {@code potionName} and the translation key to the full registry name.
	 *
	 * @param potion     The potion
	 * @param potionName The potion's name
	 */
	public static void setPotionName(final Potion potion, final String potionName) {
		potion.setRegistryName(TestMod3.MODID, potionName);
		final ResourceLocation registryName = Objects.requireNonNull(potion.getRegistryName());
		potion.setPotionName("effect." + registryName.toString());
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
	public void renderInventoryEffect(final int x, final int y, final PotionEffect effect, final Minecraft mc) {
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
	public void renderHUDEffect(final int x, final int y, final PotionEffect effect, final Minecraft mc, final float alpha) {
		mc.getTextureManager().bindTexture(iconTexture);
		Gui.drawModalRectWithCustomSizedTexture(x + 3, y + 3, 0, 0, 18, 18, 18, 18);
	}
}
