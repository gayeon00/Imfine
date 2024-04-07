package com.example.imfine.todolist.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
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
            val formatter = DateTimeFormatter.ofPattern("yy'’'M/dd h:mm a")
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