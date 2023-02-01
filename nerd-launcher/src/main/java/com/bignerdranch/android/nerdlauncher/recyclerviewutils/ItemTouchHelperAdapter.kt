package com.bignerdranch.android.nerdlauncher.recyclerviewutils

interface ItemTouchHelperAdapter {

    fun onItemMove(fromPosition: Int, toPosition: Int)

    fun onItemDismiss(position: Int)
}