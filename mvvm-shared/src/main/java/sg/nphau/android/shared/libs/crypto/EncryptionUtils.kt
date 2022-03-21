/*
 * Created by nphau on 01/11/2021, 00:51
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 01/11/2021, 00:39
 */

package sg.nphau.android.shared.libs.crypto

import android.util.Base64
import sg.nphau.android.shared.common.functional.tryOrNull
import java.security.InvalidKeyException
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import javax.crypto.Cipher
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

object EncryptionUtils {


    ///////////////////////////////////////////////////////////////////////////
    // hash encryption
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Return the hex string of MD5 encryption.
     *
     * @param data The data.
     * @return the hex string of MD5 encryption
     */
    fun encryptMD5ToString(data: String?): String {
        return if (data == null || data.isEmpty()) "" else encryptMD5ToString(data.toByteArray())
    }

    /**
     * Return the hex string of MD5 encryption.
     *
     * @param data The data.
     * @param salt The salt.
     * @return the hex string of MD5 encryption
     */
    fun encryptMD5ToString(data: String?, salt: String?): String {
        if (data == null && salt == null) return ""
        if (salt == null)
            return encryptMD5(data!!.toByteArray())?.toHex() ?: ""
        return if (data == null) {
            encryptMD5(salt.toByteArray())?.toHex() ?: ""
        } else {
            encryptMD5((data + salt).toByteArray())?.toHex() ?: ""
        }
    }

    /**
     * Return the hex string of MD5 encryption.
     *
     * @param data The data.
     * @return the hex string of MD5 encryption
     */
    fun encryptMD5ToString(data: ByteArray): String {
        return encryptMD5(data)?.toHex() ?: ""
    }

    /**
     * Return the hex string of MD5 encryption.
     *
     * @param data The data.
     * @param salt The salt.
     * @return the hex string of MD5 encryption
     */
    fun encryptMD5ToString(data: ByteArray?, salt: ByteArray?): String {
        if (data == null && salt == null) return ""
        if (salt == null) {
            return data?.let { encryptMD5(it) }?.toHex() ?: ""
        }
        if (data == null) return encryptMD5(salt)?.toHex() ?: ""
        val dataSalt = ByteArray(data.size + salt.size)
        System.arraycopy(data, 0, dataSalt, 0, data.size)
        System.arraycopy(salt, 0, dataSalt, data.size, salt.size)
        return encryptMD5(dataSalt)?.toHex() ?: ""
    }

    /**
     * Return the bytes of MD5 encryption.
     *
     * @param data The data.
     * @return the bytes of MD5 encryption
     */
    fun encryptMD5(data: ByteArray): ByteArray? {
        return hashTemplate(data, "MD5")
    }

    /**
     * Return the hex string of SHA1 encryption.
     *
     * @param data The data.
     * @return the hex string of SHA1 encryption
     */
    fun encryptSHA1ToString(data: String?): String {
        return if (data == null || data.isEmpty()) "" else encryptSHA1ToString(data.toByteArray())
    }

    /**
     * Return the hex string of SHA1 encryption.
     *
     * @param data The data.
     * @return the hex string of SHA1 encryption
     */
    fun encryptSHA1ToString(data: ByteArray): String {
        return encryptSHA1(data)?.toHex() ?: ""
    }

    /**
     * Return the bytes of SHA1 encryption.
     *
     * @param data The data.
     * @return the bytes of SHA1 encryption
     */
    fun encryptSHA1(data: ByteArray): ByteArray? {
        return hashTemplate(data, "SHA1")
    }


    /**
     * Return the hex string of SHA256 encryption.
     *
     * @param data The data.
     * @return the hex string of SHA256 encryption
     */
    fun encryptSHA256ToString(data: String?): String {
        return if (data == null || data.isEmpty()) "" else encryptSHA256ToString(data.toByteArray())
    }

    /**
     * Return the hex string of SHA256 encryption.
     *
     * @param data The data.
     * @return the hex string of SHA256 encryption
     */
    fun encryptSHA256ToString(data: ByteArray): String {
        return encryptSHA256(data)?.toHex() ?: ""
    }

