package team.exception.sakura.utils.resources

import net.minecraft.resources.ResourceLocation
import team.exception.sakura.Sakura
import team.exception.sakura.utils.MinecraftGetter.mc
import kotlin.jvm.optionals.getOrNull

class Resource(
    path: String
) {

    private val resLoc = ResourceLocation.fromNamespaceAndPath(
        Sakura.MOD_ID, path)

    fun getDataAsBytes(): ByteArray {
        val res = mc.resourceManager.getResource(resLoc).getOrNull()
            ?: throw Exception("Resource not found: $resLoc")
        return res.open().readBytes()
    }

    fun getDataAsString(): String {
        return String(getDataAsBytes())
    }

}