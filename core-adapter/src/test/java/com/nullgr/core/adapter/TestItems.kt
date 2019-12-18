package com.nullgr.core.adapter

import com.nullgr.core.adapter.items.ListItem

open class BaseTestItem : ListItem {
    override val uniqueProperty: Any = this::class.toString()
}

class TestItem1 : BaseTestItem()
class TestItem2 : BaseTestItem()
class TestItem : BaseTestItem()
class ItemTest : BaseTestItem()