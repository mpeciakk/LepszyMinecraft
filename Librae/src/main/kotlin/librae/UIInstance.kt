package librae

import ain.input.Input
import ain.rp.Renderable
import imgui.ImGui
import org.lwjgl.glfw.GLFW

abstract class UIInstance : Renderable() {

    init {
        setupMesh("default", UIMeshBuilder())
        markDirty()
    }

    open fun render() {
        val imGuiIO = ImGui.getIO()
        val mousePos = Input.mousePosition
        imGuiIO.setMousePos(mousePos.x, mousePos.y)
        imGuiIO.setMouseDown(0, Input.isButtonPressed(GLFW.GLFW_MOUSE_BUTTON_LEFT))
        imGuiIO.setMouseDown(1, Input.isButtonPressed(GLFW.GLFW_MOUSE_BUTTON_RIGHT))
        imGuiIO.setKeysDown(Input.keys)
    }

    override fun rebuild() {

    }
}