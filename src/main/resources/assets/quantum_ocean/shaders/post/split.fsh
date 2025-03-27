//credit: https://www.shadertoy.com/view/MsXGR8 author: monkeyscience
#version 150
#define CIRCLE(_size,_dist,_sharpness) pow(clamp(_dist/_size,0.0,1.0),_sharpness)

uniform sampler2D InSampler;
uniform vec2 OutSize;

in vec2 texCoord;

out vec4 fragColor;

void main(){
    vec2 aspect = vec2(OutSize.x / OutSize.y, 1.0);
    vec2 aspect_uv = aspect * texCoord;
    vec2 center_uv = aspect * vec2(0.5, 0.5);

    vec2 delta = center_uv - aspect_uv;
    float dist = length(delta);

    float blackRing = 1.0 - CIRCLE(0.3, dist, 4.0);;

    vec2 shift = -(1.0 - blackRing) * delta;
    vec3 aber = vec3(0.06, 0.08, 0.1);

    vec4 blurColor   = texture(InSampler, texCoord + shift * aber.x);
         blurColor.y = texture(InSampler, texCoord + shift * aber.y).y;
         blurColor.z = texture(InSampler, texCoord + shift * aber.z).z;
    fragColor = blackRing * blurColor;
}