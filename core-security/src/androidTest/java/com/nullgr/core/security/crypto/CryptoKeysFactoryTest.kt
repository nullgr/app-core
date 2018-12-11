package com.nullgr.core.security.crypto

import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import java.security.InvalidParameterException
import java.security.NoSuchAlgorithmException

/**
 * Created by Grishko Nikita on 01.02.18.
 */
@RunWith(AndroidJUnit4::class)
class CryptoKeysFactoryTest {

    @Test
    fun createAESKey_validKey_Success() {
        val secreteKey = CryptoKeysFactory.createAESKey()
        Assert.assertNotNull(secreteKey)
        Assert.assertEquals("AES", secreteKey.algorithm)
    }

    @Test(expected = NoSuchAlgorithmException::class)
    fun createSecretKey_incorrectAlgorithm_Fail() {
        CryptoKeysFactory.createSecreteKey("SOME", 128)
    }

    @Test
    fun createSecretKey_desAlgorithm_Success() {
        val secreteKey = CryptoKeysFactory.createSecreteKey("DES", 64)
        Assert.assertNotNull(secreteKey)
        Assert.assertEquals("DES", secreteKey.algorithm)
    }

    @Test(expected = InvalidParameterException::class)
    fun createSecretKey_desAlgorithmWithIncorrectLength_Success() {
        val secreteKey = CryptoKeysFactory.createSecreteKey("DES", 128)
        Assert.assertNotNull(secreteKey)
        Assert.assertEquals("DES", secreteKey.algorithm)
    }

    @Test
    fun createPKS12_correctKey_Success() {
        val secreteKey = CryptoKeysFactory.createPKCS12Key(CryptoKeysFactory.generateSalt(), "AnyPassword")
        Assert.assertNotNull(secreteKey)
        Assert.assertEquals("PBEWithSHA256And256BitAES-CBC-BC", secreteKey.algorithm)
    }

    @Test
    fun createRSAKeyPair_correctKey_Success() {
        val rsaPair = CryptoKeysFactory.createRSAKeyPair(InstrumentationRegistry.getTargetContext(), "TestKeyAlias")
        Assert.assertNotNull(rsaPair)
        Assert.assertEquals("RSA", rsaPair.public.algorithm)
        Assert.assertEquals("RSA", rsaPair.private.algorithm)
    }

    @Test
    fun createRSAKeyPairUserAuthRequired_correctKey_Success() {
        val rsaPair = CryptoKeysFactory.createRSAKeyPairUserAuthRequired("UserAuthKey")
        Assert.assertNotNull(rsaPair)
        Assert.assertEquals("RSA", rsaPair.public.algorithm)
        Assert.assertEquals("RSA", rsaPair.private.algorithm)
    }

    @Test
    fun createAESKeyInAndroidKeyStore_correctKey_Success() {
        val aesKey = CryptoKeysFactory.createAESKeyInAndroidKeystore("AES_TEST_KEY")
        Assert.assertNotNull(aesKey)
        Assert.assertEquals("AES", aesKey.algorithm)
    }

    @Test
    fun findOrCreateRSAKeyPair_sameAlias_Success() {
        val newRsaPair = CryptoKeysFactory.createRSAKeyPair(
            InstrumentationRegistry.getTargetContext(),
            "OneMore_RSA_Key"
        )
        val rsaPairFromKeyStore = CryptoKeysFactory.findOrCreateRSAKeyPair(
            InstrumentationRegistry.getTargetContext(),
            "OneMore_RSA_Key"
        )
        Assert.assertNotNull(newRsaPair)
        Assert.assertNotNull(rsaPairFromKeyStore)
        Assert.assertEquals(
            newRsaPair.public.encoded.toHexDecimalString(),
            rsaPairFromKeyStore.public.encoded.toHexDecimalString()
        )
    }

    @Test
    fun createRSAKeyPairUserAuthRequired_sameAlias_Success() {
        val newRsaPair = CryptoKeysFactory.createRSAKeyPairUserAuthRequired(
            "UserAuthBased_RSA_Key"
        )
        val rsaPairFromKeyStore = CryptoKeysFactory.findOrCreateRSAKeyPairUserAuthRequired(
            "UserAuthBased_RSA_Key"
        )
        Assert.assertNotNull(newRsaPair)
        Assert.assertNotNull(rsaPairFromKeyStore)
        Assert.assertEquals(
            newRsaPair.public.encoded.toHexDecimalString(),
            rsaPairFromKeyStore.public.encoded.toHexDecimalString()
        )
    }

    @Test
    fun findOrCreateAESKey_sameAlias_Success() {
        val newSecreteKey = CryptoKeysFactory.createAESKeyInAndroidKeystore(
            "KeyStore_AES_Key"
        )
        val secreteKeyFromKeyStore = CryptoKeysFactory.findOrCreateAESKey(
            "KeyStore_AES_Key"
        )
        Assert.assertNotNull(newSecreteKey)
        Assert.assertNotNull(secreteKeyFromKeyStore)
        Assert.assertEquals(
            newSecreteKey,
            secreteKeyFromKeyStore
        )
    }

    @Test
    fun findOrCreateAESKey_differentAlias_Fails() {
        val newSecreteKey = CryptoKeysFactory.createAESKeyInAndroidKeystore(
            "KeyStore_AES_Key"
        )
        val secreteKeyFromKeyStore = CryptoKeysFactory.findOrCreateAESKey(
            "Other_KeyStore_AES_Key"
        )
        Assert.assertNotNull(newSecreteKey)
        Assert.assertNotNull(secreteKeyFromKeyStore)
        Assert.assertNotEquals(
            newSecreteKey,
            secreteKeyFromKeyStore
        )
    }
}