package choonster.testmod3.data;

import choonster.testmod3.TestMod3;
import choonster.testmod3.init.ModSoundEvents;
import choonster.testmod3.text.TestMod3Lang;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.SoundDefinition;
import net.minecraftforge.common.data.SoundDefinitionsProvider;

/**
 * Generates this mod's sound definitions.
 *
 * @author Choonster
 */
public class TestMod3SoundDefinitionsProvider extends SoundDefinitionsProvider {
	public TestMod3SoundDefinitionsProvider(final PackOutput output, final ExistingFileHelper helper) {
		super(output, TestMod3.MODID, helper);
	}

	@Override
	public void registerSounds() {
		add(
				ModSoundEvents.MUSIC_DISC_SOLARIS,
				definition()
						.with(modSound("music_disc/solaris").stream())

		);

		add(
				ModSoundEvents.NINE_MM_FIRE,
				definition()
						.with(modSound("item/gun/9mm_fire"))
						.subtitle(TestMod3Lang.SUBTITLE_ITEM_GUN_FIRE.getTranslationKey())
		);

		add(
				ModSoundEvents.ACTION_SADDLE,
				definition()
						.with(sound("mob/horse/leather"))
						.subtitle(TestMod3Lang.SUBTITLE_ACTION_SADDLE.getTranslationKey())
		);
	}

	private SoundDefinition.Sound modSound(final String name) {
		return sound(ResourceLocation.fromNamespaceAndPath(TestMod3.MODID, name));
	}
}
