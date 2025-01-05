package dev.exceptionteam.sakura.utils.player

import dev.exceptionteam.sakura.events.NonNullContext
import dev.exceptionteam.sakura.managers.impl.HotbarManager.SwitchMode
import dev.exceptionteam.sakura.managers.impl.HotbarManager.switch
import dev.exceptionteam.sakura.managers.impl.RotationManager.addRotation
import dev.exceptionteam.sakura.utils.builders.BlockHitResultBuilder
import dev.exceptionteam.sakura.utils.math.RotationUtils.getRotationTo
import dev.exceptionteam.sakura.utils.player.InventoryUtils.findBlockInHotbar
import dev.exceptionteam.sakura.utils.world.BlockUtils
import dev.exceptionteam.sakura.utils.world.BlockUtils.getNeighbourSide
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.network.protocol.game.ServerboundUseItemOnPacket
import net.minecraft.world.InteractionHand
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
    fun NonNullContext.placeBlock(
        pos: BlockPos,
        block: Block,
        switchMode: SwitchMode,
        swing: Boolean = true,
        shouldRotate: Boolean = true,
        priority: Int = 0,
        hand: InteractionHand = InteractionHand.MAIN_HAND
    ) {
        val dir = getNeighbourSide(pos)
        val blockSlot = findBlockInHotbar(block) ?: return
        val rotationAngle = getRotationTo(pos, dir)

        addRotation(rotationAngle, priority, shouldRotate) {
            switch(switchMode, blockSlot) {
                place(pos, dir, hand)
                if (swing) player.swing(hand)
            }
        }
    }

    private fun NonNullContext.place(pos: BlockPos, dir: Direction? = null, hand: InteractionHand = InteractionHand.MAIN_HAND) {
        val side = dir ?: Direction.UP

        val blockHit = BlockHitResultBuilder()
            .pos(BlockUtils.getVecPos(pos, dir))
            .blockPos(pos.below())
            .side(side.opposite)
            .build()

        connection.send(ServerboundUseItemOnPacket(
            hand, blockHit, 0                       // fixme: incorrect sequence id
        ))
    }

}