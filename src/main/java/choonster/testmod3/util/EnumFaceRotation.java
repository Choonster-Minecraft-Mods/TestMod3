package choonster.testmod3.util;

import net.minecraft.util.StringRepresentable;

/**
 * An enum representing the rotation of a block's face.
 *
 * @author Choonster
 */
public enum EnumFaceRotation implements StringRepresentable {
	UP("up"),
	RIGHT("right"),
	DOWN("down"),
	LEFT("left");

	private static final EnumFaceRotation[] VALUES = values();

	private final String name;

	EnumFaceRotation(final String name) {
		this.name = name;
	}

	@Override
	public String getSerializedName() {
		return name;
	}

	@Override
	public String toString() {
		return name;
	}

	public EnumFaceRotation rotateClockwise() {
		return VALUES[(ordinal() + 1) % VALUES.length];
	}

	public EnumFaceRotation rotateCounterClockwise() {
		return VALUES[(ordinal() - 1) % VALUES.length];
	}
}
