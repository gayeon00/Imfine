package com.example.imfine.auth.presentation.view

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
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
                        showSnackbar("Register Succeed")
                        findNavController().navigate(R.id.action_registerFragment_to_registerCompleteFragment)
                    }

                    is Response.Error -> {
                        // 저장 실패 처리
                        // 에러 메시지를 사용자에게 표시
                        showSnackbar("Register Failed: User already exists.")
                    }
                }
            }

        }
    }

    private fun setBirthdayEditText() {
        val datePicker =
            MaterialDatePicker.Builder.datePicker()
                .setPositiveButtonText("OK")
                .setNegativeButtonText("CANCEL")
                .setTitleText("Select Date")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build()

        datePicker.addOnPositiveButtonClickListener {
            val localDate = //millis to localdate
                Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault()).toLocalDateTime()
            binding.editTextBirthday.setText(localDate.format(formatter))
            registerViewModel.setBirthday(binding.editTextBirthday.text.toString())
        }

        binding.editTextBirthday.setOnClickListener {
            datePicker.show(getParentFragmentManager(), "RegisterFragment")
        }


        registerViewModel.birthdayError.observe(viewLifecycleOwner) { error ->
            binding.layoutTextBirthday.error = error
        }
    }

    private fun setNameEditText() {
        binding.editTextName.doOnTextChanged { text ->
            registerViewModel.setName(text)
        }

        // 이름과 생일 입력 필드의 유효성 검사 결과 관찰
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


    private fun TextView.doOnTextChanged(onTextChanged: (String) -> Unit) {
        this.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                onTextChanged(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun startCamera() =
        findNavController().navigate(R.id.action_registerFragment_to_cameraFragment)

    private fun showSnackbar(message: String) {
        Snackbar.make(requireView(), message, Snackbar.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}