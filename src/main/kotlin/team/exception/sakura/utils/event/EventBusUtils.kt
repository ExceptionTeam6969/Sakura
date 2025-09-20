package team.exception.sakura.utils.event

import net.neoforged.bus.api.SubscribeEvent

/**
 * Check whether the object has methods annotated with @SubscribeEvent or not.
 */
fun checkEventListeners(obj: Any): Boolean {
    val methods = obj.javaClass.declaredMethods
    methods.forEach {
        if (it.isAnnotationPresent(SubscribeEvent::class.java)) {
            return true
        }
    }
    return false
}