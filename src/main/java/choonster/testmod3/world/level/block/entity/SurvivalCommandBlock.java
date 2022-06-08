package choonster.testmod3.world.level.block.entity;

import net.minecraft.network.chat.Component;
import net.minecraft.world.level.BaseCommandBlock;

public abstract class SurvivalCommandBlock extends BaseCommandBlock {
	private final Type type;

	public SurvivalCommandBlock(final Type type) {
		this.type = type;
		setName(Component.literal("Server"));
	}

	public Type getType() {
		return type;
	}

	public enum Type {
		BLOCK,
		MINECART
	}
}
