package choonster.testmod3.world.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

import java.awt.*;


/**
 * A base class for this mod's potions.
 *
 * @author Choonster
 */
public class TestMod3MobEffect extends MobEffect {
	protected TestMod3MobEffect(final MobEffectCategory effectType, final int liquidColor) {
		super(effectType, liquidColor);
	}

	public TestMod3MobEffect(final MobEffectCategory effectType, final int liquidR, final int liquidG, final int liquidB) {
		this(effectType, new Color(liquidR, liquidG, liquidB).getRGB());
	}
}
