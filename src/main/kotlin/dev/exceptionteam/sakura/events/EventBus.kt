package dev.exceptionteam.sakura.events

import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.CopyOnWriteArrayList
import java.util.concurrent.atomic.AtomicInteger

object EventBus {

    private val registers = ConcurrentHashMap<Any, CopyOnWriteArrayList<EventListener<*>>>()
    private val listeners = CopyOnWriteArrayList<EventListener<Any>>()

    private val id = AtomicInteger()

    val busID: Int
        get() {
            return id.getAndIncrement()
        }

    fun <T : EventListener<*>> register(owner: Any, listener: T) {
        registers.getOrPut(owner, ::CopyOnWriteArrayList).add(listener)
    }

    @JvmStatic
    fun subscribe(obj: Any) {
        registers[obj]?.forEach(EventBus::subscribe)
    }

    /**
     * Subscribe a listener to the event bus.
     *
     * Listeners with a higher priority will be executed first. If two listeners have the
     * same priority,the order in which they were added will be used.
     */
    @Suppress("UNCHECKED_CAST")
    @JvmStatic
    fun subscribe(listener: EventListener<*>) {
        for (i in listeners.indices) {
            val other = listeners[i]
            if (listener == other) {
                return
            } else if (listener.priority > other.priority) {
                listeners.add(i, listener as EventListener<Any>)
                return
            }
        }

        listeners.add(listener as EventListener<Any>)
    }

    @JvmStatic
    fun unsubscribe(obj: Any) {
        registers[obj]?.forEach(EventBus::unsubscribe)
    }

    @JvmStatic
    fun unsubscribe(listener: EventListener<*>) {
        listeners.removeIf {
            it == listener
        }
    }

    @JvmStatic
    fun post(event: Any) {
        listeners.forEach {
            if (it.eventClass == event.javaClass) it.function.invoke(event)
        }
    }
}