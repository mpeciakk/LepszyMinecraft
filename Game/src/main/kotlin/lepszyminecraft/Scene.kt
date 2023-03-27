package lepszyminecraft

import ain.mesh.Mesh
import ain.rp.MeshRenderer
import ain.rp.RenderPipeline
import ain.rp.Renderable
import ain.window.Window
import asset.VelaAssetManager
import imgui.internal.ImGui
import librae.UIInstance
import librae.UIRenderer
import librae.begin
import librae.frame
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL15
import org.lwjgl.opengl.GL20
import vela.Camera
import vela.scene.Scene
import vela.shader.DefaultShader

class TestRenderPipeline : RenderPipeline(DefaultShader(VelaAssetManager[String::class.java, "default"])) {
    override fun render(obj: Renderable, mesh: Mesh) {
        shader.start()

        shader.loadMatrix("projectionMatrix", Camera.current.projectionMatrix)
        shader.loadMatrix("transformationMatrix", obj.transformationMatrix)
        shader.loadMatrix("viewMatrix", Camera.current.viewMatrix)

        mesh.bind()
        mesh.vbos.values.forEach {
            GL20.glEnableVertexAttribArray(it.attributeNumber)
        }

        GL11.glDrawElements(GL11.GL_TRIANGLES, mesh.elementsCount, GL11.GL_UNSIGNED_INT, 0);

        mesh.vbos.values.forEach {
            GL20.glDisableVertexAttribArray(it.attributeNumber)
        }
        mesh.unbind()

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0)

        shader.stop()
    }
}

class TestRenderable : Renderable() {
    override fun rebuild() {
        getBuilder().drawCube(0f, 0f, 0f, 1f, 1f, 1f, true, true, true, true, true, true)
    }
}

class TestUI : UIInstance() {
    override fun render() {
        super.render()

        frame {
            begin("test") {

            }
        }

        ImGui.render()
    }
}

class Scene(window: Window) : Scene() {
    private val testRenderer = MeshRenderer<TestRenderable>(TestRenderPipeline())
    private val uiRenderer = UIRenderer(window)

    private val testRenderable = TestRenderable()
    private val ui = TestUI()

    override fun create() {
        testRenderable.markDirty()
    }

    override fun render(deltaTime: Float) {
        GL11.glClearColor(0f, 0f, 0f, 1f)
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT or GL11.GL_DEPTH_BUFFER_BIT)
        testRenderer.render(testRenderable)
        uiRenderer.render(ui)
    }
}