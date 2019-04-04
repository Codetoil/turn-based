package io.github.littoil.turnbased.events;

import io.github.littoil.turnbased.TurningEntity;
import net.minecraftforge.fml.common.eventhandler.Event;

public class TurningEntityEvent {
	public static class Attack extends Event {
		public final TurningEntity to;
		public final TurningEntity from;

		public Attack(TurningEntity to, TurningEntity from) {
			this.to = to;
			this.from = from;
		}
	}

	public static class Death extends Event {
		public final TurningEntity deadEntity;

		public Death(TurningEntity deadEntity) {
			this.deadEntity = deadEntity;
		}
	}
}
