package com.nullgr.core.adapter.ktx

import android.view.LayoutInflater
import android.view.ViewGroup

/**
 * Base ViewHolder for all [AdapterDelegate] that supports view caching.
 *
 * @author vchernyshov
 */
open class BaseViewHolder(parent: ViewGroup, layoutId: Int)
    : ViewHolder(LayoutInflater.from(parent.context).inflate(layoutId, parent, false))