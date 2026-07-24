package com.example.legendarytiers.screen;

import com.example.legendarytiers.LegendaryTiers;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.List;

public class RunicTableScreen extends AbstractContainerScreen<RunicTableMenu> {
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath(LegendaryTiers.MOD_ID, "textures/gui/runic_table.png");

    private static final int TEXTURE_WIDTH = 256;
    private static final int TEXTURE_HEIGHT = 310;
    private static final int BUTTON_X = 195;
    private static final int BUTTON_Y = 176;
    private static final int BUTTON_X2 = 152;
    private static final int BUTTON_Y2 = 176;
    private static final int BUTTON_WIDTH = 24;
    private static final int BUTTON_HEIGHT = 20;
    private static final int BUTTON_WIDTH2 = 23;
    private static final int BUTTON_HEIGHT2 = 20;
    private static final int BUTTON_TEX_X = 195;
    private static final int BUTTON_TEX_Y = 176;
    private static final int BUTTON_TEX_X2 = 152;
    private static final int BUTTON_TEX_Y2 = 176;
    private boolean buttonHovered = false;
    private boolean buttonHovered2 = false;

    public RunicTableScreen(RunicTableMenu menu, Inventory inv, Component title) {
        super(menu, inv, title);
        this.imageWidth = TEXTURE_WIDTH;
        this.imageHeight = TEXTURE_HEIGHT;
    }

    @Override
    protected void init() {
        super.init();
        // Прозрачная кнопка только для обработки кликов
        Button invisibleButton = Button.builder(Component.empty(), button -> {
                    if (this.minecraft != null && this.minecraft.gameMode != null) {
                        this.minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, 0);
                    }
                }).pos(this.leftPos + BUTTON_X, this.topPos + BUTTON_Y)
                .size(BUTTON_WIDTH, BUTTON_HEIGHT)
                .build();
        invisibleButton.setAlpha(0.0F);
        this.addRenderableWidget(invisibleButton);

        // Прозрачная кнопка реролла бонусов
        Button rerollButton = Button.builder(Component.empty(), button -> {
                    if (this.minecraft != null && this.minecraft.gameMode != null) {
                        this.minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, 1);
                    }
                }).pos(this.leftPos + BUTTON_X2, this.topPos + BUTTON_Y2)
                .size(BUTTON_WIDTH2, BUTTON_HEIGHT2)
                .build();
        rerollButton.setAlpha(0.0F);
        this.addRenderableWidget(rerollButton);
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        // Рисуем весь фон GUI
        graphics.blit(TEXTURE, leftPos, topPos, 0, 0, TEXTURE_WIDTH, TEXTURE_HEIGHT, TEXTURE_WIDTH, TEXTURE_HEIGHT);

        // Кнопка 1
        int btn1X = this.leftPos + BUTTON_X;
        int btn1Y = this.topPos + BUTTON_Y;
        buttonHovered = isMouseOver(mouseX, mouseY, btn1X, btn1Y, BUTTON_WIDTH, BUTTON_HEIGHT);
        renderButton(graphics, btn1X, btn1Y, BUTTON_TEX_X, BUTTON_TEX_Y, BUTTON_WIDTH, BUTTON_HEIGHT, buttonHovered);

        // Кнопка 2
        int btn2X = this.leftPos + BUTTON_X2;
        int btn2Y = this.topPos + BUTTON_Y2;
        buttonHovered2 = isMouseOver(mouseX, mouseY, btn2X, btn2Y, BUTTON_WIDTH2, BUTTON_HEIGHT2);
        renderButton(graphics, btn2X, btn2Y, BUTTON_TEX_X2, BUTTON_TEX_Y2,BUTTON_WIDTH2, BUTTON_HEIGHT2, buttonHovered2);
    }

    private void renderButton(GuiGraphics graphics, int x, int y, int uoff, int voff, int width, int height, boolean hovered) {
        graphics.pose().pushPose();
        if (hovered) {
            float scale = 1.1f;
            float cx = x + width / 2.0f;
            float cy = y + height / 2.0f;
            graphics.pose().translate(cx, cy, 0);
            graphics.pose().scale(scale, scale, 1.0f);
            graphics.pose().translate(-cx, -cy, 0);
        }
        graphics.blit(TEXTURE, x, y, uoff, voff, width, height, TEXTURE_WIDTH, TEXTURE_HEIGHT);
        graphics.pose().popPose();
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        super.render(graphics, mouseX, mouseY, delta);
        this.renderTooltip(graphics, mouseX, mouseY);

        // Подсказки для пустых слотов
        for (int i = 0; i < 6; i++) {
            Slot slot = this.menu.getSlot(i);
            if (!slot.hasItem()) {
                if (isHovering(slot.x, slot.y, 16, 16, mouseX, mouseY)) {
                    String tooltip = switch (i) {
                        case 0 -> Component.translatable("tooltip.legendarytiers.slot_item").getString();
                        case 1 -> Component.translatable("tooltip.legendarytiers.slot_ink").getString();
                        case 2 -> Component.translatable("tooltip.legendarytiers.slot_stencil").getString();
                        case 3 -> Component.translatable("tooltip.legendarytiers.slot_catalyst").getString();
                        case 4 -> Component.translatable("tooltip.legendarytiers.slot_item").getString();
                        case 5 -> Component.translatable("tooltip.legendarytiers.slot_ink").getString();
                        default -> "";
                    };
                    if (!tooltip.isEmpty()) {
                        graphics.renderTooltip(font, List.of(Component.literal(tooltip).getVisualOrderText()), mouseX, mouseY);
                    }
                }
            }
        }

        // Подсказка для кнопки
        if (buttonHovered) {
            graphics.renderTooltip(font, Component.translatable("tooltip.legendarytiers.reforge_button"), mouseX, mouseY);
        }
        if (buttonHovered2) {
            graphics.renderTooltip(font, Component.translatable("tooltip.legendarytiers.reroll_button"), mouseX, mouseY);
        }
    }

    @Override
    protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
        // Пусто – скрываем названия
    }

    private boolean isMouseOver(double mouseX, double mouseY, int x, int y, int w, int h) {
        return mouseX >= x && mouseX <= x + w && mouseY >= y && mouseY <= y + h;
    }
}