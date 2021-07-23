package choonster.testmod3.init;

import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ModMaterials {
	public static final Material STATIC = fluid(MaterialColor.COLOR_BROWN).build();

	public static final Material STATIC_GAS = fluid(MaterialColor.COLOR_BROWN).build();

	public static final Material NORMAL = fluid(MaterialColor.COLOR_ORANGE).build();

	public static final Material NORMAL_GAS = fluid(MaterialColor.COLOR_ORANGE).build();

	public static final Material PORTAL_DISPLACEMENT = fluid(MaterialColor.DIAMOND).build();


	private static MaterialBuilder fluid(final MaterialColor materialColor) {
		return new MaterialBuilder(materialColor)
				.noCollider()
				.notSolidBlocking()
				.nonSolid()
				.destroyOnPush()
				.replaceable()
				.liquid();
	}

	/**
	 * Extension of {@link Material.Builder} that allows access to the private and protected methods from the Vanilla class.
	 */
	private static class MaterialBuilder extends Material.Builder {
		private static final Method NOT_SOLID_BLOCKING = ObfuscationReflectionHelper.findMethod(Material.Builder.class, /* notSolidBlocking */ "func_200505_j");

		public MaterialBuilder(final MaterialColor color) {
			super(color);
		}

		@Override
		public MaterialBuilder liquid() {
			return (MaterialBuilder) super.liquid();
		}

		@Override
		public MaterialBuilder nonSolid() {
			return (MaterialBuilder) super.nonSolid();
		}

		@Override
		public MaterialBuilder noCollider() {
			return (MaterialBuilder) super.noCollider();
		}

		public MaterialBuilder notSolidBlocking() {
			try {
				NOT_SOLID_BLOCKING.invoke(this);
			} catch (final IllegalAccessException | InvocationTargetException e) {
				throw new RuntimeException("Failed to call Material.Builder#notOpaque", e);
			}

			return this;
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
		public MaterialBuilder destroyOnPush() {
			return (MaterialBuilder) super.destroyOnPush();
		}

		@Override
		public MaterialBuilder notPushable() {
			return (MaterialBuilder) super.notPushable();
		}
	}
}
