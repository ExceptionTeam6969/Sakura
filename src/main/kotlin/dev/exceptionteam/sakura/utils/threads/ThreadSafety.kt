package dev.exceptionteam.sakura.utils.threads

import dev.exceptionteam.sakura.events.SafeClientEvent
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

@OptIn(ExperimentalContracts::class)
inline fun <R> runSafe(block: SafeClientEvent.() -> R): R? {
    contract {
        callsInPlace(block, InvocationKind.AT_MOST_ONCE)
    }

    val instance = SafeClientEvent.instance
    return if (instance != null) {
        block.invoke(instance)
    } else {
        null
    }
}

suspend fun <R> runSafeSuspend(block: suspend SafeClientEvent.() -> R): R? {
    return SafeClientEvent.instance?.let { block(it) }
}