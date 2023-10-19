package com.application.moviesapp.data.local.proto

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.application.moviesapp.UserPreferences
import com.google.protobuf.InvalidProtocolBufferException
import java.io.InputStream
import java.io.OutputStream

object UserPreferencesSerializer: Serializer<UserPreferences> {
    override val defaultValue: UserPreferences get() = UserPreferences.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): UserPreferences {
        try {
            return UserPreferences.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException(message = "Cannot read proto", cause = exception)
        }
    }

    override suspend fun writeTo(t: UserPreferences, output: OutputStream) {
        t.writeTo(output)
    }

}