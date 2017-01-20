package network.dispatching

import com.google.protobuf.GeneratedMessage
import proto.GenericMessageProto


/**
 * Dispatch message to a given handler
 */
fun <T : GeneratedMessage> SimpleDispatcher(handler: (T) -> GenericMessageProto.GenericMessage?): Dispatcher<T> = object : Dispatcher<T> {
    override fun dispatch(message: T): GenericMessageProto.GenericMessage? {
        return handler(message)
    }
}