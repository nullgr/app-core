package com.nullgr.core.adapter.ktx

import androidx.recyclerview.widget.RecyclerView
import android.view.View
import kotlinx.android.extensions.LayoutContainer

/**
 * ViewHolder that implements [LayoutContainer] to use cached views.
 *
 * @author vchernyshov
 */
open class ViewHolder(
    override val containerView: View
) : RecyclerView.ViewHolder(containerView), LayoutContainer