package com.example.imfine.auth.presentation.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.imfine.R
import com.example.imfine.auth.presentation.viewmodel.RegisterViewModel
import com.example.imfine.databinding.FragmentRegisterCompleteBinding

class RegisterCompleteFragment : Fragment() {
    private var _binding: FragmentRegisterCompleteBinding? = null
    private val binding get() = _binding!!
    private val registerViewModel: RegisterViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterCompleteBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnStart.setOnClickListener {
            findNavController().navigate(R.id.action_registerCompleteFragment_to_todoListFragment)
        }

        registerViewModel.run{
            uri.observe(viewLifecycleOwner) {
                if (it != null) {
                    Glide.with(binding.ivProfileImage.context)
                        .load(it)
                        .circleCrop()
                        .into(binding.ivProfileImage)
                }
            }

            name.observe(viewLifecycleOwner) {
                binding.tvName.text = it
            }

            birthday.observe(viewLifecycleOwner) {
                binding.tvBirthday.text = it
            }
        }
    }

}