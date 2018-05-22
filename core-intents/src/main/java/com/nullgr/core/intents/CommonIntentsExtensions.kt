package com.nullgr.core.intents

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.provider.ContactsContract
import android.provider.MediaStore
import android.support.annotation.ColorInt
import android.support.annotation.RequiresPermission
import android.support.customtabs.CustomTabsIntent
import android.telephony.PhoneNumberUtils
import android.text.TextUtils
import android.widget.Toast
import com.nullgr.core.rx.RxBus
import com.nullgr.core.rx.SingletonRxBusProvider
import com.nullgr.core.rx.rxresult.RxActivityResult
import com.nullgr.core.rx.rxresult.RxResolveResultActivity
import io.reactivex.Observable
import java.util.*

/**
 * Fabric function that creates new [Intent] with [Intent.ACTION_DIAL] and given [number] as target
 */
fun callIntent(number: String): Intent {
    return Intent(Intent.ACTION_DIAL)
            .setData(Uri.parse(if (number.toLowerCase().startsWith("tel:")) number
            else String.format("tel:%s", PhoneNumberUtils.stripSeparators(number))))
}

/**
 * Fabric function that creates new [Intent] with [Intent.ACTION_VIEW] and given [url] as target.
 */
fun webIntent(url: String): Intent {
    return Intent(Intent.ACTION_VIEW).setData(Uri.parse(url))
}

/**
 * Fabric function that creates new [Intent] with [Intent.ACTION_SEND]
 * @param to an email address
 * @param subject subject of email (optional)
 * @param body email body (optional)
 */
fun emailIntent(to: String, subject: String? = null, body: String? = null): Intent {
    return Intent(android.content.Intent.ACTION_SEND).apply {
        type = "plain/text"
        putExtra(Intent.EXTRA_EMAIL, arrayOf(to))
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        if (!subject.isNullOrEmpty()) {
            putExtra(android.content.Intent.EXTRA_SUBJECT, subject)
        }
        if (!body.isNullOrEmpty()) {
            putExtra(android.content.Intent.EXTRA_TEXT, body)
        }
    }
}

/**
 * Fabric function that creates new [Intent] with [Intent.ACTION_SEND]
 * @param text [String] text to share
 */
fun shareTextIntent(text: String): Intent {
    return Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, text)
    }
}

/**
 * Fabric function that creates new [Intent] to launch application by it [packageName], or launch
 * GooglePlay to view application there (this case is optional, if you need to enable this case -
 * set true as value of [googlePlayRedirect] param)
 * @param context [Context].
 * @param packageName [String] value of application package name
 * @param googlePlayRedirect [Boolean] flag that indicates if needs to redirect to GooglePlay
 * in case when application dosen't exist
 * @return [Intent] to open application or to open GooglePlay. Can be null if application dosen't exist,
 * and [googlePlayRedirect] flag has false value
 */
fun applicationIntent(context: Context, packageName: String, googlePlayRedirect: Boolean = false): Intent? {

    val pm = context.packageManager

    val packagePresent = try {
        pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES)
        true
    } catch (e: PackageManager.NameNotFoundException) {
        false
    }
    return when {
        packagePresent -> pm.getLaunchIntentForPackage(packageName)
        googlePlayRedirect -> webIntent("http://play.google.com/store/apps/details?id=$packageName")
        else -> null
    }
}

/**
 * Fabric function that creates new [Intent] with [Intent.ACTION_VIEW] to view contact with given [contactId]
 * @param contactId [String] id of contact
 */
fun contactCardIntent(contactId: String): Intent {
    return Intent(Intent.ACTION_VIEW).apply {
        data = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_URI, contactId)
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
}

/**
 * Fabric function that creates new [Intent] with [Intent.ACTION_PICK] to pick contact.
 * Requires [Manifest.permission.READ_CONTACTS].
 * Common usage:
 * ```
 * startActivityForResult(selectContactIntent(), REQUEST_CODE)
 * ```
 * or use together with [launchForResult] or reactive [launchForResult] functions
 * ```
 * selectContactIntent().launchForResult(context, REQUEST_CODE)
 * ```
 */
