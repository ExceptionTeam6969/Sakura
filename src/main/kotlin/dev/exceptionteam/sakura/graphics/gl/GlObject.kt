package dev.exceptionteam.sakura.graphics.gl

interface GlObject {

    var id: Int

    fun bind()
    fun unbind()
    fun delete()

}