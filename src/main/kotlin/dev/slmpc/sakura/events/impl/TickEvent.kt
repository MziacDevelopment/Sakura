package dev.slmpc.sakura.events.impl

import dev.slmpc.sakura.events.Event

sealed class TickEvent {

    object Pre: Event()

    object Post: Event()

}