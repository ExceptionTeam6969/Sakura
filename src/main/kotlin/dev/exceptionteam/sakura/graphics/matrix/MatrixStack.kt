package dev.exceptionteam.sakura.graphics.matrix

import org.joml.Matrix4f
import org.joml.Quaternionf
import org.joml.Vector3f

object MatrixStack {

    private var stack = ArrayDeque<MatrixEntry>()

    init {
        stack.add(MatrixEntry())
    }

    val isEmpty: Boolean get() = stack.size <= 1

    fun push() {
        stack.add(MatrixEntry(stack.last())) // copy last matrix
    }

    fun pop() {
        stack.removeLast() // remove last matrix
    }

    fun peek(): MatrixEntry = stack.last()

    fun translate(x: Float, y: Float, z: Float) {
        stack.last().positionMatrix.translate(x, y, z)
    }

    fun translate(x: Double, y: Double, z: Double) {
        translate(x.toFloat(), y.toFloat(), z.toFloat())
    }

    fun multiply(quaternion: Quaternionf) {
        stack.last().positionMatrix.rotate(quaternion)
    }

    fun scale(x: Float, y: Float, z: Float) {
        stack.last().positionMatrix.scale(x, y, z)
    }

    fun scale(x: Double, y: Double, z: Double) {
        scale(x.toFloat(), y.toFloat(), z.toFloat())
    }

    fun getPosition(x: Float, y: Float, z: Float): Vector3f {
        return stack.last().positionMatrix.transformPosition(x, y, z, Vector3f())
    }

    fun scope(block: MatrixStack.() -> Unit) {
        push()
        this.block()
        pop()
    }

    /**
     * Update the Model-View-Projection matrix of the last matrix in the stack.
     * @param matrix MVP matrix
     */
    fun updateMvpMatrix(matrix: Matrix4f) {
        stack.last().mvpMatrix.set(matrix)
    }

}