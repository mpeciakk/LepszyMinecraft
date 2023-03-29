package lepszyminecraft

import ain.input.Input
import ain.window.Window
import ain.window.WindowHints
import org.lwjgl.glfw.GLFW
import org.lwjgl.opengl.GL30
import vela.Camera
import vela.VelaApplication
import vela.asset.VelaAssetLoader
import vela.scene.Scene
import vela.scene.SceneManager

class App : VelaApplication() {
    private val window = Window(1920, 1080, "Ain engine")
    private val assetLoader = VelaAssetLoader()
    private val sceneManager = SceneManager()
    private val camera = Camera(window.width, window.height)

    private lateinit var scene: Scene

    override fun create() {
        window.create(WindowHints())

        GL30.glEnable(GL30.GL_DEPTH_TEST)

        assetLoader.load()
        assetLoader.loadAssets()

        Input.create(window)

        camera.makeCurrent()

        scene = Scene(window)

        sceneManager.changeScene(scene)
    }

    override fun render(deltaTime: Float) {
        window.pollEvents()
        Input.update()
        Camera.current.update()

        sceneManager.render(deltaTime)

        if (Input.isKeyPressed(GLFW.GLFW_KEY_ESCAPE)) {
            close()
        }

        window.swapBuffers()
    }

    override fun destroy() {
        window.destroy()
    }
}