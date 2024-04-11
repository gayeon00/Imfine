package com.example.imfine.todolist.presentation.view

import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.imfine.R
import com.example.imfine.databinding.FragmentAddEditTodoBinding
import com.example.imfine.todolist.presentation.viewmodel.AddEditTodoViewModel
import com.example.imfine.util.doOnTextChanged
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.MaterialTimePicker.INPUT_MODE_KEYBOARD
import com.google.android.material.timepicker.TimeFormat
import dagger.hilt.android.AndroidEntryPoint
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale
import java.util.TimeZone

@AndroidEntryPoint
class AddEditTodoFragment : Fragment() {
    private var _binding: FragmentAddEditTodoBinding? = null
    private val binding get() = _binding!!
    private val addEditTodoViewModel: AddEditTodoViewModel by activityViewModels()
    private val args: AddEditTodoFragmentArgs by navArgs()

    private var selectedDate: Long? = null
    private var selectedTime: Long? = null

    //datepicker가 보이는지 여부
    private var isDatePickerShowing = false

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
        setTaskEditText()
        setDateSelection()
        //add edit button
        setAddEditButton()
    }

    /**
     * datepicker, timepicker 이용해서 날짜, 시간 순차 선택
     */
    private fun setDateSelection() {
        binding.layoutTextDate.setOnClickListener {
            if (!isDatePickerShowing) {
                showDatePicker()
            }

        }

        addEditTodoViewModel.dateTimeError.observe(viewLifecycleOwner) {
            if (it == null) {
                binding.tvErrorDate.visibility = View.INVISIBLE
                binding.layoutTextDate.backgroundTintList = null
                binding.tvDateHint.setTextColor(resources.getColor(R.color.colorHint))
            } else {
                binding.layoutTextDate.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.colorError))
                binding.tvErrorDate.visibility = View.VISIBLE
                binding.tvErrorDate.text = it
                binding.tvDateHint.setTextColor(resources.getColor(R.color.colorError))
            }

        }
    }

    /**
     * task에 입력하는 즉시 viewmodel에 저장 및 유효성 검사 후 error 메세지 세팅
     */
    private fun setTaskEditText() {
        binding.editTextTask.doOnTextChanged {
            addEditTodoViewModel.setTask(it)
        }
        addEditTodoViewModel.taskError.observe(viewLifecycleOwner) {
            binding.layoutTextTask.error = it
        }
    }

    private fun showDatePicker() {
        isDatePickerShowing = true
        //date picker
        //오늘 날짜로 기본 선택돼있도록
        val datePicker =
            MaterialDatePicker.Builder.datePicker()
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build()

        datePicker.run {
            addOnPositiveButtonClickListener {
                selectedDate = datePicker.selection
                showTimePicker()
                isDatePickerShowing = false
            }
            addOnNegativeButtonClickListener { isDatePickerShowing = false }
            addOnDismissListener { isDatePickerShowing = false }
            addOnCancelListener { isDatePickerShowing = false }
        }

        datePicker.show(getParentFragmentManager(), datePicker.toString())
    }

    private fun showTimePicker() {
        val timePicker =
            MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_12H)
                .setInputMode(INPUT_MODE_KEYBOARD)
                .build()

        timePicker.addOnPositiveButtonClickListener {
            handleTimePickerSelection(timePicker.hour, timePicker.minute)
        }

        // 모든 취소 및 닫기 이벤트에 대해 선택된 날짜와 시간을 초기화
        val resetSelection: () -> Unit = {
            selectedDate = null
            selectedTime = null
        }

        timePicker.addOnNegativeButtonClickListener {
            resetSelection()
        }
        timePicker.addOnCancelListener {
            resetSelection()
        }
        timePicker.addOnDismissListener {
            resetSelection()
        }
        timePicker.show(parentFragmentManager, timePicker.toString())
    }

    private fun handleTimePickerSelection(hour: Int, minute: Int) {
        // 선택된 시간을 밀리초로 변환하여 저장
        selectedTime = (hour * 60 * 60 * 1000 + minute * 60 * 1000).toLong()

        // 선택된 날짜와 시간을 이용하여 EditText에 텍스트를 설정
        val offset = TimeZone.getTimeZone(ZoneId.systemDefault()).rawOffset
        //Epoch Time은 1970년 1월 1일 00:00:00 UTC 기준이라 offset을 빼서 보정해준다.
        val localDate = Instant.ofEpochMilli(selectedDate!! + selectedTime!! - offset)
            .atZone(ZoneId.systemDefault())
            .toLocalDateTime()

        setDateTime(localDate)

        // ViewModel을 통해 선택된 날짜와 시간을 설정
        addEditTodoViewModel.setDateTime(localDate)
    }

    private fun setDateTime(localDate: LocalDateTime) {
        //hint는 안보이게
        binding.tvDateHint.visibility = View.INVISIBLE
        //시간 tv는 보이게
        binding.tvYear.visibility = View.VISIBLE
        binding.tvMonthDay.visibility = View.VISIBLE
        binding.tvTime.visibility = View.VISIBLE

        //시간 tv에 값 할당해주기
        binding.tvYear.text = localDate.format(DateTimeFormatter.ofPattern("yyyy", Locale.ENGLISH))
        binding.tvMonthDay.text =
            localDate.format(DateTimeFormatter.ofPattern("MM/dd", Locale.ENGLISH))
        binding.tvTime.text =
            localDate.format(DateTimeFormatter.ofPattern("hh:mm a", Locale.ENGLISH))
    }

    private fun clearTaskDate() {
        addEditTodoViewModel.setInitialTask("")
        addEditTodoViewModel.setInitialDateTime(null)
    }

    private fun addOrEdit() {
        if (args.todoId == -1) {
            //추가 화면
            binding.pageTitle.setText(R.string.label_add_task)
            binding.btnAddEdit.setText(R.string.label_add)
            clearTaskDate()
        } else {
            //편집 화면
            binding.pageTitle.setText(R.string.label_edit_task)
            binding.btnAddEdit.setText(R.string.label_edit)
            observeOriginalTodo()
        }
    }

    private fun setAddEditButton() {
        binding.btnAddEdit.setOnClickListener {
            if (args.todoId == -1) {
                //받은 todoId가 없다면 insert
                addEditTodoViewModel.addTodo {
                    goBackToList()
                }
            } else {
                //받은 todoId가 있다면 update
                addEditTodoViewModel.updateTodo {
                    goBackToList()
                }
            }

        }
    }

    private fun goBackToList() {
        findNavController().popBackStack()
    }

    private fun observeOriginalTodo() {
        addEditTodoViewModel.todo.observe(viewLifecycleOwner) {
            addEditTodoViewModel.setTask(it.task)
            addEditTodoViewModel.setDateTime(it.dateTime)

            binding.editTextTask.setText(it.task)
            setDateTime(it.dateTime)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}