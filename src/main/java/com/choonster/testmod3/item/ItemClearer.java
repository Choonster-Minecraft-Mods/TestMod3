package com.choonster.testmod3.item;

import com.google.common.collect.ImmutableList;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemClearer extends ItemTestMod3 {
	private static final ImmutableList<Block> whitelist = ImmutableList.of(Blocks.stone, Blocks.dirt, Blocks.grass, Blocks.gravel, Blocks.sand, Blocks.water, Blocks.flowing_water, Blocks.lava, Blocks.flowing_lava, Blocks.ice);

	private static final int MODE_WHITELIST = 0;
	private static final int MODE_ALL = 1;

	public ItemClearer() {
		super("clearer");
	}

	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
		if (!world.isRemote) {
			int currentMode = stack.getMetadata();

			if (player.isSneaking()) {
				int newMode = currentMode == MODE_ALL ? MODE_WHITELIST : MODE_ALL;
				stack.setItemDamage(newMode);
				player.addChatComponentMessage(new ChatComponentTranslation("message.testmod3:clearer.mode.%s", newMode));
			} else {
				int minX = MathHelper.floor_double(player.posX / 16) * 16;
				int minZ = MathHelper.floor_double(player.posZ / 16) * 16;

				player.addChatComponentMessage(new ChatComponentTranslation("message.testmod3:clearer.clearing", minX, minZ));

				for (int x = minX; x < minX + 16; x++) {
					for (int z = minZ; z < minZ + 16; z++) {
						for (int y = 0; y < 256; y++) {
							BlockPos pos = new BlockPos(x, y, z);
							Block block = world.getBlockState(pos).getBlock();
							if ((currentMode == MODE_ALL && block != Blocks.bedrock) || whitelist.contains(block)) {
								world.setBlockState(pos, Blocks.air.getDefaultState(), 2);
							}
						}
					}
				}

				world.markBlockForUpdate(player.getPosition());

				player.addChatComponentMessage(new ChatComponentTranslation("message.testmod3:clearer.cleared"));
			}
		}

		return super.onItemRightClick(stack, world, player);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public boolean hasEffect(ItemStack stack) {
		return stack.getMetadata() == MODE_ALL;
	}
}
