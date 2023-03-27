package vela

import ain.util.Destroyable

abstract class VelaApplication : Destroyable {
    internal var shouldClose = false

    open fun create() {}

    open fun render(deltaTime: Float) {}

    fun close() {
        shouldClose = true
    }
}

fun launch(app: VelaApplication) {
    var lastFrameTime = -1L
    var deltaTime = 0.0f
    var frameCounterStart = 0L
    var fps = 0
    var frames = 0

    app.create()

    while (!app.shouldClose) {
        val time = System.nanoTime()
        if (lastFrameTime == -1L) lastFrameTime = time

        deltaTime = (time - lastFrameTime) / 1000000000.0f

        lastFrameTime = time

        if (time - frameCounterStart >= 1000000000) {
            fps = frames
            frames = 0
            frameCounterStart = time
        }
        frames++

        app.render(deltaTime)
    }

    app.destroy()
}