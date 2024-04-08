package com.example.imfine.todolist.presentation.view

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

class SwipeHelper(
    private val listener: SwipeListener
) : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val position = viewHolder.adapterPosition
        when(direction) {
            ItemTouchHelper.LEFT -> {
                listener.onItemDeleted(position)
            }
            ItemTouchHelper.RIGHT -> {
                listener.onItemCompleted(position)
            }
        }
    }
}

interface SwipeListener {
    fun onItemDeleted(position: Int)
    fun onItemCompleted(position: Int)
}
