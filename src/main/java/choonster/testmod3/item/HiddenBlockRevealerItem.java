package choonster.testmod3.item;

import choonster.testmod3.capability.SerializableCapabilityProvider;
import choonster.testmod3.capability.hiddenblockrevealer.HiddenBlockRevealerCapability;
import choonster.testmod3.text.TestMod3Lang;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.Util;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import javax.annotation.Nullable;

/**
 * An item that reveals hidden blocks.
 *
 * @author Choonster
 */
public class HiddenBlockRevealerItem extends Item {
	public HiddenBlockRevealerItem(final Item.Properties properties) {
		super(properties);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(final World worldIn, final PlayerEntity playerIn, final Hand hand) {
		final ItemStack heldItem = playerIn.getHeldItem(hand);
		HiddenBlockRevealerCapability.toggleRevealHiddenBlocks(heldItem)
				.ifPresent(revealHiddenBlocks -> {
					final TestMod3Lang message = revealHiddenBlocks ? TestMod3Lang.MESSAGE_HIDDEN_BLOCK_REVEALER_REVEAL : TestMod3Lang.MESSAGE_HIDDEN_BLOCK_REVEALER_HIDE;
					playerIn.sendMessage(new TranslationTextComponent(message.getTranslationKey()), Util.DUMMY_UUID);
				});

		return new ActionResult<>(ActionResultType.SUCCESS, heldItem);
	}

	@Override
	public ICapabilityProvider initCapabilities(final ItemStack stack, @Nullable final CompoundNBT nbt) {
		//noinspection ConstantConditions
		if (HiddenBlockRevealerCapability.HIDDEN_BLOCK_REVEALER_CAPABILITY == null) return null;

		return new SerializableCapabilityProvider<>(HiddenBlockRevealerCapability.HIDDEN_BLOCK_REVEALER_CAPABILITY, HiddenBlockRevealerCapability.DEFAULT_FACING);
	}
}
