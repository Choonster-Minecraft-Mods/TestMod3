package choonster.testmod3.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;
import net.minecraftforge.common.ForgeConfigSpec.EnumValue;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.commons.lang3.tuple.Pair;

import java.util.HashMap;
import java.util.Map;

public class TestMod3Config {
	public static class Common {
		public final BooleanValue fooBar;

		public final EnumValue<EnumExample> exampleEnumProperty;

		Common(final ForgeConfigSpec.Builder builder) {
			builder.comment("Common config settings")
					.push("common");

			fooBar = builder
					.comment("This is an example boolean property.")
					.translation("testmod3.config.common.fooBar")
					.define("fooBar", false);

			exampleEnumProperty = builder
					.comment("This is an example enum property.", "It will use a GuiConfigEntries.CycleValueEntry in the config GUI.")
					.translation("testmod3.config.common.exampleEnumProperty")
					.defineEnum("exampleEnumProperty", EnumExample.VALUE_1);

			builder.pop();
		}
	}

	public static class Client {
		public final IntValue baz;

		public final EnumValue<EnumExample> exampleSubcategoryEnumProperty;

		public final EnumValue<EnumExampleNested> exampleNestedEnumProperty;

		public final HUDPos chunkEnergyHUDPos;

		Client(final ForgeConfigSpec.Builder builder) {
			builder.comment("Client-only settings")
					.push("client");

			baz = builder
					.comment("This is an example int property.")
					.translation("testmod3.config.client.baz")
					.defineInRange("baz", -100, Integer.MIN_VALUE, Integer.MAX_VALUE);

			exampleSubcategoryEnumProperty = builder
					.comment("This is an example enum property in a subcategory of the main category.")
					.translation("testmod3.config.client.subcategory.exampleSubcategoryEnumProperty")
					.defineEnum("subcategory.exampleSubcategoryEnumProperty", EnumExample.VALUE_3);

			exampleNestedEnumProperty = builder
					.comment("This is an example enum property that uses an enum defined in a nested class.")
					.translation("testmod3.config.client.exampleNestedEnumProperty")
					.defineEnum("exampleNestedEnumProperty", EnumExampleNested.NESTED_2);

			chunkEnergyHUDPos = new HUDPos(
					builder,
					"The position of the Chunk Energy HUD on the screen",
					"chunkEnergyHUDPos",
					0, 0
			);

			builder.pop();
		}

		public enum EnumExampleNested {
			NESTED_1,
			NESTED_2,
			NESTED_3,
			NESTED_4,
			NESTED_5,
		}

		public static class HUDPos {
			public final IntValue x;

			public final IntValue y;

			HUDPos(final ForgeConfigSpec.Builder builder, final String comment, final String path, final int defaultX, final int defaultY) {
				builder.comment(comment)
						.push(path);

				x = builder
						.comment("The x coordinate")
						.defineInRange("x", defaultX, 0, Integer.MAX_VALUE);

				y = builder
						.comment("The y coordinate")
						.defineInRange("y", defaultY, 0, Integer.MAX_VALUE);

				builder.pop();
			}
		}
	}

	public enum EnumExample {
		VALUE_1,
		VALUE_2,
		VALUE_3,
		VALUE_4,
	}


	private static final ForgeConfigSpec commonSpec;
	public static final Common COMMON;

	static {
		final Pair<Common, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Common::new);
		commonSpec = specPair.getRight();
		COMMON = specPair.getLeft();
	}


	private static final ForgeConfigSpec clientSpec;
	public static final Client CLIENT;

	static {
		final Pair<Client, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Client::new);
		clientSpec = specPair.getRight();
		CLIENT = specPair.getLeft();
	}

	public static void register(final ModLoadingContext context) {
		context.registerConfig(ModConfig.Type.COMMON, commonSpec);
		context.registerConfig(ModConfig.Type.CLIENT, clientSpec);
	}
}
