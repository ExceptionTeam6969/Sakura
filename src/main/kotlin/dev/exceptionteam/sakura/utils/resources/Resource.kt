package dev.exceptionteam.sakura.utils.resources

import java.io.InputStream
import java.io.StringWriter
import java.nio.charset.Charset

class Resource(private val path: String) {
    fun getString(): String {
        return javaClass.getResourceAsStream(path)!!.use { it.readText() }
    }

    private fun InputStream.readText(charset: Charset = Charsets.UTF_8, bufferSize: Int = DEFAULT_BUFFER_SIZE): String {
        val stringWriter = StringWriter()
        buffered(bufferSize / 2).reader(charset).copyTo(stringWriter, bufferSize / 2)
        return stringWriter.toString()
    }
}
