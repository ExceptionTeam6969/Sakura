package dev.exceptionteam.sakura.utils.builders

import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.Vec3

class BlockHitResultBuilder {

    var mPos = Vec3(0.0, 0.0, 0.0); private set
    var mSide = Direction.UP; private set
    var mBlockPos = BlockPos(0, 0, 0); private set
    var mInsideBlock = false; private set

    fun pos(p: Vec3): BlockHitResultBuilder {
        mPos = p
        return this
    }

    fun pos(x: Double, y: Double, z: Double): BlockHitResultBuilder {
        mPos = Vec3(x, y, z)
        return this
    }

    fun side(s: Direction): BlockHitResultBuilder {
        mSide = s
        return this
    }

    fun blockPos(b: BlockPos): BlockHitResultBuilder {
        mBlockPos = b
        return this
    }

    fun blockPos(x: Int, y: Int, z: Int): BlockHitResultBuilder {
        mBlockPos = BlockPos(x, y, z)
        return this
    }

    fun insideBlock(i: Boolean): BlockHitResultBuilder {
        mInsideBlock = i
        return this
    }

    fun build(): BlockHitResult =
        BlockHitResult(mPos, mSide, mBlockPos, mInsideBlock)

}
