package com.nullgr.corelibrary.rxcontact;

import com.nullgr.corelibrary.rxcontact.extensions.ExtensionFunctionsTest;
import com.nullgr.corelibrary.rxcontact.validator.UriToMethodValidatorTest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Created by Grishko Nikita on 01.02.18.
 */
@RunWith(Suite.class)
@Suite.SuiteClasses(value = {ExtensionFunctionsTest.class, UriToMethodValidatorTest.class})
public class RxContactsProviderTestSuite {
}
