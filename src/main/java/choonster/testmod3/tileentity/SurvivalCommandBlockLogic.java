package choonster.testmod3.tileentity;

import net.minecraft.tileentity.CommandBlockBaseLogic;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class SurvivalCommandBlockLogic extends CommandBlockBaseLogic {
	private final Type type;

	public SurvivalCommandBlockLogic(final Type type) {
		this.type = type;
		setName("Server");
	}

	public Type getType() {
		return type;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public int getCommandBlockType() {
		return type.ordinal();
	}

	public enum Type {
		BLOCK,
		MINECART
	}
}
