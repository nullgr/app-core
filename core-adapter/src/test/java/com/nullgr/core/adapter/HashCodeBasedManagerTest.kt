package com.nullgr.core.adapter

import com.nullgr.core.adapter.items.ListItem
import org.junit.Assert.*
import org.junit.Test

class HashCodeBasedManagerTest {

    @Test
    fun getItemViewType_TwoManagers_SamePosition_DifferentViewTypes() {
        val factory = object : AdapterDelegatesFactory {
            override fun createDelegate(clazz: Class<ListItem>): AdapterDelegate {
                return when (clazz) {
                    TestItem1::class.java -> TestDelegate1()
                    TestItem2::class.java -> TestDelegate2()
                    else -> throw IllegalArgumentException()
                }
            }
        }
        val outerManager = HashCodeBasedAdapterDelegatesManager(factory)
        val innerManager = HashCodeBasedAdapterDelegatesManager(factory)

        val outerType = outerManager.getItemViewType(arrayListOf(TestItem1()), 0)
        val innerType = innerManager.getItemViewType(arrayListOf(TestItem2()), 0)

        assertTrue(outerType != 0)
        assertTrue(innerType != 0)
        assertTrue(outerType != innerType)
    }

    @Test
    fun getItemViewType_OneManger_SimilarItems_DifferentViewTypes() {
        val factory = object : AdapterDelegatesFactory {
            override fun createDelegate(clazz: Class<ListItem>): AdapterDelegate {
                return when (clazz) {
                    TestItem::class.java -> TestItemDelegate()
                    ItemTest::class.java -> ItemTestDelegate()
                    else -> throw IllegalArgumentException()
                }
            }
        }
        val manager = HashCodeBasedAdapterDelegatesManager(factory)

        val viewType1 = manager.getItemViewType(arrayListOf(TestItem()), 0)
        val viewType2 = manager.getItemViewType(arrayListOf(ItemTest()), 0)

        assertTrue(viewType1 != 0)
        assertTrue(viewType2 != 0)
        assertTrue(viewType1 != viewType2)
    }
}