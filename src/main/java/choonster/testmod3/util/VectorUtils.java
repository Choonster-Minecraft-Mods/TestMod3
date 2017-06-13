package choonster.testmod3.util;

import com.google.common.collect.Maps;
import gnu.trove.TCollections;
import gnu.trove.map.TObjectDoubleMap;
import gnu.trove.map.hash.TObjectDoubleHashMap;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3i;

import javax.vecmath.AxisAngle4d;
import javax.vecmath.Matrix3d;
import javax.vecmath.Vector3d;
import java.util.HashMap;
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
	private static final Map<EnumFacing.Axis, Vec3i> AXIS_DIRECTION_VECTORS;

	private static final TObjectDoubleMap<EnumFacing> HORIZONTAL_ROTATIONS;

	static {
		final Map<EnumFacing.Axis, Vec3i> axisVectors = new HashMap<>();

		for (final EnumFacing.Axis axis : EnumFacing.Axis.values()) { // For each axis,
			// Get the direction vector of the positive facing of the axis
			final Vec3i directionVec = EnumFacing.getFacingFromAxis(EnumFacing.AxisDirection.POSITIVE, axis).getDirectionVec();
			axisVectors.put(axis, directionVec); // Add it to the map
		}

		AXIS_DIRECTION_VECTORS = Maps.immutableEnumMap(axisVectors); // Wrap the map in an immutable enum map


		// 90 degrees in radians
		final double rotationIncrement = Math.toRadians(90);

		final TObjectDoubleMap<EnumFacing> horizontalRotations = new TObjectDoubleHashMap<>();

		horizontalRotations.put(EnumFacing.NORTH, 0);
		horizontalRotations.put(EnumFacing.EAST, rotationIncrement);
		horizontalRotations.put(EnumFacing.SOUTH, 2 * rotationIncrement);
		horizontalRotations.put(EnumFacing.WEST, 3 * rotationIncrement);

		HORIZONTAL_ROTATIONS = TCollections.unmodifiableMap(horizontalRotations);
	}

	/**
	 * Create a matrix that rotates around the specified axis by the specified angle.
	 *
	 * @param axis    The axis
	 * @param radians The angle in radians
	 * @return The rotation matrix
	 */
	public static Matrix3d getRotationMatrix(final EnumFacing.Axis axis, final double radians) {
		final Vec3i axisDirectionVector = AXIS_DIRECTION_VECTORS.get(axis);
		final AxisAngle4d axisAngle = new AxisAngle4d(axisDirectionVector.getX(), axisDirectionVector.getY(), axisDirectionVector.getZ(), radians);

		final Matrix3d rotationMatrix = new Matrix3d();
		rotationMatrix.set(axisAngle);

		return rotationMatrix;
	}

	/**
	 * Rotate an {@link AxisAlignedBB} by the specified rotation matrix.
	 *
	 * @param axisAlignedBB  The AABB
	 * @param rotationMatrix The rotation matrix
	 * @param forcePositive  If true, set each coordinate of the rotated AABB to it absolute value
	 * @return The rotated AABB
	 */
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

	/**
	 * Get the angle of the specified {@link EnumFacing} relative to {@link EnumFacing#NORTH} along the horizontal plane.
	 *
	 * @param facing The facing
	 * @return The angle in radians
	 */
	public static double getHorizontalRotation(final EnumFacing facing) {
		return HORIZONTAL_ROTATIONS.get(facing);
	}
}
