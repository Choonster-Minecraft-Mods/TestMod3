package choonster.testmod3.world.item;

import choonster.testmod3.capability.SerializableCapabilityProvider;
import choonster.testmod3.capability.hiddenblockrevealer.HiddenBlockRevealer;
import choonster.testmod3.capability.hiddenblockrevealer.HiddenBlockRevealerCapability;
import choonster.testmod3.text.TestMod3Lang;
import net.minecraft.Util;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
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
	public InteractionResultHolder<ItemStack> use(final Level level, final Player playerIn, final InteractionHand hand) {
		final ItemStack heldItem = playerIn.getItemInHand(hand);
		HiddenBlockRevealerCapability.toggleRevealHiddenBlocks(heldItem)
				.ifPresent(revealHiddenBlocks -> {
					final TestMod3Lang message = revealHiddenBlocks ? TestMod3Lang.MESSAGE_HIDDEN_BLOCK_REVEALER_REVEAL : TestMod3Lang.MESSAGE_HIDDEN_BLOCK_REVEALER_HIDE;
					playerIn.sendMessage(new TranslatableComponent(message.getTranslationKey()), Util.NIL_UUID);
				});

		return new InteractionResultHolder<>(InteractionResult.SUCCESS, heldItem);
	}

	@Override
	public ICapabilityProvider initCapabilities(final ItemStack stack, @Nullable final CompoundTag nbt) {
		//noinspection ConstantConditions
		if (HiddenBlockRevealerCapability.HIDDEN_BLOCK_REVEALER_CAPABILITY == null) return null;

		return new SerializableCapabilityProvider<>(HiddenBlockRevealerCapability.HIDDEN_BLOCK_REVEALER_CAPABILITY, HiddenBlockRevealerCapability.DEFAULT_FACING, new HiddenBlockRevealer());
	}
}
