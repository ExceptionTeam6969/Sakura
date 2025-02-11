package dev.exceptionteam.sakura.graphics.texture

import dev.exceptionteam.sakura.features.modules.Category
import dev.exceptionteam.sakura.features.modules.impl.client.UiSetting
import dev.exceptionteam.sakura.utils.resources.Resource

object CategoryIcons {

    private val combatIcon =
        ImageFileUtils.loadTextureFromResource(Resource("icon/combat.png"))
    private val miscIcon =
        ImageFileUtils.loadTextureFromResource(Resource("icon/misc.png"))
    private val renderIcon =
        ImageFileUtils.loadTextureFromResource(Resource("icon/render.png"))
    private val movementIcon =
        ImageFileUtils.loadTextureFromResource(Resource("icon/movement.png"))
    private val hudIcon =
        ImageFileUtils.loadTextureFromResource(Resource("icon/hud.png"))
    private val playerIcon =
        ImageFileUtils.loadTextureFromResource(Resource("icon/player.png"))
    private val clientIcon =
        ImageFileUtils.loadTextureFromResource(Resource("icon/client.png"))

    fun getIcon(category: Category): Texture? =
        if (UiSetting.notificationIcon) when(category) {
            Category.COMBAT -> combatIcon
            Category.MISC -> miscIcon
            Category.RENDER -> renderIcon
            Category.MOVEMENT -> movementIcon
            Category.HUD -> hudIcon
            Category.PLAYER -> playerIcon
            Category.CLIENT -> clientIcon
        }
        else null

}