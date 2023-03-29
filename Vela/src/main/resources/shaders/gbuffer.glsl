#section VERTEX_SHADER

layout (location = 0) in vec3 position;

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform mat4 transformationMatrix;

out vec3 _position;

void main() {
    gl_Position = projectionMatrix * viewMatrix * transformationMatrix * vec4(position, 1.0);
    _position = position;
}

#section FRAGMENT_SHADER

in vec3 _position;

layout (location = 0) out vec4 gPosition;
layout (location = 1) out vec4 gNormal;
layout (location = 2) out vec4 gAlbedo;

void main() {
    gPosition = vec4(_position, 1);
    gNormal = vec4(1, 0, 0, 1);
    gAlbedo = vec4(0, 1, 0, 1);
}