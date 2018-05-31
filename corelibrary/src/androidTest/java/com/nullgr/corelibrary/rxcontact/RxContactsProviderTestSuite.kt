package com.nullgr.corelibrary.rxcontact

import com.nullgr.corelibrary.rxcontact.engine.cursor.CursorFactoryTest
import com.nullgr.corelibrary.rxcontact.engine.cursor.ProjectionFactoryTest
import com.nullgr.corelibrary.rxcontact.engine.query.SelectionArgsBuilderTest
import com.nullgr.corelibrary.rxcontact.extensions.ExtensionFunctionsTest
import com.nullgr.corelibrary.rxcontact.validator.UriToMethodValidatorTest

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
