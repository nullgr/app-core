package com.nullgr.core.adapter

class TestDelegate1 : AdapterDelegate() {
    override val layoutResource: Int = 1
    override val itemType: Any = TestItem1::class
}

class TestDelegate2 : AdapterDelegate() {
    override val layoutResource: Int = 2
    override val itemType: Any = TestItem2::class
}

class TestItemDelegate : AdapterDelegate() {
    override val layoutResource: Int = 11
    override val itemType: Any = TestItem::class
}

class ItemTestDelegate : AdapterDelegate() {
    override val layoutResource: Int = 12
    override val itemType: Any = ItemTest::class
}