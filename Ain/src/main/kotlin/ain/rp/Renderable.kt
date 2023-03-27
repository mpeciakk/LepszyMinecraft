package ain.rp

import ain.mesh.Mesh
import ain.mesh.MeshBuilder
import ain.mesh.VertexMeshBuilder
import org.joml.Matrix4f

abstract class Renderable : MeshHolder {
    val meshes = mutableMapOf(
        Pair("default", Mesh())
    )
    val builders = mutableMapOf<String, MeshBuilder>(
        Pair("default", VertexMeshBuilder())
    )

    var transformationMatrix = Matrix4f().identity()

    var state = RenderableState.NONE

    abstract fun rebuild()

    fun markDirty() {
        state = RenderableState.REQUESTED
    }

    protected fun setupMesh(name: String, builder: MeshBuilder = VertexMeshBuilder()) {
        meshes[name] = Mesh()
        builders[name] = builder
    }

    protected fun getMesh(): Mesh {
        return getMesh("default")!!
    }

    protected fun getBuilder(): MeshBuilder {
        return getBuilder("default")!!
    }

    protected fun getMesh(name: String): Mesh? {
        return meshes[name]
    }

    protected fun getBuilder(name: String): MeshBuilder? {
        return builders[name]
    }
}