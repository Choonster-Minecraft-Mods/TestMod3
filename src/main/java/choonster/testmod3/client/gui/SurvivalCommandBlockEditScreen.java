package choonster.testmod3.client.gui;

import choonster.testmod3.TestMod3;
import choonster.testmod3.network.SaveSurvivalCommandBlockMessage;
import choonster.testmod3.world.level.block.entity.SurvivalCommandBlock;
import choonster.testmod3.world.level.block.entity.SurvivalCommandBlockEntity;
import net.minecraft.client.gui.screens.inventory.CommandBlockEditScreen;
import net.minecraft.world.level.BaseCommandBlock;
import net.minecraft.world.level.block.entity.CommandBlockEntity;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Field;

public class SurvivalCommandBlockEditScreen extends CommandBlockEditScreen {
	private static final Logger LOGGER = LogManager.getLogger();

	private static final Field MODE = ObfuscationReflectionHelper.findField(CommandBlockEditScreen.class, /* mode */ "field_184082_w");
	private static final Field CONDITIONAL = ObfuscationReflectionHelper.findField(CommandBlockEditScreen.class, /* conditional */ "field_184084_y");
	private static final Field AUTOEXEC = ObfuscationReflectionHelper.findField(CommandBlockEditScreen.class, /* autoexec */ "field_184085_z");

	private final SurvivalCommandBlock survivalCommandBlock;

	public SurvivalCommandBlockEditScreen(final SurvivalCommandBlockEntity survivalCommandBlockEntity) {
		super(survivalCommandBlockEntity);
		survivalCommandBlock = survivalCommandBlockEntity.getCommandBlock();
	}

	@Override
	protected void populateAndSendPacket(final BaseCommandBlock commandBlock) {
		try {
			final CommandBlockEntity.Mode mode = (CommandBlockEntity.Mode) MODE.get(this);
			final boolean conditional = (boolean) CONDITIONAL.get(this);
			final boolean autoexec = (boolean) AUTOEXEC.get(this);

			TestMod3.network.sendToServer(new SaveSurvivalCommandBlockMessage(survivalCommandBlock, commandEdit.getValue(), mode, conditional, autoexec));
		} catch (final IllegalAccessException e) {
			LOGGER.error("Couldn't set survival command block", e);
		}
	}
}
