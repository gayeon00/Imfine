package com.example.imfine.todolist.presentation.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.imfine.R

class SwipeHelper(
    private val listener: SwipeListener,
    context: Context
) : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

    private val completeIcon =
        ContextCompat.getDrawable(context, R.drawable.task_alt_white_24dp)
    private val editIcon = ContextCompat.getDrawable(context, R.drawable.edit_white_24dp)
    private val backGroundColor = ContextCompat.getColor(context, R.color.colorPrimary)
    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val position = viewHolder.adapterPosition
        when (direction) {
            ItemTouchHelper.LEFT -> {
                listener.onItemEdited(position)
            }

            ItemTouchHelper.RIGHT -> {
                listener.onItemCompleted(position)
            }
        }
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)

        val itemView = viewHolder.itemView
        val itemHeight = itemView.bottom - itemView.top

        if (dX < 0) {
            Log.d("swipe", "왼쪽")

            //사각형 그리기
            val background = RectF(
                viewHolder.itemView.right.toFloat() + dX,
                viewHolder.itemView.top.toFloat(),
                viewHolder.itemView.right.toFloat(),
                viewHolder.itemView.bottom.toFloat()
            )
            c.drawRect(
                background,
                Paint().apply { color = backGroundColor })

            //사각형 위 아이콘 그리기
            val editIconMargin = (itemHeight - editIcon!!.intrinsicHeight) / 2
            val editIconTop = itemView.top + editIconMargin
            val editIconBottom = editIconTop + editIcon.intrinsicHeight

            val editIconLeft = itemView.right - editIconMargin - editIcon.intrinsicWidth
            val editIconRight = itemView.right - editIconMargin

            editIcon.setBounds(editIconLeft, editIconTop, editIconRight, editIconBottom)
            editIcon.draw(c)

        } else if (dX > 0) {
            Log.d("swipe", "오른 쪽")
            completeIcon?.draw(c)
            val background = RectF(
                viewHolder.itemView.left.toFloat(),
                viewHolder.itemView.top.toFloat(),
                dX,
                viewHolder.itemView.bottom.toFloat()
            )
            c.drawRect(
                background,
                Paint().apply { color = backGroundColor })

            //사각형 위 아이콘 그리기
            val completeIconMargin = (itemHeight - completeIcon!!.intrinsicHeight) / 2
            val completeIconTop = itemView.top + completeIconMargin
            val completeIconBottom = completeIconTop + completeIcon.intrinsicHeight

            val completeIconLeft = itemView.left + completeIconMargin
            val completeIconRight = completeIconLeft + completeIcon.intrinsicWidth

            completeIcon.setBounds(
                completeIconLeft,
                completeIconTop,
                completeIconRight,
                completeIconBottom
            )
            completeIcon.draw(c)
        }
    }
}

interface SwipeListener {
    fun onItemEdited(position: Int)
    fun onItemCompleted(position: Int)
}
