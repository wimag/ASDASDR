package broker

import entity.Group
import entity.User
import proto.GenericMessageProto

/**
 * Interface describing entities capable
 * of sending messages
 */
interface GroupBroker {
    /**
     * Send message to a whole group
     */
    fun broadcastAsync(group: Group, msg: GenericMessageProto.GenericMessage)

    /**
     * Send message to a single user
     */
    fun sendAsync(user: User, msg: GenericMessageProto.GenericMessage)

}