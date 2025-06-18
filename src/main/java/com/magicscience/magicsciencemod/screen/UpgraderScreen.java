package com.magicscience.magicsciencemod.screen;


import com.magicscience.magicsciencemod.MagicScienceMod;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class UpgraderScreen extends AbstractContainerScreen<UpgraderMenu> {
    private static final ResourceLocation TEXTURE =
            new ResourceLocation(MagicScienceMod.MOD_ID, "textures/gui/upgrader_gui.png");

    public UpgraderScreen(UpgraderMenu menu, Inventory inventory, Component component) {
        super(menu, inventory, component);
    }

    @Override
    protected void init() {
        super.init();
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        guiGraphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);

        // Отрисовка прогресса энергии
        int energy = this.menu.getEnergy();
        int maxEnergy = this.menu.getMaxEnergy();
        int energyHeight = (int)(50 * ((float)energy / maxEnergy));
        guiGraphics.blit(TEXTURE,
                this.leftPos + 10, this.topPos + 16 + (50 - energyHeight),
                176, 50 - energyHeight,
                12, energyHeight);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, delta);
        renderTooltip(guiGraphics, mouseX, mouseY);
    }
}