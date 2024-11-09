package dev.exceptionteam.sakura.utils.builders

import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.math.Vec3d

class BlockHitResultBuilder {

    var mPos = Vec3d(0.0, 0.0, 0.0); private set
    var mSide = Direction.UP; private set
    var mBlockPos = BlockPos(0, 0, 0); private set
    var mInsideBlock = false; private set

    fun pos(p: Vec3d): BlockHitResultBuilder {
        mPos = p
        return this
    }

    fun pos(x: Double, y: Double, z: Double): BlockHitResultBuilder {
        mPos = Vec3d(x, y, z)
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
