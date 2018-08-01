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
        val crypton = Crypton(InstrumentationRegistry.getTargetContext())
        val originalText = this::class.java.simpleName
        val encryptedText = crypton.encrypt(originalText, PASSWORD)
        val encryptionTime = System.currentTimeMillis() - startTime
        Log.d("CryptonTest", "Encryption takes $encryptionTime ms.")
        Assert.assertNotNull(encryptedText)
        Assert.assertFalse(originalText == encryptedText)
    }

    @Test
    fun decrypt_afterEncryptionByTheSamePassword_Success() {
        val crypton = Crypton(InstrumentationRegistry.getTargetContext())
        val originalText = this::class.java.simpleName
        val encryptedText = crypton.encrypt(originalText, PASSWORD)

        val startTime = System.currentTimeMillis()
        val decryptedText = crypton.decryptAsString(encryptedText, PASSWORD)
        val decryptionTime = System.currentTimeMillis() - startTime

        Log.d("CryptonTest", "Decryption takes $decryptionTime ms.")

        Assert.assertNotNull(decryptedText)
        Assert.assertEquals(originalText, decryptedText)
    }

    @Test(expected = GeneralSecurityException::class)
    fun decrypt_afterEncryptionByTheDifferentPassword_Fails() {
        val crypton = Crypton(InstrumentationRegistry.getTargetContext())
        val originalText = this::class.java.simpleName
        val encryptedText = crypton.encrypt(originalText, PASSWORD)
        crypton.decryptAsString(encryptedText, "SomeOtherPassword")
    }

    @Test(expected = IllegalArgumentException::class)
    fun decrypt_wrongTextByPassword_Fails() {
        val crypton = Crypton(InstrumentationRegistry.getTargetContext())
        crypton.decryptAsString("ThisIsAbsolutelyIncorrectText", PASSWORD)
    }

    @Test(expected = GeneralSecurityException::class)
    fun decrypt_brokenAfterEncryptionByPassword_Fails() {
        val crypton = Crypton(InstrumentationRegistry.getTargetContext())
        val originalText = this::class.java.simpleName
        val encryptedText = crypton.encrypt(originalText, PASSWORD)
        val brokenText = encryptedText.substring(0, encryptedText.length - 4)
        crypton.decryptAsString(brokenText, PASSWORD)
    }

    @Test
    fun encrypt_bySecretKeyLocal_Success() {
        val startTime = System.currentTimeMillis()
        val crypton = Crypton(InstrumentationRegistry.getTargetContext())
        val originalText = this::class.java.simpleName
        val secreteKey = CryptoKeysFactory.createAESKey()
        val encryptedText = crypton.encrypt(originalText, secreteKey)

        val encryptionTime = System.currentTimeMillis() - startTime
        Log.d("CryptonTest", "Encryption takes $encryptionTime ms.")

        Assert.assertNotNull(encryptedText)
        Assert.assertFalse(originalText == encryptedText)
    }

    @Test
    fun encrypt_bySecretKeyFromKeyStore_Success() {
        val startTime = System.currentTimeMillis()
        val crypton = Crypton(InstrumentationRegistry.getTargetContext())
        val originalText = this::class.java.simpleName
        val secreteKey = CryptoKeysFactory.findOrCreateAESKey("AesTestKey")
        val encryptedText = crypton.encrypt(originalText, secreteKey)

        val encryptionTime = System.currentTimeMillis() - startTime
        Log.d("CryptonTest", "Encryption takes $encryptionTime ms.")

        Assert.assertNotNull(encryptedText)
        Assert.assertFalse(originalText == encryptedText)
    }

    @Test
    fun decrypt_afterEncryptionByTheSameSecreteKeyLocal_Success() {
        val crypton = Crypton(InstrumentationRegistry.getTargetContext())

        val originalText = this::class.java.simpleName
        val secretKey = CryptoKeysFactory.createAESKey()
        val encryptedText = crypton.encrypt(originalText, secretKey)

        val startTime = System.currentTimeMillis()
        val decryptedText = crypton.decryptAsString(encryptedText, secretKey)
        val decryptionTime = System.currentTimeMillis() - startTime

        Log.d("CryptonTest", "Decryption takes $decryptionTime ms.")

        Assert.assertNotNull(decryptedText)
        Assert.assertEquals(originalText, decryptedText)
    }

    @Test
    fun decrypt_afterEncryptionByTheSameSecreteKeyFromKeyStore_Success() {
        val crypton = Crypton(InstrumentationRegistry.getTargetContext())

        val originalText = this::class.java.simpleName
        val secretKey = CryptoKeysFactory.findOrCreateAESKey("AesTestKey")
        val encryptedText = crypton.encrypt(originalText, secretKey)

        val startTime = System.currentTimeMillis()
        val decryptedText = crypton.decryptAsString(encryptedText, secretKey)
        val decryptionTime = System.currentTimeMillis() - startTime

        Log.d("CryptonTest", "Decryption takes $decryptionTime ms.")

        Assert.assertNotNull(decryptedText)
        Assert.assertEquals(originalText, decryptedText)
    }

    @Test(expected = GeneralSecurityException::class)
    fun decrypt_afterEncryptionByTheDifferentSecretKeys_Fails() {
        val crypton = Crypton(InstrumentationRegistry.getTargetContext())
        val originalText = this::class.java.simpleName
        val encryptedText = crypton.encrypt(originalText, CryptoKeysFactory.createAESKey())
        crypton.decrypt(encryptedText, CryptoKeysFactory.createAESKey())
    }

    @Test(expected = GeneralSecurityException::class)
    fun decrypt_brokenAfterEncryptionBySecretKey_Fails() {
        val crypton = Crypton(InstrumentationRegistry.getTargetContext())
        val secreteKey = CryptoKeysFactory.createAESKey()
        val originalText = this::class.java.simpleName
        val encryptedText = crypton.encrypt(originalText, secreteKey)
        val brokenText = encryptedText.substring(0, encryptedText.length - 4)
        crypton.decryptAsString(brokenText, secreteKey)
    }

    @Test(expected = InvalidKeyException::class)
    fun encrypt_withIncorrectSecretKeyType_Fails() {
        val crypton = Crypton(InstrumentationRegistry.getTargetContext())
        val secreteKey = CryptoKeysFactory.createSecreteKey("DES", 64)
        val originalText = this::class.java.simpleName
        crypton.encrypt(originalText, secreteKey)
    }

    @Test(expected = InvalidKeyException::class)
    fun decrypt_afterEncryptionWithAesDecryptWithIncorrectSecretKeyType_Fails() {
        val crypton = Crypton(InstrumentationRegistry.getTargetContext())
        val secreteKey = CryptoKeysFactory.createAESKey()
        val originalText = this::class.java.simpleName
        val encrypted = crypton.encrypt(originalText, secreteKey)
        crypton.decrypt(encrypted, CryptoKeysFactory.createSecreteKey("DES", 64))
    }
}