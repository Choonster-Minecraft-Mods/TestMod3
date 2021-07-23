package choonster.testmod3.util;

import com.google.common.collect.ImmutableMap;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import it.unimi.dsi.fastutil.objects.Object2DoubleMap;
import it.unimi.dsi.fastutil.objects.Object2DoubleMaps;
import it.unimi.dsi.fastutil.objects.Object2DoubleOpenHashMap;
import net.minecraft.Util;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.EnumMap;
import java.util.Map;

/**
 * Utility methods for vectors and {@link AABB}s.
 *
 * @author Choonster
 */
public class VectorUtils {
	private static final int EPSILON_SCALE = 5;

	/**
	 * A cache of the positive facing's direction vector for each axis.
	 */
	private static final Map<Direction.Axis, Vector3f> AXIS_DIRECTION_VECTORS = Util.make(() -> {
		final Map<Direction.Axis, Vector3f> axisVectors = new EnumMap<>(Direction.Axis.class);

		for (final Direction.Axis axis : Direction.Axis.values()) { // For each axis,
			// Get the direction vector of the positive facing of the axis
			final Vec3i directionVec = Direction.get(Direction.AxisDirection.POSITIVE, axis).getNormal();
			axisVectors.put(axis, new Vector3f(directionVec.getX(), directionVec.getY(), directionVec.getZ())); // Add it to the map
		}

		return ImmutableMap.copyOf(axisVectors); // Create an immutable map
	});

	private static final Object2DoubleMap<Direction> HORIZONTAL_ROTATIONS = Util.make(() -> {
		// 90 degrees in radians
		final double rotationIncrement = Math.toRadians(90);

		final Object2DoubleMap<Direction> horizontalRotations = new Object2DoubleOpenHashMap<>();

		horizontalRotations.put(Direction.NORTH, 0);
		horizontalRotations.put(Direction.EAST, rotationIncrement);
		horizontalRotations.put(Direction.SOUTH, 2 * rotationIncrement);
		horizontalRotations.put(Direction.WEST, 3 * rotationIncrement);

		return Object2DoubleMaps.unmodifiable(horizontalRotations);
	});

	/**
	 * Create a quaternion that rotates around the specified axis by the specified angle.
	 *
	 * @param axis    The axis
	 * @param radians The angle in radians
	 * @return The rotation quaternion
	 */
	public static Quaternion getRotationQuaternion(final Direction.Axis axis, final float radians) {
		final Vector3f axisDirectionVector = AXIS_DIRECTION_VECTORS.get(axis);

		return new Quaternion(axisDirectionVector, radians, false);
	}

	/**
	 * Rotate an {@link AABB} by the specified quaternion.
	 *
	 * @param axisAlignedBB      The AABB
	 * @param rotationQuaternion The rotation quaternion to apply
	 * @return The rotated AABB
	 */
	public static AABB rotateAABB(final AABB axisAlignedBB, final Quaternion rotationQuaternion) {
		// Extract the minimum and maximum coordinates of the AABB into vectors
		final Vector3f minCoords = new Vector3f((float) axisAlignedBB.minX, (float) axisAlignedBB.minY, (float) axisAlignedBB.minZ);
		final Vector3f maxCoords = new Vector3f((float) axisAlignedBB.maxX, (float) axisAlignedBB.maxY, (float) axisAlignedBB.maxZ);

		// Rotate the vectors in-place
		minCoords.transform(rotationQuaternion);
		maxCoords.transform(rotationQuaternion);

		// Return an AABB with the new coordinates
		return new AABB(minCoords.x(), minCoords.y(), minCoords.z(), maxCoords.x(), maxCoords.y(), maxCoords.z());
	}

	/**
	 * Rounds and offsets an {@link AABB} for use in a {@link VoxelShape}.
	 * <p>
	 * The coordinates are rounded to 5 decimal places and offset such that all coordinates are in the range [0, 1].
	 *
	 * @param axisAlignedBB The AABB
	 * @return The rounded and offset AABB
	 */
	public static AABB adjustAABBForVoxelShape(final AABB axisAlignedBB) {
		final AABB roundedAABB = new AABB(
				epsilonRound(axisAlignedBB.minX), epsilonRound(axisAlignedBB.minY), epsilonRound(axisAlignedBB.minZ),
				epsilonRound(axisAlignedBB.maxX), epsilonRound(axisAlignedBB.maxY), epsilonRound(axisAlignedBB.maxZ)
		);

		final double offsetX = roundedAABB.minX < 0 ? 1 : 0;
		final double offsetY = roundedAABB.minY < 0 ? 1 : 0;
		final double offsetZ = roundedAABB.minZ < 0 ? 1 : 0;

		return roundedAABB.move(offsetX, offsetY, offsetZ);
	}

	private static double epsilonRound(final double value) {
		return new BigDecimal(value)
				.setScale(EPSILON_SCALE, RoundingMode.HALF_UP)
				.doubleValue();
	}

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
