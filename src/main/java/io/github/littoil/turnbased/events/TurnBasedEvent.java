package io.github.littoil.turnbased.events;

import io.github.littoil.turnbased.TurnedCombat;
import net.minecraftforge.fml.common.eventhandler.Event;

public class TurnBasedEvent {

	public static class StartTurnBasedEvent extends Event {
		public final TurnedCombat turnedCombat;

		public StartTurnBasedEvent(TurnedCombat turnedCombat) {
			this.turnedCombat = turnedCombat;
		}
	}

	public static class EndTurnBasedEvent extends Event {
		public final TurnedCombat turnedCombat;

		public EndTurnBasedEvent(TurnedCombat turnedCombat) {
			this.turnedCombat = turnedCombat;
		}
	}
}
