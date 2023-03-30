package lepszyminecraft

import ain.mesh.VertexMeshBuilder
import ain.rp.MeshRenderer
import ain.rp.Renderable
import ain.window.Window
import asset.VelaAssetManager
import imgui.flag.ImGuiCond
import imgui.flag.ImGuiConfigFlags
import imgui.flag.ImGuiStyleVar
import imgui.flag.ImGuiWindowFlags
import imgui.internal.ImGui
import librae.*
import org.lwjgl.opengl.GL11
import vela.asset.ObjModel
import vela.render.deferred.DeferredRenderingPipeline
import vela.scene.Scene

class TestRenderable : Renderable() {
    override fun rebuild() {
        getBuilder().drawCube(0f, 0f, 0f, 1f, 1f, 1f, true, true, true, true, true, true)
    }
}

class GameObject(private val model: ObjModel) : Renderable() {
    override fun rebuild() {
        (getBuilder() as VertexMeshBuilder).setIndices(model.indices.asList())
        (getBuilder() as VertexMeshBuilder).setVertices(model.vertices)
    }
}

class TestUI(val rp: DeferredRenderingPipeline, val window: Window) : UIInstance() {

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
                for (i in rp.gBuffer.textures) {
                    image(i, 455f, 256f, 0f, 1f, 1f, 0f)
                }
            }

            begin("Main") {
                image(rp.finalTexture, 1600f, 900f, 0f, 1f, 1f, 0f)
            }
        }
    }
}

class Scene(window: Window) : Scene() {
    private val rp = DeferredRenderingPipeline(window)
    private val renderer = MeshRenderer<Renderable>(rp)
    private val uiRenderer = UIRenderer(window)

    private val testRenderable = GameObject(VelaAssetManager[ObjModel::class.java, "model"])
    private val ui = TestUI(rp, window)

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