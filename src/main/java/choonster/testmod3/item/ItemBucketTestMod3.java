package choonster.testmod3.item;

import choonster.testmod3.TestMod3;
import choonster.testmod3.fluids.FluidHandlerUniversalBucket;
import choonster.testmod3.util.RegistryUtil;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.*;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nullable;

/**
 * A copy of Forge's Universal Bucket that uses a single {@link Item} for both the full and empty buckets.
 * <p>
 * Test for this thread:
 * http://www.minecraftforge.net/forum/topic/60374-re-skinned-universal-bucket/
 *
 * @author Choonster
 */
public class ItemBucketTestMod3 extends UniversalBucket {
	private final ItemStack empty = new ItemStack(this);

	public ItemBucketTestMod3(final String name) {
		this(name, Fluid.BUCKET_VOLUME);
	}

	public ItemBucketTestMod3(final String name, final int capacity) {
		super(capacity, ItemStack.EMPTY, true);
		RegistryUtil.setItemName(this, name);
		setCreativeTab(TestMod3.creativeTab);
	}

	@Override
	public void getSubItems(@Nullable final CreativeTabs tab, final NonNullList<ItemStack> subItems) {
		if (!this.isInCreativeTab(tab)) return;

		subItems.add(empty);

		for (final Fluid fluid : FluidRegistry.getRegisteredFluids().values()) {
			// Add all fluids that the bucket can be filled with
			final FluidStack fs = new FluidStack(fluid, getCapacity());
			final ItemStack stack = new ItemStack(this);
			final IFluidHandlerItem fluidHandler = stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null);
			if (fluidHandler != null && fluidHandler.fill(fs, true) == fs.amount) {
				final ItemStack filled = fluidHandler.getContainer();
				subItems.add(filled);
			}
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public String getItemStackDisplayName(final ItemStack stack) {
		final FluidStack fluidStack = getFluid(stack);
		final String translationKey = this.getUnlocalizedNameInefficiently(stack);

		// If the bucket is empty, translate the translation key directly
		if (fluidStack == null) {
			return I18n.translateToLocal(translationKey + ".name").trim();
		}

		// If there's a fluid-specific translation, use it
		final String fluidUnlocalisedName = translationKey + ".filled." + fluidStack.getFluid().getName() + ".name";
		if (I18n.canTranslate(fluidUnlocalisedName)) {
			return I18n.translateToLocal(fluidUnlocalisedName);
		}

		// Else translate the filled name directly, formatting it with the fluid name
		return I18n.translateToLocalFormatted(translationKey + ".filled.name", fluidStack.getLocalizedName());
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(final World world, final EntityPlayer player, final EnumHand hand) {
		final ItemStack heldItem = player.getHeldItem(hand);
		final FluidStack fluidStack = getFluid(heldItem);

		// If the bucket is full, call the super method to try and empty it
		if (fluidStack != null) return super.onItemRightClick(world, player, hand);

		// If the bucket is empty, try and fill it
		final RayTraceResult target = this.rayTrace(world, player, true);

		if (target == null || target.typeOfHit != RayTraceResult.Type.BLOCK) {
			return new ActionResult<>(EnumActionResult.PASS, heldItem);
		}

		final BlockPos pos = target.getBlockPos();

		final ItemStack singleBucket = heldItem.copy();
		singleBucket.setCount(1);

		final FluidActionResult filledResult = FluidUtil.tryPickUpFluid(singleBucket, player, world, pos, target.sideHit);
		if (filledResult.isSuccess()) {
			final ItemStack filledBucket = filledResult.result;

			if (player.capabilities.isCreativeMode)
				return new ActionResult<>(EnumActionResult.SUCCESS, heldItem);

			heldItem.shrink(1);
			if (heldItem.isEmpty())
				return new ActionResult<>(EnumActionResult.SUCCESS, filledBucket);

			ItemHandlerHelper.giveItemToPlayer(player, filledBucket);

			return new ActionResult<>(EnumActionResult.SUCCESS, heldItem);
		}

		return new ActionResult<>(EnumActionResult.PASS, heldItem);
	}


	@Override
	public ItemStack getEmpty() {
		return empty;
	}

	@Nullable
	@Override
	public FluidStack getFluid(final ItemStack container) {
		return FluidUtil.getFluidContained(container);
	}

	@Override
	public ICapabilityProvider initCapabilities(final ItemStack stack, final NBTTagCompound nbt) {
		// FluidBucketWrapper only works with Forge's UniversalBucket instance, use a different IFluidHandlerItem implementation instead
		return new FluidHandlerUniversalBucket(stack, getCapacity());
	}
}
