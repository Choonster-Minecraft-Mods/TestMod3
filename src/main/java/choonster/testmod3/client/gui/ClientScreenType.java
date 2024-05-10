package choonster.testmod3.client.gui;

import choonster.testmod3.init.ModClientScreenTypes;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record ClientScreenType<T>(StreamCodec<RegistryFriendlyByteBuf, T> extraDataCodec) {
	public static final StreamCodec<RegistryFriendlyByteBuf, ClientScreenType<?>> STREAM_CODEC = ByteBufCodecs.registry(ModClientScreenTypes.KEY);
}