@SuppressLint("MissingPermission")
@RequiresPermission(Manifest.permission.READ_CONTACTS)
fun selectContactIntent(): Intent {
    return Intent(Intent.ACTION_PICK).apply {
        type = ContactsContract.Contacts.CONTENT_TYPE
    }
}

/**
 * Fabric function that creates new [Intent] with [Intent.ACTION_PICK] to pick contact phone.
 * Requires [Manifest.permission.READ_CONTACTS].
 * Common usage:
 * ```
 * startActivityForResult(selectContactPhoneIntent(), REQUEST_CODE)
 * ```
 * or use together with [launchForResult] or reactive [launchForResult] functions
 * ```
 * selectContactPhoneIntent().launchForResult(context, REQUEST_CODE)
 * ```
 */
@SuppressLint("MissingPermission")
@RequiresPermission(Manifest.permission.READ_CONTACTS)
fun selectContactPhoneIntent(): Intent {
    return Intent(Intent.ACTION_PICK).apply {
        type = ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE
    }
}

/**
 * Fabric function that creates new [Intent] with [Intent.ACTION_PICK] to pick contact email.
 * Requires [Manifest.permission.READ_CONTACTS].
 * Common usage:
 * ```
 * startActivityForResult(selectContactEmailIntent(), REQUEST_CODE)
 * ```
 * or use together with [launchForResult] or reactive [launchForResult] functions
 * ```
 * selectContactEmailIntent().launchForResult(context, REQUEST_CODE)
 * ```
 */
@SuppressLint("MissingPermission")
@RequiresPermission(android.Manifest.permission.READ_CONTACTS)
fun selectContactEmailIntent(): Intent {
    return Intent(Intent.ACTION_PICK).apply {
        type = ContactsContract.CommonDataKinds.Email.CONTENT_TYPE
    }
}

/**
 * Fabric function that creates new [Intent] with [Intent.ACTION_VIEW]
 * to open GoogleMaps (com.google.android.apps.maps) application and build route to location with give
 * [lat], [lng]
 *
 * @param lat [Double] latitude
 * @param lng [Double] longitude
 */
fun navigationIntent(lat: Double, lng: Double): Intent {
    return Intent(Intent.ACTION_VIEW, Uri.parse(String.format("google.navigation:q=%s,%s", lat, lng)))
            .apply { `package` = "com.google.android.apps.maps" }
}

/**
 * Fabric function that creates new [Intent] with [Intent.ACTION_SEND], to share image and text.
 * Requires [Manifest.permission.WRITE_EXTERNAL_STORAGE] permission
 *
 * @param text [String] text to share
 * @param image [Bitmap] to share
 */
@RequiresPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
@Throws(IllegalStateException::class)
fun shareImageAndTextIntent(context: Context, image: Bitmap, text: String?): Intent {

    val path = MediaStore.Images.Media.insertImage(context.contentResolver,
            image, String.format("ShareImage_%s", UUID.randomUUID().toString()), null)

    if (TextUtils.isEmpty(path))
        throw IllegalStateException("Unable to insert image!")

    val imageUri = Uri.parse(path)

    return Intent().apply {
        action = Intent.ACTION_SEND
        if (!text.isNullOrEmpty())
            putExtra(Intent.EXTRA_TEXT, text)
        putExtra(Intent.EXTRA_STREAM, imageUri)
        type = "image/*"
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }
}

/**
 * Fabric function that creates new [Intent] with [Intent.ACTION_SEND],to share list of images and text.
 * Requires [Manifest.permission.WRITE_EXTERNAL_STORAGE] permission
 *
 * @param text [String] text to share
 * @param images [List] of [Bitmap] to share
 */
@RequiresPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
@Throws(IllegalStateException::class)
fun shareListOfImagesAndTextIntent(context: Context, images: List<Bitmap>, text: String?): Intent {

    val uriList = arrayListOf<Uri>()

    for (bitmap in images) {
        val path = MediaStore.Images.Media.insertImage(context.contentResolver,
                bitmap, String.format("ShareImage_%s", UUID.randomUUID().toString()), null)

        if (TextUtils.isEmpty(path))
            throw IllegalStateException("Unable to insert image!")

        uriList.add(Uri.parse(path))
    }

    return Intent().apply {
        action = Intent.ACTION_SEND
        if (!text.isNullOrEmpty())
            putExtra(Intent.EXTRA_TEXT, text)
        putParcelableArrayListExtra(Intent.EXTRA_STREAM, uriList)
        type = "image/*"
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }
}

