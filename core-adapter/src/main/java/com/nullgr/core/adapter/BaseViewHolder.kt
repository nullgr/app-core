package com.nullgr.core.adapter

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup

/**
 * Base ViewHolder for all [AdapterDelegate].
 *
 * @author a.komarovskyi
 */
open class BaseViewHolder(parent: ViewGroup, layoutId: Int)
    : RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(layoutId, parent, false))