package choonster.testmod3.client.gui;

import choonster.testmod3.Logger;
import choonster.testmod3.TestMod3;
import choonster.testmod3.network.MessageSurvivalCommandBlockSaveChanges;
import choonster.testmod3.tileentity.SurvivalCommandBlockLogic;
import choonster.testmod3.tileentity.TileEntitySurvivalCommandBlock;
import choonster.testmod3.util.ReflectionUtil;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiCommandBlock;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.tileentity.TileEntityCommandBlock;

import java.io.IOException;
import java.lang.invoke.MethodHandle;

public class GuiSurvivalCommandBlock extends GuiCommandBlock {
	private static final MethodHandle COMMAND_TEXT_GETTER = ReflectionUtil.findFieldGetter(GuiCommandBlock.class, "field_146485_f" /* commandTextField */);
	private static final MethodHandle COMMAND_BLOCK_MODE_GETTER = ReflectionUtil.findFieldGetter(GuiCommandBlock.class, "field_184082_w" /* commandBlockMode */);
	private static final MethodHandle CONDITIONAL_GETTER = ReflectionUtil.findFieldGetter(GuiCommandBlock.class, "field_184084_y" /* conditional */);
	private static final MethodHandle AUTOMATIC_GETTER = ReflectionUtil.findFieldGetter(GuiCommandBlock.class, "field_184085_z" /* automatic */);

	private final SurvivalCommandBlockLogic survivalCommandBlockLogic;

	public GuiSurvivalCommandBlock(final TileEntitySurvivalCommandBlock tileEntitySurvivalCommandBlock) {
		super(tileEntitySurvivalCommandBlock);
		this.survivalCommandBlockLogic = tileEntitySurvivalCommandBlock.getCommandBlockLogic();
	}

	@Override
	protected void actionPerformed(final GuiButton button) throws IOException {
		if (button.enabled && button.id == 0) {
			try {
				final GuiTextField commandTextField = (GuiTextField) COMMAND_TEXT_GETTER.invokeExact((GuiCommandBlock) this);
				final TileEntityCommandBlock.Mode commandBlockMode = (TileEntityCommandBlock.Mode) COMMAND_BLOCK_MODE_GETTER.invokeExact((GuiCommandBlock) this);
				final boolean conditional = (boolean) CONDITIONAL_GETTER.invokeExact((GuiCommandBlock) this);
				final boolean automatic = (boolean) AUTOMATIC_GETTER.invokeExact((GuiCommandBlock) this);

				TestMod3.network.sendToServer(new MessageSurvivalCommandBlockSaveChanges(survivalCommandBlockLogic, commandTextField.getText(), commandBlockMode, conditional, automatic));
			} catch (final Throwable throwable) {
				Logger.error(throwable, "Couldn't set survival command block");
			}

			if (!this.survivalCommandBlockLogic.shouldTrackOutput()) {
				this.survivalCommandBlockLogic.setLastOutput(null);
			}

			this.mc.displayGuiScreen(null);
		} else {
			super.actionPerformed(button);
		}
	}
}
