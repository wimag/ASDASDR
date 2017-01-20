package network

import com.google.protobuf.GeneratedMessage
import network.dispatching.Dispatcher

/**
 * Entity that can provide a dispatcher
 * for given message
 */
interface Service<T : GeneratedMessage> {
    fun getDispatcher(): Dispatcher<T>
}