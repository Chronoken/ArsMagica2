package am2.client.gui;

import java.io.IOException;

import org.lwjgl.opengl.GL11;

import am2.common.container.ContainerRuneBag;
import am2.common.container.InventoryRuneBag;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class GuiRuneBag extends GuiContainer{

	private static final ResourceLocation background = new ResourceLocation("arsmagica2", "textures/gui/rune_bag_gui.png");
	private static final ResourceLocation extras = new ResourceLocation("arsmagica2", "textures/gui/spellbookgui_2.png");

	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int i, int j){
		mc.renderEngine.bindTexture(background);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		int l = (width - xSize) / 2;
		int i1 = (height - ySize) / 2;
		drawTexturedModalRect(l, i1, 0, 0, xSize, ySize);
	}

	public GuiRuneBag(InventoryPlayer inventoryplayer, ItemStack spellBookStack, InventoryRuneBag inventorybag){
		super(new ContainerRuneBag(inventoryplayer, spellBookStack, inventorybag));
		xSize = 175;
		ySize = 140;
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2){
		mc.renderEngine.bindTexture(extras);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		//special slot
		int index = ((ContainerRuneBag)this.inventorySlots).specialSlotIndex - 32;
		int x = 8 + 18 * index;
		int y = 116;
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.5F);
		drawTexturedModalRect(x, y, 0, 20, 16, 16);
		GL11.glDisable(GL11.GL_BLEND);
	}

	@Override
	protected void keyTyped(char par1, int par2) throws IOException{
		if (!Character.isDigit(par1))
			super.keyTyped(par1, par2);
	}

}
