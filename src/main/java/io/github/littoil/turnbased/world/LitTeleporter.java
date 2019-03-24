package io.github.littoil.turnbased.world;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ITeleporter;

public class LitTeleporter implements ITeleporter {

	// Cords before teleportation to turn-based-dimension
	private final double xVal;
	private final double yVal;
	private final double zVal;

	public LitTeleporter(double xVal, double yVal, double zVal) {
		this.xVal = xVal;
		this.yVal = yVal;
		this.zVal = zVal;
	}

	@Override
	public void placeEntity(World world, Entity entityIn, float rotationYaw) {
		if (entityIn instanceof EntityPlayerMP) {
			((EntityPlayerMP) entityIn).connection.setPlayerLocation(xVal, yVal, zVal, entityIn.rotationYaw, entityIn.rotationPitch);
		} else {
			entityIn.setLocationAndAngles(xVal, yVal, zVal, entityIn.rotationYaw, entityIn.rotationPitch);
		}
	}

	@Override
	public boolean isVanilla() {
		return false;
	}
}
