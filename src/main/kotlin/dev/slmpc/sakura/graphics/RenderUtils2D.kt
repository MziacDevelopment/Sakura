package dev.slmpc.sakura.graphics

import dev.slmpc.sakura.graphics.color.ColorRGB
import dev.slmpc.sakura.graphics.gl.buffer.PMBuffer
import dev.slmpc.sakura.graphics.gl.shader.PosColorShader2D
import org.lwjgl.opengl.GL45

object RenderUtils2D {

    private val shader = PosColorShader2D()

    private var vertexSize = 0

    fun drawRectFilled(x: Float, y: Float, width: Float, height: Float, color: ColorRGB) {
        putVertex(x + width, y, color)
        putVertex(x, y, color)
        putVertex(x + width, y + height, color)
        putVertex(x, y + height, color)

        draw(GL45.GL_TRIANGLE_STRIP)
    }

    private fun putVertex(x: Float, y: Float, color: ColorRGB) {
        val ptr = PMBuffer.arr.ptr
        ptr[0] = x
        ptr[4] = y
        ptr[8] = color.rgba
        PMBuffer.arr += 12
        vertexSize++
    }

    @Suppress("SameParameterValue")
    private fun draw(mode: Int) {
        if (vertexSize <= 0) return
        shader.bind()
        shader.default()
        GL45.glBindVertexArray(PMBuffer.VAO_2D)
        GL45.glDrawArrays(mode, PMBuffer.offset.toInt(), vertexSize)
        PMBuffer.end(12)
        GL45.glBindVertexArray(0)
        vertexSize = 0
    }

}