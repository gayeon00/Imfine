package com.example.imfine.auth.presentation.view

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Matrix
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.imfine.auth.presentation.viewmodel.RegisterViewModel
import com.example.imfine.databinding.FragmentCameraBinding
import com.example.imfine.util.BitmapUtil
import com.example.imfine.util.FileUtil
import com.google.android.material.snackbar.Snackbar

class CameraFragment : Fragment() {
    private var _binding: FragmentCameraBinding? = null
    private val binding get() = _binding!!
    private var imageCapture: ImageCapture? = null

    private val cameraPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) {
                startCamera()
            } else {
                Snackbar.make(binding.root, "Camera permission is required.", Snackbar.LENGTH_SHORT)
                    .show()
            }
        }

    private val registerViewModel: RegisterViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCameraBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (isCameraPermissionGranted()) {
            startCamera()
        } else {
            requestCameraPermission()
        }

        binding.btnImageCapture.setOnClickListener {
            takePhoto { bitmap ->
                val uri = FileUtil.saveBitmapToFile(requireContext(), bitmap)
                registerViewModel.setUri(uri)
                //RegisterFragment로 돌아가기
                findNavController().popBackStack()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun takePhoto(onPhotoTaken: (bitmap: Bitmap) -> Unit) {
        // image capture use case
        val imageCapture = imageCapture ?: return
        // image capture listener: 사진 찍힌 후 호출됨
        imageCapture.takePicture(
            ContextCompat.getMainExecutor(requireContext()),
            object : ImageCapture.OnImageCapturedCallback() {
                override fun onCaptureSuccess(image: ImageProxy) {
                    super.onCaptureSuccess(image)


                    //이미지의 rotation반영
                    val matrix = Matrix().apply {
                        postRotate(image.imageInfo.rotationDegrees.toFloat())
                        postScale(-1f, 1f)
                    }

                    //front camera 반전해주기 (preview에 보이는 대로)
                    val flippedBitmap = Bitmap.createBitmap(
                        image.toBitmap(),
                        0,
                        0,
                        image.width,
                        image.height,
                        matrix,
                        true
                    )

                    val grayBitmap = BitmapUtil.convertToGrayScale(flippedBitmap)

                    onPhotoTaken(grayBitmap)
                }

                override fun onError(exception: ImageCaptureException) {
                    super.onError(exception)
                    Log.e(TAG, "Photo capture failed: ${exception.message}", exception)
                }
            }
        )
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())

        cameraProviderFuture.addListener({
            // lifecycle owner에게 camera의 lifecycle을 bind 함 -> 카메라를 직접 open, close할 필요 없음
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            // Preview
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(binding.viewFinder.surfaceProvider)
                }

            // 전면 카메라
            val cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA

            //image capture usecase
            imageCapture = ImageCapture.Builder()
                .build()

            try {
                // Unbind use cases before rebinding
                cameraProvider.unbindAll()

                // Bind use cases to camera
                cameraProvider.bindToLifecycle(
                    this, cameraSelector, imageCapture, preview
                )

            } catch (exc: Exception) {
                Log.e(TAG, "Use case binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(requireContext()))
    }

    private fun requestCameraPermission() {
        cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
    }

    private fun isCameraPermissionGranted(): Boolean = ContextCompat.checkSelfPermission(
        requireContext(),
        Manifest.permission.CAMERA
    ) == PackageManager.PERMISSION_GRANTED

    companion object {
        private const val TAG = "CameraFragment"
    }

}