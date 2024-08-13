package dev.slmpc.sakura.events.impl

import dev.slmpc.sakura.events.Event

sealed class GameLoopEvent {

    object AfterRender: Event()

}