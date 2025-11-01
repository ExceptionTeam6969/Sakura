package team.exception.sakura.graphics.geek2

abstract class G2Device {

    abstract fun createCommandList(): G2CommandList

    abstract fun getTempCommandList(): G2CommandList

    abstract fun createBuffer(
        size: Long,
        access: G2Buffer.Access = G2Buffer.Access.READ_WRITE
    ): G2Buffer

}