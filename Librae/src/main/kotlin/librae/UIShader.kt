package librae

import ain.shader.Shader

class UIShader : Shader(
    "#section VERTEX_SHADER\n" +
            "\n" +
            "layout (location=0) in vec2 inPos;\n" +
            "layout (location=1) in vec2 inTextCoords;\n" +
            "layout (location=2) in vec4 inColor;\n" +
            "\n" +
            "out vec2 frgTextCoords;\n" +
            "out vec4 frgColor;\n" +
            "\n" +
            "uniform vec2 scale;\n" +
            "\n" +
            "void main() {\n" +
            "    frgTextCoords = inTextCoords;\n" +
            "    frgColor = inColor;\n" +
            "    gl_Position = vec4(inPos * scale + vec2(-1.0, 1.0), 0.0, 1.0);\n" +
            "}\n" +
            "\n" +
            "#section FRAGMENT_SHADER\n" +
            "\n" +
            "in vec2 frgTextCoords;\n" +
            "in vec4 frgColor;\n" +
            "\n" +
            "uniform sampler2D txtSampler;\n" +
            "\n" +
            "out vec4 outColor;\n" +
            "\n" +
            "void main() {\n" +
            "    outColor = frgColor  * texture(txtSampler, frgTextCoords);\n" +
            "}"
) {
    override fun bindAttributes() {

    }
}