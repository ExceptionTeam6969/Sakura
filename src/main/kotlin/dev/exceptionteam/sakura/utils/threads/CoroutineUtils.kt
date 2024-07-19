package dev.exceptionteam.sakura.utils.threads

import kotlinx.coroutines.*
import net.minecraft.client.MinecraftClient
import net.minecraft.util.crash.CrashReport
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledThreadPoolExecutor
import kotlin.math.max

//Scopes
val defaultScope = CoroutineScope(Dispatchers.Default)
val IOScope = CoroutineScope(Dispatchers.IO)

inline fun runAsyncThread(crossinline task: () -> Unit) {
    val service = Executors.newScheduledThreadPool((Runtime.getRuntime().availableProcessors() / 2).coerceAtMost(2).coerceAtLeast(1))
    service.execute { task() }
}

object ConcurrentScope : CoroutineScope by CoroutineScope(concurrentContext) {
    val context = concurrentContext
}

object BackgroundScope : CoroutineScope by CoroutineScope(backgroundContext) {
    val pool = backgroundPool
    val context = backgroundContext
}

//Private Field
private val defaultContext =
    CoroutineName("<Sakura> Default") + Dispatchers.Default + CoroutineExceptionHandler { _, throwable ->
        MinecraftClient.getInstance().setCrashReportSupplier(CrashReport.create(throwable, "<Sakura> Default Scope"))
    }


@OptIn(ExperimentalCoroutinesApi::class)
private val concurrentContext = CoroutineName("<Sakura> Concurrent") + Dispatchers.Default.limitedParallelism(
    max(
        Runtime.getRuntime().availableProcessors() / 2,
        1
    )
) + CoroutineExceptionHandler { _, throwable ->
    MinecraftClient.getInstance().setCrashReportSupplier(CrashReport.create(throwable, "<Sakura> Concurrent Scope"))
}

suspend inline fun delay(timeMillis: Int) {
    delay(timeMillis.toLong())
}

private val backgroundPool =
    ScheduledThreadPoolExecutor(Runtime.getRuntime().availableProcessors(), CountingThreadFactory("<Sakura> Background") {
        isDaemon = true
        priority = 3
    })

private val backgroundContext =
    CoroutineName("<Sakura> Background") + backgroundPool.asCoroutineDispatcher() + CoroutineExceptionHandler { _, throwable ->
        MinecraftClient.getInstance().setCrashReportSupplier(CrashReport.create(throwable, "<Sakura> Background Scope"))
    }

inline val Job?.isActiveOrFalse get() = this?.isActive ?: false