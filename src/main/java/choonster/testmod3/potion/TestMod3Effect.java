package choonster.testmod3.potion;

import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;

import java.awt.*;


/**
 * A base class for this mod's potions.
 *
 * @author Choonster
 */
public class TestMod3Effect extends Effect {
	protected TestMod3Effect(final EffectType effectType, final int liquidColor) {
		super(effectType, liquidColor);
	}

	public TestMod3Effect(final EffectType effectType, final int liquidR, final int liquidG, final int liquidB) {
		this(effectType, new Color(liquidR, liquidG, liquidB).getRGB());
	}
}
