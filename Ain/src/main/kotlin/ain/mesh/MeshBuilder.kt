package ain.mesh

import org.lwjgl.BufferUtils
import java.nio.FloatBuffer
import java.nio.IntBuffer

abstract class MeshBuilder {
    abstract fun drawCube(
        x: Float,
        y: Float,
        z: Float,
        w: Float,
        h: Float,
        d: Float,
        renderNorth: Boolean,
        renderSouth: Boolean,
        renderEast: Boolean,
        renderWest: Boolean,
        renderUp: Boolean,
        renderDown: Boolean
    )

    abstract fun processMesh(mesh: Mesh): Mesh

    fun processMesh(): Mesh {
        return processMesh(Mesh())
    }

    companion object {
        fun getIntBuffer(data: IntArray): IntBuffer {
            return BufferUtils.createIntBuffer(data.size).put(data).flip()
        }

        fun getFloatBuffer(data: FloatArray): FloatBuffer {
            return BufferUtils.createFloatBuffer(data.size).put(data).flip()
        }
    }
}