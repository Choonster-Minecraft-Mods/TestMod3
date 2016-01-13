package com.choonster.testmod3.block;

import com.choonster.testmod3.tileentity.TileEntityPotionEffect;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 * A block that applies a potion effect to all entities within a certain distance of it.
 * <p>
 * Test for this thread:
 * http://www.minecraftforge.net/forum/index.php/topic,35818.0.html
 */
public class BlockPotionEffect extends BlockTestMod3 {
	public BlockPotionEffect() {
		super(Material.rock, "potionEffect");
	}

	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new TileEntityPotionEffect();
	}
}
