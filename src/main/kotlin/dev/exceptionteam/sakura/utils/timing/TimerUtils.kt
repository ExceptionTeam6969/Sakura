package dev.exceptionteam.sakura.utils.timing

/**
 * Created on 2/12/2023 by B312
 * Updated on 6/12/2024 by B312
 */
class TimerUtils(val timeUnit: Duration = Duration.Millisecond) {

    private var time = System.nanoTime()

    fun passed(interval: Int, timeUnit: Duration = this.timeUnit): Boolean {
        return System.nanoTime() - time > interval * timeUnit.multiplier
    }

    fun reset() {
        time = System.nanoTime()
    }

    fun passedAndReset(interval: Int, timeUnit: Duration = this.timeUnit): Boolean {
        val result = passed(interval, timeUnit)
        if (result) reset()
        return result
    }

    inline fun passedAndReset(interval: Int, timeUnit: Duration = this.timeUnit, block: () -> Unit): Boolean {
        val result = passed(interval, timeUnit)
        if (result) {
            reset()
            block()
        }
        return result
    }

    inline fun passedAndRun(
        interval: Int,
        timeUnit: Duration = this.timeUnit,
        reset: Boolean = false,
        block: () -> Unit
    ): Boolean {
        val result = passed(interval, timeUnit)
        if (result) {
            block()
            if (reset) reset()
        }
        return result
    }

    private var offset = 0L

    // Tick per second
    fun tps(tps: Int, block: () -> Unit) {
        val currentNanoTime = System.nanoTime()
        val delayNanoTime = ((1000000000.0 / tps).toLong() - offset).coerceAtLeast(0)
        val timeLapsed = currentNanoTime - time
        if (timeLapsed >= delayNanoTime) {
            offset = timeLapsed - delayNanoTime
            time = currentNanoTime
            block()
        }
    }

}

/**
 * Created on 2/12/2023 by B312
 */
enum class Duration(val multiplier: Long) {
    Nanosecond(1),
    Microsecond(1000),
    Millisecond(1000000),
    Second(1000000000),
    Minute(60000000000),
    Hour(3600000000000),
    Day(86400000000000),
    Week(604800000000000)
}