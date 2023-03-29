package vela.render

import ain.mesh.Mesh
import ain.rp.RenderPipeline
import ain.rp.Renderable
import ain.window.Window
import asset.VelaAssetManager
import org.lwjgl.opengl.GL30.*
import vela.Camera
import vela.shader.DefaultShader
import java.nio.ByteBuffer

class DeferredRenderingPipeline(private val window: Window) : RenderPipeline(DefaultShader(VelaAssetManager[String::class.java, "empty"])) {

    val gBuffer = GBuffer(window)
    private val finalBuffer = glGenFramebuffers()
    val finalTexture = glGenTextures()

    init {
        glBindFramebuffer(GL_FRAMEBUFFER, finalBuffer)

        glBindTexture(GL_TEXTURE_2D, finalTexture)
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA32F, 1600, 900, 0, GL_RGBA, GL_FLOAT, null as ByteBuffer?)

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST)

        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, finalTexture, 0)

        glDrawBuffers(GL_COLOR_ATTACHMENT0)

        glBindFramebuffer(GL_FRAMEBUFFER, 0)
    }

    private val quadMesh = QuadMesh()

    private val gBufferShader = DefaultShader(VelaAssetManager[String::class.java, "gbuffer"])
    private val finalShader = DefaultShader(VelaAssetManager[String::class.java, "final"])

    init {
        glEnable(GL_DEPTH_TEST)
    }

    private fun render(mesh: Mesh) {
        mesh.bind()
        mesh.vbos.values.forEach {
            glEnableVertexAttribArray(it.attributeNumber)
        }

        glDrawElements(GL_TRIANGLES, mesh.elementsCount, GL_UNSIGNED_INT, 0)

        mesh.vbos.values.forEach {
            glDisableVertexAttribArray(it.attributeNumber)
        }
        mesh.unbind()
    }

    override fun render(obj: Renderable, mesh: Mesh) {
        glEnable(GL_DEPTH_TEST)
        glBindFramebuffer(GL_DRAW_FRAMEBUFFER, gBuffer.id)
        glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)
        glViewport(0, 0, window.width, window.height)
        glDisable(GL_BLEND)

        gBufferShader.start()
        gBufferShader.loadMatrix("projectionMatrix", Camera.current.projectionMatrix)
        gBufferShader.loadMatrix("viewMatrix", Camera.current.viewMatrix)
        gBufferShader.loadMatrix("transformationMatrix", obj.transformationMatrix)

        render(mesh)

        glEnable(GL_BLEND)
        gBufferShader.stop()

        glBindFramebuffer(GL_DRAW_FRAMEBUFFER, 0)

        glDisable(GL_DEPTH_TEST)
        finalShader.start()

        glBindFramebuffer(GL_READ_FRAMEBUFFER, gBuffer.id)
        glBindFramebuffer(GL_DRAW_FRAMEBUFFER, finalBuffer)

        val textures = gBuffer.textures
        val numTextures = textures.size
        for (i in 0 until numTextures) {
            glActiveTexture(GL_TEXTURE0 + i)
            glBindTexture(GL_TEXTURE_2D, textures[i])
        }

        finalShader.loadInt("positionSampler", 0)
        finalShader.loadInt("normalSampler", 1)
        finalShader.loadInt("specularSampler", 2)
        finalShader.loadInt("depthSampler", 3)

        glBindVertexArray(quadMesh.vao)
        glDrawElements(GL_TRIANGLES, quadMesh.numVertices, GL_UNSIGNED_INT, 0)
        glBindVertexArray(0)

        finalShader.stop()

        glBindFramebuffer(GL_READ_FRAMEBUFFER, 0)
        glBindFramebuffer(GL_DRAW_FRAMEBUFFER, 0)
    }
}