package ain.rp

import ain.mesh.Mesh
import ain.shader.Shader

abstract class RenderPipeline(protected val shader: Shader) {
    abstract fun render(obj: Renderable, mesh: Mesh)
}