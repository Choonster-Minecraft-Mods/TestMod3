package choonster.testmod3.item;

import choonster.testmod3.capability.CapabilityProviderSerializable;
import choonster.testmod3.capability.hiddenblockrevealer.CapabilityHiddenBlockRevealer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import javax.annotation.Nullable;
import java.util.Optional;

/**
 * An item that reveals hidden blocks.
 *
 * @author Choonster
 */
public class ItemHiddenBlockRevealer extends ItemTestMod3 {
	public ItemHiddenBlockRevealer() {
		super("hidden_block_revealer");
		CapabilityHiddenBlockRevealer.RevealHiddenBlocksGetter.addToItem(this);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand hand) {
		final ItemStack heldItem = playerIn.getHeldItem(hand);
		final Optional<Boolean> revealHiddenBlocks = CapabilityHiddenBlockRevealer.toggleRevealHiddenBlocks(heldItem);

		if (!worldIn.isRemote) {
			revealHiddenBlocks.ifPresent((value) -> {
				final String message = value ? "message.testmod3:hidden_block_revealer.reveal" : "message.testmod3:hidden_block_revealer.hide";
				playerIn.sendMessage(new TextComponentTranslation(message));
			});
		}

		return new ActionResult<>(EnumActionResult.SUCCESS, heldItem);
	}

	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable NBTTagCompound nbt) {
		return new CapabilityProviderSerializable<>(CapabilityHiddenBlockRevealer.HIDDEN_BLOCK_REVEALER_CAPABILITY, CapabilityHiddenBlockRevealer.DEFAULT_FACING);
	}
}
