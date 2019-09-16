package com.nullgr.core

import com.nullgr.core.prefs.CryptoPreferencesTest
import com.nullgr.core.prefs.PreferencesCryptonTest
import com.nullgr.core.security.crypto.CryptoKeysFactoryTest
import com.nullgr.core.security.crypto.CryptonTest
import com.nullgr.core.security.crypto.SecureUtilsExtensionsTest
import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite::class)
@Suite.SuiteClasses(value = [
    CryptoPreferencesTest::class,
    PreferencesCryptonTest::class,
    CryptoKeysFactoryTest::class,
    CryptonTest::class,
    SecureUtilsExtensionsTest::class
])
class CoreSecurityTestSuite