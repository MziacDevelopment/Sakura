package dev.slmpc.sakura.events

import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.CopyOnWriteArrayList
import java.util.concurrent.atomic.AtomicInteger

object EventBus {

    private val registers = ConcurrentHashMap<Any, CopyOnWriteArrayList<dev.slmpc.sakura.events.EventListener<*>>>()
    private val listeners = CopyOnWriteArrayList<dev.slmpc.sakura.events.EventListener<Any>>()

    private val id = AtomicInteger()

    val busID: Int
        get() {
            return id.getAndIncrement()
        }

    fun <T : dev.slmpc.sakura.events.EventListener<*>> register(owner: Any, listener: T) {
        registers.getOrPut(owner, ::CopyOnWriteArrayList).add(listener)
    }

    @JvmStatic
    fun subscribe(obj: Any) {
        registers[obj]?.forEach(EventBus::subscribe)
    }

    @Suppress("UNCHECKED_CAST")
    @JvmStatic
    fun subscribe(listener: dev.slmpc.sakura.events.EventListener<*>) {
        for (i in listeners.indices) {
            val other = listeners[i]
            if (listener == other) {
                return
            } else if (listener.priority > other.priority) {
                listeners.add(i, listener as dev.slmpc.sakura.events.EventListener<Any>)
                return
            }
        }

        listeners.add(listener as dev.slmpc.sakura.events.EventListener<Any>)
    }

    @JvmStatic
    fun unsubscribe(obj: Any) {
        registers[obj]?.forEach(EventBus::unsubscribe)
    }

    @JvmStatic
    fun unsubscribe(listener: dev.slmpc.sakura.events.EventListener<*>) {
        listeners.removeIf {
            it == listener
        }
    }

    @JvmStatic
    fun post(event: Any) {
        listeners.forEach {
            if (it.eventClass == event.javaClass) it.function.invoke(event)
        }
    }
}