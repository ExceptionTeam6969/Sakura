package dev.exceptionteam.sakura.events.impl

import dev.exceptionteam.sakura.events.CancellableEvent
import dev.exceptionteam.sakura.utils.control.KeyBind

class KeyEvent(val key: Int, val action: Int): CancellableEvent() {

    val keyBind: KeyBind get() = KeyBind(KeyBind.Type.KEYBOARD, key)

}