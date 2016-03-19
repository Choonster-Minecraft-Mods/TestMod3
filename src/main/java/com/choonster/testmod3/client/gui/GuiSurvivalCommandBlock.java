package com.choonster.testmod3.client.gui;

import com.choonster.testmod3.Logger;
import com.choonster.testmod3.TestMod3;
import com.choonster.testmod3.network.MessageSurvivalCommandBlockSaveChanges;
import com.choonster.testmod3.tileentity.SurvivalCommandBlockLogic;
import com.choonster.testmod3.tileentity.TileEntitySurvivalCommandBlock;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiCommandBlock;
import net.minecraft.client.gui.GuiTextField;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;
import java.lang.reflect.Field;

@SideOnly(Side.CLIENT)
public class GuiSurvivalCommandBlock extends GuiCommandBlock {
	private static final Field COMMAND_TEXT_FIELD = ReflectionHelper.findField(GuiCommandBlock.class, "commandTextField", "field_146485_f");

	private final SurvivalCommandBlockLogic survivalCommandBlockLogic;

	public GuiSurvivalCommandBlock(TileEntitySurvivalCommandBlock tileEntitySurvivalCommandBlock) {
		super(tileEntitySurvivalCommandBlock);
		this.survivalCommandBlockLogic = tileEntitySurvivalCommandBlock.getCommandBlockLogic();
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		if (button.enabled && button.id == 0) {
			try {
				final GuiTextField commandTextField = (GuiTextField) COMMAND_TEXT_FIELD.get(this);
				TestMod3.network.sendToServer(new MessageSurvivalCommandBlockSaveChanges(survivalCommandBlockLogic, commandTextField.getText()));
			} catch (IllegalAccessException e) {
				Logger.error(e, "Couldn't set survival command block");
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
