package com.nullgr.corelibrary

import com.nullgr.corelibrary.date.DatePackageTestSuite
import com.nullgr.corelibrary.rxcontact.RxContactsProviderTestSuite
import org.junit.runner.RunWith
import org.junit.runners.Suite

/**
 * @author Grishko Nikita
 */
@RunWith(Suite::class)
@Suite.SuiteClasses(value = [RxContactsProviderTestSuite::class, DatePackageTestSuite::class])
class CoreLibraryMasterTestSuite
