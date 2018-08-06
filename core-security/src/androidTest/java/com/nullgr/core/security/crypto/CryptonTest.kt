package com.nullgr.core.security.crypto

import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import android.util.Log
import junit.framework.Assert
import org.junit.Test
import org.junit.runner.RunWith
import java.security.GeneralSecurityException
import java.security.InvalidKeyException

/**
 * Created by Grishko Nikita on 01.02.18.
 */
@RunWith(AndroidJUnit4::class)
class CryptonTest {

    companion object {
        const val PASSWORD = "absCDC123CHCHasb"
    }

    @Test
    fun encrypt_byPassword_Success() {
        val startTime = System.currentTimeMillis()
        val originalText = this::class.java.simpleName
        val encryptedText = Crypton.encrypt(originalText, PASSWORD)
        val encryptionTime = System.currentTimeMillis() - startTime
        Log.d("CryptonTest", "Encryption takes $encryptionTime ms.")
        Assert.assertNotNull(encryptedText)
        Assert.assertFalse(originalText == encryptedText)
    }

    @Test
    fun decrypt_afterEncryptionByTheSamePassword_Success() {
        val originalText = this::class.java.simpleName
        val encryptedText = Crypton.encrypt(originalText, PASSWORD)

        val startTime = System.currentTimeMillis()
        val decryptedText = Crypton.decryptAsString(encryptedText, PASSWORD)
        val decryptionTime = System.currentTimeMillis() - startTime

        Log.d("CryptonTest", "Decryption takes $decryptionTime ms.")

        Assert.assertNotNull(decryptedText)
        Assert.assertEquals(originalText, decryptedText)
    }

    @Test(expected = GeneralSecurityException::class)
    fun decrypt_afterEncryptionByTheDifferentPassword_Fails() {
        val originalText = this::class.java.simpleName
        val encryptedText = Crypton.encrypt(originalText, PASSWORD)
        Crypton.decryptAsString(encryptedText, "SomeOtherPassword")
    }

    @Test(expected = IllegalArgumentException::class)
    fun decrypt_wrongTextByPassword_Fails() {
        Crypton.decryptAsString("ThisIsAbsolutelyIncorrectText", PASSWORD)
    }

    @Test(expected = GeneralSecurityException::class)
    fun decrypt_brokenAfterEncryptionByPassword_Fails() {
        val originalText = this::class.java.simpleName
        val encryptedText = Crypton.encrypt(originalText, PASSWORD)
        val brokenText = encryptedText.substring(0, encryptedText.length - 4)
        Crypton.decryptAsString(brokenText, PASSWORD)
    }

    @Test
    fun encrypt_bySecretKeyLocal_Success() {
        val startTime = System.currentTimeMillis()
        val originalText = this::class.java.simpleName
        val secreteKey = CryptoKeysFactory.createAESKey()
        val encryptedText = Crypton.encrypt(originalText, secreteKey)

        val encryptionTime = System.currentTimeMillis() - startTime
        Log.d("CryptonTest", "Encryption takes $encryptionTime ms.")

        Assert.assertNotNull(encryptedText)
        Assert.assertFalse(originalText == encryptedText)
    }

    @Test
    fun encrypt_bySecretKeyFromKeyStore_Success() {
        val startTime = System.currentTimeMillis()
        val originalText = this::class.java.simpleName
        val secreteKey = CryptoKeysFactory.findOrCreateAESKey("AesTestKey")
        val encryptedText = Crypton.encrypt(originalText, secreteKey)

        val encryptionTime = System.currentTimeMillis() - startTime
        Log.d("CryptonTest", "Encryption takes $encryptionTime ms.")

        Assert.assertNotNull(encryptedText)
        Assert.assertFalse(originalText == encryptedText)
    }

    @Test
    fun decrypt_afterEncryptionByTheSameSecreteKeyLocal_Success() {
        val originalText = this::class.java.simpleName
        val secretKey = CryptoKeysFactory.createAESKey()
        val encryptedText = Crypton.encrypt(originalText, secretKey)

        val startTime = System.currentTimeMillis()
        val decryptedText = Crypton.decryptAsString(encryptedText, secretKey)
        val decryptionTime = System.currentTimeMillis() - startTime

        Log.d("CryptonTest", "Decryption takes $decryptionTime ms.")

        Assert.assertNotNull(decryptedText)
        Assert.assertEquals(originalText, decryptedText)
    }

