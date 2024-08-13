package dev.slmpc.sakura.managers.impl

import dev.slmpc.sakura.graphics.RenderSystem
import dev.slmpc.sakura.graphics.gl.buffer.PMBuffer
import dev.slmpc.sakura.managers.AbstractManager

object GraphicsManager: AbstractManager() {

    override fun onInit() {
        RenderSystem
        PMBuffer
    }

}