package com.example.imfine.todolist.presentation.view

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.imfine.R
import com.example.imfine.databinding.FragmentAddEditTodoBinding
import com.example.imfine.todolist.presentation.viewmodel.AddEditTodoViewModel
import com.google.android.material.datepicker.MaterialDatePicker
import dagger.hilt.android.AndroidEntryPoint
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@AndroidEntryPoint
class AddEditTodoFragment : Fragment() {
    private var _binding: FragmentAddEditTodoBinding? = null
    private val binding get() = _binding!!
    private val addEditTodoViewModel: AddEditTodoViewModel by activityViewModels()
    private val args: AddEditTodoFragmentArgs by navArgs()
    private val formatter = DateTimeFormatter.ofPattern("yyyy MM/dd hh:mm a")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddEditTodoBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        addOrEdit()

        //task edittext
        binding.editTextTask.doOnTextChanged {
            addEditTodoViewModel.validateTask(it)
        }
        addEditTodoViewModel.taskError.observe(viewLifecycleOwner) {
            binding.layoutTextTask.error = it
        }

        //date edittext
        val datePicker =
            MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select date")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build()

        datePicker.addOnPositiveButtonClickListener {
            val localDate = //millis to localdate
                Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault()).toLocalDateTime()
            binding.editTextDate.setText(localDate.format(formatter))
            addEditTodoViewModel.validateDateTime(localDate)
        }

        binding.editTextDate.setOnClickListener {
            datePicker.show(getParentFragmentManager(), "AddEditTodoFragment")
        }

        addEditTodoViewModel.dateTimeError.observe(viewLifecycleOwner) {
            binding.layoutTextDate.error = it
        }

        //add edit button
        observeAddEditButton()
    }

    private fun addOrEdit() {
        if (args.todoId == -1) {
            //추가 화면
            binding.pageTitle.setText(R.string.label_add_task)
            binding.btnAddEdit.setText(R.string.label_add)
        } else {
            //편집 화면
            binding.pageTitle.setText(R.string.label_edit_task)
            binding.btnAddEdit.setText(R.string.label_edit)
            observeOriginalTodo()
        }
    }

    private fun observeAddEditButton() {
        addEditTodoViewModel.isAddEditEnabled.observe(viewLifecycleOwner) {
            binding.btnAddEdit.isEnabled = it
        }
        binding.btnAddEdit.setOnClickListener {
            if (args.todoId == -1) addEditTodoViewModel.addTodo()
            else addEditTodoViewModel.updateTodo()
            //todolist 화면으로 돌아가기
            goBackToList()

        }
    }

    private fun goBackToList() {
        findNavController().popBackStack()
    }

    private fun observeOriginalTodo() {
        addEditTodoViewModel.todo.observe(viewLifecycleOwner) {
            binding.editTextTask.setText(it.task)


            binding.editTextDate.setText(it.dateTime.format(formatter))
        }
    }

    private fun TextView.doOnTextChanged(onTextChanged: (String) -> Unit) {
        this.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                onTextChanged(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

}