package com.bignerdranch.android.nerdlauncher.ui.recyclerviewutils

interface ItemTouchHelperAdapter {

    fun onItemMove(fromPosition: Int, toPosition: Int)

    fun onItemDismiss(position: Int)
}