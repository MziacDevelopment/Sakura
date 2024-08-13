package dev.slmpc.sakura.events

import dev.slmpc.sakura.utils.threads.ConcurrentScope
import dev.slmpc.sakura.utils.threads.runSafe
import dev.slmpc.sakura.utils.threads.runSafeSuspend
import kotlinx.coroutines.launch


inline fun <reified E : dev.slmpc.sakura.events.Event> Any.nonNullListener(
    noinline function: dev.slmpc.sakura.events.SafeClientEvent.(E) -> Unit
) = listener(this, E::class.java, 0, false) { runSafe { function.invoke(this, it) } }

inline fun <reified E : dev.slmpc.sakura.events.Event> Any.nonNullListener(
    priority: Int,
    noinline function: dev.slmpc.sakura.events.SafeClientEvent.(E) -> Unit
) = listener(this, E::class.java, priority, false) { runSafe { function.invoke(this, it) } }

inline fun <reified E : dev.slmpc.sakura.events.Event> Any.nonNullListener(
    priority: Int,
    alwaysListening: Boolean,
    noinline function: dev.slmpc.sakura.events.SafeClientEvent.(E) -> Unit
) = listener(this, E::class.java, priority, alwaysListening) { runSafe { function.invoke(this, it) } }

inline fun <reified E : dev.slmpc.sakura.events.Event> Any.nonNullListener(
    alwaysListening: Boolean,
    noinline function: dev.slmpc.sakura.events.SafeClientEvent.(E) -> Unit
) = listener(this, E::class.java, 0, alwaysListening) { runSafe { function.invoke(this, it) } }

inline fun <reified E : dev.slmpc.sakura.events.Event>Any.nonNullConcurrentListener(
    alwaysListening: Boolean,
    noinline function: suspend dev.slmpc.sakura.events.SafeClientEvent.(E) -> Unit
) = concurrentListener(this, E::class.java, 0, alwaysListening) { runSafeSuspend { function.invoke(this, it) } }

inline fun <reified E : dev.slmpc.sakura.events.Event>Any.nonNullConcurrentListener(
    priority: Int,
    alwaysListening: Boolean,
    noinline function: suspend dev.slmpc.sakura.events.SafeClientEvent.(E) -> Unit
) = concurrentListener(this, E::class.java, priority, alwaysListening) { runSafeSuspend { function.invoke(this, it) } }

@JvmOverloads
@JvmSynthetic
inline fun <reified E : dev.slmpc.sakura.events.Event> Any.listener(
        priority: Int = 0,
        alwaysListening: Boolean = false,
        noinline function: (E) -> Unit
) = listener(this, E::class.java, priority, alwaysListening, function)

fun <E : dev.slmpc.sakura.events.Event> Any.listener(
        owner: Any,
        eventClass: Class<E>,
        priority: Int,
        alwaysListening: Boolean,
        function: (E) -> Unit
) {
    with(
        dev.slmpc.sakura.events.EventListener(
            owner,
            eventClass,
            dev.slmpc.sakura.events.EventBus.busID,
            priority,
            function
        )
    ) {
        if (alwaysListening) dev.slmpc.sakura.events.EventBus.subscribe(this)
        else dev.slmpc.sakura.events.EventBus.register(owner, this)
    }
}

@JvmOverloads
@JvmSynthetic
inline fun <reified E : dev.slmpc.sakura.events.Event> Any.concurrentListener(
    priority: Int = 0,
    alwaysListening: Boolean = false,
    noinline function: (E) -> Unit
) = listener(this, E::class.java, priority, alwaysListening, function)

fun <E : dev.slmpc.sakura.events.Event> Any.concurrentListener(
    owner: Any,
    eventClass: Class<E>,
    priority: Int,
    alwaysListening: Boolean,
    function: suspend (E) -> Unit
) {
    with(
        dev.slmpc.sakura.events.EventListener(
            owner,
            eventClass,
            dev.slmpc.sakura.events.EventBus.busID,
            priority
        ) { ConcurrentScope.launch { function.invoke(it as E) } }) {
        if (alwaysListening) dev.slmpc.sakura.events.EventBus.subscribe(this)
        else dev.slmpc.sakura.events.EventBus.register(owner, this)
    }
}

open class EventListener<E : Any>(
        owner: Any,
        val eventClass: Class<E>,
        val eventID: Int,
        val priority: Int,
        val function: (E) -> Unit
) : Comparable<dev.slmpc.sakura.events.EventListener<*>> {
    override fun compareTo(other: dev.slmpc.sakura.events.EventListener<*>): Int {
        return other.priority.compareTo(priority)
    }

    override fun equals(other: Any?): Boolean {
        return this === other
                || (other is dev.slmpc.sakura.events.EventListener<*>
                && other.eventClass == this.eventClass
                && other.eventID == this.eventID)
    }

    override fun hashCode(): Int {
        var result = eventClass.hashCode()
        result = 31 * result + priority
        result = 31 * result + function.hashCode()
        return result
    }
}