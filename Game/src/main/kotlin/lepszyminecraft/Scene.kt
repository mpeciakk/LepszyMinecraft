package lepszyminecraft

import ain.rp.MeshRenderer
import ain.rp.Renderable
import ain.window.Window
import asset.VelaAssetManager
import imgui.internal.ImGui
import librae.*
import org.joml.Matrix4f
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL11.GL_TEXTURE_2D
import org.lwjgl.opengl.GL11.glBindTexture
import vela.asset.Texture
import vela.render.DeferredRenderingPipeline
import vela.render.GBuffer
import vela.scene.Scene

class TestRenderable : Renderable() {
    override fun rebuild() {
        getBuilder().drawCube(0f, 0f, 0f, 1f, 1f, 1f, true, true, true, true, true, true)
    }
}

class TestUI(val gBuffer: GBuffer) : UIInstance() {
    override fun render() {
        super.render()

        begin("test") {
            image(gBuffer.textures[0], 256f, 256f, 0f, 1f, 1f, 0f)
            ImGui.sameLine()
            image(gBuffer.textures[1], 256f, 256f, 0f, 1f, 1f, 0f)
            image(gBuffer.textures[2], 256f, 256f, 0f, 1f, 1f, 0f)
            ImGui.sameLine()
            image(gBuffer.textures[3], 256f, 256f, 0f, 1f, 1f, 0f)
        }
    }
}

class Scene(window: Window) : Scene() {
    private val rp = DeferredRenderingPipeline(window)
    private val renderer = MeshRenderer<TestRenderable>(rp)
    private val uiRenderer = UIRenderer(window)

    private val testRenderable = TestRenderable()
    private val ui = TestUI(rp.gBuffer)

    override fun create() {
        testRenderable.transformationMatrix.setTranslation(0f, 0f, -3f)
        testRenderable.markDirty()
    }

    override fun render(deltaTime: Float) {
        GL11.glClearColor(0f, 0f, 0f, 1f)
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT or GL11.GL_DEPTH_BUFFER_BIT)
        renderer.render(testRenderable)
        uiRenderer.render(ui)
    }
}