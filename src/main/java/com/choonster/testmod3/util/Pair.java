package com.choonster.testmod3.util;

/**
 * A pair of objects.
 * <p>
 * Taken from FloraSoma (https://github.com/LogicTechCorp/FloraSoma)
 * <p>
 * Used for example in this thread:
 * http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/modification-development/2418536-get-stair-state-and-apply-it-to-new-stair-block
 *
 * @param <Left>  The first object's type
 * @param <Right> The second object's type
 * @author LogicTechCorp
 */
public class Pair<Left, Right> {
	public final Left left;
	public final Right right;

	public Pair(Left left, Right right) {
		this.left = left;
		this.right = right;
	}
}