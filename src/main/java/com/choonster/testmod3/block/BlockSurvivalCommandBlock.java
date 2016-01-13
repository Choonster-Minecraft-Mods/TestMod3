package com.choonster.testmod3.block;

import com.choonster.testmod3.TestMod3;
import com.choonster.testmod3.tileentity.TileEntitySurvivalCommandBlock;
import net.minecraft.block.BlockCommandBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 * A Command Block that's accessible outside of Creative Mode.
 * <p>
 * Test for this thread:
 * http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/modification-development/2540671-command-block-replica-issue
 *
 * @author Choonster
 */
public class BlockSurvivalCommandBlock extends BlockCommandBlock {

	public BlockSurvivalCommandBlock() {
		super();
		setCreativeTab(TestMod3.creativeTab);
		BlockTestMod3.setBlockName(this, "survivalCommandBlock");
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntitySurvivalCommandBlock();
	}
}
