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
import androidx.lifecycle.MutableLiveData
import com.bumptech.glide.Glide
import com.example.imfine.databinding.FragmentRegisterBinding

class RegisterFragment : Fragment() {
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    private val registerViewModel: RegisterViewModel by viewModels()

    private val galleryLauncher =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) {
            registerViewModel.setImageUrl(it)
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
            btnProfileImage.setOnClickListener {
                openGalleryForImage()
            }

            updateValueOnTextChanged(editTextName, registerViewModel.name)
            updateValueOnTextChanged(editTextBirthday, registerViewModel.birthday)


            registerViewModel.run {
                //register 버튼의 가용여부 판단
                isRegisterEnabled.observe(viewLifecycleOwner) { isEnabled ->
                    binding.btnRegister.isEnabled = isEnabled
                }

                //선택된 사진 uri 관찰하여 imagebutton에 넣어줌
                uri.observe(viewLifecycleOwner) {
                    if (it != null) {
                        Glide.with(binding.btnProfileImage.context).load(it)
                            .into(binding.btnProfileImage)
                    }

                }

                // 이름과 생일 입력 필드의 유효성 검사 결과 관찰
                nameError.observe(viewLifecycleOwner) { error ->
                    layoutTextName.error = error
                }

                birthdayError.observe(viewLifecycleOwner) { error ->
                    layoutTextBirthday.error = error
                }

                uriError.observe(viewLifecycleOwner) { error ->
                    tvErrorProfileImage.text = error
                    tvErrorProfileImage.visibility = View.VISIBLE
                }
            }

        }
    }

    private fun updateValueOnTextChanged(
        textView: TextView, liveData: MutableLiveData<String>
    ) {
        textView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                liveData.value = p0.toString()
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })
    }

    private fun openGalleryForImage() =
        galleryLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))

}