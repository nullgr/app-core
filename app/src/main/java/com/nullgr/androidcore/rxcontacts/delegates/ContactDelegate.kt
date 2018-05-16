package com.nullgr.androidcore.rxcontacts.delegates

import android.graphics.Bitmap
import android.graphics.BitmapShader
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Shader
import android.support.v7.widget.RecyclerView
import com.nullgr.androidcore.R
import com.nullgr.androidcore.rxcontacts.items.ContactItem
import com.nullgr.corelibrary.adapter.AdapterDelegate
import com.nullgr.corelibrary.adapter.items.ListItem
import com.nullgr.corelibrary.ui.extensions.toggleView
import com.squareup.picasso.Picasso
import com.squareup.picasso.Transformation
import kotlinx.android.synthetic.main.item_contact.view.photoImageView
import kotlinx.android.synthetic.main.item_contact.view.photoPlaceholderTextView
import kotlinx.android.synthetic.main.item_contact.view.userCredentialsTextView
import kotlinx.android.synthetic.main.item_contact.view.userNameTextView

/**
 * Created by Grishko Nikita on 01.02.18.
 */
class ContactDelegate : AdapterDelegate() {

    override val layoutResource: Int = R.layout.item_contact
    override val itemType: Any = ContactItem::class

    override fun onBindViewHolder(items: List<ListItem>, position: Int, vh: RecyclerView.ViewHolder) {
        val item = items[position] as ContactItem

        with(vh.itemView) {

            photoImageView.toggleView(item.photo != null)
            photoPlaceholderTextView.toggleView(item.photo == null)

            if (item.photo != null) {
                Picasso.get().load(item.photo).transform(CircleTransform).into(photoImageView)
            } else {
                photoPlaceholderTextView.text = item.displayName?.first().toString()
            }

            userNameTextView.text = item.displayName

            val phoneCredentials = item.phones?.joinToString(", ", "Phones: ") ?: ""
            val emailCredentials = item.emails?.joinToString(", ", "Emails: ") ?: ""
            userCredentialsTextView.text = resources.getString(R.string.credentials_mask, phoneCredentials, emailCredentials)
        }
    }
}

object CircleTransform : Transformation {

    override fun transform(source: Bitmap): Bitmap {
        val size = Math.min(source.width, source.height)

        val x = (source.width - size) / 2
        val y = (source.height - size) / 2

        val squaredBitmap = Bitmap.createBitmap(source, x, y, size, size)
        if (squaredBitmap != source) {
            source.recycle()
        }
        val bitmap = Bitmap.createBitmap(size, size, source.config)
        val canvas = Canvas(bitmap)
        val paint = Paint()
        val shader = BitmapShader(squaredBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        paint.shader = shader
        paint.isAntiAlias = true

        val r = size / 2f
        canvas.drawCircle(r, r, r, paint)

        squaredBitmap.recycle()
        return bitmap
    }

    override fun key(): String {
        return "circle"
    }
}
