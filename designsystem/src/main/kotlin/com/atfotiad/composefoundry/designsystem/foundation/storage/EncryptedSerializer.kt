package com.atfotiad.composefoundry.designsystem.foundation.storage

import androidx.datastore.core.Serializer
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

/**
 * 🔐 A Generic Serializer that encrypts ANY data class using [CryptoManager].
 *
 * Usage in App Module:
 * class AppSettingsSerializer : EncryptedSerializer<AppSettings>(
 *    serializer = AppSettings.serializer(),
 *    defaultValue = AppSettings()
 * )
 */
abstract class EncryptedSerializer<T>(
    private val cryptoManager: CryptoManager = CryptoManager(),
    private val json: Json = Json,
    private val serializer: KSerializer<T>,
    override val defaultValue: T
) : Serializer<T> {

    override suspend fun readFrom(input: InputStream): T {
        return try {
            val decryptedBytes = cryptoManager.decrypt(input)
            json.decodeFromString(serializer, decryptedBytes.decodeToString())
        } catch (e: Exception) {
            // If decryption fails (new install, corrupted file), return default
            // e.printStackTrace() // Log if needed
            defaultValue
        }
    }

    override suspend fun writeTo(t: T, output: OutputStream) {
        val jsonString = json.encodeToString(serializer, t)
        cryptoManager.encrypt(jsonString.encodeToByteArray(), output)
    }
}
