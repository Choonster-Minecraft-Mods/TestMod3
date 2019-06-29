package choonster.testmod3.client.gui;

import choonster.testmod3.TestMod3;
import choonster.testmod3.network.SaveSurvivalCommandBlockMessage;
import choonster.testmod3.tileentity.SurvivalCommandBlockLogic;
import choonster.testmod3.tileentity.SurvivalCommandBlockTileEntity;
import choonster.testmod3.util.ReflectionUtil;
import net.minecraft.client.gui.screen.CommandBlockScreen;
import net.minecraft.tileentity.CommandBlockLogic;
import net.minecraft.tileentity.CommandBlockTileEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Field;

public class GuiSurvivalCommandBlock extends CommandBlockScreen {
	private static final Logger LOGGER = LogManager.getLogger();

	private static final Field COMMAND_BLOCK_MODE = ReflectionUtil.findField(CommandBlockScreen.class, "field_184082_w" /* commandBlockMode */);
	private static final Field CONDITIONAL = ReflectionUtil.findField(CommandBlockScreen.class, "field_184084_y" /* conditional */);
	private static final Field AUTOMATIC = ReflectionUtil.findField(CommandBlockScreen.class, "field_184085_z" /* automatic */);

	private final SurvivalCommandBlockLogic survivalCommandBlockLogic;

	public GuiSurvivalCommandBlock(final SurvivalCommandBlockTileEntity survivalCommandBlockTileEntity) {
		super(survivalCommandBlockTileEntity);
		survivalCommandBlockLogic = survivalCommandBlockTileEntity.getCommandBlockLogic();
	}

	@Override
	protected void func_195235_a(final CommandBlockLogic p_195235_1_) {
		try {
			final CommandBlockTileEntity.Mode commandBlockMode = (CommandBlockTileEntity.Mode) COMMAND_BLOCK_MODE.get(this);
			final boolean conditional = (boolean) CONDITIONAL.get(this);
			final boolean automatic = (boolean) AUTOMATIC.get(this);

			TestMod3.network.sendToServer(new SaveSurvivalCommandBlockMessage(survivalCommandBlockLogic, commandTextField.getText(), commandBlockMode, conditional, automatic));
		} catch (final IllegalAccessException e) {
			LOGGER.error("Couldn't set survival command block", e);
		}
	}
}
