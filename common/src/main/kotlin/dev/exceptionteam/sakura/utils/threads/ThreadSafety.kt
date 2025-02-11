package dev.exceptionteam.sakura.utils.threads

import dev.exceptionteam.sakura.events.NonNullContext
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

@OptIn(ExperimentalContracts::class)
inline fun <R> runSafe(block: NonNullContext.() -> R): R? {
    contract {
        callsInPlace(block, InvocationKind.AT_MOST_ONCE)
    }

    val instance = NonNullContext.instance
    return if (instance != null) {
        block.invoke(instance)
    } else {
        null
    }
}

suspend fun <R> runSafeSuspend(block: suspend NonNullContext.() -> R): R? {
    return NonNullContext.instance?.let { block(it) }
}