package vela.render.deferred

import org.lwjgl.opengl.GL30.*
import org.lwjgl.system.MemoryStack

class QuadMesh {
    val numVertices = 6
    val vao = glGenVertexArrays()
    private val vbos = mutableListOf<Int>()

    init {
        MemoryStack.stackPush().use { stack ->
            val positions = floatArrayOf(
                -1.0f, 1.0f, 0.0f,
                1.0f, 1.0f, 0.0f,
                -1.0f, -1.0f, 0.0f,
                1.0f, -1.0f, 0.0f
            )
            val uvs = floatArrayOf(
                0.0f, 1.0f,
                1.0f, 1.0f,
                0.0f, 0.0f,
                1.0f, 0.0f
            )
            val indices = intArrayOf(0, 2, 1, 1, 2, 3)

            glBindVertexArray(vao)

            var vbo = glGenBuffers()
            vbos.add(vbo)
            val positionsBuffer = stack.callocFloat(positions.size)
            positionsBuffer.put(0, positions)
            glBindBuffer(GL_ARRAY_BUFFER, vbo)
            glBufferData(GL_ARRAY_BUFFER, positionsBuffer, GL_STATIC_DRAW)
            glEnableVertexAttribArray(0)
            glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0)

            vbo = glGenBuffers()
            vbos.add(vbo)
            val textCoordsBuffer = stack.callocFloat(uvs.size)
            textCoordsBuffer.put(0, uvs)
            glBindBuffer(GL_ARRAY_BUFFER, vbo)
            glBufferData(GL_ARRAY_BUFFER, textCoordsBuffer, GL_STATIC_DRAW)
            glEnableVertexAttribArray(1)
            glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0)

            vbo = glGenBuffers()
            vbos.add(vbo)
            val indicesBuffer = stack.callocInt(indices.size)
            indicesBuffer.put(0, indices)
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vbo)
            glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW)
            glBindBuffer(GL_ARRAY_BUFFER, 0)
            glBindVertexArray(0)
        }
    }
}