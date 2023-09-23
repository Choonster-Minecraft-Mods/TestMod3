package choonster.testmod3.client.gui;

import choonster.testmod3.TestMod3;
import choonster.testmod3.network.SaveSurvivalCommandBlockMessage;
import choonster.testmod3.world.level.block.entity.SurvivalCommandBlock;
import choonster.testmod3.world.level.block.entity.SurvivalCommandBlockEntity;
import com.mojang.logging.LogUtils;
import net.minecraft.client.gui.screens.inventory.CommandBlockEditScreen;
import net.minecraft.world.level.BaseCommandBlock;
import net.minecraft.world.level.block.entity.CommandBlockEntity;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import net.minecraftforge.network.PacketDistributor;
import org.slf4j.Logger;

import java.lang.reflect.Field;

public class SurvivalCommandBlockEditScreen extends CommandBlockEditScreen {
	private static final Logger LOGGER = LogUtils.getLogger();

	private static final Field MODE = ObfuscationReflectionHelper.findField(CommandBlockEditScreen.class, /* mode */ "f_98378_");
	private static final Field CONDITIONAL = ObfuscationReflectionHelper.findField(CommandBlockEditScreen.class, /* conditional */ "f_98379_");
	private static final Field AUTOEXEC = ObfuscationReflectionHelper.findField(CommandBlockEditScreen.class, /* autoexec */ "f_98380_");

	private final SurvivalCommandBlock survivalCommandBlock;

	public SurvivalCommandBlockEditScreen(final SurvivalCommandBlockEntity survivalCommandBlockEntity) {
		super(survivalCommandBlockEntity);
		survivalCommandBlock = survivalCommandBlockEntity.getCommandBlock();
	}

	@Override
	protected void populateAndSendPacket(final BaseCommandBlock commandBlock) {
		try {
			final CommandBlockEntity.Mode mode = (CommandBlockEntity.Mode) MODE.get(this);
			final boolean conditional = (boolean) CONDITIONAL.get(this);
			final boolean autoexec = (boolean) AUTOEXEC.get(this);

			TestMod3.network.send(
					new SaveSurvivalCommandBlockMessage(
							survivalCommandBlock,
							commandEdit.getValue(),
							mode,
							conditional,
							autoexec
					),
					PacketDistributor.SERVER.noArg()
			);
		} catch (final IllegalAccessException e) {
			LOGGER.error("Couldn't set survival command block", e);
		}
	}
}
