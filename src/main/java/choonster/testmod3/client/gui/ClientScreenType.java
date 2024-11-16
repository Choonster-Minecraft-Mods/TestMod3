package choonster.testmod3.client.gui;

import choonster.testmod3.init.ModClientScreenTypes;
import choonster.testmod3.serialization.ForgeRegistryStreamCodec;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public record ClientScreenType<T>(StreamCodec<RegistryFriendlyByteBuf, T> extraDataCodec) {
	public static final StreamCodec<FriendlyByteBuf, ClientScreenType<?>> STREAM_CODEC =
			new ForgeRegistryStreamCodec<>(ModClientScreenTypes.KEY);
}
