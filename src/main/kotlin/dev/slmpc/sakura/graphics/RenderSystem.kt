package dev.slmpc.sakura.graphics

import com.mojang.blaze3d.systems.RenderSystem
import dev.slmpc.sakura.events.impl.Render2DEvent
import dev.slmpc.sakura.events.impl.Render3DEvent
import dev.slmpc.sakura.events.impl.WindowResizeEvent
import dev.slmpc.sakura.events.listener
import dev.slmpc.sakura.features.modules.impl.client.RenderSystemMod
import dev.slmpc.sakura.graphics.gl.buffer.FrameBuffer
import dev.slmpc.sakura.graphics.gl.buffer.PMBuffer
import dev.slmpc.sakura.graphics.gl.shader.PosColorShader2D
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import org.joml.Matrix4f
import org.lwjgl.opengl.GL45

object RenderSystem {

    private val frameBuffer = FrameBuffer()

    init {

        listener<WindowResizeEvent>(alwaysListening = true) {
            frameBuffer.resize()
        }

    }

    fun onRender2d(context: DrawContext) {
        preRender2d()

        val modelView = Matrix4f(RenderSystem.getModelViewMatrix())
        val projection = Matrix4f(RenderSystem.getProjectionMatrix())
        PosColorShader2D.setMatrix(projection.mul(modelView))
        Render2DEvent(context).post()

        postRender2d()
    }

    private fun preRender2d() {
        PMBuffer.onSync()
        RenderSystem.enableBlend()
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f)
        preFrameBuffer()
    }

    private fun postRender2d() {
        RenderSystem.disableBlend()
        postFrameBuffer()
    }

    fun onRender3d() {
        preRender3d()

        Render3DEvent().post()

        postRender3d()
    }

    private fun preRender3d() {
        PMBuffer.onSync()
        preFrameBuffer()
        GL45.glDisable(GL45.GL_DEPTH_TEST)
    }

    private fun postRender3d() {
        postFrameBuffer()
        GL45.glEnable(GL45.GL_DEPTH_TEST)
    }

    private fun preFrameBuffer() {
        if (!RenderSystemMod.frameBuffer) return
        val mc = MinecraftClient.getInstance()

        val wWidth = mc.window.framebufferWidth
        val wHeight = mc.window.framebufferHeight

        GL45.glBindFramebuffer(GL45.GL_DRAW_FRAMEBUFFER, frameBuffer.id)
        GL45.glBindFramebuffer(GL45.GL_READ_FRAMEBUFFER, mc.framebuffer.fbo)
        GL45.glBlitFramebuffer(0, 0, wWidth, wHeight, 0, 0, wWidth, wHeight,
            GL45.GL_COLOR_BUFFER_BIT, GL45.GL_NEAREST)
    }

    private fun postFrameBuffer() {
        if (!RenderSystemMod.frameBuffer) return

        val mc = MinecraftClient.getInstance()

        val wWidth = mc.window.framebufferWidth
        val wHeight = mc.window.framebufferHeight

        GL45.glBindFramebuffer(GL45.GL_DRAW_FRAMEBUFFER, mc.framebuffer.fbo)
        GL45.glBindFramebuffer(GL45.GL_READ_FRAMEBUFFER, frameBuffer.id)
        GL45.glBlitFramebuffer(0, 0, wWidth, wHeight, 0, 0, wWidth, wHeight,
            GL45.GL_COLOR_BUFFER_BIT, GL45.GL_NEAREST)
        GL45.glBindFramebuffer(GL45.GL_FRAMEBUFFER, mc.framebuffer.fbo)
    }

}