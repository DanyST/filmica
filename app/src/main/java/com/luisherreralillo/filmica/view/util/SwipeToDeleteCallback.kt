package com.luisherreralillo.filmica.view.util

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.ColorDrawable
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.View
import com.luisherreralillo.filmica.R

abstract class SwipeToDeleteCallback: ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {

    // Se implementa porque no queremos que haya aviso de movimientos
    override fun onMove(p0: RecyclerView, p1: RecyclerView.ViewHolder, p2: RecyclerView.ViewHolder): Boolean {
        return false
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float, // arrastre en X
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        val context = recyclerView.context
        val itemView = viewHolder.itemView

        // Background
        setupBackground(context, itemView, dX, c)

        // Icono
        setupIcon(context, itemView, c)

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }

    private fun setupIcon(context: Context, itemView: View, c: Canvas) {
        val checkIcon = ContextCompat.getDrawable(context, R.drawable.ic_check)!!
        val iconMargin = (itemView.height - checkIcon.intrinsicHeight) / 3
        val iconTop = itemView.top + (itemView.height - checkIcon.intrinsicHeight) / 2
        val iconLeft = itemView.left + iconMargin
        val iconRight = itemView.left + iconMargin + checkIcon.intrinsicWidth
        val iconBottom = iconTop + checkIcon.intrinsicHeight

        checkIcon.setBounds(
            iconLeft,
            iconTop,
            iconRight,
            iconBottom
        )

        checkIcon.draw(c)
    }

    private fun setupBackground(
        context: Context,
        itemView: View,
        dX: Float,
        c: Canvas
    ) {
        val color = ContextCompat.getColor(context, R.color.colorPrimaryDark)
        val background = ColorDrawable(color) // Permite añadirle un color y este pintarlo en una vista
        background.setBounds(
            itemView.left,
            itemView.top,
            itemView.left + dX.toInt(),
            itemView.bottom
        )

        background.draw(c)
    }

}