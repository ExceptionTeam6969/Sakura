package dev.exceptionteam.sakura.events

import dev.exceptionteam.sakura.utils.threads.ConcurrentScope
import dev.exceptionteam.sakura.utils.threads.runSafe
import dev.exceptionteam.sakura.utils.threads.runSafeSuspend
import kotlinx.coroutines.launch


inline fun <reified E : Event> Any.nonNullListener(
    noinline function: NonNullContext.(E) -> Unit
) = listener(this, E::class.java, 0, false) { runSafe { function.invoke(this, it) } }

inline fun <reified E : Event> Any.nonNullListener(
    priority: Int,
    noinline function: NonNullContext.(E) -> Unit
) = listener(this, E::class.java, priority, false) { runSafe { function.invoke(this, it) } }

inline fun <reified E : Event> Any.nonNullListener(
    priority: Int,
    alwaysListening: Boolean,
    noinline function: NonNullContext.(E) -> Unit
) = listener(this, E::class.java, priority, alwaysListening) { runSafe { function.invoke(this, it) } }

inline fun <reified E : Event> Any.nonNullListener(
    alwaysListening: Boolean,
    noinline function: NonNullContext.(E) -> Unit
) = listener(this, E::class.java, 0, alwaysListening) { runSafe { function.invoke(this, it) } }

inline fun <reified E : Event>Any.nonNullConcurrentListener(
    alwaysListening: Boolean,
    noinline function: suspend NonNullContext.(E) -> Unit
) = concurrentListener(this, E::class.java, 0, alwaysListening) { runSafeSuspend { function.invoke(this, it) } }

inline fun <reified E : Event>Any.nonNullConcurrentListener(
    priority: Int,
    alwaysListening: Boolean,
    noinline function: suspend NonNullContext.(E) -> Unit
) = concurrentListener(this, E::class.java, priority, alwaysListening) { runSafeSuspend { function.invoke(this, it) } }

@JvmOverloads
@JvmSynthetic
inline fun <reified E : Event> Any.listener(
        priority: Int = 0,
        alwaysListening: Boolean = false,
        noinline function: (E) -> Unit
) = listener(this, E::class.java, priority, alwaysListening, function)

fun <E : Event> Any.listener(
        owner: Any,
        eventClass: Class<E>,
        priority: Int,
        alwaysListening: Boolean,
        function: (E) -> Unit
) {
    with(EventListener(owner, eventClass, EventBus.busID, priority, function)) {
        if (alwaysListening) EventBus.subscribe(this)
        else EventBus.register(owner, this)
    }
}

@JvmOverloads
@JvmSynthetic
inline fun <reified E : Event> Any.concurrentListener(
    priority: Int = 0,
    alwaysListening: Boolean = false,
    noinline function: (E) -> Unit
) = listener(this, E::class.java, priority, alwaysListening, function)

fun <E : Event> Any.concurrentListener(
    owner: Any,
    eventClass: Class<E>,
    priority: Int,
    alwaysListening: Boolean,
    function: suspend (E) -> Unit
) {
    with(EventListener(owner, eventClass, EventBus.busID, priority) { ConcurrentScope.launch { function.invoke(it) } }) {
        if (alwaysListening) EventBus.subscribe(this)
        else EventBus.register(owner, this)
    }
}

open class EventListener<E : Any>(
        owner: Any,
        val eventClass: Class<E>,
        val eventID: Int,
        val priority: Int,
        val function: (E) -> Unit
) {
    override fun equals(other: Any?): Boolean {
        return this === other
                || (other is EventListener<*>
                && other.eventClass == this.eventClass
                && other.eventID == this.eventID)
    }

    override fun hashCode(): Int {
        var result = eventClass.hashCode()
        result = 31 * result + priority
        result = 31 * result + eventID
        return result
    }
}