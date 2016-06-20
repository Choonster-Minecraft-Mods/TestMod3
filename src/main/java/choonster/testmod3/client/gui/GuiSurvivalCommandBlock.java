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
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;
import java.lang.invoke.MethodHandle;

@SideOnly(Side.CLIENT)
public class GuiSurvivalCommandBlock extends GuiCommandBlock {
	private static final MethodHandle COMMAND_TEXT_GETTER = ReflectionUtil.findFieldGetter(GuiCommandBlock.class, "commandTextField", "field_146485_f");
	private static final MethodHandle COMMAND_BLOCK_MODE_GETTER = ReflectionUtil.findFieldGetter(GuiCommandBlock.class, "commandBlockMode", "field_184082_w");
	private static final MethodHandle CONDITIONAL_GETTER = ReflectionUtil.findFieldGetter(GuiCommandBlock.class, "conditional", "field_184084_y");
	private static final MethodHandle AUTOMATIC_GETTER = ReflectionUtil.findFieldGetter(GuiCommandBlock.class, "automatic", "field_184085_z");

	private final SurvivalCommandBlockLogic survivalCommandBlockLogic;

	public GuiSurvivalCommandBlock(TileEntitySurvivalCommandBlock tileEntitySurvivalCommandBlock) {
		super(tileEntitySurvivalCommandBlock);
		this.survivalCommandBlockLogic = tileEntitySurvivalCommandBlock.getCommandBlockLogic();
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		if (button.enabled && button.id == 0) {
			try {
				final GuiTextField commandTextField = (GuiTextField) COMMAND_TEXT_GETTER.invoke(this);
				final TileEntityCommandBlock.Mode commandBlockMode = (TileEntityCommandBlock.Mode) COMMAND_BLOCK_MODE_GETTER.invoke(this);
				final boolean conditional = (boolean) CONDITIONAL_GETTER.invoke(this);
				final boolean automatic = (boolean) AUTOMATIC_GETTER.invoke(this);

				TestMod3.network.sendToServer(new MessageSurvivalCommandBlockSaveChanges(survivalCommandBlockLogic, commandTextField.getText(), commandBlockMode, conditional, automatic));
			} catch (Throwable throwable) {
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
