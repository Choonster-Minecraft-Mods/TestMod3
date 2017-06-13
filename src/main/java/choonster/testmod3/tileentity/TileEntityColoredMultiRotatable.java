package choonster.testmod3.tileentity;

import choonster.testmod3.util.EnumFaceRotation;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;

/**
 * A TileEntity that stores an {@link EnumFacing} value and an {@link EnumFaceRotation} value.
 *
 * @author Choonster
 */
public class TileEntityColoredMultiRotatable extends TileEntityColoredRotatable {
	private EnumFaceRotation faceRotation = EnumFaceRotation.UP;

	public EnumFaceRotation getFaceRotation() {
		return faceRotation;
	}

	public void setFaceRotation(final EnumFaceRotation faceRotation) {
		this.faceRotation = faceRotation;
		markDirty();
	}

	@Override
	public void readFromNBT(final NBTTagCompound compound) {
		super.readFromNBT(compound);
		faceRotation = EnumFaceRotation.values()[compound.getInteger("faceRotation")];
	}

	@Override
	public NBTTagCompound writeToNBT(final NBTTagCompound compound) {
		super.writeToNBT(compound);
		compound.setInteger("faceRotation", faceRotation.ordinal());
		return compound;
	}
}
