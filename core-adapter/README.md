Core Adapter
============

This module represents work with DynamicAdapter.
```DynamicAdapter``` is adapter for ```ListItem```s based on 
[Hannes Dorfmann AdapterDelegates](https://github.com/sockeqwe/AdapterDelegates)
but without necessity to create all ```AdapterDelegate```s with adapter creation.
Delegates creates by necessity via factory depends on set of ```ListItem``` added to adapter.
Also ```DynamicAdapter``` encapsulates logic of working with ```DiffUtil```.

Example
============

First need to init ```DynamicAdapter```:

```kotlin
val delegatesFactory = SomeAdapterDelegatesFactory()
val adapter = DynamicAdapter(delegatesFactory)
```

```AdapterDelegatesFactory``` could be like this:
```kotlin
class SomeDelegatesFactory : AdapterDelegatesFactory {

    override fun createDelegate(clazz: Class<ListItem>): AdapterDelegate =
            when (clazz) {
                SomeItem1::class.java -> SomeDelegate1()
                else -> throw IllegalArgumentException("No delegate defined for ${clazz.simpleName}")
            }
}
```

For ```ListItem``` subclasses we recommend to use Kotlin data classes.

If you need to updated some view that represents one of item property using DiffUtils - override
```ListItem.getChangePayload```.

Example of ```ListItem``` with payloads:
```kotlin
data class SomeItemWithPayloads(
    val id: String,
    val title: String,
    val subTitle: String,
    @ColorInt val color: Int,
    override val uniqueProperty: Any = id
) : ListItem {

    override fun getChangePayload(other: ListItem): Any {
        if (this::class == other::class) {
            other as ExampleItemWithPayloads
            return mutableSetOf<Payload>().apply {
                if (title != other.title) add(Payload.TITLE_CHANGED)
                if (subTitle != other.subTitle) add(Payload.SUB_TITLE_CHANGED)
                if (color != other.color) add(Payload.COLOR_CHANGED)
            }
        }
        return super.getChangePayload(other)
    }

    enum class Payload {
        TITLE_CHANGED, SUB_TITLE_CHANGED, COLOR_CHANGED
    }
}
```

```AdapterDelegate``` for ```SomeItemWithPayloads``` could be like this:
```kotlin
class SomeDelegateWithPayloads : AdapterDelegate() {

    override val layoutResource: Int = R.layout.item_some_with_payloads
    override val itemType: Any = SomeItemWithPayloads::class
    
    override fun onBindViewHolder(items: List<ListItem>, position: Int, holder: RecyclerView.ViewHolder) {
        val item = items[position] as SomeItemWithPayloads

        with(holder.itemView) {
            titleView.text = item.title
            subTitleView.text = item.subTitle
            colorView.setBackgroundColor(item.color)
        }
    }
    
    override fun onBindViewHolder(items: List<ListItem>, position: Int, holder: RecyclerView.ViewHolder, payload: Any) {
        val item = items[position] as SomeItemWithPayloads
        when (payload) {
            SomeItemWithPayloads.Payload.TITLE_CHANGED -> {
                holder.itemView.titleView.text = item.title
            }
            SomeItemWithPayloads.Payload.SUB_TITLE_CHANGED -> {
                holder.itemView.subTitleView.text = item.subTitle
            }
            SomeItemWithPayloads.Payload.COLOR_CHANGED -> {
                holder.itemView.colorView.setBackgroundColor(item.color)
            }
        }
    }
}
```

For click handling you should override ```AdapterDelegate.onCreateViewHolder```
```kotlin
override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        return super.onCreateViewHolder(parent).apply {
            val onClickListener = View.OnClickListener { view ->
                this.withAdapterPosition<SomeItem> { item, position ->
                    when (view.id) {
                        // handle click action depends on view id
                    }
                }
            }
            this.itemView.setOnClickListener(onClickListener)
        }
    }
```

In this example we used [extensions for ViewHolder](../core-adapter/src/main/java/com/nullgr/core/adapter/ViewHolderExtensions.kt)
to get access to items from adapter but you can handle click in regular way without it.

**Pay attention** in simple ```AdapterDelegate.onBindViewHolder``` or with payloads, ```items: List<ListItem>``` 
passed as argument valid only for particular method call, this means that you can't 
use ```AdapterDelegate.onBindViewHolder``` for setting listeners if you are going 
to update items with payloads. In this case listener will hold reference to old list of items 
and you will receive not valid result.

**Wrong way:**
```kotlin
override fun onBindViewHolder(items: List<ListItem>, position: Int, holder: RecyclerView.ViewHolder) {
        val item = items[position] as ExampleItemWithPayloads

        with(holder.itemView) {
            titleView.text = item.title
            subTitleView.text = item.subTitle
            colorView.setBackgroundColor(item.color)
            setOnClickListener { 
                val clickedItem = items[position]
                // some other actions with item
            }
        }
    }
```

For full implementation guide, view [sample project.](../app/src/main/java/com/nullgr/androidcore/adapter)