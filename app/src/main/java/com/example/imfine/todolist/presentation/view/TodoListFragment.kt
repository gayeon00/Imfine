package com.example.imfine.todolist.presentation.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import com.example.imfine.databinding.FragmentTodoListBinding
import com.example.imfine.todolist.presentation.adapter.BorderItemDecoration
import com.example.imfine.todolist.presentation.adapter.TodoListAdapter
import com.example.imfine.todolist.presentation.viewmodel.TodoListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TodoListFragment : Fragment() {

    private var _binding: FragmentTodoListBinding? = null
    private val binding get() = _binding!!

    private val todoListViewModel: TodoListViewModel by viewModels()
    private val adapter = TodoListAdapter()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTodoListBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setTodoList()
        observeTodoList()
        setAddButton()
    }

    private fun setTodoList() {
        binding.todoList.adapter = this.adapter
        binding.todoList.addItemDecoration(BorderItemDecoration(requireContext()))
        val swipeHelper = SwipeHelper(object : SwipeListener {
            override fun onItemEdited(position: Int) {
                todoListViewModel.editTodo(todoListViewModel.todoList.value!![position])
            }

            override fun onItemCompleted(position: Int) {
                todoListViewModel.completeTodo(todoListViewModel.todoList.value!![position])
            }

        }, requireContext())
        ItemTouchHelper(swipeHelper).attachToRecyclerView(binding.todoList)
    }

    private fun setAddButton() {
        binding.btnAddTask.setOnClickListener {
            val action = TodoListFragmentDirections.actionTodoListFragmentToAddEditTodoFragment(-1)
            findNavController().navigate(action)
        }
    }

    private fun observeTodoList() {
        todoListViewModel.todoList.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                binding.linearLayoutNoTodo.visibility = View.INVISIBLE
                binding.todoList.visibility = View.VISIBLE

                //todolist adapter에 등록해주기
                adapter.submitList(it)
            } else {
                binding.linearLayoutNoTodo.visibility = View.VISIBLE
                binding.todoList.visibility = View.INVISIBLE
            }
        }
    }
}