package dev.exceptionteam.sakura.utils.resources

import dev.exceptionteam.sakura.Sakura

class Resource(
    path: String
) {
    val byteArr: ByteArray

    val data: String get() = String(byteArr, Charsets.UTF_8)

    init {
        val stream = javaClass.getResourceAsStream("${Sakura.ASSETS_DIRECTORY}/${path}")
            ?: throw IllegalArgumentException("Resource not found: ${Sakura.ASSETS_DIRECTORY}/${path}")

        byteArr = stream.readBytes()

        stream.close()
    }

}
