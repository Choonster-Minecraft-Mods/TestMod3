package choonster.testmod3.client.gui;

import choonster.testmod3.TestMod3;
import choonster.testmod3.api.capability.lock.ILock;
import choonster.testmod3.network.MessageLockSetLockCode;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import org.lwjgl.glfw.GLFW;

import javax.annotation.Nullable;

/**
 * Allows a player to lock an {@link ILock}.
 *
 * @author Choonster
 */
public class GuiLock extends GuiScreen {
	/**
	 * The lock.
	 */
	private final ILock lock;

	/**
	 * The position.
	 */
	private final BlockPos pos;

	/**
	 * The facing.
	 */
	private final EnumFacing facing;

	/**
	 * The lock code text field.
	 */
	private GuiTextField lockCodeTextField;
	private GuiButton doneButton;
	private GuiButton cancelButton;

	public GuiLock(final ILock lock, final BlockPos pos, @Nullable final EnumFacing facing) {
		this.lock = lock;
		this.pos = pos;
		this.facing = facing;
	}

	@Override
	public void tick() {
		lockCodeTextField.tick();
	}

	@Override
	public void initGui() {
		mc.keyboardListener.enableRepeatEvents(true);

		doneButton = addButton(new GuiButton(0, width / 2 - 4 - 150, height / 4 + 120 + 12, 150, 20, I18n.format("gui.done")) {
			@Override
			public void onClick(final double mouseX, final double mouseY) {
				done();
			}
		});

		cancelButton = addButton(new GuiButton(1, width / 2 + 4, height / 4 + 120 + 12, 150, 20, I18n.format("gui.cancel")) {
			@Override
			public void onClick(final double mouseX, final double mouseY) {
				close();
			}
		});

		lockCodeTextField = new GuiTextField(2, fontRenderer, width / 2 - 150, 50, 300, 20);
		lockCodeTextField.setMaxStringLength(32500);
		lockCodeTextField.setFocused(true);
	}

	private void done() {
		TestMod3.network.sendToServer(new MessageLockSetLockCode(pos, facing, lockCodeTextField.getText()));
		mc.displayGuiScreen(null);
	}

	@Override
	public void onGuiClosed() {
		mc.keyboardListener.enableRepeatEvents(false);
	}

	@Override
	public boolean keyPressed(final int keyCode, final int scanCode, final int modifiers) {
		if (keyCode == GLFW.GLFW_KEY_ESCAPE) {
			return super.keyPressed(keyCode, scanCode, modifiers);
		} else if (keyCode == GLFW.GLFW_KEY_ENTER || keyCode == GLFW.GLFW_KEY_KP_ENTER) {
			done();
			return true;
		}

		return lockCodeTextField.keyPressed(keyCode, scanCode, modifiers);
	}

	@Override
	public boolean mouseClicked(final double mouseX, final double mouseY, final int mouseButton) {
		return lockCodeTextField.mouseClicked(mouseX, mouseY, mouseButton) || super.mouseClicked(mouseX, mouseY, mouseButton);
	}

	@Override
	public void render(final int mouseX, final int mouseY, final float partialTicks) {
		drawDefaultBackground();
		drawCenteredString(fontRenderer, I18n.format("testmod3:lock.set_lock_code"), width / 2, 20, 0xffffff);
		drawString(fontRenderer, I18n.format("testmod3:lock.lock_code"), width / 2 - 150, 37, 0xa0a0a0);
		lockCodeTextField.drawTextField(mouseX, mouseY, partialTicks);
		drawString(fontRenderer, "", width / 2 - 150, 75 * fontRenderer.FONT_HEIGHT, 0xa0a0a0);

		super.render(mouseX, mouseY, partialTicks);
	}
}
