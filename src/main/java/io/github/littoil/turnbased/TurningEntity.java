package io.github.littoil.turnbased;

import io.github.littoil.turnbased.events.TurningEntityEvent;
import io.github.littoil.turnbased.world.LitTeleporter;
import io.github.littoil.turnbased.world.LitWorldMethods;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.ITeleporter;
import net.minecraftforge.fml.common.FMLCommonHandler;

import java.util.UUID;

public class TurningEntity {
	private final double initialXVal;
	private final double initialYVal;
	private final double initialZVal;
	private Entity entity;
	private UUID uuid;

	public TurningEntity(Entity entity) {
		this.entity = entity;
		this.uuid = this.entity.getUniqueID();
		this.initialXVal = this.entity.posX;
		this.initialYVal = this.entity.posY;
		this.initialZVal = this.entity.posZ;
	}

	@Override
	public String toString() {
		return "Turning Entity " + entity.getName() + " (" + entity.getUniqueID() + ")";
	}

	public Entity getEntity() {
		return entity;
	}

	public UUID getUuid() {
		return uuid;
	}

	public void attackMe(TurningEntity source, float amount) {
		if (source.entity instanceof EntityPlayer) {
			//attackMe(DamageSource.causePlayerDamage((EntityPlayer) source.entity), amount);
			((EntityPlayer) source.entity).attackTargetEntityWithCurrentItem(this.entity);
		} else if (source.entity instanceof EntityLivingBase) {
			//attackMe(DamageSource.causeMobDamage((EntityLivingBase) source.entity), amount);
			((EntityLivingBase) source.entity).attackEntityAsMob(this.entity);
		} else {
			TurnBased.getLogger().warn("Can't attack nonliving entity! (Out)");
		}
	}

	public void killMe(EntityLivingBase from) {
		this.entity.onKillEntity(from);
		TurnBased.EVENT_BUS.post(new TurningEntityEvent.Death(this));
	}

	public boolean isDead() {
		return this.entity.isDead;
	}

	private void logTeleport(int formerDimension, int laterDimension) {
		MinecraftServer minecraftServer = FMLCommonHandler.instance().getMinecraftServerInstance();
		WorldServer oldWorld = minecraftServer.getWorld(formerDimension);
		WorldServer newWorld = minecraftServer.getWorld(laterDimension);
		if (LitWorldMethods.containsEntity(this.uuid, oldWorld)) {
			TurnBased.getLogger().debug("Entity \"" + this.uuid.toString() + "\" does exist in old dimension!");
		} else {
			TurnBased.getLogger().debug("Entity \"" + this.uuid.toString() + "\" does not exist in old dimension!");
		}

		if (LitWorldMethods.containsEntity(this.uuid, newWorld)) {
			TurnBased.getLogger().debug("Entity \"" + this.uuid.toString() + "\" does exist in new dimension!");
		} else {
			TurnBased.getLogger().debug("Entity \"" + this.uuid.toString() + "\" does not exist in new dimension!");
		}
	}

	public void teleportTo(int laterDimension) {
		MinecraftServer minecraftServer = FMLCommonHandler.instance().getMinecraftServerInstance();
		int formerDimension = this.entity.dimension;
		WorldServer oldWorld = minecraftServer.getWorld(this.entity.dimension);
		WorldServer newWorld = minecraftServer.getWorld(laterDimension);
		ITeleporter teleporter = new LitTeleporter(initialXVal, (laterDimension == 8748) ? 4.0 : initialYVal, initialZVal);

		logTeleport(formerDimension, laterDimension);

		TurnBased.getLogger().debug("Teleporting...");
		boolean dead = this.entity.isDead;
		this.entity.isDead = false;
		Entity oldEntity = this.entity;
		Entity newEntity = oldEntity.changeDimension(laterDimension, teleporter);
		oldEntity.isDead = dead;
		newEntity.isDead = dead;

		logTeleport(formerDimension, laterDimension);

		if (LitWorldMethods.containsEntity(this.uuid, oldWorld)) {
			oldWorld.removeEntityDangerously(oldEntity);
			TurnBased.getLogger().debug("Removed entity \"" + this.uuid.toString() + "\" from old dimension");
		}

		logTeleport(formerDimension, laterDimension);

		TurnBased.getLogger().info("");
		this.entity = newEntity;
		oldWorld.tick();
		newWorld.tick();
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof TurningEntity)) {
			return false;
		} else {
			TurningEntity turningEntity = (TurningEntity) obj;
			if (turningEntity.uuid.equals(this.uuid)) {
				return turningEntity.isDead() == this.isDead();
			}
		}
		return false;
	}
}