package io.github.littoil.turnbased.world;

import io.github.littoil.turnbased.TurnBased;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DimensionType;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.gen.IChunkGenerator;

public class WorldProviderTurnBased extends WorldProvider {

	@Override
	public DimensionType getDimensionType() {
		return TurnBased.getTurnBasedWorld();
	}

	public IChunkGenerator createChunkGenerator() {
		return new ChunkGeneratorTurnBased(this.world);
	}

	@Override
	public boolean canMineBlock(EntityPlayer player, BlockPos pos) {
		return false;
	}

	/**
	 * True if the player can respawn in this dimension.
	 */
	@Override
	public boolean canRespawnHere() {
		return false;
	}

	@Override
	public boolean isSurfaceWorld() {
		return false;
	}

}
