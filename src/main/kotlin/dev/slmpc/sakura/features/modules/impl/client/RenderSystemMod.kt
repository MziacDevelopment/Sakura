package dev.slmpc.sakura.features.modules.impl.client

import dev.slmpc.sakura.features.modules.Category
import dev.slmpc.sakura.features.modules.Module

object RenderSystemMod: Module(
    name = "RenderSystem",
    category = Category.CLIENT,
    description = "Custom render system"
) {

    val frameBuffer by setting("FrameBuffer", true)

}