    /**
     * Return the bytes of SHA256 encryption.
     *
     * @param data The data.
     * @return the bytes of SHA256 encryption
     */
    fun encryptSHA256(data: ByteArray): ByteArray? {
        return hashTemplate(data, "SHA-256")
    }

    /**
     * Return the hex string of SHA512 encryption.
     *
     * @param data The data.
     * @return the hex string of SHA512 encryption
     */
    fun encryptSHA512ToString(data: String?): String {
        return if (data == null || data.isEmpty()) "" else encryptSHA512ToString(data.toByteArray())
    }

    /**
     * Return the hex string of SHA512 encryption.
     *
     * @param data The data.
     * @return the hex string of SHA512 encryption
     */
    fun encryptSHA512ToString(data: ByteArray): String {
        return encryptSHA512(data)?.toHex() ?: ""
    }

    /**
     * Return the bytes of SHA512 encryption.
     *
     * @param data The data.
     * @return the bytes of SHA512 encryption
     */
    fun encryptSHA512(data: ByteArray): ByteArray? {
        return hashTemplate(data, "SHA-512")
    }

    /**
     * Return the bytes of hash encryption.
     *
     * @param data      The data.
     * @param algorithm The name of hash encryption.
     * @return the bytes of hash encryption
     */
    private fun hashTemplate(data: ByteArray?, algorithm: String): ByteArray? {
        if (data == null || data.isEmpty()) return null
        return tryOrNull {
            val md = MessageDigest.getInstance(algorithm)
            md.update(data)
            md.digest()
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // hmac encryption
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Return the hex string of HmacMD5 encryption.
     *
     * @param data The data.
     * @param key  The key.
     * @return the hex string of HmacMD5 encryption
     */
    fun encryptHmacMD5ToString(data: String?, key: String?): String {
        return if (data == null || data.isEmpty() || key == null || key.isEmpty()) "" else encryptHmacMD5ToString(
            data.toByteArray(),
            key.toByteArray()
        )
    }

    /**
     * Return the hex string of HmacMD5 encryption.
     *
     * @param data The data.
     * @param key  The key.
     * @return the hex string of HmacMD5 encryption
     */
    fun encryptHmacMD5ToString(data: ByteArray, key: ByteArray): String {
        return encryptHmacMD5(data, key)?.toHex() ?: ""
    }

    /**
     * Return the bytes of HmacMD5 encryption.
     *
     * @param data The data.
     * @param key  The key.
     * @return the bytes of HmacMD5 encryption
     */
    fun encryptHmacMD5(data: ByteArray, key: ByteArray): ByteArray? {
        return hmacTemplate(data, key, "HmacMD5")
    }

    /**
     * Return the hex string of HmacSHA1 encryption.
     *
     * @param data The data.
     * @param key  The key.
     * @return the hex string of HmacSHA1 encryption
     */
    fun encryptHmacSHA1ToString(data: String?, key: String?): String {
        return if (data == null || data.isEmpty() || key == null || key.isEmpty()) "" else encryptHmacSHA1ToString(
            data.toByteArray(),
            key.toByteArray()
        )
    }

    /**
     * Return the hex string of HmacSHA1 encryption.
     *
     * @param data The data.
     * @param key  The key.
     * @return the hex string of HmacSHA1 encryption
     */
    fun encryptHmacSHA1ToString(data: ByteArray, key: ByteArray): String {
        return encryptHmacSHA1(data, key)?.toHex() ?: ""
    }

    /**
     * Return the bytes of HmacSHA1 encryption.
     *
     * @param data The data.
     * @param key  The key.
     * @return the bytes of HmacSHA1 encryption
     */
    fun encryptHmacSHA1(data: ByteArray, key: ByteArray): ByteArray? {
        return hmacTemplate(data, key, "HmacSHA1")
    }

    /**
     * Return the hex string of HmacSHA256 encryption.
     *
     * @param data The data.
     * @param key  The key.
     * @return the hex string of HmacSHA256 encryption
     */
    fun encryptHmacSHA256ToString(data: String?, key: String?): String {
        return if (data == null || data.isEmpty() || key == null || key.isEmpty()) "" else encryptHmacSHA256ToString(
            data.toByteArray(),
            key.toByteArray()
        )
    }

    /**
     * Return the hex string of HmacSHA256 encryption.
     *
     * @param data The data.
     * @param key  The key.
     * @return the hex string of HmacSHA256 encryption
     */
    fun encryptHmacSHA256ToString(data: ByteArray, key: ByteArray): String {
        return encryptHmacSHA256(data, key)?.toHex() ?: ""
    }

    /**
     * Return the bytes of HmacSHA256 encryption.
     *
     * @param data The data.
     * @param key  The key.
     * @return the bytes of HmacSHA256 encryption
     */
    fun encryptHmacSHA256(data: ByteArray, key: ByteArray): ByteArray? {
        return hmacTemplate(data, key, "HmacSHA256")
    }

    /**
     * Return the bytes of hmac encryption.
     *
     * @param data      The data.
     * @param key       The key.
     * @param algorithm The name of hmac encryption.
     * @return the bytes of hmac encryption
     */
    private fun hmacTemplate(
        data: ByteArray?,
        key: ByteArray?,
        algorithm: String
    ): ByteArray? {
        if (data == null || data.isEmpty() || key == null || key.isEmpty()) return null
        return try {
            val secretKey = SecretKeySpec(key, algorithm)
            val mac = Mac.getInstance(algorithm)
            mac.init(secretKey)
            mac.doFinal(data)
        } catch (e: InvalidKeyException) {
            e.printStackTrace()
            null
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
            null
        }

    }

    /**
     * AES, key length must be 16
     * @param input
     * @param key key length must be 16
     * @return
     */
    fun encryptAES(input: String, key: String): String? {
        return encryptBySymmetrical("AES", input, key)
    }

    /**
     * AES，key length must be 16
     * @param input
     * @param key key length must be 16
     * @return
     */
    fun decryptAES(input: String, key: String): String? {
        return decryptBySymmetrical("AES", input, key)
    }

    /**
     * DES, key length must be 8
     * @param input
     * @param key key length must be 8
     * @return
     */
    fun encryptDES(input: String, key: String): String? {
        return encryptBySymmetrical("DES", input, key)
    }

    /**
     * DES, key length must be 8
     * @param input
     * @param key key length must be 8
     * @return
     */
    fun decryptDES(input: String, key: String): String? {
        return decryptBySymmetrical("DES", input, key)
    }

    private fun encryptBySymmetrical(name: String, input: String, key: String): String? {
        try {
            val cipher = Cipher.getInstance(name)
            val secretKeySpec = SecretKeySpec(key.toByteArray(), name)
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec)
            val bytes = cipher.doFinal(input.toByteArray())
            return Base64.encodeToString(bytes, Base64.DEFAULT)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return null
    }

    private fun decryptBySymmetrical(name: String, input: String, key: String): String? {
        try {
            val cipher = Cipher.getInstance(name)
            val secretKeySpec = SecretKeySpec(key.toByteArray(), name)
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec)
            val bytes = cipher.doFinal(Base64.decode(input, Base64.DEFAULT))
            return String(bytes)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return null
    }

}

fun String.toMD5() = EncryptionUtils.encryptMD5ToString(this)

fun String.toSHA1() = EncryptionUtils.encryptSHA1ToString(this)

fun String.toSHA256() = EncryptionUtils.encryptSHA256ToString(this)

fun String.toSHA512() = EncryptionUtils.encryptSHA512ToString(this)

fun String.encryptDES(key: String) = EncryptionUtils.encryptDES(this, key)

fun String.decryptDES(key: String) = EncryptionUtils.decryptDES(this, key)

fun String.encryptAES(key: String) = EncryptionUtils.encryptAES(this, key)

fun String.decryptAES(key: String) = EncryptionUtils.decryptAES(this, key)

fun ByteArray.toHex(): String = joinToString("") { "%02x".format(it) }