/**
 * Fabric function that creates new [CustomTabsIntent] with specified [tabsColor]
 * Common usage (using [launch] function from this library):
 * ```
 * chromeTabsIntent().launch(context, "http://someurl.com")
 * ```
 * or you can use [CustomTabsIntent.launchUrl]
 * @param tabsColor [Int] color
 * @return instance of [CustomTabsIntent]
 */
fun chromeTabsIntent(@ColorInt tabsColor: Int? = null): CustomTabsIntent {
    val builder = CustomTabsIntent.Builder()
    tabsColor?.let { builder.setToolbarColor(it) }
    return builder.build()
}

/**
 * Shorter version of [CustomTabsIntent.launchUrl],
 * created to launch all any intent in the same code style.
 */
fun CustomTabsIntent.launch(context: Context, url: String) {
    launchUrl(context, Uri.parse(url))
}

/**
 * Extension function to start activity in pretty way,
 * or display [noActivityErrorMessage] if activity dosen't exist.
 */
fun Intent?.launch(context: Context?, noActivityErrorMessage: String? = null) {
    if (this != null && context != null && this.resolveActivity(context.packageManager) != null) {
        context.startActivity(this)
    } else if (!noActivityErrorMessage.isNullOrEmpty()) {
        Toast.makeText(context, noActivityErrorMessage, Toast.LENGTH_SHORT).show()
    }
}

/**
 * Extension function to start activity for result in pretty way,
 * or display [noActivityErrorMessage] if activity dosen't exist.
 */
fun Intent?.launchForResult(context: Activity?, requestCode: Int, noActivityErrorMessage: String? = null) {
    if (this != null && context != null && this.resolveActivity(context.packageManager) != null) {
        context.startActivityForResult(this, requestCode)
    } else if (!noActivityErrorMessage.isNullOrEmpty()) {
        Toast.makeText(context, noActivityErrorMessage, Toast.LENGTH_SHORT).show()
    }
}

/**
 * Extension function to send broadcast in pretty way
 */
fun Intent?.sendBroadcast(context: Context?) {
    if (this != null && context != null) {
        context.sendBroadcast(this)
    }
}


/**
 * Extension function to start service in pretty way
 */
fun Intent?.launchService(context: Context?) {
    if (this != null && context != null) {
        context.startService(this)
    }
}

/**
 * Extension function to get activity result, in reactive way.
 * Can be called from any place in your application. With this function you have no need to override
 * [Activity.onActivityResult]. Just do something like this:
 * ```
 * selectContactIntent()
 *        .launchForResult()
 *        .subscribe(
 *            { doSomeThingWithResult(rxActivityResult)},
 *            { handleResultError(throwable)})
 *
 * ```
 * Its safe to call this function in onClick or etc.
 * Every new subscription will replace previous one.
 * So you can dispose it only one last time in onDestroy. 
 *
 * @param context [Activity]
 * @return [Observable] that emits [RxActivityResult]
 * which contains result code [RxActivityResult.resultCode] and result intent [RxActivityResult.intent],
 * or [Observable.error] with [ActivityNotFoundException] if activity dosen't exist
 */
fun Intent?.launchForResult(context: Activity?): Observable<RxActivityResult> {
    return if (this != null && context != null && this.resolveActivity(context.packageManager) != null)
        Observable
                .fromCallable {
                    RxResolveResultActivity
                            .newInstance(context, this)
                            .launch(context)
                }.flatMap {
                    SingletonRxBusProvider.BUS.observable(RxBus.KEYS.SINGLE)
                            .filter { it is RxActivityResult }
                            .map { it as RxActivityResult }
                            .flatMap { Observable.just(it) }
                }
    else
        Observable.error<RxActivityResult>(ActivityNotFoundException())
}