    @Test
    fun decrypt_afterEncryptionByTheSameSecreteKeyFromKeyStore_Success() {
        val originalText = this::class.java.simpleName
        val secretKey = CryptoKeysFactory.findOrCreateAESKey("AesTestKey")
        val encryptedText = Crypton.encrypt(originalText, secretKey)

        val startTime = System.currentTimeMillis()
        val decryptedText = Crypton.decryptAsString(encryptedText, secretKey)
        val decryptionTime = System.currentTimeMillis() - startTime

        Log.d("CryptonTest", "Decryption takes $decryptionTime ms.")

        Assert.assertNotNull(decryptedText)
        Assert.assertEquals(originalText, decryptedText)
    }

    @Test(expected = GeneralSecurityException::class)
    fun decrypt_afterEncryptionByTheDifferentSecretKeys_Fails() {
        val originalText = this::class.java.simpleName
        val encryptedText = Crypton.encrypt(originalText, CryptoKeysFactory.createAESKey())
        Crypton.decrypt(encryptedText, CryptoKeysFactory.createAESKey())
    }

    @Test(expected = GeneralSecurityException::class)
    fun decrypt_brokenAfterEncryptionBySecretKey_Fails() {
        val secreteKey = CryptoKeysFactory.createAESKey()
        val originalText = this::class.java.simpleName
        val encryptedText = Crypton.encrypt(originalText, secreteKey)
        val brokenText = encryptedText.substring(0, encryptedText.length - 4)
        Crypton.decryptAsString(brokenText, secreteKey)
    }

    @Test(expected = InvalidKeyException::class)
    fun encrypt_withIncorrectSecretKeyType_Fails() {
        val secreteKey = CryptoKeysFactory.createSecreteKey("DES", 64)
        val originalText = this::class.java.simpleName
        Crypton.encrypt(originalText, secreteKey)
    }

    @Test(expected = InvalidKeyException::class)
    fun decrypt_afterEncryptionWithAesDecryptWithIncorrectSecretKeyType_Fails() {
        val secreteKey = CryptoKeysFactory.createAESKey()
        val originalText = this::class.java.simpleName
        val encrypted = Crypton.encrypt(originalText, secreteKey)
        Crypton.decrypt(encrypted, CryptoKeysFactory.createSecreteKey("DES", 64))
    }

    @Test
    fun encrypt_rsaKeyPair_Success() {
        val rsaKeyPair = CryptoKeysFactory.findOrCreateRSAKeyPair(InstrumentationRegistry.getTargetContext(), "TEST_CORE_RSA_KEY")
        val originalText = this::class.java.simpleName

        val startTime = System.currentTimeMillis()
        val encrypted = Crypton.encryptRsa(originalText, rsaKeyPair)

        val encryptionTime = System.currentTimeMillis() - startTime
        Log.d("CryptonTest", "Encryption takes $encryptionTime ms.")

        Assert.assertNotNull(encrypted)
        Assert.assertFalse(encrypted == originalText)
    }

    @Test
    fun decrypt_afterEncryptWithRsaKeyPair_Success() {
        val rsaKeyPair = CryptoKeysFactory.findOrCreateRSAKeyPair(InstrumentationRegistry.getTargetContext(), "TEST_CORE_RSA_KEY")
        val originalText = this::class.java.simpleName
        val encrypted = Crypton.encryptRsa(originalText, rsaKeyPair)

        val startTime = System.currentTimeMillis()
        val decryptedText = Crypton.decryptRsaAsString(encrypted, rsaKeyPair)
        val decryptionTime = System.currentTimeMillis() - startTime
        Log.d("CryptonTest", "Decryption takes $decryptionTime ms.")

        Assert.assertNotNull(decryptedText)
        Assert.assertEquals(originalText, decryptedText)
    }

    @Test
    fun decrypt_afterEncryptWithRsaKeyPairByAlias_Success() {

        val originalText = this::class.java.simpleName
        val encrypted = Crypton.encryptRsa(InstrumentationRegistry.getTargetContext(), originalText, "SOME_KEY_RSA")

        val startTime = System.currentTimeMillis()
        val decryptedText = Crypton.decryptRsaAsString(InstrumentationRegistry.getTargetContext(), encrypted, "SOME_KEY_RSA")
        val decryptionTime = System.currentTimeMillis() - startTime
        Log.d("CryptonTest", "Decryption takes $decryptionTime ms.")

        Assert.assertNotNull(decryptedText)
        Assert.assertEquals(originalText, decryptedText)
    }

    @Test(expected = GeneralSecurityException::class)
    fun decrypt_afterEncryptWithRsaKeyPairByDifferentAlias_Fails() {
        val originalText = this::class.java.simpleName
        val encrypted = Crypton.encryptRsa(InstrumentationRegistry.getTargetContext(), originalText, "SOME_KEY_RSA")
        Crypton.decryptRsa(InstrumentationRegistry.getTargetContext(), encrypted, "SOME_KEY_RSA_2")
    }
}