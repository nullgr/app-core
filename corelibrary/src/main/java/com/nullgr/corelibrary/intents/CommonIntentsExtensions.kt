package com.nullgr.corelibrary.intents

import android.Manifest
import android.app.Activity
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
import java.util.*


/**
 * Created by Grishko Nikita on 01.02.18.
 */
fun callIntent(number: String): Intent {
    return Intent(Intent.ACTION_DIAL)
            .setData(Uri.parse(if (number.toLowerCase().startsWith("tel:")) number
            else String.format("tel:%s", PhoneNumberUtils.stripSeparators(number))))
}

fun webIntent(url: String): Intent {
    return Intent(Intent.ACTION_VIEW).setData(Uri.parse(url))
}

fun emailIntent(to: String, subject: String? = null, body: String? = null): Intent {
    return Intent(android.content.Intent.ACTION_SEND).apply {
        type = "plain/text"
        putExtra(android.content.Intent.EXTRA_EMAIL, arrayOf(to))
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        if (!subject.isNullOrEmpty()) {
            putExtra(android.content.Intent.EXTRA_SUBJECT, subject)
        }
        if (!body.isNullOrEmpty()) {
            putExtra(android.content.Intent.EXTRA_TEXT, body)
        }
    }
}

fun shareTextIntent(text: String): Intent {
    return Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, text)
    }
}

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

fun contactCardIntent(contactId: String): Intent {
    return Intent(Intent.ACTION_VIEW).apply {
        data = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_URI, contactId)
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
}

@RequiresPermission(Manifest.permission.READ_CONTACTS)
fun selectContactIntent(): Intent {
    return Intent(Intent.ACTION_PICK).apply {
        type = ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE
    }
}

fun navigationIntent(lat: Double, lon: Double): Intent {
    return Intent(Intent.ACTION_VIEW, Uri.parse(String.format("google.navigation:q=%s,%s", lat, lon)))
            .apply { `package` = "com.google.android.apps.maps" }
}

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

fun chromeTabsIntent(@ColorInt tabsColor: Int? = null): CustomTabsIntent {
    val builder = CustomTabsIntent.Builder()
    tabsColor?.let { builder.setToolbarColor(it) }
    return builder.build()
}

fun CustomTabsIntent.launch(context: Context, url: String) {
    launchUrl(context, Uri.parse(url))
}

fun Intent?.launch(context: Context?, noActivityErrorMessage: String? = null) {
    if (this != null && context != null && this.resolveActivity(context.packageManager) != null) {
        context.startActivity(this)
    } else if (!noActivityErrorMessage.isNullOrEmpty()) {
        Toast.makeText(context, noActivityErrorMessage, Toast.LENGTH_SHORT).show()
    }
}

fun Intent?.launchForResult(context: Activity?, requestCode: Int, noActivityErrorMessage: String? = null) {
    if (this != null && context != null && this.resolveActivity(context.packageManager) != null) {
        context.startActivityForResult(this, requestCode)
    } else if (!noActivityErrorMessage.isNullOrEmpty()) {
        Toast.makeText(context, noActivityErrorMessage, Toast.LENGTH_SHORT).show()
    }
}

fun Intent?.sendBroadcast(context: Context?) {
    if (this != null && context != null) {
        context.sendBroadcast(this)
    }
}

fun Intent?.launchService(context: Context?) {
    if (this != null && context != null) {
        context.startService(this)
    }
}