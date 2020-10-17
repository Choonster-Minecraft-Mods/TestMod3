package choonster.testmod3.util;

import it.unimi.dsi.fastutil.objects.Object2DoubleMap;
import it.unimi.dsi.fastutil.objects.Object2DoubleMaps;
import it.unimi.dsi.fastutil.objects.Object2DoubleOpenHashMap;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.vector.Vector3i;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

/**
 * Utility methods for vectors and {@link AxisAlignedBB}s.
 *
 * @author Choonster
 */
public class VectorUtils {
	/**
	 * A cache of the positive facing's direction vector for each axis.
	 */
	private static final Map<Direction.Axis, Vector3i> AXIS_DIRECTION_VECTORS;

	private static final Object2DoubleMap<Direction> HORIZONTAL_ROTATIONS;

	static {
		final Map<Direction.Axis, Vector3i> axisVectors = new EnumMap<>(Direction.Axis.class);

		for (final Direction.Axis axis : Direction.Axis.values()) { // For each axis,
			// Get the direction vector of the positive facing of the axis
			final Vector3i directionVec = Direction.getFacingFromAxis(Direction.AxisDirection.POSITIVE, axis).getDirectionVec();
			axisVectors.put(axis, directionVec); // Add it to the map
		}

		AXIS_DIRECTION_VECTORS = Collections.unmodifiableMap(axisVectors); // Wrap the map in an unmodifiable map


		// 90 degrees in radians
		final double rotationIncrement = Math.toRadians(90);

		final Object2DoubleMap<Direction> horizontalRotations = new Object2DoubleOpenHashMap<>();

		horizontalRotations.put(Direction.NORTH, 0);
		horizontalRotations.put(Direction.EAST, rotationIncrement);
		horizontalRotations.put(Direction.SOUTH, 2 * rotationIncrement);
		horizontalRotations.put(Direction.WEST, 3 * rotationIncrement);

		HORIZONTAL_ROTATIONS = Object2DoubleMaps.unmodifiable(horizontalRotations);
	}

	/**
	 * Create a matrix that rotates around the specified axis by the specified angle.
	 *
	 * @param axis    The axis
	 * @param radians The angle in radians
	 * @return The rotation matrix
	 */
	/*
	// TODO: Vecmath isn't used any more, figure out how to do this with Minecraft's vectors
	public static Matrix3d getRotationMatrix(final Direction.Axis axis, final double radians) {
		final Vector3i axisDirectionVector = AXIS_DIRECTION_VECTORS.get(axis);
		final AxisAngle4d axisAngle = new AxisAngle4d(axisDirectionVector.getX(), axisDirectionVector.getY(), axisDirectionVector.getZ(), radians);

		final Matrix3d rotationMatrix = new Matrix3d();
		rotationMatrix.set(axisAngle);

		return rotationMatrix;
	}
	*/

	/**
	 * Rotate an {@link AxisAlignedBB} by the specified rotation matrix.
	 *
	 * @param axisAlignedBB  The AABB
	 * @param rotationMatrix The rotation matrix
	 * @param forcePositive  If true, set each coordinate of the rotated AABB to it absolute value
	 * @return The rotated AABB
	 */
	/*
	// TODO: Vecmath isn't used any more, figure out how to do this with Minecraft's vectors
	public static AxisAlignedBB rotateAABB(final AxisAlignedBB axisAlignedBB, final Matrix3d rotationMatrix, final boolean forcePositive) {
		// Extract the minimum and maximum coordinates of the AABB into vectors
		final Vector3d minCoords = new Vector3d(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ);
		final Vector3d maxCoords = new Vector3d(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ);

		// Rotate the vectors in-place
		rotationMatrix.transform(minCoords);
		rotationMatrix.transform(maxCoords);

		if (forcePositive) {
			// Get the absolute value of the coordinates
			minCoords.absolute();
			maxCoords.absolute();
		}

		// Return an AABB with the new coordinates
		return new AxisAlignedBB(minCoords.getX(), minCoords.getY(), minCoords.getZ(), maxCoords.getX(), maxCoords.getY(), maxCoords.getZ());
	}
	*/

	/**
	 * Get the angle of the specified {@link Direction} relative to {@link Direction#NORTH} along the horizontal plane.
	 *
	 * @param facing The facing
	 * @return The angle in radians
	 */
	public static double getHorizontalRotation(final Direction facing) {
		return HORIZONTAL_ROTATIONS.getDouble(facing);
	}
}
