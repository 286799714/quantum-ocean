#version 150

uniform sampler2D InSampler;
uniform sampler2D DepthSampler;
uniform float TimeFromStart;

in vec2 texCoord;
in mat4 projInverse;

out vec4 fragColor;

float screenToViewDepth(float depth) {
    depth = depth * 2.0 - 1.0;
    return 1.0 / (depth * projInverse[2].w + projInverse[3].w);
}

vec3 screenToViewPos(vec2 coord, float depth) {
    vec3 projPos = vec3(coord.xy, depth) * 2.0 - 1.0;
    vec4 viewPos = vec4(projInverse[0].x, projInverse[1].y, projInverse[2].zw) * projPos.xyzz + projInverse[3];
    return viewPos.xyz / viewPos.w;
}

float normalDistribution(float x, float mean, float sigma) {
    float exponent = - (x - mean) * (x - mean) / (2.0 * sigma * sigma);
    return (1.0 / (sigma * sqrt(2.0 * 3.141592653589793))) * exp(exponent);
}

void main(){
    vec4 color = texture(InSampler, texCoord);
    vec3 viewPos = screenToViewPos(texCoord, texture(DepthSampler, texCoord).r);
    float viewDistance = length(viewPos);
    float mean = TimeFromStart * 65;
    float mappedDepth = normalDistribution(viewDistance, mean, 1.0) * 2;
    fragColor = mix(color, vec4(1.0, 1.0, 1.0, 1.0), mappedDepth);
}
