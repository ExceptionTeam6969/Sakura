package dev.exceptionteam.sakura.graphics.font.stb

import dev.exceptionteam.sakura.utils.memory.memStack
import org.lwjgl.stb.STBTTFontinfo
import org.lwjgl.stb.STBTruetype.*
import java.nio.ByteBuffer

class Font(
    path: String,
    val size: Int = 16
) {

   init {
       val stream = javaClass.getResourceAsStream(path)
           ?: throw RuntimeException("Font file not found: $path")
       val data = ByteBuffer.wrap(stream.readBytes())

       val fontInfo = STBTTFontinfo.create()
       stbtt_InitFont(fontInfo, data)

//       stbtt_Font
   }

}