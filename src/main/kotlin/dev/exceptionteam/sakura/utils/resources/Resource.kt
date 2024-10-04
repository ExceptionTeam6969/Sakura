package dev.exceptionteam.sakura.utils.resources

import dev.exceptionteam.sakura.Sakura
import java.io.InputStream
import java.io.StringWriter
import java.nio.charset.Charset

class Resource(
    path: String
) {

    val data: String

    init {

        val stream = javaClass.getResourceAsStream("${Sakura.ASSETS_DIRECTORY}/${path}")
            ?: throw IllegalArgumentException("Resource not found: ${Sakura.ASSETS_DIRECTORY}/${path}")

        data = stream.readText()

    }

}

fun InputStream.readText(charset: Charset = Charsets.UTF_8, bufferSize: Int = DEFAULT_BUFFER_SIZE): String {
    val stringWriter = StringWriter()
    buffered(bufferSize / 2).reader(charset).copyTo(stringWriter, bufferSize / 2)
    return stringWriter.toString()
}
