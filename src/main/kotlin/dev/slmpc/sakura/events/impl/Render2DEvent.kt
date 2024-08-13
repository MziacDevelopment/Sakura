package dev.slmpc.sakura.events.impl

import dev.slmpc.sakura.events.Event
import net.minecraft.client.gui.DrawContext

class Render2DEvent(
    val context: DrawContext
): Event()