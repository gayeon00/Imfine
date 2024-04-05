package com.example.imfine.auth.presentation

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.imfine.databinding.FragmentRegisterBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment : Fragment() {
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    private val registerViewModel: RegisterViewModel by viewModels()

    private val galleryLauncher =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) {
            registerViewModel.run{
                validateUri(it)
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.run {

            setRegisterButton()
            setProfileImageButton()
            setNameEditText()
            setBirthdayEditText()

        }
    }

    private fun setBirthdayEditText() {
        binding.editTextBirthday.doOnTextChanged { text ->
            registerViewModel.run {
                validateBirthday(text)
            }
        }

        registerViewModel.birthdayError.observe(viewLifecycleOwner) { error ->
            binding.layoutTextBirthday.error = error
        }
    }

    private fun setNameEditText() {
        binding.editTextName.doOnTextChanged { text ->
            registerViewModel.run {
                validateName(text)
            }
        }

        // 이름과 생일 입력 필드의 유효성 검사 결과 관찰
        registerViewModel.nameError.observe(viewLifecycleOwner) { error ->
            binding.layoutTextName.error = error
        }
    }

    private fun setProfileImageButton() {
        binding.btnProfileImage.setOnClickListener {
            openGalleryForImage()
        }

        //선택된 사진 uri 관찰하여 imagebutton에 넣어줌
        registerViewModel.uri.observe(viewLifecycleOwner) {
            if (it != null) {
                Glide.with(binding.btnProfileImage.context).load(it)
                    .into(binding.btnProfileImage)
            }
        }

        registerViewModel.uriError.observe(viewLifecycleOwner) { error ->
            binding.tvErrorProfileImage.text = error
            binding.tvErrorProfileImage.visibility = View.VISIBLE
        }

    }


    private fun setRegisterButton() {
        registerViewModel.isRegisterEnabled.observe(viewLifecycleOwner) { isEnabled ->
            binding.btnRegister.isEnabled = isEnabled
        }
        binding.btnRegister.setOnClickListener {
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

    private fun openGalleryForImage() =
        galleryLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))

}