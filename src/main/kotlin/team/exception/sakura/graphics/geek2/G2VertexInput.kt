package team.exception.sakura.graphics.geek2

abstract class G2VertexInput {

    abstract fun int(index: Int)

    abstract fun float(index: Int)

    abstract fun vec2(index: Int)

    abstract fun vec3(index: Int)

    abstract fun vec4(index: Int)

    abstract fun mat2(index: Int)

    abstract fun mat3(index: Int)

    abstract fun mat4(index: Int)

    abstract fun destroy()

}