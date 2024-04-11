package com.example.imfine.auth.presentation.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.imfine.R
import com.example.imfine.auth.data.model.Response
import com.example.imfine.auth.presentation.viewmodel.RegisterViewModel
import com.example.imfine.auth.presentation.viewmodel.UserViewModel
import com.example.imfine.databinding.FragmentRegisterBinding
import com.example.imfine.util.doOnTextChanged
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@AndroidEntryPoint
class RegisterFragment : Fragment() {
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    private val registerViewModel: RegisterViewModel by activityViewModels()
    private val userViewModel: UserViewModel by viewModels()

    private val formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd")

    //datepicker가 보이는지 여부
    private var isDatePickerShowing = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userViewModel.userExists.observe(viewLifecycleOwner) { userExists ->
            if (userExists) {
                findNavController().navigate(R.id.action_registerFragment_to_todoListFragment)
            }
        }

        binding.run {

            setRegisterButton()
            setProfileImageButton()
            setNameEditText()
            setBirthdayEditText()

            registerViewModel.saveResult.observe(viewLifecycleOwner) {
                when (it) {
                    is Response.Success -> {
                        // 저장 성공 처리
                        // 사용자에게 완료 여부 표시
                        showSnackBar("Register Succeed")
                        findNavController().navigate(R.id.action_registerFragment_to_registerCompleteFragment)
                    }

                    is Response.Error -> {
                        // 저장 실패 처리
                        // 에러 메시지를 사용자에게 표시
                        showSnackBar("Register Failed: User already exists.")
                    }
                }
            }

        }
    }

    private fun setBirthdayEditText() {
        binding.editTextBirthday.setOnClickListener {
            if(!isDatePickerShowing) {
                showDatePicker()
            }

        }

        registerViewModel.birthdayError.observe(viewLifecycleOwner) { error ->
            binding.layoutTextBirthday.error = error
        }
    }

    private fun showDatePicker() {
        isDatePickerShowing = true
        val datePicker =
            MaterialDatePicker.Builder.datePicker()
                .setPositiveButtonText("OK")
                .setNegativeButtonText("CANCEL")
                .setTitleText("Select Date")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build()

        datePicker.run{
            addOnPositiveButtonClickListener {
                //millis to localdate
                val localDate = Instant.ofEpochMilli(it)
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime()
                binding.editTextBirthday.setText(localDate.format(formatter))
                registerViewModel.setBirthday(binding.editTextBirthday.text.toString())
                isDatePickerShowing = false
            }

            addOnNegativeButtonClickListener {
                isDatePickerShowing = false
            }

            addOnCancelListener {
                isDatePickerShowing = false
            }

            addOnDismissListener {
                isDatePickerShowing = false
            }
        }

        datePicker.show(getParentFragmentManager(), datePicker.toString())

    }

    private fun setNameEditText() {
        binding.editTextName.doOnTextChanged { text ->
            registerViewModel.setName(text)
        }

        // 이름 입력 필드의 유효성 검사 결과 관찰
        registerViewModel.nameError.observe(viewLifecycleOwner) { error ->
            binding.layoutTextName.error = error
        }
    }

    private fun setProfileImageButton() {
        binding.btnProfileImage.setOnClickListener {
            startCamera()
        }

        //선택된 사진 uri 관찰하여 imagebutton에 넣어줌
        registerViewModel.profileImageUri.observe(viewLifecycleOwner) {
            if (it != null) {
                Glide.with(binding.btnProfileImage.context)
                    .load(it)
                    .circleCrop()
                    .into(binding.btnProfileImage)
            }
        }

        registerViewModel.profileImageUriError.observe(viewLifecycleOwner) { error ->
            binding.tvErrorProfileImage.text = error
            binding.tvErrorProfileImage.visibility = View.VISIBLE
        }

    }


    private fun setRegisterButton() {
        binding.btnRegister.setOnClickListener {
            registerViewModel.setName(binding.editTextName.text.toString())
            registerViewModel.setBirthday(binding.editTextBirthday.text.toString())
            //가입하기(profileimageuri는 사진을 찍어올 때, viewmodel에 저장)
            registerViewModel.registerUser()
        }
    }

    private fun startCamera() =
        findNavController().navigate(R.id.action_registerFragment_to_cameraFragment)

    private fun showSnackBar(message: String) {
        Snackbar.make(requireView(), message, Snackbar.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}