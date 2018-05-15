package com.nullgr.corelibrary.date

import org.junit.runner.RunWith
import org.junit.runners.Suite

/**
 * Created by Grishko Nikita on 01.02.18.
 */
@RunWith(Suite::class)
@Suite.SuiteClasses(value = [DateFormatExtensionsTest::class, DateHelperExtensionsTest::class])
class DatePackageTestSuite