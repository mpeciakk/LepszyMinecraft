package ain.mesh

open class VertexMeshBuilder : MeshBuilder() {
    val vertices = mutableListOf<Vertex>()
    val indices = mutableListOf<Int>()

    fun addVertex(vertex: Vertex) {
        vertices.add(vertex)
    }

    fun addIndex(index: Int) {
        indices.add(index)
    }

    fun setVertices(list: List<Vertex>) {
        vertices.clear()
        vertices.addAll(list)
    }

    fun setIndices(list: List<Int>) {
        indices.clear()
        indices.addAll(list)
    }

    override fun drawCube(
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
    ) {
        if (renderNorth) {
            drawQuad(
                Vertex(x, y, z, 0f, 1f, null, 0f, 0f, 0f),
                Vertex(x + w, y, z, 1f, 1f, null, 0f, 0f, 0f),
                Vertex(x, y + h, z, 0f, 0f, null, 0f, 0f, 0f),
                Vertex(x + w, y + h, z, 1f, 0f, null, 0f, 0f, 0f)
            )
        }

        if (renderSouth) {
            drawQuad(
                Vertex(x, y, z + d, 0f, 1f, null, 0f, 0f, 0f),
                Vertex(x + w, y, z + d, 1f, 1f, null, 0f, 0f, 0f),
                Vertex(x, y + h, z + d, 0f, 0f, null, 0f, 0f, 0f),
                Vertex(x + w, y + h, z + d, 1f, 0f, null, 0f, 0f, 0f)
            )
        }

        if (renderEast) {
            drawQuad(
                Vertex(x, y, z, 0f, 1f, null, 0f, 0f, 0f),
                Vertex(x, y, z + d, 1f, 1f, null, 0f, 0f, 0f),
                Vertex(x, y + h, z, 0f, 0f, null, 0f, 0f, 0f),
                Vertex(x, y + h, z + d, 1f, 0f, null, 0f, 0f, 0f)
            )
        }

        if (renderWest) {
            drawQuad(
                Vertex(x + w, y, z, 0f, 1f, null, 0f, 0f, 0f),
                Vertex(x + w, y, z + d, 1f, 1f, null, 0f, 0f, 0f),
                Vertex(x + w, y + h, z, 0f, 0f, null, 0f, 0f, 0f),
                Vertex(x + w, y + h, z + d, 1f, 0f, null, 0f, 0f, 0f)
            )
        }

        if (renderUp) {
            drawQuad(
                Vertex(x, y + h, z, 0f, 1f, null, 0f, 0f, 0f),
                Vertex(x, y + h, z + d, 1f, 1f, null, 0f, 0f, 0f),
                Vertex(x + w, y + h, z, 0f, 0f, null, 0f, 0f, 0f),
                Vertex(x + w, y + h, z + d, 1f, 0f, null, 0f, 0f, 0f)
            )
        }

        if (renderDown) {
            drawQuad(
                Vertex(x, y, z, 0f, 1f, null, 0f, 0f, 0f),
                Vertex(x, y, z + d, 1f, 1f, null, 0f, 0f, 0f),
                Vertex(x + w, y, z, 0f, 0f, null, 0f, 0f, 0f),
                Vertex(x + w, y, z + d, 1f, 0f, null, 0f, 0f, 0f)
            )
        }
    }

    fun drawQuad(a: Vertex, b: Vertex, c: Vertex, d: Vertex) {
        val n = vertices.size

        vertices.addAll(listOf(a, b, c, d))

        indices.addAll(
            listOf(
                n + 0, n + 1, n + 2,
                n + 3, n + 2, n + 1
            )
        )
    }

    override fun processMesh(mesh: Mesh): Mesh {
        mesh.bind()

        val vertices = mesh.getVbo(0, 3)
        val uvs = mesh.getVbo(1, 2)
        val normals = mesh.getVbo(2, 3)
        val indices = mesh.addVbo(IndicesVBO())

        val meshVertices = this.vertices
        val meshIndices = this.indices

        val verticesData = FloatArray(meshVertices.size * 3)
        val uvsData = FloatArray(meshVertices.size * 2)
        val normalsData = FloatArray(meshVertices.size * 3)
        val indicesData = meshIndices.toTypedArray()

        var verticesIndex = 0
        var uvsIndex = 0
        var normalsIndex = 0

        for (vertex in meshVertices) {
            verticesData[verticesIndex++] = vertex.position.x
            verticesData[verticesIndex++] = vertex.position.y
            verticesData[verticesIndex++] = vertex.position.z
            uvsData[uvsIndex++] = vertex.uvs.x
            uvsData[uvsIndex++] = vertex.uvs.y
            normalsData[normalsIndex++] = vertex.normal.y
            normalsData[normalsIndex++] = vertex.normal.y
            normalsData[normalsIndex++] = vertex.normal.y
        }

        vertices.flush(getFloatBuffer(verticesData))
        uvs.flush(getFloatBuffer(uvsData))
        normals.flush(getFloatBuffer(normalsData))
        indices.flush(getIntBuffer(indicesData.toIntArray()))

        mesh.elementsCount = indicesData.size

        mesh.unbind()

        return mesh
    }
}