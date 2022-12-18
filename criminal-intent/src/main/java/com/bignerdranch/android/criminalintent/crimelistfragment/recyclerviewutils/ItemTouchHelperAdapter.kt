package com.bignerdranch.android.criminalintent.crimelistfragment.recyclerviewutils

interface ItemTouchHelperAdapter {

    fun onItemMove(fromPosition: Int, toPosition: Int)

    fun onItemDismiss(position: Int)
}