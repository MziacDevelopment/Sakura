package dev.slmpc.sakura.managers.impl

import dev.slmpc.sakura.events.impl.KeyEvent
import dev.slmpc.sakura.events.nonNullListener
import dev.slmpc.sakura.features.modules.AbstractModule
import dev.slmpc.sakura.features.modules.impl.client.ChatInfo
import dev.slmpc.sakura.features.modules.impl.client.ClickGUI
import dev.slmpc.sakura.features.modules.impl.client.RenderSystemMod
import dev.slmpc.sakura.managers.AbstractManager

object ModuleManager: AbstractManager() {

    lateinit var modules: Array<AbstractModule>

    override fun onInit() {
        nonNullListener<KeyEvent>(Int.MIN_VALUE, true) { event ->
            modules.forEach {
                if (it.keyBind == event.keyBind && event.action == 1) it.toggle()
            }
        }

        loadModules()
    }

    private fun loadModules() {
        modules = arrayOf(
            // Client
            ClickGUI,
            ChatInfo,
            RenderSystemMod
        )
    }


}