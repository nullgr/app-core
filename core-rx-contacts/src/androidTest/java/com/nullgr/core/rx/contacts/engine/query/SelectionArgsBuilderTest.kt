package com.nullgr.core.rx.contacts.engine.query

import android.support.test.runner.AndroidJUnit4

import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

/**
 * @author Grishko Nikita
 */
@RunWith(AndroidJUnit4::class)
class SelectionArgsBuilderTest {

    @Test
    fun buildWhere_SingleStringSelectionArg_Equals() {
        val whereArg = SelectionArgsBuilder.buildWhere("Test User")
        Assert.assertEquals(" = 'Test User'", whereArg)
    }

    @Test
    fun buildLike_SingleStringSelectionArg_Equals() {
        val whereArg = SelectionArgsBuilder.buildLike("Test User")
        Assert.assertEquals(" LIKE '%Test User%'", whereArg)
    }

    @Test
    fun buildIn_StringArraySelectionArg_Equals() {
        val whereArg = SelectionArgsBuilder.buildIn(arrayOf("User1", "User2"))
        Assert.assertEquals(" IN ('User1','User2')", whereArg)
    }
}
