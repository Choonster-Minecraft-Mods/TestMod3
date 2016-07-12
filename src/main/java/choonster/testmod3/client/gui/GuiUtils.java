package choonster.testmod3.client.gui;

import choonster.testmod3.Logger;
import choonster.testmod3.util.ReflectionUtil;
import net.minecraft.client.gui.Gui;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.lang.invoke.MethodHandle;

/**
 * Utility methods for GUI rendering.
 *
 * @author Choonster
 */
@SideOnly(Side.CLIENT)
public class GuiUtils {
	private static final MethodHandle Z_LEVEL = ReflectionUtil.findFieldGetter(Gui.class, "zLevel", "field_73735_i");

	/**
	 * Get the value of the {@link Gui#zLevel} field for the specified {@link Gui}.
	 *
	 * @param gui The Gui.
	 * @return The z level, or 0 if an exception was thrown.
	 */
	@SideOnly(Side.CLIENT)
	private static float getZLevel(Gui gui) {
		try {
			return (float) Z_LEVEL.invoke(gui);
		} catch (Throwable throwable) {
			Logger.fatal(throwable, "Failed to get z level of GUI");
			return 0.0f;
		}
	}

	/**
	 * Draw a textured rectangle at the {@link Gui}'s z level.
	 *
	 * @param x        The x coordinate to draw at
	 * @param y        The y coordinate to draw at
	 * @param textureX The x coordinate within the texture
	 * @param textureY The y coordinate within the texture
	 * @param width    The width of the rectangle
	 * @param height   The height of the rectangle
	 * @param gui      The Gui to get the z level from
	 */
	public static void drawTexturedModalRect(int x, int y, int textureX, int textureY, int width, int height, Gui gui) {
		net.minecraftforge.fml.client.config.GuiUtils.drawTexturedModalRect(x, y, textureX, textureY, width, height, getZLevel(gui));
	}
}
