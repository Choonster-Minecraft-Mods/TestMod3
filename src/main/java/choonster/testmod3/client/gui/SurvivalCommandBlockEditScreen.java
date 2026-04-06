package choonster.testmod3.client.gui;

import choonster.testmod3.TestMod3;
import choonster.testmod3.network.SaveSurvivalCommandBlockMessage;
import choonster.testmod3.world.level.block.entity.SurvivalCommandBlockEntity;
import com.mojang.logging.LogUtils;
import net.minecraft.client.gui.screens.inventory.CommandBlockEditScreen;
import net.minecraft.world.level.block.entity.CommandBlockEntity;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import net.minecraftforge.network.PacketDistributor;
import org.slf4j.Logger;

import java.lang.reflect.Field;

public class SurvivalCommandBlockEditScreen extends CommandBlockEditScreen {
	private static final Logger LOGGER = LogUtils.getLogger();

	private static final Field MODE = ObfuscationReflectionHelper.findField(CommandBlockEditScreen.class, "mode");
	private static final Field CONDITIONAL = ObfuscationReflectionHelper.findField(CommandBlockEditScreen.class, "conditional");
	private static final Field AUTOEXEC = ObfuscationReflectionHelper.findField(CommandBlockEditScreen.class, "autoexec");

	private final SurvivalCommandBlockEntity survivalCommandBlockEntity;

	public SurvivalCommandBlockEditScreen(final SurvivalCommandBlockEntity survivalCommandBlockEntity) {
		super(survivalCommandBlockEntity);
		this.survivalCommandBlockEntity = survivalCommandBlockEntity;
	}

	@Override
	protected void populateAndSendPacket() {
		try {
			final var mode = (CommandBlockEntity.Mode) MODE.get(this);
			final var conditional = (boolean) CONDITIONAL.get(this);
			final var autoexec = (boolean) AUTOEXEC.get(this);

			TestMod3.network.send(
					new SaveSurvivalCommandBlockMessage(
							survivalCommandBlockEntity,
							commandEdit.getValue(),
							mode,
							conditional,
							autoexec
					),
					PacketDistributor.SERVER.noArg()
			);
		} catch (final IllegalAccessException e) {
			LOGGER.error("Couldn't set survival command block", e);
		}
	}
}
