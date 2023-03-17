package choonster.testmod3.client.gui;

import choonster.testmod3.TestMod3;
import choonster.testmod3.api.capability.lock.ILock;
import choonster.testmod3.network.SetLockCodeMessage;
import choonster.testmod3.text.TestMod3Lang;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.GameNarrator;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;

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
	@Nullable
	private final Direction facing;

	/**
	 * The lock code text field.
	 */
	@Nullable
	private EditBox lockCodeTextField;

	public LockScreen(final ILock lock, final BlockPos pos, @Nullable final Direction facing) {
		super(GameNarrator.NO_TITLE);
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
		addRenderableWidget(
				Button.builder(CommonComponents.GUI_DONE, button -> onDone())
						.bounds(width / 2 - 4 - 150, height / 4 + 120 + 12, 150, 20)
						.build()
		);

		addRenderableWidget(
				Button.builder(CommonComponents.GUI_CANCEL, button -> onClose())
						.bounds(width / 2 + 4, height / 4 + 120 + 12, 150, 20)
						.build()
		);

		lockCodeTextField = new EditBox(font, width / 2 - 150, 50, 300, 20, Component.translatable("gui.testmod3.lock.lock_code"));
		lockCodeTextField.setMaxLength(32500);
		lockCodeTextField.setFocused(true);
		addWidget(lockCodeTextField);
	}

	private void onDone() {
		TestMod3.network.sendToServer(new SetLockCodeMessage(pos, facing, lockCodeTextField.getValue()));
		minecraft.setScreen(null);
	}

	@Override
	public boolean keyPressed(final int keyCode, final int scanCode, final int modifiers) {
		if (keyCode == GLFW.GLFW_KEY_ESCAPE) {
			return super.keyPressed(keyCode, scanCode, modifiers);
		} else if (keyCode == GLFW.GLFW_KEY_ENTER || keyCode == GLFW.GLFW_KEY_KP_ENTER) {
			onDone();
			return true;
		}

		return lockCodeTextField.keyPressed(keyCode, scanCode, modifiers);
	}

	@Override
	public boolean mouseClicked(final double mouseX, final double mouseY, final int mouseButton) {
		return lockCodeTextField.mouseClicked(mouseX, mouseY, mouseButton) || super.mouseClicked(mouseX, mouseY, mouseButton);
	}

	@Override
	public void render(final PoseStack matrixStack, final int mouseX, final int mouseY, final float partialTicks) {
		renderBackground(matrixStack);
		drawCenteredString(matrixStack, font, I18n.get(TestMod3Lang.LOCK_SET_LOCK_CODE.getTranslationKey()), width / 2, 20, 0xffffff);
		drawString(matrixStack, font, I18n.get(TestMod3Lang.LOCK_LOCK_CODE.getTranslationKey()), width / 2 - 150, 37, 0xa0a0a0);
		lockCodeTextField.render(matrixStack, mouseX, mouseY, partialTicks);
		drawString(matrixStack, font, "", width / 2 - 150, 75 * font.lineHeight, 0xa0a0a0);

		super.render(matrixStack, mouseX, mouseY, partialTicks);
	}
}
