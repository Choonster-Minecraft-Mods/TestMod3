package com.choonster.testmod3.block;

import com.choonster.testmod3.TestMod3;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;

/**
 * A block that forces a player to right click (from the client side) every 10 ticks (0.5 seconds) while standing on it.
 * <p>
 * Test for this thread:
 * http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/modification-development/2512904-make-the-player-perform-a-right-click-on-non
 *
 * @author Choonster
 */
public class BlockClientPlayerRightClick extends BlockStaticPressurePlate {
	public BlockClientPlayerRightClick() {
		super(Material.rock, "clientPlayerRightClick");
	}

	@Override
	public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
		// If on the client side, the colliding Entity is the client player and the total world time is a multiple of 10
		if (worldIn.isRemote && entityIn == TestMod3.proxy.getClientPlayer() && worldIn.getTotalWorldTime() % 10 == 0) {
			// Make the player right click
			entityIn.addChatMessage(new ChatComponentText("Right click!!"));
			TestMod3.proxy.doClientRightClick();
		}
	}
}
