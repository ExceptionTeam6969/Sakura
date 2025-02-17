package dev.exceptionteam.sakura.utils.player

import dev.exceptionteam.sakura.events.NonNullContext
import dev.exceptionteam.sakura.managers.impl.HotbarManager.SwitchMode
import dev.exceptionteam.sakura.managers.impl.HotbarManager.switch
import dev.exceptionteam.sakura.managers.impl.RotationManager.addRotation
import dev.exceptionteam.sakura.utils.builders.BlockHitResultBuilder
import dev.exceptionteam.sakura.utils.math.RotationUtils.getRotationTo
import dev.exceptionteam.sakura.utils.player.InventoryUtils.findBlockInHotbar
import dev.exceptionteam.sakura.utils.player.InventoryUtils.findItemInHotbar
import dev.exceptionteam.sakura.utils.world.BlockUtils
import dev.exceptionteam.sakura.utils.world.BlockUtils.getBestSide
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.network.protocol.game.ServerboundInteractPacket
import net.minecraft.network.protocol.game.ServerboundUseItemOnPacket
import net.minecraft.world.InteractionHand
import net.minecraft.world.entity.Entity
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block

object InteractionUtils {

    /**
     * Places a block in the world and switches to the specified hotbar slot.
     * @param pos The position where the block should be placed.
     * @param block The block that should be placed.
     * @param switchMode The mode that should be used to switch to the hotbar slot.
     * @param swing Whether the player should swing his arm.
     * @param shouldRotate Whether the player should be rotated to the block's direction.
     * @param priority The priority of the rotation.
     * @param hand The hand that should be used to place the block.
     */
    fun NonNullContext.place(
        pos: BlockPos,
        block: Block,
        switchMode: SwitchMode,
        swing: Boolean = true,
        shouldRotate: Boolean = true,
        priority: Int = 0,
        hand: InteractionHand = InteractionHand.MAIN_HAND
    ) {
        val dir = getBestSide(pos)
        val rotationAngle = getRotationTo(pos, dir)

        addRotation(rotationAngle, priority, shouldRotate) {
            val blockSlot = findBlockInHotbar(block) ?: return@addRotation

            switch(switchMode, blockSlot) {
                place(pos, dir, hand)
                if (swing) player.swing(hand)
            }
        }
    }

    fun NonNullContext.useItem(
        pos: BlockPos,
        item: Item,
        switchMode: SwitchMode,
        swing: Boolean = true,
        shouldRotate: Boolean = true,
        priority: Int = 0,
        hand: InteractionHand = InteractionHand.MAIN_HAND
    ) {
        val dir = getBestSide(pos)
        val rotationAngle = getRotationTo(pos, dir)

        addRotation(rotationAngle, priority, shouldRotate) {
            val blockSlot = findItemInHotbar(item) ?: return@addRotation

            switch(switchMode, blockSlot) {
                useItem(pos, dir, hand)
                if (swing) player.swing(hand)
            }
        }
    }

    private fun NonNullContext.useItem(pos: BlockPos, dir: Direction? = null, hand: InteractionHand = InteractionHand.MAIN_HAND) {
        val side = dir ?: Direction.UP

        val blockHit = BlockHitResultBuilder()
            .pos(BlockUtils.getVecPos(pos, dir))
            .blockPos(pos)
            .side(side)
            .build()

        connection.send(ServerboundUseItemOnPacket(
            hand, blockHit, 0                       // fixme: incorrect sequence id
        ))
    }

    private fun NonNullContext.place(pos: BlockPos, dir: Direction? = null, hand: InteractionHand = InteractionHand.MAIN_HAND) {
        val side = dir ?: Direction.UP

        val blockHit = BlockHitResultBuilder()
            .pos(BlockUtils.getVecPos(pos, dir))
            .blockPos(pos) //.below()
            .side(side)
            .build()

        connection.send(ServerboundUseItemOnPacket(
            hand, blockHit, 0                       // fixme: incorrect sequence id
        ))
    }

    /**
     * Attacks an entity and switches to the main hand.
     * @param target The entity that should be attacked.
     * @param shouldRotate Whether the player should be rotated to the entity's direction.
     * @param swing Whether the player should swing his arm.
     * @param priority The priority of the rotation.
     */
    fun NonNullContext.attack(
        target: Entity,
        shouldRotate: Boolean = true,
        swing: Boolean = true,
        priority: Int = 0,
        hand: InteractionHand = InteractionHand.MAIN_HAND
    ) {
        val rotAngle = getRotationTo(target.position())
        addRotation(rotAngle, priority, shouldRotate) {
            connection.send(ServerboundInteractPacket.createAttackPacket(target, player.isShiftKeyDown))
            if (swing) player.swing(hand)
        }
    }

}