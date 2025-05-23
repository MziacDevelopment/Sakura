package dev.slmpc.sakura.utils.interfaces

interface DisplayEnum {

    val displayName: CharSequence

    val displayString: String
        get() = displayName.toString()

}