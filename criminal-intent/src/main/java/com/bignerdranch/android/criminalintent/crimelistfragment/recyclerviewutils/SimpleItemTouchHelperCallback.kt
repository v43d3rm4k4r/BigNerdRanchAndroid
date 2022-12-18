package com.bignerdranch.android.criminalintent.crimelistfragment.recyclerviewutils

import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
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
    private val displayMetrics = resources.displayMetrics
    private val width  = (displayMetrics.widthPixels / displayMetrics.density).toInt().dp
    private val deleteColor = resources.getColor(android.R.color.holo_red_light)
    private val deleteIcon  = resources.getDrawable(R.drawable.ic_outline_delete, null)
    private val selectedViewHolderBgRect = Rect()
    private val selectedViewHolderPaint  = Paint()

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

    override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder): Float = .33F

    override fun isItemViewSwipeEnabled(): Boolean = true
    override fun isLongPressDragEnabled(): Boolean = false

    override fun onChildDraw(
        canvas: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        //1. Background color based upon direction swiped
        with(selectedViewHolderBgRect) {
            if (dX > 0) {
                left = recyclerView.left
                top = viewHolder.itemView.top
                right = left + dX.roundToInt()
                bottom = viewHolder.itemView.bottom
            } else {
                right = recyclerView.right
                top = viewHolder.itemView.top
                left = right + dX.roundToInt()
                bottom = viewHolder.itemView.bottom
            }
        }
        selectedViewHolderPaint.color = if (abs(dX) < width / 3) Color.GRAY
            else deleteColor

        canvas.drawRect(selectedViewHolderBgRect, selectedViewHolderPaint)

        //2. Printing the icons
        val textMargin = resources.getDimension(R.dimen.text_margin)
            .roundToInt()
        deleteIcon.bounds = if (dX > 0) {
            Rect(
                textMargin,
                viewHolder.itemView.top + textMargin + 8.dp,
                textMargin + deleteIcon.intrinsicWidth,
                viewHolder.itemView.top + deleteIcon.intrinsicHeight
                        + textMargin + 8.dp
            )
        } else {
            Rect(
                width - textMargin - deleteIcon.intrinsicWidth,
                viewHolder.itemView.top + textMargin + 8.dp,
                width - textMargin,
                viewHolder.itemView.top + deleteIcon.intrinsicHeight
                        + textMargin + 8.dp
            )
        }

        deleteIcon.draw(canvas)

        super.onChildDraw(
            canvas,
            recyclerView,
            viewHolder,
            dX,
            dY,
            actionState,
            isCurrentlyActive
        )
    }

    private val Int.dp
        get() = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            toFloat(), resources.displayMetrics
        ).roundToInt()
}