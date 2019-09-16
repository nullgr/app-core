package com.nullgr.core.security.crypto

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import android.util.Log
import org.junit.Assert
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
        val encryptedText = Crypton.passwordBasedEncryption().encrypt(originalText, PASSWORD)
        val encryptionTime = System.currentTimeMillis() - startTime
        Log.d("CryptonTest", "Encryption takes $encryptionTime ms.")
        Assert.assertNotNull(encryptedText)
        Assert.assertFalse(originalText == encryptedText)
    }

    @Test
    fun decrypt_afterEncryptionByTheSamePassword_Success() {
        val originalText = this::class.java.simpleName
        val encryptedText = Crypton.passwordBasedEncryption().encrypt(originalText, PASSWORD)

        val startTime = System.currentTimeMillis()
        val decryptedText = Crypton.passwordBasedEncryption().decryptAsString(encryptedText, PASSWORD)
        val decryptionTime = System.currentTimeMillis() - startTime

        Log.d("CryptonTest", "Decryption takes $decryptionTime ms.")

        Assert.assertNotNull(decryptedText)
        Assert.assertEquals(originalText, decryptedText)
    }

    @Test(expected = GeneralSecurityException::class)
    fun decrypt_afterEncryptionByTheDifferentPassword_Fails() {
        val originalText = this::class.java.simpleName
        val encryptedText = Crypton.passwordBasedEncryption().encrypt(originalText, PASSWORD)
        Crypton.passwordBasedEncryption().decryptAsString(encryptedText, "SomeOtherPassword")
    }

    @Test(expected = IllegalArgumentException::class)
    fun decrypt_wrongTextByPassword_Fails() {
        Crypton.passwordBasedEncryption().decryptAsString("ThisIsAbsolutelyIncorrectText", PASSWORD)
    }

    @Test(expected = GeneralSecurityException::class)
    fun decrypt_brokenAfterEncryptionByPassword_Fails() {
        val originalText = this::class.java.simpleName
        val encryptedText = Crypton.passwordBasedEncryption().encrypt(originalText, PASSWORD)
        val brokenText = encryptedText.substring(0, encryptedText.length - 4)
        Crypton.passwordBasedEncryption().decryptAsString(brokenText, PASSWORD)
    }

    @Test
    fun encrypt_bySecretKeyLocal_Success() {
        val startTime = System.currentTimeMillis()
        val originalText = this::class.java.simpleName
        val secreteKey = CryptoKeysFactory.createAESKey()
        val encryptedText = Crypton.aesCbcEncryption().encrypt(originalText, secreteKey)

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
        val encryptedText = Crypton.aesCbcEncryption().encrypt(originalText, secreteKey)

        val encryptionTime = System.currentTimeMillis() - startTime
        Log.d("CryptonTest", "Encryption takes $encryptionTime ms.")

        Assert.assertNotNull(encryptedText)
        Assert.assertFalse(originalText == encryptedText)
    }

    @Test
    fun decrypt_afterEncryptionByTheSameSecreteKeyLocal_Success() {
        val originalText = this::class.java.simpleName
        val secretKey = CryptoKeysFactory.createAESKey()
        val encryptedText = Crypton.aesCbcEncryption().encrypt(originalText, secretKey)

        val startTime = System.currentTimeMillis()
        val decryptedText = Crypton.aesCbcEncryption().decryptAsString(encryptedText, secretKey)
        val decryptionTime = System.currentTimeMillis() - startTime

        Log.d("CryptonTest", "Decryption takes $decryptionTime ms.")

        Assert.assertNotNull(decryptedText)
        Assert.assertEquals(originalText, decryptedText)
    }

    @Test
    fun decrypt_afterEncryptionByTheSameSecreteKeyFromKeyStore_Success() {
        val originalText = this::class.java.simpleName
        val secretKey = CryptoKeysFactory.findOrCreateAESKey("AesTestKey")
        val encryptedText = Crypton.aesCbcEncryption().encrypt(originalText, secretKey)

        val startTime = System.currentTimeMillis()
        val decryptedText = Crypton.aesCbcEncryption().decryptAsString(encryptedText, secretKey)
        val decryptionTime = System.currentTimeMillis() - startTime

        Log.d("CryptonTest", "Decryption takes $decryptionTime ms.")

        Assert.assertNotNull(decryptedText)
        Assert.assertEquals(originalText, decryptedText)
    }

    @Test(expected = GeneralSecurityException::class)
    fun decrypt_afterEncryptionByTheDifferentSecretKeys_Fails() {
        val originalText = this::class.java.simpleName
        val encryptedText = Crypton.aesCbcEncryption().encrypt(originalText, CryptoKeysFactory.createAESKey())
        Crypton.aesCbcEncryption().decrypt(encryptedText, CryptoKeysFactory.createAESKey())
    }

    @Test(expected = GeneralSecurityException::class)
    fun decrypt_brokenAfterEncryptionBySecretKey_Fails() {
        val secreteKey = CryptoKeysFactory.createAESKey()
        val originalText = this::class.java.simpleName
        val encryptedText = Crypton.aesCbcEncryption().encrypt(originalText, secreteKey)
        val brokenText = encryptedText.substring(0, encryptedText.length - 4)
        Crypton.aesCbcEncryption().decryptAsString(brokenText, secreteKey)
    }

    @Test(expected = InvalidKeyException::class)
    fun encrypt_withIncorrectSecretKeyType_Fails() {
        val secreteKey = CryptoKeysFactory.createSecreteKey("DES", 64)
        val originalText = this::class.java.simpleName
        Crypton.aesCbcEncryption().encrypt(originalText, secreteKey)
    }

    @Test(expected = InvalidKeyException::class)
    fun decrypt_afterEncryptionWithAesDecryptWithIncorrectSecretKeyType_Fails() {
        val secreteKey = CryptoKeysFactory.createAESKey()
        val originalText = this::class.java.simpleName
        val encrypted = Crypton.aesCbcEncryption().encrypt(originalText, secreteKey)
        Crypton.aesCbcEncryption().decrypt(encrypted, CryptoKeysFactory.createSecreteKey("DES", 64))
    }

    @Test
    fun encrypt_rsaKeyPair_Success() {
        val rsaKeyPair = CryptoKeysFactory.findOrCreateRSAKeyPair(InstrumentationRegistry.getInstrumentation().targetContext, "TEST_CORE_RSA_KEY")
        val originalText = this::class.java.simpleName

        val startTime = System.currentTimeMillis()
        val encrypted = Crypton.rsaEncryption().encrypt(originalText, rsaKeyPair)

        val encryptionTime = System.currentTimeMillis() - startTime
        Log.d("CryptonTest", "Encryption takes $encryptionTime ms.")

        Assert.assertNotNull(encrypted)
        Assert.assertFalse(encrypted == originalText)
    }

    @Test
    fun decrypt_afterEncryptWithRsaKeyPair_Success() {
        val rsaKeyPair = CryptoKeysFactory.findOrCreateRSAKeyPair(InstrumentationRegistry.getInstrumentation().targetContext, "TEST_CORE_RSA_KEY")
        val originalText = this::class.java.simpleName
        val encrypted = Crypton.rsaEncryption().encrypt(originalText, rsaKeyPair)

        val startTime = System.currentTimeMillis()
        val decryptedText = Crypton.rsaEncryption().decryptAsString(encrypted, rsaKeyPair)
        val decryptionTime = System.currentTimeMillis() - startTime
        Log.d("CryptonTest", "Decryption takes $decryptionTime ms.")

        Assert.assertNotNull(decryptedText)
        Assert.assertEquals(originalText, decryptedText)
    }

    @Test
    fun decrypt_afterEncryptWithRsaKeyPairByAlias_Success() {

        val originalText = this::class.java.simpleName
        val encrypted = Crypton.rsaEncryption().encrypt(InstrumentationRegistry.getInstrumentation().targetContext, originalText, "SOME_KEY_RSA")

        val startTime = System.currentTimeMillis()
        val decryptedText = Crypton.rsaEncryption().decryptAsString(InstrumentationRegistry.getInstrumentation().targetContext, encrypted, "SOME_KEY_RSA")
        val decryptionTime = System.currentTimeMillis() - startTime
        Log.d("CryptonTest", "Decryption takes $decryptionTime ms.")

        Assert.assertNotNull(decryptedText)
        Assert.assertEquals(originalText, decryptedText)
    }

    @Test(expected = GeneralSecurityException::class)
    fun decrypt_afterEncryptWithRsaKeyPairByDifferentAlias_Fails() {
        val originalText = this::class.java.simpleName
        val encrypted = Crypton.rsaEncryption().encrypt(InstrumentationRegistry.getInstrumentation().targetContext, originalText, "SOME_KEY_RSA")
        Crypton.rsaEncryption().decrypt(InstrumentationRegistry.getInstrumentation().targetContext, encrypted, "SOME_KEY_RSA_2")
    }
}