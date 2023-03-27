package ain.rp

open class MeshRenderer<T : Renderable>(
    private val defaultRenderPipeline: RenderPipeline,
    private val renderPipelines: Map<String, RenderPipeline> = emptyMap()
) {
    fun render(t: T) {
        if (t.state == RenderableState.REQUESTED) {
            t.state = RenderableState.BUILDING
            t.rebuild()
            t.state = RenderableState.BUILT
        }

        if (t.state == RenderableState.BUILT) {
            for (mesh in t.meshes) {
                t.builders[mesh.key]?.processMesh(mesh.value)
            }

            t.state = RenderableState.READY
        }

        if (t.state == RenderableState.READY) {
            for (mesh in t.meshes) {
                if (renderPipelines.containsKey(mesh.key)) {
                    renderPipelines[mesh.key]?.render(t, mesh.value)
                } else {
                    defaultRenderPipeline.render(t, mesh.value)
                }
            }
        }
    }
}