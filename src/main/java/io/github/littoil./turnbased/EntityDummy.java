package io.github.littoil.turnbased;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class EntityDummy extends Entity {
	public EntityDummy(World world) {
		super(world);
	}

	@Override
	protected void entityInit() {
		TurnBased.getLogger().info("Initializing dummy entity");
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound compound) {
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound compound) {
	}
}
