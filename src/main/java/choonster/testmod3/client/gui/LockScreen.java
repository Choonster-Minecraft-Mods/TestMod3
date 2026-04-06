package choonster.testmod3.client.gui;

import choonster.testmod3.TestMod3;
import choonster.testmod3.api.capability.lock.ILock;
import choonster.testmod3.network.SetLockCodeMessage;
import choonster.testmod3.text.TestMod3Lang;
import net.minecraft.client.GameNarrator;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraftforge.network.PacketDistributor;
import org.jspecify.annotations.Nullable;

/**
 * Allows a player to lock an {@link ILock}.
 *
 * @author Choonster
 */
public class LockScreen extends Screen {
	private static final Component SET_LOCK_CODE_LABEL = Component.translatable(
			TestMod3Lang.LOCK_SET_LOCK_CODE.getTranslationKey()
	);

	private static final Component LOCK_CODE_LABEL = Component.translatable(
			TestMod3Lang.LOCK_LOCK_CODE.getTranslationKey()
	);

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
	public void extractRenderState(final GuiGraphicsExtractor graphics, final int mouseX, final int mouseY, final float a) {
		super.extractRenderState(graphics, mouseX, mouseY, a);

		graphics.centeredText(font, SET_LOCK_CODE_LABEL, width / 2, 20, 0xffffffff);
		graphics.text(font, LOCK_CODE_LABEL, width / 2 - 150 + 1, 40, 0xffa0a0a0);

		lockCodeTextField.extractRenderState(graphics, mouseX, mouseY, a);

		graphics.text(font, "", width / 2 - 150, 75 * font.lineHeight, 0xa0a0a0);
	}
}
