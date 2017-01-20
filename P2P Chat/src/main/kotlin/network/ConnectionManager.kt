package network

import Settings
import network.dispatching.EnumDispatcher
import proto.GenericMessageProto
import java.net.InetSocketAddress

object ConnectionManager : ConnectionManagerClass(Settings.clientAddress,
        Settings.hostAddress) {
}

/**
 * Entity responsible for conducting and monitoring
 * all network operations
 */
open class ConnectionManagerClass(client: InetSocketAddress, host: InetSocketAddress) {
    val dispatcher = EnumDispatcher(
            GenericMessageProto.GenericMessage.getDefaultInstance())

    private val client = MessageClient(client)
    private val server = MessageServer(host, dispatcher)

    val services = mutableSetOf<Service<*>>()

    fun addService(event: GenericMessageProto.GenericMessage.Type,
                   service: Service<*>) {
        dispatcher.register(event, service.getDispatcher())
        services.add(service)
    }

    fun sendAsync(addr: InetSocketAddress,
                  msg: GenericMessageProto.GenericMessage) {
        if (addr == Settings.hostAddress){
            dispatcher.dispatch(msg)
        }else{
            client.sendAsync(addr, msg)
        }
    }

    fun request(addr: InetSocketAddress,
                msg: GenericMessageProto.GenericMessage): GenericMessageProto.GenericMessage {
        return client.request(addr, msg)
    }

    fun close() {
        client.close()
        server.close()
    }
}