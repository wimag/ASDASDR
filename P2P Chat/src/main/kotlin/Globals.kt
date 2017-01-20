import java.net.InetSocketAddress


val DEBUG = true

object Settings {
    var clientAddress: InetSocketAddress = InetSocketAddress(1231)
    var hostAddress: InetSocketAddress = InetSocketAddress(1232)
    var defaultUsername = "Unknown"
}
