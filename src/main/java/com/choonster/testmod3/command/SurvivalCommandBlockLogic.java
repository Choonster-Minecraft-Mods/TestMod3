package com.choonster.testmod3.command;

import net.minecraft.command.server.CommandBlockLogic;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class SurvivalCommandBlockLogic extends CommandBlockLogic {
	private final Type type;

	public SurvivalCommandBlockLogic(Type type) {
		this.type = type;
	}

	public Type getType() {
		return type;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public int func_145751_f() {
		return type.ordinal();
	}

	public enum Type {
		BLOCK,
		MINECART
	}
}
