Kotlin Extension for Core Adapter
============

According to [article](https://proandroiddev.com/kotlin-android-extensions-using-view-binding-the-right-way-707cd0c9e648)  
if you want to work with ```DynamicAdapter``` and keep performance of ```ViewHolder``` bind your ```ListItem```s in next way:  
```kotlin
import com.nullgr.core.adapter.ktx.AdapterDelegate
import com.nullgr.core.adapter.ktx.ViewHolder

import kotlinx.android.synthetic.main.item_some.*

class SomeDelegate : AdapterDelegate() {

    override val layoutResource: Int = R.layout.item_some
    override val itemType: Any = SomeItem::class
    
    override fun onBindViewHolder(items: List<ListItem>, position: Int, holder: RecyclerView.ViewHolder) {
        val item = items[position] as SomeItem

        with(holder as ViewHolder) {
            titleView.text = item.title
            subTitleView.text = item.subTitle
            colorView.setBackgroundColor(item.color)
        }
    }
}
```