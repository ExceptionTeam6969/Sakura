package team.exception.sakura.utils.extension

import java.nio.ByteBuffer

object ByteBufferUtils {

    fun ByteBuffer.readAsString(): String {
        val bytes = ByteArray(remaining())
        get(bytes)
        return String(bytes)
    }

}