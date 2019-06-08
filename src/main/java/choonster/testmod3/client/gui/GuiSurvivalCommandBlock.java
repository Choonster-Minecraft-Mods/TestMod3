package choonster.testmod3.client.gui;

import choonster.testmod3.TestMod3;
import choonster.testmod3.network.MessageSurvivalCommandBlockSaveChanges;
import choonster.testmod3.tileentity.SurvivalCommandBlockLogic;
import choonster.testmod3.tileentity.TileEntitySurvivalCommandBlock;
import choonster.testmod3.util.ReflectionUtil;
import net.minecraft.client.gui.GuiCommandBlock;
import net.minecraft.tileentity.CommandBlockBaseLogic;
import net.minecraft.tileentity.TileEntityCommandBlock;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Field;

public class GuiSurvivalCommandBlock extends GuiCommandBlock {
	private static final Logger LOGGER = LogManager.getLogger();

	private static final Field COMMAND_BLOCK_MODE = ReflectionUtil.findField(GuiCommandBlock.class, "field_184082_w" /* commandBlockMode */);
	private static final Field CONDITIONAL = ReflectionUtil.findField(GuiCommandBlock.class, "field_184084_y" /* conditional */);
	private static final Field AUTOMATIC = ReflectionUtil.findField(GuiCommandBlock.class, "field_184085_z" /* automatic */);

	private final SurvivalCommandBlockLogic survivalCommandBlockLogic;

	public GuiSurvivalCommandBlock(final TileEntitySurvivalCommandBlock tileEntitySurvivalCommandBlock) {
		super(tileEntitySurvivalCommandBlock);
		survivalCommandBlockLogic = tileEntitySurvivalCommandBlock.getCommandBlockLogic();
	}

	@Override
	protected void func_195235_a(final CommandBlockBaseLogic p_195235_1_) {
		try {
			final TileEntityCommandBlock.Mode commandBlockMode = (TileEntityCommandBlock.Mode) COMMAND_BLOCK_MODE.get(this);
			final boolean conditional = (boolean) CONDITIONAL.get(this);
			final boolean automatic = (boolean) AUTOMATIC.get(this);

			TestMod3.network.sendToServer(new MessageSurvivalCommandBlockSaveChanges(survivalCommandBlockLogic, commandTextField.getText(), commandBlockMode, conditional, automatic));
		} catch (final IllegalAccessException e) {
			LOGGER.error("Couldn't set survival command block", e);
		}
	}
}
