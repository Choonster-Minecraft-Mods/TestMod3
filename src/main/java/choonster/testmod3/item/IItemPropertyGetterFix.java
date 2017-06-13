package choonster.testmod3.item;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

/**
 * A wrapper for {@link net.minecraft.item.IItemPropertyGetter} that allows lambdas to be used as implementations.
 * <p>
 *
 * @author Choonster
 * @see "MeshDefinitionFix"
 */
public interface IItemPropertyGetterFix extends IItemPropertyGetter {
	float applyPropertyGetter(ItemStack stack, @Nullable World worldIn, @Nullable EntityLivingBase entityIn);

	static IItemPropertyGetterFix create(final IItemPropertyGetterFix lambda) {
		return lambda;
	}

	@Override
	@SideOnly(Side.CLIENT)
	default float apply(final ItemStack stack, @Nullable final World worldIn, @Nullable final EntityLivingBase entityIn) {
		return applyPropertyGetter(stack, worldIn, entityIn);
	}
}
