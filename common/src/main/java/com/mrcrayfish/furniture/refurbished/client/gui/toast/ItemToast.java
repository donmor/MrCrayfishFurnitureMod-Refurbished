package com.mrcrayfish.furniture.refurbished.client.gui.toast;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.client.gui.components.toasts.ToastComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

/**
 * Author: MrCrayfish
 */
public class ItemToast implements Toast
{
    private final Component title;
    private final Component description;
    private final ItemStack icon;

    public ItemToast(Component title, Component description, ItemStack icon)
    {
        this.title = title;
        this.description = description;
        this.icon = icon;
    }

    @Override
    public Visibility render(GuiGraphics graphics, ToastComponent component, long time)
    {
        graphics.blit(TEXTURE, 0, 0, 0, 32, this.width(), this.height());
        graphics.drawString(component.getMinecraft().font, this.title, 30, 7, 0xFF500050, false);
        graphics.drawString(component.getMinecraft().font, this.description, 30, 18, 0xFF000000, false);
        graphics.renderFakeItem(this.icon, 8, 8);
        return time < 5000 ? Visibility.SHOW : Visibility.HIDE;
    }
}
