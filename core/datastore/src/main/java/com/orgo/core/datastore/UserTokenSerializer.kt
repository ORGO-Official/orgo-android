package com.orgo.core.datastore

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.google.protobuf.InvalidProtocolBufferException
import com.orgo.android.SavedUserToken
import java.io.InputStream
import java.io.OutputStream
import javax.inject.Inject

class UserTokenSerializer @Inject constructor() : Serializer<SavedUserToken> {
    override val defaultValue: SavedUserToken = SavedUserToken.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): SavedUserToken {
        try {
            return SavedUserToken.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override suspend fun writeTo(t: SavedUserToken, output: OutputStream) = t.writeTo(output)
}