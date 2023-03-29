package vela.render

import ain.util.Destroyable
import ain.window.Window
import org.lwjgl.opengl.GL30.*
import org.lwjgl.system.MemoryStack
import java.nio.ByteBuffer

class GBuffer(window: Window) : Destroyable {
    val id = glGenFramebuffers()

    val textures = IntArray(TOTAL_TEXTURES)

    init {
        glBindFramebuffer(GL_DRAW_FRAMEBUFFER, id)
        glGenTextures(textures)

        val width = window.width
        val height = window.height

        for (i in 0 until TOTAL_TEXTURES) {
            glBindTexture(GL_TEXTURE_2D, textures[i])
            val attachmentType = if (i == TOTAL_TEXTURES - 1) {
                glTexImage2D(
                    GL_TEXTURE_2D, 0, GL_DEPTH_COMPONENT32F, width, height, 0, GL_DEPTH_COMPONENT, GL_FLOAT,
                    null as ByteBuffer?
                )
                GL_DEPTH_ATTACHMENT
            } else {
                glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA32F, width, height, 0, GL_RGBA, GL_FLOAT, null as ByteBuffer?)
                GL_COLOR_ATTACHMENT0 + i
            }
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST)
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST)
            glFramebufferTexture2D(GL_FRAMEBUFFER, attachmentType, GL_TEXTURE_2D, textures[i], 0)
        }

        MemoryStack.stackPush().use { stack ->
            val intBuff = stack.mallocInt(TOTAL_TEXTURES)
            for (i in 0 until TOTAL_TEXTURES) {
                intBuff.put(i, GL_COLOR_ATTACHMENT0 + i)
            }
            glDrawBuffers(intBuff)
        }

        glBindFramebuffer(GL_DRAW_FRAMEBUFFER, 0)
    }

    override fun destroy() {
        glDeleteFramebuffers(id)

        for (i in 0 until TOTAL_TEXTURES) {
            glDeleteTextures(textures[i])
        }
    }

    companion object {
        private const val TOTAL_TEXTURES = 4
    }
}