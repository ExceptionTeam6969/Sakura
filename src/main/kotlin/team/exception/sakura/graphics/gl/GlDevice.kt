package team.exception.sakura.graphics.gl

import team.exception.sakura.graphics.geek2.G2Buffer
import team.exception.sakura.graphics.geek2.G2Device

class GlDevice: G2Device() {

    private val tmpCommandList = createCommandList()

    override fun createCommandList(): GlCommandList = GlCommandList()

    override fun getTempCommandList(): GlCommandList = tmpCommandList

    override fun createBuffer(
        size: Long,
        access: G2Buffer.Access
    ): G2Buffer = GlBuffer(this, size, access)

}