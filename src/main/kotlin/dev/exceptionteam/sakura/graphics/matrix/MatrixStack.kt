package dev.exceptionteam.sakura.graphics.matrix

object MatrixStack {

    private var stack = ArrayDeque<MatrixEntry>()

    init {
        stack.add(MatrixEntry())
    }

    val isEmpty: Boolean get() = stack.isEmpty()

    fun push() {
        stack.add(stack.last())     // copy last matrix
    }

    fun pop() {
        stack.removeLast()          // remove last matrix
    }

    fun peek(): MatrixEntry = stack.last()

    fun translate(x: Float, y: Float, z: Float) {
        stack.last().positionMatrix.translate(x, y, z)
    }

    fun translate(x: Double, y: Double, z: Double) {
        translate(x.toFloat(), y.toFloat(), z.toFloat())
    }

    fun scale(x: Float, y: Float, z: Float) {
        stack.last().positionMatrix.scale(x, y, z)
    }

    fun scale(x: Double, y: Double, z: Double) {
        scale(x.toFloat(), y.toFloat(), z.toFloat())
    }

}