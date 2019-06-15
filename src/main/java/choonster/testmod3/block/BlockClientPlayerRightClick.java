package choonster.testmod3.block;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;

/**
 * A block that forces a player to right click (from the client side) every 10 ticks (0.5 seconds) while standing on it.
 * <p>
 * Test for this thread:
 * http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/modification-development/2512904-make-the-player-perform-a-right-click-on-non
 *
 * @author Choonster
 */
public class BlockClientPlayerRightClick extends BlockStaticPressurePlate {
	public BlockClientPlayerRightClick(final Block.Properties properties) {
		super(properties);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onEntityCollision(final IBlockState state, final World world, final BlockPos pos, final Entity entity) {
		final EntityPlayer clientPlayer = DistExecutor.callWhenOn(Dist.CLIENT, () -> () -> Minecraft.getInstance().player);

		// If on the client side, the colliding Entity is the client player and the total world time is a multiple of 10
		if (world.isRemote && entity == clientPlayer && world.getGameTime() % 10 == 0) {
			// Make the player right click
			entity.sendMessage(new TextComponentTranslation("message.testmod3.client_player_right_click.right_click"));

			DistExecutor.runWhenOn(Dist.CLIENT, () -> () ->
			{
				// Press the Use Item keybinding
				KeyBinding.onTick(Minecraft.getInstance().gameSettings.keyBindUseItem.getKey());
			});
		}
	}
}
