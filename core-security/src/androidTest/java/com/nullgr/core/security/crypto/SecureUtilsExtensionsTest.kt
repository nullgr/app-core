package com.nullgr.core.security.crypto

import android.support.test.runner.AndroidJUnit4
import junit.framework.Assert
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Created by Grishko Nikita on 01.02.18.
 */
@RunWith(AndroidJUnit4::class)
class SecureUtilsExtensionsTest {

    @Test
    fun toMd5String_compareWithOriginalString_NotSuccess() {
        val originalString = this::class.java.simpleName
        Assert.assertFalse(originalString.toMd5String() == originalString)
    }

    @Test
    fun toMd5String_compareWithEncryptedString_Success() {
        val originalString = this::class.java.simpleName
        Assert.assertEquals(originalString.toMd5String(), "4c3ee751490123d90d0497eb11e8109b")
    }

    @Test
    fun toSHA512String_compareWithOriginalString_NotSuccess() {
        val originalString = this::class.java.simpleName
        Assert.assertFalse(originalString.toSHA512String() == originalString)
    }

    @Test
    fun toSHA512String_compareWithEncryptedString_Success() {
        val originalString = this::class.java.simpleName
        Assert.assertEquals(originalString.toSHA512String(), "bee67cdc56ddfabd11a16673edcee9d72c43a01f8a29b31d38e544efed459caf8dc7a03bf7b846b633cbbc0d779b63ea5d36c1aea369f689633dfa0225c03921")
    }

    @Test
    fun toBase64_compareWithOriginalString_NotSuccess() {
        val originalString = this::class.java.simpleName
        Assert.assertFalse(originalString.toBase64() == originalString)
    }

    @Test
    fun toBase64_compareWithBase44EncryptedString_Success() {
        val originalString = this::class.java.simpleName
        Assert.assertEquals(originalString.toBase64(), "U2VjdXJlVXRpbHNFeHRlbnNpb25zVGVzdA==")
    }

    @Test
    fun toBase64_fromBase64EqualsOriginalString_Success() {
        val originalString = this::class.java.simpleName
        val base64String = originalString.toBase64()
        Assert.assertEquals(base64String.fromBase64(), originalString)
    }

    @Test
    fun toHexDecimal_compareWithHexDecimalString_Success() {
        val originalString = this::class.java.simpleName
        Assert.assertEquals(originalString.toByteArray().toHexDecimalString(), "5365637572655574696c73457874656e73696f6e7354657374")
    }

    @Test
    fun fromHexDecimal_compareWithToHexDecimalResultString_Success() {
        val originalString = this::class.java.simpleName
        val hexDecimalString = originalString.toByteArray().toHexDecimalString()
        Assert.assertEquals(originalString, hexDecimalString.fromHexDecimalString())
    }
}