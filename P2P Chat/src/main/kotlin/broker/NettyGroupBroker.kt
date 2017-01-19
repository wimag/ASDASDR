package broker

import entity.Group
import entity.User
import network.ConnectionManager
import network.ConnectionManagerClass
import proto.GenericMessageProto

/**
 * Message broker based on Netty framework
 * @see{#GroupBroker}
 */
class NettyGroupBroker(val connectionManager: ConnectionManagerClass = ConnectionManager) : GroupBroker {

    /**
     * @see{#GroupBroker.broadcastAsync}
     */
    override fun broadcastAsync(group: Group,
                                msg: GenericMessageProto.GenericMessage) {
        synchronized(group.users) {
            for (user in group.users) {
                sendAsync(user, msg)
            }
        }
    }


    /**
     * @see{#GroupBroker.broadcastAsync}
     */
    override fun sendAsync(user: User,
                           msg: GenericMessageProto.GenericMessage) {
        connectionManager.sendAsync(user.hostAddress, msg)
    }


}