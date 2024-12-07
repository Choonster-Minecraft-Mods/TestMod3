package choonster.testmod3.client.init;

import choonster.testmod3.TestMod3;
import choonster.testmod3.client.renderer.item.properties.numeric.TicksSinceLastUse;
import com.mojang.serialization.MapCodec;
import net.minecraft.client.renderer.item.properties.conditional.ConditionalItemModelProperties;
import net.minecraft.client.renderer.item.properties.conditional.ConditionalItemModelProperty;
import net.minecraft.client.renderer.item.properties.numeric.RangeSelectItemModelProperties;
import net.minecraft.client.renderer.item.properties.numeric.RangeSelectItemModelProperty;
import net.minecraft.client.renderer.item.properties.select.SelectItemModelProperties;
import net.minecraft.client.renderer.item.properties.select.SelectItemModelProperty;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;

/**
 * Registers this mod's {@link ConditionalItemModelProperty}, {@link RangeSelectItemModelProperty} and
 * {@link SelectItemModelProperty} implementations.
 *
 * @author Choonster
 */
@Mod.EventBusSubscriber(modid = TestMod3.MODID, bus = Bus.MOD, value = Dist.CLIENT)
public class ModItemModelProperties {
	private static final ExtraCodecs.LateBoundIdMapper<ResourceLocation, MapCodec<? extends SelectItemModelProperty.Type<?, ?>>> SELECT_ID_MAPPER;
	private static final ExtraCodecs.LateBoundIdMapper<ResourceLocation, MapCodec<? extends ConditionalItemModelProperty>> CONDITIONAL_ID_MAPPER;
	private static final ExtraCodecs.LateBoundIdMapper<ResourceLocation, MapCodec<? extends RangeSelectItemModelProperty>> RANGE_SELECT_ID_MAPPER;

	// TODO: Refactor if/when Forge adds a proper event for this
	@SubscribeEvent
	public static void registerItemModelProperties(final FMLClientSetupEvent event) {
		event.enqueueWork(() -> {
//			RevealHiddenBlocksItemPropertyFunction.registerForItem(ModItems.HIDDEN_BLOCK_REVEALER.get());
			registerRangeSelect("ticks_since_last_use", TicksSinceLastUse.MAP_CODEC);

//			TicksSinceLastUse.registerForItem(ModItems.MODEL_TEST.get());
//			TicksSinceLastUse.registerForItem(ModItems.SLINGSHOT.get());
		});
	}

	private static <T extends RangeSelectItemModelProperty> void registerRangeSelect(
			final String name,
			final MapCodec<T> mapCodec
	) {
		RANGE_SELECT_ID_MAPPER.put(ResourceLocation.fromNamespaceAndPath(TestMod3.MODID, name), mapCodec);
	}

	static {
		try {
			@SuppressWarnings("unchecked") final var selectIdMapper =
					(ExtraCodecs.LateBoundIdMapper<ResourceLocation, MapCodec<? extends SelectItemModelProperty.Type<?, ?>>>)
							ObfuscationReflectionHelper.findField(
									SelectItemModelProperties.class,
									"ID_MAPPER"
							).get(null);

			SELECT_ID_MAPPER = selectIdMapper;

			@SuppressWarnings("unchecked") final var conditionalIdMapper =
					(ExtraCodecs.LateBoundIdMapper<ResourceLocation, MapCodec<? extends ConditionalItemModelProperty>>)
							ObfuscationReflectionHelper.findField(
									ConditionalItemModelProperties.class,
									"ID_MAPPER"
							).get(null);

			CONDITIONAL_ID_MAPPER = conditionalIdMapper;

			@SuppressWarnings("unchecked") final var rangeSelectIdMapper =
					(ExtraCodecs.LateBoundIdMapper<ResourceLocation, MapCodec<? extends RangeSelectItemModelProperty>>)
							ObfuscationReflectionHelper.findField(
									RangeSelectItemModelProperties.class,
									"ID_MAPPER"
							).get(null);
			
			RANGE_SELECT_ID_MAPPER = rangeSelectIdMapper;
		} catch (final IllegalAccessException e) {
			throw new RuntimeException("Failed to initialise Item Property ID Mappers", e);
		}
	}
}
