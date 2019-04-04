package io.github.littoil.turnbased.world;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;

import java.util.UUID;

public class LitWorldMethods {
	public static boolean containsEntity(UUID uuid, World world) {
		for (Entity entity1 : world.loadedEntityList) {
			if (uuid.equals(entity1.getUniqueID()))
				return true;
		}
		return false;
	}
}
