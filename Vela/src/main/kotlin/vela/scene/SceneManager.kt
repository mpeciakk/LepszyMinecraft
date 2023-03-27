package vela.scene

class SceneManager {
    fun changeScene(scene: Scene) {
        if (!scene.initialized) {
            scene.create()
        }

        scene.show()

        Companion.scene = scene
    }

    fun render(deltaTime: Float) {
        scene.render(deltaTime)
    }

    companion object {
        lateinit var scene: Scene
    }
}