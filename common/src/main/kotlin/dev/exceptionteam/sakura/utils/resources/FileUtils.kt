package dev.exceptionteam.sakura.utils.resources

import java.io.File

fun File.checkFile() {
    try {
        if (!this.exists()) {
            this.parentFile.mkdirs()
            this.createNewFile()
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}