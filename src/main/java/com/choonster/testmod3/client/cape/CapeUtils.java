package com.choonster.testmod3.client.cape;

import com.choonster.testmod3.Logger;
import com.choonster.testmod3.TestMod3;
import com.choonster.testmod3.util.ReflectionUtil;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.lang.invoke.MethodHandle;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@SideOnly(Side.CLIENT)
class CapeUtils {
	private static final ResourceLocation CAPE_LOCATION = new ResourceLocation(TestMod3.MODID, "textures/capes/testmod3.png");
	private static final UUID UUID_CHOONSTER = UUID.fromString("12bbe833-bf2b-4daa-adb0-9a7f6e2f4f38");

	// Copied from SkinManager
	private static final ExecutorService THREAD_POOL = new ThreadPoolExecutor(0, 2, 1L, TimeUnit.MINUTES, new LinkedBlockingQueue<>());

	private static final MethodHandle GET_PLAYER_INFO = ReflectionUtil.findMethod(AbstractClientPlayer.class, new String[]{"getPlayerInfo", "func_175155_b"});
	private static final MethodHandle GET_PLAYER_TEXTURES = ReflectionUtil.findFieldGetter(NetworkPlayerInfo.class, "playerTextures", "field_187107_a");

	/**
	 * Queue the replacement of a player's cape with the TestMod3 cape.
	 * <p>
	 * In at least 100 milliseconds, the player's cape will be replaced on the next iteration of the client's main loop.
	 *
	 * @param player The player
	 */
	static void queuePlayerCapeReplacement(AbstractClientPlayer player) {
		String displayName = player.getDisplayNameString();

		Logger.info("Queueing cape replacement for %s", displayName);

		THREAD_POOL.submit(() -> {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				Logger.fatal(e, "Cape delay thread for %s interrupted", displayName);
				return;
			}

			Minecraft.getMinecraft().addScheduledTask(() -> replacePlayerCape(player));
		});
	}

	/**
	 * Replace a player's cape with the TestMod3 cape.
	 *
	 * @param player The player
	 */
	@SuppressWarnings("unchecked")
	private static void replacePlayerCape(AbstractClientPlayer player) {
		String displayName = player.getDisplayNameString();

		NetworkPlayerInfo playerInfo;

		try {
			playerInfo = (NetworkPlayerInfo) GET_PLAYER_INFO.invoke(player);
		} catch (Throwable throwable) {
			Logger.fatal(throwable, "Failed to get NetworkPlayerInfo of %s", displayName);
			return;
		}

		if (playerInfo == null) {
			Logger.fatal("NetworkPlayerInfo of %s is null", displayName);
			return;
		}


		final Map<MinecraftProfileTexture.Type, ResourceLocation> playerTextures;
		try {
			playerTextures = (Map<MinecraftProfileTexture.Type, ResourceLocation>) GET_PLAYER_TEXTURES.invoke(playerInfo);
		} catch (Throwable throwable) {
			Logger.fatal(throwable, "Failed to get player textures of %s", displayName);
			return;
		}

		playerTextures.put(MinecraftProfileTexture.Type.CAPE, CAPE_LOCATION);

		Logger.info("Replaced cape of %s!", displayName);
	}

	/**
	 * Does the player have a TestMod3 cape?
	 * <p>
	 * Currently only returns true for me (Choonster)
	 *
	 * @param player The player
	 * @return True if the player has a TestMod3 cape
	 */
	static boolean doesPlayerHaveCape(AbstractClientPlayer player) {
		return player.getUniqueID().equals(UUID_CHOONSTER);
	}
}
