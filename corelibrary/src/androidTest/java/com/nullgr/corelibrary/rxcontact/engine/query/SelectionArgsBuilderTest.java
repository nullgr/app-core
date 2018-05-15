package com.nullgr.corelibrary.rxcontact.engine.query;

import android.support.test.runner.AndroidJUnit4;

import com.nullgr.corelibrary.rxcontacts.engine.query.SelectionArgsBuilder;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Created by Grishko Nikita on 01.02.18.
 */
@SuppressWarnings("KotlinInternalInJava")
@RunWith(AndroidJUnit4.class)
public class SelectionArgsBuilderTest {

    @Test
    public void buildWhere_SingleStringSelectionArg_Equals() {
        String whereArg = SelectionArgsBuilder.INSTANCE.buildWhere("Test User");
        Assert.assertEquals(" = 'Test User'", whereArg);
    }

    @Test
    public void buildLike_SingleStringSelectionArg_Equals() {
        String whereArg = SelectionArgsBuilder.INSTANCE.buildLike("Test User");
        Assert.assertEquals(" LIKE '%Test User%'", whereArg);
    }

    @Test
    public void buildIn_StringArraySelectionArg_Equals() {
        String whereArg = SelectionArgsBuilder.INSTANCE.buildIn(new String[]{"User1", "User2"});
        Assert.assertEquals(" IN ('User1','User2')", whereArg);
    }
}
