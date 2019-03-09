package choonster.testmod3.tileentity;

import net.minecraft.tileentity.CommandBlockBaseLogic;
import net.minecraft.util.text.TextComponentString;

public abstract class SurvivalCommandBlockLogic extends CommandBlockBaseLogic {
	private final Type type;

	public SurvivalCommandBlockLogic(final Type type) {
		this.type = type;
		setName(new TextComponentString("Server"));
	}

	public Type getType() {
		return type;
	}

	public enum Type {
		BLOCK,
		MINECART
	}
}
