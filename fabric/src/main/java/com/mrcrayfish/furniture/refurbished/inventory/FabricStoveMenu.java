package com.mrcrayfish.furniture.refurbished.inventory;

import com.mrcrayfish.furniture.refurbished.blockentity.IPowerSwitch;
import com.mrcrayfish.furniture.refurbished.blockentity.StoveBlockEntity;
import com.mrcrayfish.furniture.refurbished.core.ModMenuTypes;
import com.mrcrayfish.furniture.refurbished.core.ModRecipeBookTypes;
import com.mrcrayfish.furniture.refurbished.core.ModRecipeTypes;
import com.mrcrayfish.furniture.refurbished.inventory.slot.ResultSlot;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.RecipeBookType;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.StackedContentsCompatible;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;

/**
 * Author: MrCrayfish
 */
public class FabricStoveMenu extends SimpleContainerMenu implements IPowerSwitchMenu, IElectricityMenu, IContainerHolder, IBakingMenu
{
    private final ContainerData data;
    private final Level level;

    public FabricStoveMenu(int windowId, Inventory playerInventory)
    {
        this(windowId, playerInventory, new SimpleContainer(6), new SimpleContainerData(8));
    }

    public FabricStoveMenu(int windowId, Inventory playerInventory, Container container, ContainerData data)
    {
        super(ModMenuTypes.STOVE.get(), windowId, container);
        checkContainerSize(container, 6);
        checkContainerDataCount(data, 8);
        container.startOpen(playerInventory.player);
        this.data = data;
        this.level = playerInventory.player.level();
        this.addContainerSlots(85, 18, 3, 1, 0);
        this.addContainerSlots(85, 54, 3, 1, 3, ResultSlot::new);
        this.addPlayerInventorySlots(8, 84, playerInventory);
        this.addDataSlots(data);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int slotIndex)
    {
        ItemStack stack = ItemStack.EMPTY;
        Slot slot = this.slots.get(slotIndex);
        if(slot.hasItem())
        {
            ItemStack slotStack = slot.getItem();
            stack = slotStack.copy();
            if(slotIndex < this.container.getContainerSize())
            {
                if(!this.moveItemStackTo(slotStack, this.container.getContainerSize(), this.slots.size(), true))
                {
                    return ItemStack.EMPTY;
                }
            }
            else if(this.isRecipe(slotStack))
            {
                if(!this.moveItemStackTo(slotStack, 0, 3, false))
                {
                    return ItemStack.EMPTY;
                }
            }
            else if(slotIndex < this.container.getContainerSize() + 27)
            {
                if(!this.moveItemStackTo(slotStack, this.container.getContainerSize() + 27, this.slots.size(), false))
                {
                    return ItemStack.EMPTY;
                }
            }
            else if(!this.moveItemStackTo(slotStack, this.container.getContainerSize(), this.slots.size() - 9, false))
            {
                return ItemStack.EMPTY;
            }

            if(slotStack.isEmpty())
            {
                slot.setByPlayer(ItemStack.EMPTY);
            }
            else
            {
                slot.setChanged();
            }
        }
        return stack;
    }

    private boolean isRecipe(ItemStack stack)
    {
        return this.level.getRecipeManager().getRecipeFor(ModRecipeTypes.OVEN_BAKING.get(), new SingleRecipeInput(stack), this.level).isPresent();
    }

    @Override
    public int getBakingProgress(int index)
    {
        return switch(index) {
            case 0 -> this.data.get(StoveBlockEntity.DATA_PROGRESS_1);
            case 1 -> this.data.get(StoveBlockEntity.DATA_PROGRESS_2);
            case 2 -> this.data.get(StoveBlockEntity.DATA_PROGRESS_3);
            default -> 0;
        };
    }

    @Override
    public int getTotalBakingProgress(int index)
    {
        return switch(index) {
            case 0 -> this.data.get(StoveBlockEntity.DATA_TOTAL_PROGRESS_1);
            case 1 -> this.data.get(StoveBlockEntity.DATA_TOTAL_PROGRESS_2);
            case 2 -> this.data.get(StoveBlockEntity.DATA_TOTAL_PROGRESS_3);
            default -> 0;
        };
    }

    @Override
    public boolean isPowered()
    {
        return this.data.get(StoveBlockEntity.DATA_POWERED) != 0;
    }

    @Override
    public boolean isEnabled()
    {
        return this.data.get(StoveBlockEntity.DATA_ENABLED) != 0;
    }

    @Override
    public void toggle()
    {
        if(this.container instanceof IPowerSwitch powerSwitch)
        {
            powerSwitch.togglePower();
        }
    }

    @Override
    public Container container()
    {
        return this.container;
    }
}
