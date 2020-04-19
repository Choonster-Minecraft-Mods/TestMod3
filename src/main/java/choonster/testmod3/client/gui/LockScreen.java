package choonster.testmod3.client.gui;

import choonster.testmod3.TestMod3;
import choonster.testmod3.api.capability.lock.ILock;
import choonster.testmod3.network.SetLockCodeMessage;
import net.minecraft.client.gui.chat.NarratorChatListener;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import org.lwjgl.glfw.GLFW;

import javax.annotation.Nullable;

/**
 * Allows a player to lock an {@link ILock}.
 *
 * @author Choonster
 */
public class LockScreen extends Screen {
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
	private final Direction facing;

	/**
	 * The lock code text field.
	 */
	private TextFieldWidget lockCodeTextField;
	private Button doneButton;
	private Button cancelButton;

	public LockScreen(final ILock lock, final BlockPos pos, @Nullable final Direction facing) {
		super(NarratorChatListener.EMPTY);
		this.lock = lock;
		this.pos = pos;
		this.facing = facing;
	}

	@Override
	public void tick() {
		lockCodeTextField.tick();
	}


	@Override
	protected void init() {
		minecraft.keyboardListener.enableRepeatEvents(true);

		doneButton = addButton(new Button(width / 2 - 4 - 150, height / 4 + 120 + 12, 150, 20, I18n.format("gui.done"), button -> done()));

		cancelButton = addButton(new Button(width / 2 + 4, height / 4 + 120 + 12, 150, 20, I18n.format("gui.cancel"), button -> onClose()));

		lockCodeTextField = new TextFieldWidget(font, width / 2 - 150, 50, 300, 20, I18n.format("gui.testmod3.lock.lock_code"));
		lockCodeTextField.setMaxStringLength(32500);
		lockCodeTextField.setFocused2(true);
		children.add(lockCodeTextField);
	}

	private void done() {
		TestMod3.network.sendToServer(new SetLockCodeMessage(pos, facing, lockCodeTextField.getText()));
		minecraft.displayGuiScreen(null);
	}

	@Override
	public void onClose() {
		minecraft.keyboardListener.enableRepeatEvents(false);
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
		renderBackground();
		drawCenteredString(font, I18n.format("testmod3.lock.set_lock_code"), width / 2, 20, 0xffffff);
		drawString(font, I18n.format("testmod3.lock.lock_code"), width / 2 - 150, 37, 0xa0a0a0);
		lockCodeTextField.render(mouseX, mouseY, partialTicks);
		drawString(font, "", width / 2 - 150, 75 * font.FONT_HEIGHT, 0xa0a0a0);

		super.render(mouseX, mouseY, partialTicks);
	}
}
