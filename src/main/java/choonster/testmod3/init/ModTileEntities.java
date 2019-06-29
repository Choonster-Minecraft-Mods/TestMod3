package choonster.testmod3.init;

import choonster.testmod3.TestMod3;
import choonster.testmod3.tileentity.*;
import com.mojang.datafixers.DataFixUtils;
import com.mojang.datafixers.types.Type;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SharedConstants;
import net.minecraft.util.datafix.DataFixesManager;
import net.minecraft.util.datafix.TypeReferences;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.registries.ObjectHolder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static choonster.testmod3.util.InjectionUtil.Null;

@ObjectHolder(TestMod3.MODID)
public class ModTileEntities {
	public static TileEntityType<SurvivalCommandBlockTileEntity> SURVIVAL_COMMAND_BLOCK = Null();

	public static TileEntityType<FluidTankTileEntity> FLUID_TANK = Null();

	public static TileEntityType<RestrictedFluidTankTileEntity> FLUID_TANK_RESTRICTED = Null();

	public static TileEntityType<PotionEffectTileEntity> POTION_EFFECT = Null();

	public static TileEntityType<ModChestTileEntity> MOD_CHEST = Null();

	public static TileEntityType<HiddenTileEntity> HIDDEN = Null();


	@Mod.EventBusSubscriber(modid = TestMod3.MODID, bus = Bus.MOD)
	public static class RegistrationHandler {
		private static final Logger LOGGER = LogManager.getLogger();

		@SubscribeEvent
		public static void registerTileEntityTypes(final RegistryEvent.Register<TileEntityType<?>> event) {
			final TileEntityType<?>[] tileEntityTypes = new TileEntityType[]{
					build("survival_command_block", TileEntityType.Builder.create(SurvivalCommandBlockTileEntity::new)),
					build("fluid_tank", TileEntityType.Builder.create(FluidTankTileEntity::new)),
					build("fluid_tank_restricted", TileEntityType.Builder.create(RestrictedFluidTankTileEntity::new)),
					build("potion_effect", TileEntityType.Builder.create(PotionEffectTileEntity::new)),
					build("mod_chest", TileEntityType.Builder.create(ModChestTileEntity::new)),
					build("hidden", TileEntityType.Builder.create(HiddenTileEntity::new)),
			};

			event.getRegistry().registerAll(tileEntityTypes);
		}

		private static <T extends TileEntity> TileEntityType<T> build(final String name, final TileEntityType.Builder<T> builder) {
			final ResourceLocation registryName = new ResourceLocation(TestMod3.MODID, name);

			Type<?> dataFixerType = null;

			try {
				dataFixerType = DataFixesManager.getDataFixer()
						.getSchema(DataFixUtils.makeKey(ModDataFixers.DATA_VERSION))
						.getChoiceType(TypeReferences.BLOCK_ENTITY, registryName.toString());
			} catch (final IllegalArgumentException e) {
				if (SharedConstants.developmentMode) {
					throw e;
				}

				LOGGER.warn("No data fixer registered for TileEntity {}", registryName);
			}

			@SuppressWarnings("ConstantConditions")
			// dataFixerType will always be null until mod data fixers are implemented
			final TileEntityType<T> tileEntityType = builder.build(dataFixerType);
			tileEntityType.setRegistryName(registryName);

			return tileEntityType;
		}
	}
}
