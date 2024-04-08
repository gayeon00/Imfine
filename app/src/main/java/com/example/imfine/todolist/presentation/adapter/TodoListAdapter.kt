package com.example.imfine.todolist.presentation.adapter

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.imfine.R
import com.example.imfine.databinding.ItemTodoBinding
import com.example.imfine.todolist.data.model.Todo
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class TodoListAdapter() :
    ListAdapter<Todo, TodoListAdapter.TodoViewHolder>(TodoDiffCallBack()) {
    class TodoViewHolder(
        private val itemTodoBinding: ItemTodoBinding
    ) : RecyclerView.ViewHolder(itemTodoBinding.root) {
        fun bind(item: Todo) {
            itemTodoBinding.run {
                tvItemTodo.text = item.task
                tvItemTodoTime.text = formatLocalDateTime(item.dateTime)
            }
        }

        private fun formatLocalDateTime(localDateTime: LocalDateTime): String {
            val formatter = DateTimeFormatter.ofPattern("yy'’' M/dd h:mm a")
            return localDateTime.format(formatter)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        return TodoViewHolder(
            ItemTodoBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

private class TodoDiffCallBack : DiffUtil.ItemCallback<Todo>() {
    override fun areItemsTheSame(oldItem: Todo, newItem: Todo): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: Todo, newItem: Todo): Boolean {
        return oldItem == newItem
    }
}

class DividerItemDecoration(context: Context) : RecyclerView.ItemDecoration() {

    private val dividerHeight: Int =
        context.resources.getDimensionPixelSize(R.dimen.divider_height) // 테두리 높이 지정
    private val dividerPaint: Paint = Paint()

    init {
        dividerPaint.color = ContextCompat.getColor(context, R.color.itemBorder) // 테두리 색상 지정
        dividerPaint.strokeWidth =
            context.resources.getDimensionPixelSize(R.dimen.divider_height).toFloat()
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDrawOver(c, parent, state)


        val childCount = parent.childCount
        for (i in 0 until childCount) { // 마지막 아이템은 테두리를 그리지 않음
            val child = parent.getChildAt(i)

            //위쪽 테두리
            if (i == 0) {
                c.drawLine(
                    child.left.toFloat() - dividerHeight / 2,
                    child.top.toFloat(),
                    child.right.toFloat() + dividerHeight / 2,
                    child.top.toFloat(),
                    dividerPaint
                )
            }

            //아래 테두리
            c.drawLine(
                child.left.toFloat() - dividerHeight / 2,
                child.bottom.toFloat(),
                child.right.toFloat() + dividerHeight / 2,
                child.bottom.toFloat(),
                dividerPaint
            )
            //왼쪽 테두리
            c.drawLine(
                child.left.toFloat(),
                child.top.toFloat(),
                child.left.toFloat(),
                child.bottom.toFloat(),
                dividerPaint
            )
            //오른쪽 테두리
            c.drawLine(
                child.right.toFloat(),
                child.top.toFloat(),
                child.right.toFloat(),
                child.bottom.toFloat(),
                dividerPaint
            )
        }
    }
}