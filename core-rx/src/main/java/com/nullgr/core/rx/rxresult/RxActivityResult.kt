package com.nullgr.core.rx.rxresult

import android.content.Intent

/**
 * Class that wraps result from [android.app.Activity.onActivityResult]
 *
 * @property intent intent which usually sets with [android.app.Activity.setResult]
 * @property resultCode result code which usually sets with [android.app.Activity.setResult].
 * Can be [android.app.Activity.RESULT_OK] or [android.app.Activity.RESULT_CANCELED]
 * @author Grishko Nikita
 */
data class RxActivityResult(val intent: Intent?, val resultCode: Int)