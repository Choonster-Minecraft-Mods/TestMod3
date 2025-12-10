package choonster.testmod3.world.level.block.entity;

import choonster.testmod3.init.ModBlockEntities;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.permissions.LevelBasedPermissionSet;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CommandBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.CommandBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

/**
 * A Command Block that's accessible outside Creative Mode.
 * <p>
 * Test for this thread:
 * http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/modification-development/2540671-command-block-replica-issue
 *
 * @author Choonster
 */
public class SurvivalCommandBlockEntity extends CommandBlockEntity {

	private final SurvivalCommandBlock survivalCommandBlock = new SurvivalCommandBlock(SurvivalCommandBlock.Type.BLOCK) {
		@Override
		public void setCommand(final String command) {
			super.setCommand(command);
			setChanged();
		}

		@Override
		public void onUpdated(final ServerLevel level) {
			final var state = level.getBlockState(worldPosition);
			level.sendBlockUpdated(worldPosition, state, state, Block.UPDATE_ALL);
		}

		@Override
		public CommandSourceStack createCommandSourceStack(final ServerLevel level, final CommandSource commandSource) {
			final var facing = getBlockState().getValue(CommandBlock.FACING);
			
			return new CommandSourceStack(
					commandSource,
					Vec3.atCenterOf(worldPosition),
					new Vec2(0.0F, facing.toYRot()),
					level,
					LevelBasedPermissionSet.GAMEMASTER,
					getName().getString(),
					getName(),
					level.getServer(),
					null
			);
		}

		@Override
		public boolean isValid() {
			return !isRemoved();
		}
	};

	public SurvivalCommandBlockEntity(final BlockPos pos, final BlockState state) {
		super(pos, state);
	}

	@Override
	public BlockEntityType<?> getType() {
		return ModBlockEntities.SURVIVAL_COMMAND_BLOCK.get();
	}

	@Override
	public SurvivalCommandBlock getCommandBlock() {
		return survivalCommandBlock;
	}

	@Override
	protected void loadAdditional(final ValueInput input) {
		super.loadAdditional(input);

		input.child("SurvivalCommandBlockLogic").ifPresent(
				child -> getCommandBlock().load(child)
		);
	}

	@Override
	protected void saveAdditional(final ValueOutput output) {
		getCommandBlock().save(output.child("SurvivalCommandBlockLogic"));
	}

	@Override
	public Mode getMode() {
		return ((choonster.testmod3.world.level.block.SurvivalCommandBlock) getBlockState().getBlock()).getCommandBlockMode();
	}
}
