package vela.scene

abstract class Scene {
    var initialized = false

    abstract fun render(deltaTime: Float)

    open fun create() {}
    
    fun show() {}
}