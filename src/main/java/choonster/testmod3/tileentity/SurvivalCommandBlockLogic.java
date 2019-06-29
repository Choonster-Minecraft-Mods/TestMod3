package choonster.testmod3.tileentity;

import net.minecraft.tileentity.CommandBlockLogic;
import net.minecraft.util.text.StringTextComponent;

public abstract class SurvivalCommandBlockLogic extends CommandBlockLogic {
	private final Type type;

	public SurvivalCommandBlockLogic(final Type type) {
		this.type = type;
		setName(new StringTextComponent("Server"));
	}

	public Type getType() {
		return type;
	}

	public enum Type {
		BLOCK,
		MINECART
	}
}
