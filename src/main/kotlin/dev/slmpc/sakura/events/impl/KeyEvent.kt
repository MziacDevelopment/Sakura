package dev.slmpc.sakura.events.impl

import dev.slmpc.sakura.events.CancellableEvent
import dev.slmpc.sakura.utils.control.KeyBind

class KeyEvent(val key: Int, val action: Int): CancellableEvent() {

    val keyBind: KeyBind get() = KeyBind(KeyBind.Type.KEYBOARD, key)

}