package com.nullgr.core.adapter.exceptions

import com.nullgr.core.adapter.items.ListItem

class DelegateNotDefinedException(clazz: Class<ListItem>) : IllegalArgumentException("No delegate defined for ${clazz.simpleName}")