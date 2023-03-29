package vela.render

import ain.mesh.Mesh
import ain.rp.RenderPipeline
import ain.rp.Renderable
import ain.window.Window
import asset.VelaAssetManager
import org.lwjgl.opengl.GL30.*
import vela.Camera
import vela.shader.DefaultShader

class DeferredRenderingPipeline(private val window: Window) : RenderPipeline(DefaultShader(VelaAssetManager[String::class.java, "empty"])) {

    val gBuffer = GBuffer(window)

    private val gBufferShader = DefaultShader(VelaAssetManager[String::class.java, "gbuffer"])

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
    }
}