package choonster.testmod3.client.gui;

import choonster.testmod3.TestMod3;
import choonster.testmod3.api.capability.lock.ILock;
import choonster.testmod3.network.SetLockCodeMessage;
import choonster.testmod3.text.TestMod3Lang;
import net.minecraft.client.GameNarrator;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.Nullable;

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
		TestMod3.network.send(new SetLockCodeMessage(pos, facing, lockCodeTextField.getValue()), PacketDistributor.SERVER.noArg());
		onClose();
	}

	@Override
	public boolean keyPressed(final KeyEvent event) {
		if (event.isEscape()) {
			return super.keyPressed(event);
		} else if (event.isConfirmation()) {
			onDone();
			return true;
		}

		return lockCodeTextField.keyPressed(event) || super.keyPressed(event);
	}

	@Override
	public boolean mouseClicked(final MouseButtonEvent event, final boolean repeat) {
		return lockCodeTextField.mouseClicked(event, repeat) || super.mouseClicked(event, repeat);
	}

	@Override
	public void render(final GuiGraphics guiGraphics, final int mouseX, final int mouseY, final float partialTicks) {
		super.render(guiGraphics, mouseX, mouseY, partialTicks);

		guiGraphics.drawCenteredString(font, I18n.get(TestMod3Lang.LOCK_SET_LOCK_CODE.getTranslationKey()), width / 2, 20, 0xffffff);
		guiGraphics.drawString(font, I18n.get(TestMod3Lang.LOCK_LOCK_CODE.getTranslationKey()), width / 2 - 150, 37, 0xa0a0a0);
		lockCodeTextField.render(guiGraphics, mouseX, mouseY, partialTicks);
		guiGraphics.drawString(font, "", width / 2 - 150, 75 * font.lineHeight, 0xa0a0a0);
	}
}
