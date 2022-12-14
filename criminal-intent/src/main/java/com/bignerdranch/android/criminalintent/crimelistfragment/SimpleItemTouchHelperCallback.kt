package com.bignerdranch.android.criminalintent.crimelistfragment

import android.content.Context
import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Rect
import android.util.DisplayMetrics
import android.util.TypedValue

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

import com.bignerdranch.android.criminalintent.R

import kotlin.math.abs
import kotlin.math.roundToInt

/**
 * Gestures helpers for [ItemTouchHelperAdapter] implemented by [CrimesAdapter].
 */
class SimpleItemTouchHelperCallback(
    private val resources: Resources,
    private val adapter: ItemTouchHelperAdapter
    ) : ItemTouchHelper.Callback() {

    // Using resources for onChildDraw()
//    private val displayMetrics: DisplayMetrics = resources.displayMetrics
//    private val height = (displayMetrics.heightPixels / displayMetrics.density).toInt().dp
//    private val width = (displayMetrics.widthPixels / displayMetrics.density).toInt().dp
//    private val deleteColor = resources.getColor(android.R.color.holo_red_light)
//    private val deleteIcon = resources.getDrawable(R.drawable.ic_outline_delete, null)

    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
        val dragFlags  = ItemTouchHelper.UP or ItemTouchHelper.DOWN
        val swipeFlags = ItemTouchHelper.START or ItemTouchHelper.END
        return makeMovementFlags(dragFlags, swipeFlags)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        adapter.onItemMove(viewHolder.adapterPosition, target.adapterPosition);
        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        adapter.onItemDismiss(viewHolder.adapterPosition);
    }

    override fun isItemViewSwipeEnabled(): Boolean = true
    override fun isLongPressDragEnabled(): Boolean = false

//    override fun onChildDraw(
//        canvas: Canvas,
//        recyclerView: RecyclerView,
//        viewHolder: RecyclerView.ViewHolder,
//        dX: Float,
//        dY: Float,
//        actionState: Int,
//        isCurrentlyActive: Boolean
//    ) {
//
//        //1. Background color based upon direction swiped
//        if (abs(dX) < width / 2)
//            canvas.drawColor(Color.GRAY)
//        else
//            canvas.drawColor(deleteColor)
//
//        //2. Printing the icons
//        val textMargin = resources.getDimension(R.dimen.text_margin)
//            .roundToInt()
//        deleteIcon.bounds = Rect(
//            textMargin,
//            viewHolder.itemView.top + textMargin + 8.dp,
//            textMargin + deleteIcon.intrinsicWidth,
//            viewHolder.itemView.top + deleteIcon.intrinsicHeight
//                    + textMargin + 8.dp
//        )
//
//        //3. Drawing icon based upon direction swiped
//        if (dX > 0) deleteIcon.draw(canvas)// else archiveIcon.draw(canvas)
//
//        super.onChildDraw(
//            canvas,
//            recyclerView,
//            viewHolder,
//            dX,
//            dY,
//            actionState,
//            isCurrentlyActive
//        )
//    }

//    private val Int.dp
//        get() = TypedValue.applyDimension(
//            TypedValue.COMPLEX_UNIT_DIP,
//            toFloat(), resources.displayMetrics
//        ).roundToInt()
}