package dev.slmpc.sakura

import dev.slmpc.sakura.managers.Managers
import org.apache.logging.log4j.LogManager
import org.lwjgl.glfw.GLFW

object Sakura {

    val logger = LogManager.getLogger("Sakura")

    const val NAME = "Sakura"
    const val VERSION = "1.0"

    @JvmStatic
    fun init() {
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 4)
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 5)

        Managers.init()
    }

}