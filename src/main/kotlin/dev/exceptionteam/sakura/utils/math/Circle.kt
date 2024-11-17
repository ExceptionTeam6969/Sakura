package dev.exceptionteam.sakura.utils.math

fun drawCircleBresenham(radius: Int) {
    val centerX = 20
    val centerY = 10

    val canvasWidth = 40
    val canvasHeight = 20
    val canvas = Array(canvasHeight) { CharArray(canvasWidth) { ' ' } }

    var x = 0
    var y = radius
    var d = 3 - 2 * radius

    // 使用对称性绘制圆的八分之一
    while (x <= y) {
        drawSymmetricPoints(canvas, centerX, centerY, x, y)
        if (d <= 0) {
            d += 4 * x + 6
        } else {
            d += 4 * (x - y) + 10
            y--
        }
        x++
    }

    // 输出画布
    for (row in canvas) {
        println(row.concatToString())
    }
}

fun drawSymmetricPoints(canvas: Array<CharArray>, centerX: Int, centerY: Int, x: Int, y: Int) {
    val points = listOf(
        centerX + x to centerY + y,
        centerX - x to centerY + y,
        centerX + x to centerY - y,
        centerX - x to centerY - y,
        centerX + y to centerY + x,
        centerX - y to centerY + x,
        centerX + y to centerY - x,
        centerX - y to centerY - x
    )
    for ((px, py) in points) {
        if (py in canvas.indices && px in canvas[0].indices) {
            canvas[py][px] = '*'
        }
    }
}

fun main() {
    drawCircleBresenham(8)
}