package network.dispatching

import com.google.protobuf.GeneratedMessage
import proto.GenericMessageProto

/**
 * Describes a dispatcher for messages: an entity,
 * that gets Generated message to a appropriate handler
 */
@FunctionalInterface
interface Dispatcher<T : GeneratedMessage> {
    fun dispatch(message: T): GenericMessageProto.GenericMessage?
}

