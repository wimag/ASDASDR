package entity

import com.google.protobuf.GeneratedMessage

/**
 * Describes objects, that can be easily converted to
 * protobuf
 */
interface ProtobufSerializable<T : GeneratedMessage> {
    fun getProto(): T
}