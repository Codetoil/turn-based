package io.github.codetoil.turnbased;

import io.github.codetoil.turnbased.events.EventHandler;
import io.github.codetoil.turnbased.world.WorldProviderTurnBased;
import net.minecraft.world.DimensionType;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.EventBus;
import org.apache.logging.log4j.Logger;

@Mod(modid = TurnBased.MODID, name = TurnBased.NAME, version = TurnBased.VERSION)
public class TurnBased {
	public static final String MODID = "turn_based";
	public static final String NAME = "Turn Based Combat";
	public static final String VERSION = "0.0";

	public static final EventBus EVENT_BUS = new EventBus();
	private static DimensionType turnBasedWorld;
	private static Logger logger;

	public TurnBased() {
	}

	public static DimensionType getTurnBasedWorld() {
		return turnBasedWorld;
	}

	public static boolean logBool(Object object) {
		TurnBased.getLogger().info(object);
		return true;
	}

	public static Logger getLogger() {
		return logger;
	}

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		logger = event.getModLog();
		registerForEventBus();
	}

	private void registerForEventBus() {
		MinecraftForge.EVENT_BUS.register(EventHandler.class);
	}

	@Mod.EventHandler
	public void init(FMLInitializationEvent event) {
		//(8748, "turn_based_combat_world", "_turnbased", WorldProviderTurnBased.class);
		turnBasedWorld = DimensionType.register("turn_based_combat_world", "_turnbased", 8748, WorldProviderTurnBased.class, false);
		DimensionManager.registerDimension(8748, turnBasedWorld);
		//logger.info("DIRT BLOCK >> {}", Blocks.DIRT.getRegistryName());
		//Just testing exceptions -> //throw new TestException("Test Exception", new Throwable("Why the f*** do I do this..."));
	}


}
