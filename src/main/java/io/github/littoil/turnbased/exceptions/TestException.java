package io.github.littoil.turnbased.exceptions;

import io.github.littoil.turnbased.TurnBased;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiErrorScreen;
import net.minecraftforge.fml.client.CustomModLoadingErrorDisplayException;

public class TestException extends CustomModLoadingErrorDisplayException {

	public TestException() {
	}

	public TestException(String message, Throwable cause) {
		super(message, cause);
	}

	@Override
	public void initGui(GuiErrorScreen errorScreen, FontRenderer fontRenderer) {

	}

	@Override
	public void drawScreen(GuiErrorScreen errorScreen, FontRenderer fontRenderer, int mouseRelX, int mouseRelY, float tickTime) {
		errorScreen.drawCenteredString(fontRenderer, "ERROR for Mod \"" + TurnBased.class.getName() + "\": " + this.getLocalizedMessage(), errorScreen.width / 2, 90, 16777215);
		errorScreen.drawCenteredString(fontRenderer, this.getCause().getLocalizedMessage(), errorScreen.width / 2, 110, 16777215);
	}
}
