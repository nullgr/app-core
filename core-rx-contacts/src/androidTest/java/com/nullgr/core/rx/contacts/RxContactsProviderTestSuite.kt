package com.nullgr.core.rx.contacts

import com.nullgr.core.rx.contacts.engine.cursor.CursorFactoryTest
import com.nullgr.core.rx.contacts.engine.cursor.ProjectionFactoryTest
import com.nullgr.core.rx.contacts.engine.query.SelectionArgsBuilderTest
import com.nullgr.core.rx.contacts.extensions.ExtensionFunctionsTest
import com.nullgr.core.rx.contacts.validator.UriToMethodValidatorTest

import org.junit.runner.RunWith
import org.junit.runners.Suite

/**
 * @author Grishko Nikita
 */
@RunWith(Suite::class)
@Suite.SuiteClasses(value = [ExtensionFunctionsTest::class,
    UriToMethodValidatorTest::class,
    CursorFactoryTest::class,
    ProjectionFactoryTest::class,
    SelectionArgsBuilderTest::class])
class RxContactsProviderTestSuite
