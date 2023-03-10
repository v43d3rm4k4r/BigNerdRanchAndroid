package com.bignerdranch.android.nerdlauncher.ui.utils.recyclerview

interface ItemTouchHelperAdapter {

    fun onItemMove(fromPosition: Int, toPosition: Int)

    fun onItemDismiss(position: Int)
}