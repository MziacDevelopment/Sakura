package dev.slmpc.sakura.features.gui.clickgui

import dev.slmpc.sakura.events.impl.Render2DEvent
import dev.slmpc.sakura.events.nonNullListener
import dev.slmpc.sakura.features.gui.shared.AbstractGUIScreen
import dev.slmpc.sakura.features.gui.shared.Panel
import dev.slmpc.sakura.features.modules.Category
import dev.slmpc.sakura.features.modules.impl.client.ClickGUI
import dev.slmpc.sakura.graphics.RenderUtils2D
import dev.slmpc.sakura.graphics.color.ColorRGB
import dev.slmpc.sakura.managers.impl.ModuleManager
import dev.slmpc.sakura.utils.control.MouseButtonType
import net.minecraft.client.gui.DrawContext
import java.util.concurrent.CopyOnWriteArrayList

object ClickGUIScreen: AbstractGUIScreen("ClickGUI") {

    private val panels = CopyOnWriteArrayList<Panel>()

    private var mouseX: Float = 0f
    private var mouseY: Float = 0f

    init {
        var xOffset = 10f

        nonNullListener<Render2DEvent>(alwaysListening = true) { e ->
            if (mc.currentScreen !is ClickGUIScreen) return@nonNullListener
            if (ClickGUI.background) {
                RenderUtils2D.drawRectFilled(0f, 0f, mc.window.scaledWidth.toFloat(),
                    mc.window.scaledHeight.toFloat(), ColorRGB(0, 0, 0, 120)
                )
            }
            panels.forEach { it.render(mouseX, mouseY) }
        }

        Category.entries
            .filter { it != Category.HUD }
            .forEach { cate ->
                val modules = ModuleManager.modules.filter {
                    it.category == cate
                }

                panels.add(Panel(cate, modules, xOffset, 10f, WIDTH, HEIGHT))

                xOffset += WIDTH + 10f
            }
    }

    override fun close() {
        ClickGUI.disable()
        super.close()
    }

    override fun render(context: DrawContext, mouseX: Int, mouseY: Int, delta: Float) {
        this@ClickGUIScreen.mouseX = mouseX.toFloat()
        this@ClickGUIScreen.mouseY = mouseY.toFloat()
    }

    override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int): Boolean {
        val type = when (button) {
            0 -> MouseButtonType.LEFT
            1 -> MouseButtonType.RIGHT
            else -> MouseButtonType.NONE
        }
        panels.forEach { it.mouseClicked(mouseX.toFloat(), mouseY.toFloat(), type) }
        return super.mouseClicked(mouseX, mouseY, button)
    }

    override fun mouseReleased(mouseX: Double, mouseY: Double, button: Int): Boolean {
        val type = when (button) {
            0 -> MouseButtonType.LEFT
            1 -> MouseButtonType.RIGHT
            else -> MouseButtonType.NONE
        }
        panels.forEach { it.mouseReleased(type) }
        return super.mouseReleased(mouseX, mouseY, button)
    }

}