package com.lightman210567.testingmod;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class ContainerBlock extends Block implements EntityBlock {
    public ContainerBlock (BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new ContainerEntity(blockPos, blockState);
    }

    @Override
    public ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level,
                                           BlockPos pos, Player player, InteractionHand hand,
                                           BlockHitResult hitResult) {
        if (level.getBlockEntity(pos) instanceof ContainerEntity containerEntity) {
            Item itemInContainer = containerEntity.inventory.getStackInSlot(0).getItem();
            Item handItem =  stack.getItem();

            if (handItem.getDescriptionId().equals(itemInContainer.getDescriptionId())) {
                if (containerEntity.inventory.getStackInSlot(0).getCount() < ContainerEntity.STACK_SIZE) {
                    containerEntity.inventory.insertItem(0, stack.copy(), false);
                    stack.shrink(1);
                    level.playSound(player, pos, SoundEvents.AMETHYST_CLUSTER_BREAK, SoundSource.BLOCKS, 1.0F, 1.0F);
                }
            } else if (!containerEntity.inventory.getStackInSlot(0).isEmpty() && !stack.isEmpty()) {
                if (containerEntity.inventory.getStackInSlot(0).getCount() < ContainerEntity.STACK_SIZE) {
                    containerEntity.inventory.insertItem(0, stack.copy(), false);
                    stack.shrink(1);
                    level.playSound(player, pos, SoundEvents.AMETHYST_CLUSTER_BREAK, SoundSource.BLOCKS, 1.0F, 1.0F);
                }
            } else {
                ItemStack containerStack = containerEntity.inventory.extractItem(1, 1, false);
                player.setItemInHand(InteractionHand.MAIN_HAND, containerStack);
            }
        }
    return ItemInteractionResult.SUCCESS;
    }
}
//        if (level.getBlockEntity(pos) instanceof ContainerEntity containerEntity) {
//            if (containerEntity.inventory.getStackInSlot(0).isEmpty() && !stack.isEmpty()) {
//                // check if the items are the same
//                if (containerEntity.inventory.getStackInSlot(0).getItem() == stack.getItem()) {
//                    containerEntity.inventory.insertItem(0, stack.copy(), false);
//                    stack.shrink(1);
//                    level.playSound(player, pos, SoundEvents.AMETHYST_CLUSTER_BREAK, SoundSource.BLOCKS, 1.0F, 1.0F);
//                }
//            // either the stack is empty, or the item stored item is the same as the stack
//            } else if (stack.isEmpty() || stack.getItem() == containerEntity.inventory.getStackInSlot(0).getItem()) {
//                ItemStack containerStack = containerEntity.inventory.extractItem(0, 1, false);
//                player.setItemInHand(InteractionHand.MAIN_HAND, containerStack);
//            }
//        }
//        return ItemInteractionResult.SUCCESS;
//    }
