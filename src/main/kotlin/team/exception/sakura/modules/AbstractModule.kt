package team.exception.sakura.modules

import team.exception.sakura.events.ToggleModuleEvent
import team.exception.sakura.settings.AbstractSetting
import team.exception.sakura.settings.BooleanSetting
import team.exception.sakura.settings.KeyBindSetting
import team.exception.sakura.settings.SettingsDesigner
import team.exception.sakura.utils.input.KeyBind
import thedarkcolour.kotlinforforge.neoforge.forge.FORGE_BUS

abstract class AbstractModule(
    val name: String,
    val category: Category,
    defaultKey: Int = -1
): SettingsDesigner<AbstractModule> {

    val settings: MutableList<AbstractSetting<*>> = mutableListOf()

    private val enableMethods: MutableList<() -> Unit> = mutableListOf()
    private val disableMethods: MutableList<() -> Unit> = mutableListOf()

    private val toggleSetting0 = BooleanSetting("toggle", false) { false }
    var enabled by toggleSetting0
    val disabled get() = !enabled

    private val visibleSetting0 = BooleanSetting("visible", false) { false }
    val visible by visibleSetting0

    private val keySetting0 = KeyBindSetting("key",
        KeyBind(KeyBind.Type.KEYBOARD, defaultKey)) { true }
    val key by keySetting0

    init {

        settings.addAll(listOf(toggleSetting0, visibleSetting0, keySetting0))

        toggleSetting0.onChangeValue {
            if (enabled) enableMethods.forEach { it() }
            else disableMethods.forEach { it() }
        }

        enableMethods.add {
            try {
                FORGE_BUS.register(this)
            } catch (_: Exception) {}
            FORGE_BUS.post(ToggleModuleEvent(this, true))
        }

        disableMethods.add {
            try {
                FORGE_BUS.unregister(this)
            } catch (_: Exception) {}
            FORGE_BUS.post(ToggleModuleEvent(this, false))
        }

    }

    fun toggle() {
        enabled = !enabled
    }

    fun enable() = if (enabled) Unit else toggle()
    fun disable() = if (disabled) Unit else toggle()

    fun onEnable(method: () -> Unit) = enableMethods.add(method)
    fun onDisable(method: () -> Unit) = disableMethods.add(method)

    fun getI18NKeyBySetting(setting: AbstractSetting<*>): String = when (setting.name) {
        "toggle" -> "setting.toggle"
        "visible" -> "setting.visible"
        "key" -> "setting.key"
        else -> "modules.${name}.${setting.name}"
    }

    override fun <S : AbstractSetting<*>> AbstractModule.setting(setting: S): S =
        setting.apply { settings.add(this) }

}