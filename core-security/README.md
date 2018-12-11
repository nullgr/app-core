#Core-Security
Provides a set of classes that implement basic security mechanisms.
Globally divided into three packages: ***crypto***, ***fingerprint***, ***prefs***.

Crypto
------
Contains classes that provides simple encryption/decryption mechanisms.
* ```CryptoKeysFactory``` contains fabric methods to generate secret keys.
* ```Crypton``` provides encryption/decryption mechanisms.
Supported transformations: AES/CBC/PKCS7Padding, RSA/ECB/OAEPWITHSHA-512ANDMGF1PADDING.
* ```SecureConverterExtensions``` contains set of extension functions, that provides 
simple transformation such as MD5, SHA512, Base64.

Fingerprint
-----------
This package contains classes that simplify the implementation of interaction with the fingerprint sensor.
Also contains rx based implementation and utilities that provide encryption / decryption.
requiring user authorization.

Simple usage:
```kotlin
 val fingerprintAuthenticationManager = FingerprintAuthenticationManager
                .from(it)
                .attachView(fingerprintView)
                .withResultListener(object : FingerprintResultListener {
                    override fun onSuccess(cryptoObject: FingerprintManagerCompat.CryptoObject?) {
                        dismissAllowingStateLoss()
                    }
                })
                .build()
                
 fingerprintAuthenticationManager.startListening()
```
For more detailed implementation guide and rx based implementation please look 
[sample project](../app/src/main/java/com/nullgr/androidcore/fingerprint)

Prefs
-----
This package represents ```CryptoPreferences``` - secure wrapper for ```SharedPreferences```.
This class used to safe storing of secure needed information in simple ```SharedPreferences```.
An encryption/decryption mechanism depends on current SDK version. 

Sample usage:
```kotlin
// MY_KEY_ALIAS will be used to create key in AndroidKeyStore. 
// For android version before Build.VERSION_CODES.JELLY_BEAN_MR2 this parameter will be used as part of key chain 
// "my-secure-storage-file-name" - the end part of preferences file name. 
// As first part your application package name will be used.
val cryptoPreferences = CryptoPreferences(context, MY_KEY_ALIAS, "my-secure-storage-file-name")
cryptoPreferences.setString("MyKey","SomeSecureData")
val encryptedData = cryptoPreferences.getString("MyKey","DefaultValue")
```

