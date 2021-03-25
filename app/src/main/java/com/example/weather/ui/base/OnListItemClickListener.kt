package com.example.authorization.ui.base

import android.view.View

interface OnListItemClickListener<in T> {
    fun onItemClick(item: T, view: View)
}