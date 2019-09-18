package com.nullgr.androidcore.adapter

import android.graphics.Color
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.widget.CompoundButton
import android.widget.Toast
import androidx.annotation.ColorInt
import androidx.recyclerview.widget.LinearLayoutManager
import com.nullgr.androidcore.R
import com.nullgr.androidcore.adapter.items.ExampleItemWithPayloads
import com.pes.androidmaterialcolorpickerdialog.ColorPicker
import com.pes.androidmaterialcolorpickerdialog.ColorPickerCallback
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_adapter_example_with_payloads.*

/**
 * @author chernyshov.
 */
class AdapterExampleWithPayloadsActivity : BaseAdapterExampleActivity(), ColorPickerCallback {

    private var useDiffUtils: Boolean = true

    @ColorInt
    private var itemColor: Int = Color.RED

    private val colorPicker by lazy(LazyThreadSafetyMode.NONE) { ColorPicker(this) }

    private val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_adapter_example_with_payloads)

        itemsView.layoutManager = LinearLayoutManager(this)
        itemsView.adapter = adapter

        val item = ExampleItemWithPayloads("Initial title", "Initial subtitle", itemColor)
        itemsRelay.accept(arrayListOf(item))

        val disposable = bus.observable()
            .filter { it is Event }
            .map { it as Event }
            .subscribe { event ->
                when (event) {
                    is Event.Click -> Toast.makeText(this, "Clicked ${event.item}", Toast.LENGTH_SHORT).show()
                    is Event.Payload -> {
                        val text = "${logView.text}\n${event.payload}"
                        logView.text = text
                    }
                }
            }

        compositeDisposable.add(disposable)

        titleInputView.setText(item.title)
        subTitleInputView.setText(item.subTitle)
        useDiffUtilsView.isChecked = useDiffUtils
        useDiffUtilsView.setOnCheckedChangeListener { _: CompoundButton, checked: Boolean ->
            useDiffUtils = checked
        }
        pickColorButtonView.setOnClickListener {
            colorPicker.show()
        }
        applyChangesButtonView.setOnClickListener {
            val newTitle = titleInputView.text.toString()
            val newSubTitle = subTitleInputView.text.toString()
            val newItem = item.copy(title = newTitle, subTitle = newSubTitle, color = itemColor)
            if (useDiffUtils) itemsRelay.accept(arrayListOf(newItem))
            else {
                adapter.setData(arrayListOf(newItem))
                adapter.notifyDataSetChanged()
            }
        }

        logView.movementMethod = ScrollingMovementMethod()
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }

    override fun provideDelegatesFactory() = ExampleDelegatesFactory(bus)

    override fun onColorChosen(color: Int) {
        itemColor = color
        pickColorButtonView.setBackgroundColor(itemColor)
        colorPicker.dismiss()
    }
}