package com.nullgr.corelibrary.adapter

import android.support.v7.widget.RecyclerView
import com.nhaarman.mockito_kotlin.given
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.spy
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import com.nullgr.core.adapter.items.ListItem
import com.nullgr.core.adapter.withAdapterPosition
import org.junit.Test
import kotlin.test.BeforeTest
import kotlin.test.assertEquals

class ViewHolderExtensionsTest {

    private val block   = { _: ListItem, _: Int -> }
    private val blockSpy = spy(block)

    private lateinit var item: ListItem
    private lateinit var viewHolder: RecyclerView.ViewHolder

    @BeforeTest
    fun setUp(){
        item = mock { ListItem::class.java }
        viewHolder = mock { RecyclerView.ViewHolder::class.java }
    }

    @Test
    fun withAdapterPosition_NoPosition_BlockNotInvoked() {
        val items: List<ListItem>? = null
        val position = RecyclerView.NO_POSITION
        given(viewHolder.adapterPosition).willReturn(position)
        assertEquals(position, viewHolder.adapterPosition)
        viewHolder.withAdapterPosition(items, blockSpy)
        verify(blockSpy, times(0)).invoke(item, position)
    }

    @Test
    fun withAdapterPosition_ItemsNull_BlockNotInvoked() {
        val items: List<ListItem>? = null
        val position = 0
        given(viewHolder.adapterPosition).willReturn(position)
        assertEquals(position, viewHolder.adapterPosition)
        viewHolder.withAdapterPosition(items, blockSpy)
        verify(blockSpy, times(0)).invoke(item, position)
    }

    @Test
    fun withAdapterPosition_PositionLessThatZero_BlockNotInvoked() {
        val items: List<ListItem> = arrayListOf(item)
        val position = -1
        given(viewHolder.adapterPosition).willReturn(position)
        assertEquals(position, viewHolder.adapterPosition)
        viewHolder.withAdapterPosition(items, blockSpy)
        verify(blockSpy, times(0)).invoke(item, position)
    }

    @Test
    fun withAdapterPosition_PositionBiggerThatItemSize_BlockNotInvoked() {
        val items: List<ListItem> = arrayListOf(item)
        val position = 1
        given(viewHolder.adapterPosition).willReturn(position)
        assertEquals(position, viewHolder.adapterPosition)
        viewHolder.withAdapterPosition(items, blockSpy)
        verify(blockSpy, times(0)).invoke(item, position)
    }

    @Test
    fun withAdapterPosition_ValidArguments_BlockInvoked() {
        val items: List<ListItem> = arrayListOf(item)
        val position = 0
        given(viewHolder.adapterPosition).willReturn(position)
        assertEquals(position, viewHolder.adapterPosition)
        viewHolder.withAdapterPosition(items, blockSpy)
        verify(blockSpy, times(1)).invoke(item, position)
    }
}