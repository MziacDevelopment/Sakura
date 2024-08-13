package dev.slmpc.sakura.managers

import dev.slmpc.sakura.events.SafeClientEvent
import dev.slmpc.sakura.managers.impl.*

object Managers {

    fun init() {
        SafeClientEvent

        GraphicsManager.onInit()
        ModuleManager.onInit()
    }

}