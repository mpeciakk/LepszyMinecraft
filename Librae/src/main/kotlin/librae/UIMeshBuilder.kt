package librae

import ain.mesh.Mesh
import ain.mesh.MeshBuilder
import imgui.ImDrawData
import org.lwjgl.opengl.GL20

class UIMeshBuilder : MeshBuilder() {
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
        TODO("Not yet implemented")
    }

    override fun processMesh(mesh: Mesh): Mesh {
        mesh.bind()

        val vertices = mesh.getVbo(0, 8)

        GL20.glBindBuffer(GL20.GL_ARRAY_BUFFER, vertices.id)
        GL20.glEnableVertexAttribArray(0)
        GL20.glVertexAttribPointer(0, 2, GL20.GL_FLOAT, false, ImDrawData.SIZEOF_IM_DRAW_VERT, 0)
        GL20.glEnableVertexAttribArray(1)
        GL20.glVertexAttribPointer(1, 2, GL20.GL_FLOAT, false, ImDrawData.SIZEOF_IM_DRAW_VERT, 8)
        GL20.glEnableVertexAttribArray(2)
        GL20.glVertexAttribPointer(2, 4, GL20.GL_UNSIGNED_BYTE, true, ImDrawData.SIZEOF_IM_DRAW_VERT, 16)
        GL20.glBindBuffer(GL20.GL_ARRAY_BUFFER, 0)

        mesh.unbind()

        return mesh
    }
}