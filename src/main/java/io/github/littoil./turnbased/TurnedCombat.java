package io.github.littoil.turnbased;

import io.github.littoil.turnbased.events.EventHandler;
import io.github.littoil.turnbased.events.TurnBasedEvent;
import io.github.littoil.turnbased.events.TurningEntityEvent;
import io.github.littoil.turnbased.gui.GuiTurnBased;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TurnedCombat {
	private final int initialDimention;
	private TurningEntity side1;
	private TurningEntity side2;

	//private WorldServer turnedWorld;
	private TurnedCombat(EntityLivingBase side1, EntityLivingBase side2) {
		initialDimention = side1.dimension;
		this.side1 = new TurningEntity(side1);
		this.side2 = new TurningEntity(side2);
		TurnBased.EVENT_BUS.register(this);
		MinecraftForge.EVENT_BUS.register(this);
	}

	public static TurnedCombat newTurnedCombat(EntityLivingBase side1, EntityLivingBase side2) {
		TurnedCombat turnedCombat = new TurnedCombat(side1, side2);
		TurnBased.getLogger().info(turnedCombat);
		return turnedCombat;
	}

	public TurningEntity getSide1() {
		return side1;
	}

	public TurningEntity getSide2() {
		return side2;
	}

	@Override
	public String toString() {
		return side1.getEntity().getDisplayName().getUnformattedText() + " vs \"" +
				side2.getEntity().getName();
	}

	public void init(float firstStrikeAmount) {
		TurnBased.EVENT_BUS.post(new TurnBasedEvent.StartTurnBasedEvent(this));
		this.transformToTurned();
		this.getSide2().attackMe(this.getSide1(), firstStrikeAmount);
	}

	public void run() {
		//this.end();
	}

	public boolean allEnemiesDead() {
		return side2.isDead();
	}

	public void transformToTurned() {
		TurnBased.getLogger().info("Activating Turned Based Combat!");
		FMLCommonHandler.instance().getMinecraftServerInstance().getWorld(8748).loadedEntityList.clear();
		side1.teleportTo(8748);
		side2.teleportTo(8748);
		//side1.getEntity().changeDimension(8748);
		//side2.getEntity().changeDimension(8748);
		//turnedWorld = new WorldServer(FMLCommonHandler.instance().getMinecraftServerInstance(), new LitSaveHandler(), FMLCommonHandler.instance().getMinecraftServerInstance().getEntityWorld().getWorldInfo(), 0, FMLCommonHandler.instance().getMinecraftServerInstance().profiler);
		//turnedWorld.spawnEntity(side1.getEntity());
		//turnedWorld.spawnEntity(side2.getEntity());
	}

	public void transformToWorld() {
		TurnBased.getLogger().info("Returning to world!");
		side1.teleportTo(this.initialDimention);
		side2.teleportTo(this.initialDimention);
		//side1.getEntity().changeDimension(this.initialDimention);
		//side2.getEntity().changeDimension(this.initialDimention);
		//turnedWorld.removeEntity(side1.getEntity());
		//turnedWorld.removeEntity(side2.getEntity());
		//turnedWorld = null;
	}

	public void end() {
		EventHandler.turnedCombatSet.remove(this);
		TurnBased.EVENT_BUS.post(new TurnBasedEvent.EndTurnBasedEvent(this));
		transformToWorld();
	}

	@SubscribeEvent
	public void ThingDeath(TurningEntityEvent.Death event) {
		if (event.deadEntity.getEntity().getUniqueID().equals(side1.getUuid())) {
			TurnBased.getLogger().info("side2 wins!");
			this.end();
		} else if (event.deadEntity.getEntity().getUniqueID().equals(side2.getUuid())) {
			TurnBased.getLogger().info("side1 wins!");
			this.end();
		}
	}

	@SubscribeEvent
	public void projectileHit(ProjectileImpactEvent event) {
		/*if (event.getRayTraceResult().typeOfHit.equals(RayTraceResult.Type.ENTITY))
		{
			if (event.getRayTraceResult().entityHit.getUniqueID().toString().equals(side1.getEntity().getUniqueID().toString()))
			{
				new TurningEntity(event.getEntity()).teleportTo(this.side1.getEntity().dimension);
			}
			if (event.getRayTraceResult().entityHit.getUniqueID().toString().equals(side2.getEntity().getUniqueID().toString()))
			{
				new TurningEntity(event.getEntity()).teleportTo(this.side2.getEntity().dimension);
			}
		}*/
	}

	@SideOnly(Side.CLIENT)
	private void ActivateTurnedGui() {
		Minecraft.getMinecraft().displayGuiScreen(new GuiTurnBased());
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof TurnedCombat)) {
			return false;
		}
		TurnedCombat turnedCombat1 = (TurnedCombat) obj;
		if (turnedCombat1.side1.getUuid().equals(this.side1.getUuid()) && turnedCombat1.side2.getUuid().equals(this.side2.getUuid())) {
			return true;
		}
		return turnedCombat1.side1.getUuid().equals(this.side2.getUuid()) && turnedCombat1.side2.getUuid().equals(this.side1.getUuid());
	}
}
