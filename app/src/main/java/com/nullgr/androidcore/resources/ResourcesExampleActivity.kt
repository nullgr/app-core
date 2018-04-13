package com.nullgr.androidcore.resources

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.nullgr.androidcore.R
import com.nullgr.corelibrary.resources.ResourceProvider
import com.nullgr.corelibrary.resources.dpToPx
import com.nullgr.corelibrary.resources.getDisplaySize
import com.nullgr.corelibrary.resources.pxToDp
import kotlinx.android.synthetic.main.activity_resources_example.*

/**
 * Created by Grishko Nikita on 01.02.18.
 */
class ResourcesExampleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resources_example)
        val resourceProvider = ResourceProvider(this)

        val emptyMessage = resourceProvider.getString(R.string.msg_error_empty)

        stringSampleTitle.text = resourceProvider.getString(R.string.string_example_one, 1)

        buttonConvertPxToDp.setOnClickListener {
            if (pxToDpExampleInput.text.isNullOrEmpty()) {
                showToast(emptyMessage)
            } else {
                val pxValue = pxToDpExampleInput.text.toString().toFloatOrNull()
                showToast(pxValue?.pxToDp(this)?.toString() ?: emptyMessage)
            }
        }

        buttonConvertDpToPx.setOnClickListener {
            if (dpToPxExampleInput.text.isNullOrEmpty()) {
                showToast(emptyMessage)
            } else {
                val dpValue = dpToPxExampleInput.text.toString().toFloatOrNull()
                showToast(dpValue?.dpToPx(this)?.toString() ?: emptyMessage)
            }
        }

        buttonShowScreenSize.setOnClickListener {
            val displaySize = getDisplaySize(this)
            showToast(resourceProvider.getString(R.string.msg_screen_size, displaySize.first, displaySize.second))
        }

        buttonSetImageByName.setOnClickListener {
            val imageResId = resourceProvider.getDrawableId("ic_launcher_round")
            imageByName.setImageResource(imageResId)
        }
    }


    private fun showToast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }
}