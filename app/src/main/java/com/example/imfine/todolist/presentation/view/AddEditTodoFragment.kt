package com.example.imfine.todolist.presentation.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.imfine.R
import com.example.imfine.databinding.FragmentAddEditTodoBinding
import com.example.imfine.todolist.presentation.viewmodel.AddEditTodoViewModel
import java.time.format.DateTimeFormatter

class AddEditTodoFragment : Fragment() {
    private var _binding: FragmentAddEditTodoBinding? = null
    private val binding get() = _binding!!
    private val addEditTodoViewModel: AddEditTodoViewModel by viewModels()
    private val args: AddEditTodoFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddEditTodoBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (args.todoId == -1) {
            //추가 화면
            binding.pageTitle.setText(R.string.label_add_task)
            binding.btnAdd.setText(R.string.label_add)
        } else {
            //편집 화면
            binding.pageTitle.setText(R.string.label_edit_task)
            binding.btnAdd.setText(R.string.label_edit)
            observeOriginalTodo()
        }
    }

    private fun observeOriginalTodo() {
        addEditTodoViewModel.todo.observe(viewLifecycleOwner) {
            binding.editTextTask.setText(it.task)

            val formatter = DateTimeFormatter.ofPattern("yyyy M/dd h:mm a")
            binding.editTextDate.setText(it.dateTime.format(formatter))
        }
    }

}