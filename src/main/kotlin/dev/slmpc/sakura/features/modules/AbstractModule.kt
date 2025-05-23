package dev.slmpc.sakura.features.modules

import dev.slmpc.sakura.events.EventBus
import dev.slmpc.sakura.features.modules.impl.client.ChatInfo
import dev.slmpc.sakura.features.settings.*
import dev.slmpc.sakura.utils.control.KeyBind
import dev.slmpc.sakura.utils.ingame.ChatUtils
import dev.slmpc.sakura.utils.threads.runSafe
import net.minecraft.util.Formatting
import java.util.concurrent.CopyOnWriteArrayList

abstract class AbstractModule(
    val name: String,
    val category: Category,
    val description: String,
    defaultEnable: Boolean,
    defaultBind: Int
): SettingsDesigner<AbstractModule> {

    private val enableCustomers = CopyOnWriteArrayList<() -> Unit>()
    private val disableCustomers = CopyOnWriteArrayList<() -> Unit>()

    val settings = CopyOnWriteArrayList<AbstractSetting<*>>()

    private val toggleSetting0 = BooleanSetting("Toggle", false) { false }
    var isEnabled by toggleSetting0
    val isDisabled get() = !isEnabled

    private val keyBind0 = KeyBindSetting("KeyBind", KeyBind(KeyBind.Type.KEYBOARD, defaultBind, 1))
    val keyBind by keyBind0

    init {

        settings.add(toggleSetting0)
        settings.add(keyBind0)

        toggleSetting0.onChangeValue {
            if (isEnabled) {
                enableCustomers.forEach { it.invoke() }
            } else {
                disableCustomers.forEach { it.invoke() }
            }
        }

        enableCustomers.add {
            EventBus.subscribe(this)
            runSafe {
                if (ChatInfo.isEnabled) ChatUtils.sendMessageWithID("$name ${Formatting.GREEN}Enabled", 1337)
            }
        }

        disableCustomers.add {
            EventBus.unsubscribe(this)
            runSafe {
                if (ChatInfo.isEnabled) ChatUtils.sendMessageWithID("$name ${Formatting.RED}Disabled", 1337)
            }
        }

        if (defaultEnable) enable()

    }

    fun toggle() = if (isEnabled) disable() else enable()

    open fun onEnable(run: () -> Unit) = enableCustomers.add(run)
    open fun onDisable(run: () -> Unit) = disableCustomers.add(run)

    fun enable() {
        isEnabled = true
    }

    fun disable() {
        isEnabled = false
    }

    open fun hudInfo(): String {
        return ""
    }

    override fun <S : AbstractSetting<*>> AbstractModule.setting(setting: S): S {
        return setting.apply { settings.add(setting) }
    }

}