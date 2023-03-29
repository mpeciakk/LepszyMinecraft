#section VERTEX_SHADER

layout (location = 0) in vec3 position;
layout (location = 1) in vec2 uv;

out vec3 _position;
out vec2 _uv;

void main() {
    gl_Position = vec4(position, 1.0);
    _position = position;
    _uv = uv;
}

#section FRAGMENT_SHADER

in vec2 _uv;

uniform sampler2D positionSampler;
uniform sampler2D normalSampler;
uniform sampler2D specularSampler;
uniform sampler2D depthSampler;

out vec4 fragColor;

void main() {
    fragColor = vec4(1.0 - texture(positionSampler, _uv).x, 1.0 - texture(positionSampler, _uv).y, 1.0 - texture(positionSampler, _uv).z, 1.0) + texture(specularSampler, _uv);
}