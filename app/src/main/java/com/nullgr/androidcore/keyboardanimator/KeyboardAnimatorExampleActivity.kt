package com.nullgr.androidcore.keyboardanimator

import android.annotation.TargetApi
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import android.view.WindowManager
import com.nullgr.androidcore.R
import com.nullgr.androidcore.keyboardanimator.dialog.SimpleBottomSheetDialog
import com.nullgr.core.ui.keyboardanimator.BaseKeyboardAnimator
import com.nullgr.core.ui.keyboardanimator.simple.SimpleKeyboardAnimator
import kotlinx.android.synthetic.main.activity_keyboard_animator_example.*

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
class KeyboardAnimatorExampleActivity : AppCompatActivity() {

    private val animator: BaseKeyboardAnimator by lazy(LazyThreadSafetyMode.NONE) { SimpleKeyboardAnimator(window) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        setContentView(R.layout.activity_keyboard_animator_example)
        showBottomSheetView.setOnClickListener {
            SimpleBottomSheetDialog(this, checkBoxView.isChecked).show()
        }
        checkBoxView.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) animator.start() else animator.stop()
        }
    }
}