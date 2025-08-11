package com.magicscience.magicsciencemod.client.screen;

import com.magicscience.magicsciencemod.blocks.entity.MagicWorkbenchBlockEntity;
import com.magicscience.magicsciencemod.client.menu.MagicWorkbenchMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class MagicWorkbenchScreen extends AbstractContainerScreen<MagicWorkbenchMenu> {

    private static final ResourceLocation TEXTURE =
        new ResourceLocation("magicscience", "textures/gui/magic_workbench.png");

    public MagicWorkbenchScreen(MagicWorkbenchMenu menu, Inventory inv, Component title) {
        super(menu, inv, title);
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTicks, int mouseX, int mouseY) {
        graphics.setColor(1f, 1f, 1f, 1f);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;
        graphics.blit(TEXTURE, x, y, 0, 0, imageWidth, imageHeight);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        renderBackground(graphics);
        super.render(graphics, mouseX, mouseY, partialTicks);
        renderTooltip(graphics, mouseX, mouseY);

        // Отладочный вывод уровня чернил
        MagicWorkbenchBlockEntity blockEntity = this.menu.getBlockEntity();
        int inkLevel = blockEntity.getInkLevel();
        int maxInkLevel = blockEntity.getMaxInkLevel();
        String inkText = "Ink Level: " + inkLevel + "/" + maxInkLevel;
        graphics.drawString(this.font, inkText, this.leftPos + 8, this.topPos + 20, 0xFFFFFF, false);
    }
}