package lepszyminecraft

import ain.rp.MeshRenderer
import ain.rp.Renderable
import ain.window.Window
import asset.VelaAssetManager
import imgui.flag.ImGuiCond
import imgui.flag.ImGuiConfigFlags
import imgui.flag.ImGuiStyleVar
import imgui.flag.ImGuiWindowFlags
import imgui.internal.ImGui
import imgui.type.ImBoolean
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

class TestUI(val gBuffer: GBuffer, val window: Window) : UIInstance() {

    init {
        ImGui.getIO().configFlags = ImGui.getIO().configFlags or ImGuiConfigFlags.DockingEnable
    }

    override fun render() {
        super.render()

        ImGui.setNextWindowPos(0.0f, 0.0f, ImGuiCond.Always)
        ImGui.setNextWindowSize(window.width.toFloat(), window.height.toFloat())
        ImGui.pushStyleVar(ImGuiStyleVar.WindowRounding, 0.0f)
        ImGui.pushStyleVar(ImGuiStyleVar.WindowBorderSize, 0.0f)

        begin(
            "Dockspace",
            true,
            ImGuiWindowFlags.MenuBar or ImGuiWindowFlags.NoTitleBar or ImGuiWindowFlags.NoCollapse or ImGuiWindowFlags.NoResize or ImGuiWindowFlags.NoMove or ImGuiWindowFlags.NoBringToFrontOnFocus or ImGuiWindowFlags.NoNavFocus
        ) {
            ImGui.popStyleVar(2)

            ImGui.dockSpace(ImGui.getID("Dockspace"))

            begin("GBuffer") {
                for (i in 0 until GBuffer.TOTAL_TEXTURES) {
                    image(gBuffer.textures[i], 455f, 256f, 0f, 1f, 1f, 0f)
                }
            }

            begin("Main") {
                image(gBuffer.textures[0], 1600f, 900f, 0f, 1f, 1f, 0f)
            }
        }
    }
}

class Scene(window: Window) : Scene() {
    private val rp = DeferredRenderingPipeline(window)
    private val renderer = MeshRenderer<TestRenderable>(rp)
    private val uiRenderer = UIRenderer(window)

    private val testRenderable = TestRenderable()
    private val ui = TestUI(rp.gBuffer, window)

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