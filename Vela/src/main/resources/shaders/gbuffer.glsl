#section VERTEX_SHADER

layout (location = 0) in vec3 position;
layout (location = 2) in vec3 normal;

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform mat4 transformationMatrix;

out vec3 _position;
out vec3 _normal;

void main() {
    gl_Position = projectionMatrix * viewMatrix * transformationMatrix * vec4(position, 1.0);
    _position = position;
    _normal = normal;
}

#section FRAGMENT_SHADER

in vec3 _position;
in vec3 _normal;

layout (location = 0) out vec4 gPosition;
layout (location = 1) out vec4 gNormal;
layout (location = 2) out vec4 gAlbedo;

void main() {
    gPosition = vec4(_position, 1);
    gNormal = vec4(_normal, 1);
    gAlbedo = vec4(0, 1, 0, 1);
}