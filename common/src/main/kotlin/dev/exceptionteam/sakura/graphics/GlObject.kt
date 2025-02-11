package dev.exceptionteam.sakura.graphics

interface GlObject {

    var id: Int

    fun bind()
    fun unbind()
    fun delete()

}