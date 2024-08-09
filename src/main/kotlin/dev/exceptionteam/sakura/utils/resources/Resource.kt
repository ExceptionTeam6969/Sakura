package dev.exceptionteam.sakura.utils.resources

import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.nio.charset.StandardCharsets

class Resource(private val path: String) {
    private val inputStream: InputStream? get() = Resource::class.java.getResourceAsStream(path)

    fun getString(): String {
        val result = ByteArrayOutputStream()
        val buffer = ByteArray(1024)
        var length: Int

        val inputStream0 = this.inputStream ?: return ""

        while ((inputStream0.read(buffer).also { length = it }) != -1) {
            result.write(buffer, 0, length)
        }
        val str = result.toString(StandardCharsets.UTF_8.name())
        return str
    }
}