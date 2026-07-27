package com.lightman210567.testingmod;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Containers;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;

import static com.lightman210567.testingmod.TestingMod.CONTAINER_ENTITY;

public class ContainerEntity extends BlockEntity {
    public static final int SLOT_COUNT = 1;
    public static final int STACK_SIZE = 64;

    public final ItemStackHandler inventory = new ItemStackHandler(SLOT_COUNT) {
      @Override
      protected int getStackLimit(int slot, ItemStack stack) {
        return STACK_SIZE;
      }

      @Override
      protected void onContentsChanged(int slot) {
        setChanged();
        if (!level.isClientSide) {
            level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
        }
      }
    };

    // not required
    public void clearContents() {
        for (int i = 0; i < SLOT_COUNT; i++) {
            inventory.setStackInSlot(i, ItemStack.EMPTY);
        }
    }

    public void drops() {
        SimpleContainer inv = new SimpleContainer(inventory.getSlots());
        for (int i = 0; i < inventory.getSlots(); i++) {
            inv.setItem(i, inventory.getStackInSlot(i));
        }

        Containers.dropContents(this.level, this.worldPosition, inv);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put("inventory", inventory.serializeNBT(registries));
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        inventory.deserializeNBT(registries, tag.getCompound("inventory"));
    }

    public ContainerEntity(BlockPos pos, BlockState state) {
        super(CONTAINER_ENTITY.get(), pos, state);
    }

    // TODO: IMPLEMENT SYNC ON THE CONTAINER ENTITY
    // there is a bug where you will be able to insert an item to a full inventory
    // after you have reloaded the save, due to syncing isses
    // see https://docs.neoforged.net/docs/1.21.1/blockentities/#syncing-on-block-update
}
