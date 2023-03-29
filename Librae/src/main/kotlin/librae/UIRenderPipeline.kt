package librae

import ain.mesh.Mesh
import ain.rp.RenderPipeline
import ain.rp.Renderable
import ain.shader.Shader
import ain.window.Window
import imgui.ImGui
import imgui.gl3.ImGuiImplGl3
import imgui.glfw.ImGuiImplGlfw

// TODO: do something with shader, add empty shader to Ain?
class UIRenderPipeline(window: Window) : RenderPipeline(object : Shader("#section VERTEX_SHADER\n" +
        "\n" +
        "void main() {\n" +
        "\n" +
        "}\n" +
        "\n" +
        "#section FRAGMENT_SHADER\n" +
        "\n" +
        "void main() {\n" +
        "\n" +
        "}") {
    override fun bindAttributes() {

    }
}) {
    private val imGuiGlfw = ImGuiImplGlfw()
    private val imGuiGl3 = ImGuiImplGl3()

    init {
        ImGui.createContext()

        imGuiGlfw.init(window.id, true)
        imGuiGl3.init("#version 130")

        val imGuiIO = ImGui.getIO()
        imGuiIO.iniFilename = null
        imGuiIO.setDisplaySize(window.width.toFloat(), window.height.toFloat())
    }

    override fun render(obj: Renderable, mesh: Mesh) {
        imGuiGlfw.newFrame()
        ImGui.newFrame()

        (obj as UIInstance).render()

        ImGui.render()
        imGuiGl3.renderDrawData(ImGui.getDrawData())
    }
}