package io.github.codetoil.turnbased.events;

import io.github.codetoil.turnbased.TurnBased;
import io.github.codetoil.turnbased.TurnedCombat;
import io.github.codetoil.turnbased.TurningEntity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.logging.log4j.Logger;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class EventHandler {

	public static Set<TurnedCombat> turnedCombatSet = new HashSet<TurnedCombat>();
	private static Logger logger = TurnBased.getLogger();

	@SubscribeEvent
	public static void LivingDeath(LivingDeathEvent event) {
		Iterator<TurnedCombat> iterator = turnedCombatSet.iterator();
		while (iterator.hasNext()) {
			iterator.next().ThingDeath(new TurningEntityEvent.Death(new TurningEntity(event.getEntityLiving())));
		}
	}

	@SubscribeEvent
	public static void LivingDamage(LivingDamageEvent event) {
		if (event.getSource().getTrueSource() instanceof EntityLivingBase) {
			float amount = event.getAmount();
			if (event.getEntity().dimension != 8748) // NOT IN PVP DIMENSION
			{
				event.setCanceled(true);
				TurnedCombat turnedCombat = TurnedCombat.newTurnedCombat((EntityLivingBase) event.getSource().getTrueSource(), event.getEntityLiving());
				turnedCombatSet.add(turnedCombat);
				turnedCombat.init(amount);
				turnedCombat.run();
			}
		}
	}

	@SubscribeEvent
	public static void ProjectileHit(ProjectileImpactEvent event)
	{
		if (event.getRayTraceResult().typeOfHit.equals(RayTraceResult.Type.ENTITY))
		{
			//event.getRayTraceResult().
			if (event.getEntity().dimension != 8748) // NOT IN PVP DIMENSION
			{
				event.getEntity().isDead = false;
				new TurningEntity(event.getEntity()).teleportTo(8748);
			}
		}
	}

	/*@SubscribeEvent(priority = EventPriority.HIGH)
	public void LoadWorld(WorldEvent.Load event)
	{
		if (event.getWorld().provider instanceof WorldProviderTurnBased)
		{
			resetWorld(event.getWorld());
		}
	}*/

	/*public static void resetWorld(World world) {
		File folder = new File(world.getSaveHandler().getWorldDirectory(), world.provider.getSaveFolder());
		if (folder.exists()) {
			deleteFolderOrFolder(folder);
		}
		TurnBased.getLogger().warn("Saving world");
		IChunkProvider iChunkProvider = world.getChunkProvider();
		if (iChunkProvider instanceof ChunkProviderServer) {
			((ChunkProviderServer) iChunkProvider).id2ChunkMap.clear();
		}
		world.unloadEntities(world.loadedEntityList);
		world.loadedTileEntityList.forEach(tileEntity -> {
			world.removeTileEntity(tileEntity.getPos());
		});
	}

	public static void deleteFolderOrFolder(File thing) {
		try {
			if (thing.isDirectory()) {
				for (File file : thing.listFiles()) {
					deleteFolderOrFolder(file);
				}
			} else {
				if (!thing.delete()) {
					TurnBased.getLogger().error("ERROR DELETING FILE " + thing.toString());
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace(System.err);
		}

	}

	@SubscribeEvent(priority = EventPriority.HIGH)
	public void UnloadWorld(WorldEvent.Unload event) {
		if (event.getWorld().provider instanceof WorldProviderTurnBased) {
			resetWorld(event.getWorld());
		}
	}

	@SubscribeEvent(priority = EventPriority.HIGH)
	public void SaveWorld(WorldEvent.Save event) {
		if (event.getWorld().provider instanceof WorldProviderTurnBased) {
			resetWorld(event.getWorld());
		}
	}*/
	/*private static boolean included(EntityLivingBase e1, EntityLivingBase e2)
	{
		TurnedCombat reference = TurnedCombat.newTurnedCombat(e1, e2);
		for (TurnedCombat turnedCombat : turnedCombatSet) {
			if (turnedCombat.equals(reference))
			{
				return true;
			}
		}
		return false;
	}

	private static TurnedCombat get(EntityLivingBase e1, EntityLivingBase e2)
	{
		TurnedCombat reference = TurnedCombat.newTurnedCombat(e1, e2);
		for (TurnedCombat turnedCombat : turnedCombatSet) {
			if (turnedCombat.equals(reference)) {
				return turnedCombat;
			}
		}
		return null;
	}*/

}
