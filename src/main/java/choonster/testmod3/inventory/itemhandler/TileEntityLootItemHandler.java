package choonster.testmod3.inventory.itemhandler;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameter;
import net.minecraft.loot.LootParameters;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

import javax.annotation.Nullable;

/**
 * An extension of {@link LootItemHandler} with a {@link TileEntity}.
 * <p>
 * This uses the {@link TileEntity}'s {@link World} and adds {@link LootParameter}s for it.
 *
 * @author Choonster
 */
public class TileEntityLootItemHandler extends LootItemHandler {
	private final TileEntity tileEntity;

	public TileEntityLootItemHandler(final TileEntity tileEntity) {
		super(tileEntity::getWorld);
		this.tileEntity = tileEntity;
	}

	public TileEntityLootItemHandler(final int size, final TileEntity tileEntity) {
		super(size, tileEntity::getWorld);
		this.tileEntity = tileEntity;
	}

	public TileEntityLootItemHandler(final NonNullList<ItemStack> stacks, final TileEntity tileEntity) {
		super(stacks, tileEntity::getWorld);
		this.tileEntity = tileEntity;
	}

	/**
	 * Adds additional parameters to the loot context builder before loot is generated.
	 *
	 * @param player  The player generating the loot.
	 * @param builder The loot context builder
	 */
	@Override
	protected void addAdditionalLootParameters(@Nullable final PlayerEntity player, final LootContext.Builder builder) {
		builder.withParameter(LootParameters./*ORIGIN*/field_237457_g_, Vector3d.copyCentered(tileEntity.getPos()));
	}
}
