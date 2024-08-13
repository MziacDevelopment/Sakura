package dev.slmpc.sakura.features.modules.impl.client

import dev.slmpc.sakura.features.gui.clickgui.ClickGUIScreen
import dev.slmpc.sakura.features.modules.Category
import dev.slmpc.sakura.features.modules.Module
import dev.slmpc.sakura.utils.threads.runSafe
import org.lwjgl.glfw.GLFW

object ClickGUI: Module(
    name = "ClickGUI",
    description = "",
    category = Category.CLIENT,
    defaultBind = GLFW.GLFW_KEY_RIGHT_SHIFT
) {

    val background by setting("Background", true)

    init {

        onEnable {
            runSafe {
                mc.setScreen(ClickGUIScreen)
            }
        }

        onDisable {
            runSafe {
                mc.setScreen(null)
            }
        }

    }

}