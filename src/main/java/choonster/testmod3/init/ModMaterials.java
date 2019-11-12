package choonster.testmod3.init;

import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ModMaterials {
	public static final Material STATIC = fluid(MaterialColor.BROWN).build();

	public static final Material STATIC_GAS = fluid(MaterialColor.BROWN).build();

	public static final Material NORMAL = fluid(MaterialColor.ADOBE).build();

	public static final Material NORMAL_GAS = fluid(MaterialColor.ADOBE).build();

	public static final Material PORTAL_DISPLACEMENT = fluid(MaterialColor.DIAMOND).build();


	private static MaterialBuilder fluid(final MaterialColor materialColor) {
		return new MaterialBuilder(materialColor)
				.doesNotBlockMovement()
				.notOpaque()
				.notSolid()
				.pushDestroys()
				.replaceable()
				.liquid();
	}

	/**
	 * Extension of {@link Material.Builder} that allows access to the private and protected methods from the Vanilla class.
	 */
	private static class MaterialBuilder extends Material.Builder {
		private static final Method NOT_OPAQUE = ObfuscationReflectionHelper.findMethod(Material.Builder.class, "func_200505_j" /* notOpaque */);

		public MaterialBuilder(final MaterialColor color) {
			super(color);
		}

		@Override
		public MaterialBuilder liquid() {
			return (MaterialBuilder) super.liquid();
		}

		@Override
		public MaterialBuilder notSolid() {
			return (MaterialBuilder) super.notSolid();
		}

		@Override
		public MaterialBuilder doesNotBlockMovement() {
			return (MaterialBuilder) super.doesNotBlockMovement();
		}

		public MaterialBuilder notOpaque() {
			try {
				NOT_OPAQUE.invoke(this);
			} catch (final IllegalAccessException | InvocationTargetException e) {
				throw new RuntimeException("Failed to call Material.Builder#notOpaque", e);
			}

			return this;
		}

		@Override
		public MaterialBuilder requiresTool() {
			return (MaterialBuilder) super.requiresTool();
		}

		@Override
		public MaterialBuilder flammable() {
			return (MaterialBuilder) super.flammable();
		}

		@Override
		public MaterialBuilder replaceable() {
			return (MaterialBuilder) super.replaceable();
		}

		@Override
		public MaterialBuilder pushDestroys() {
			return (MaterialBuilder) super.pushDestroys();
		}

		@Override
		public MaterialBuilder pushBlocks() {
			return (MaterialBuilder) super.pushBlocks();
		}
	}
}
