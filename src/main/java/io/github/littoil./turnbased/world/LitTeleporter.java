package io.github.littoil.turnbased.world;

import io.github.littoil.turnbased.TurnBased;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ITeleporter;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayDeque;
import java.util.Queue;

public class LitTeleporter implements ITeleporter {
	private final Queue<Entity> teleportQueue = new ArrayDeque<>();

	// Cords before teleportation to turn-based-dimension
	private final double xVal;
	private final double yVal;
	private final double zVal;

	public LitTeleporter(double xVal, double yVal, double zVal) {
		this.xVal = xVal;
		this.yVal = yVal;
		this.zVal = zVal;
		try {
			MinecraftForge.EVENT_BUS.register(this);
		} catch (LinkageError e)
		{
			try {
				Thread.sleep(1000);
			} catch (Throwable t)
			{
				t.printStackTrace();
			}
			MinecraftForge.EVENT_BUS.register(this);
		}

	}

	@SubscribeEvent
	public void Tick(TickEvent.WorldTickEvent event)
	{
		if (event.phase.equals(TickEvent.WorldTickEvent.Phase.END))
		{
			teleportAllInQueue();
		}
	}

	@Override
	public void placeEntity(World world, Entity entityIn, float rotationYaw) {
		teleportQueue.offer(entityIn);
	}

	public void teleportAllInQueue()
	{
		while (!teleportQueue.isEmpty())
		{
			Entity entity = teleportQueue.poll();
			if (entity != null)
			{
				placeEntityUnsafe(entity);
			}
			else
			{
				TurnBased.getLogger().warn("Found null entity in queue!");
			}
		}
	}

	public void placeEntityUnsafe(Entity entityIn)
	{
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
