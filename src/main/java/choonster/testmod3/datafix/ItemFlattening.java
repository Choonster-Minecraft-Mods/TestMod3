package choonster.testmod3.datafix;

import choonster.testmod3.TestMod3;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.datafix.IFixableData;

import java.util.List;

/**
 * A data fixer that remaps stacks of flattened items to their new form.
 *
 * @author Choonster
 */
public class ItemFlattening implements IFixableData {
	private final List<FlatteningDefinition> flatteningDefinitions;

	public ItemFlattening(final List<FlatteningDefinition> flatteningDefinitions) {
		this.flatteningDefinitions = flatteningDefinitions;
	}

	@Override
	public int getFixVersion() {
		return 103;
	}

	@Override
	public NBTTagCompound fixTagCompound(final NBTTagCompound compound) {
		final ResourceLocation oldName = new ResourceLocation(compound.getString("id"));
		final short oldMetadata = compound.getShort("Damage");
		final NBTTagCompound oldStackCompoundTag = compound.getCompoundTag("tag");

		flatteningDefinitions.stream()
				.filter(flatteningDefinition -> flatteningDefinition.oldName.equals(oldName))
				.filter(flatteningDefinition -> flatteningDefinition.oldMetadata == oldMetadata)
				.findFirst()
				.ifPresent(flatteningDefinition -> {
					final ItemStack newItemStack = flatteningDefinition.itemStackGetter.getItemStack(flatteningDefinition.newItem, flatteningDefinition.oldMetadata, oldStackCompoundTag);
					final NBTTagCompound newNBT = newItemStack.serializeNBT();

					compound.setString("id", newNBT.getString("id"));
					compound.setShort("Damage", newNBT.getShort("Damage"));
					compound.setTag("tag", newNBT.getCompoundTag("tag"));
				});

		return compound;
	}

	static class FlatteningDefinition {
		final ResourceLocation oldName;
		final int oldMetadata;

		final Item newItem;

		final ItemStackGetter itemStackGetter;

		FlatteningDefinition(final ResourceLocation oldName, final int oldMetadata, final Item newItem, final ItemStackGetter itemStackGetter) {
			this.oldName = oldName;
			this.oldMetadata = oldMetadata;
			this.newItem = newItem;
			this.itemStackGetter = itemStackGetter;
		}

		FlatteningDefinition(final String oldName, final int oldMetadata, final Item newItem, final ItemStackGetter itemStackGetter) {
			this(new ResourceLocation(TestMod3.MODID, oldName), oldMetadata, newItem, itemStackGetter);
		}
	}

	@FunctionalInterface
	interface ItemStackGetter {
		ItemStack getItemStack(Item item, int oldMetadata, NBTTagCompound oldStackTagCompound);
	}
}
