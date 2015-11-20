package com.choonster.testmod3.tileentity;

import com.choonster.testmod3.block.BlockColoredMultiRotatable.EnumFaceRotation;

public class TileEntityColoredMultiRotatable extends TileEntityColoredRotatable {
	private EnumFaceRotation faceRotation = EnumFaceRotation.UP;

	public EnumFaceRotation getFaceRotation() {
		return faceRotation;
	}

	public void setFaceRotation(EnumFaceRotation faceRotation) {
		this.faceRotation = faceRotation;
		markDirty();
	}
}
