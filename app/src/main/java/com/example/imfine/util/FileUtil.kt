package com.example.imfine.util

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.core.net.toUri
import com.example.imfine.auth.presentation.view.CameraFragment
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Locale

object FileUtil {
    fun saveBitmapToFile(context: Context, bitmap: Bitmap):Uri? {
        // 파일 저장을 위한 파일 객체 생성
        val file = File(context.filesDir, generateFileName())
        file.createNewFile()

        // FileOutputStream을 사용하여 파일에 Bitmap 저장
        return try {
            val outputStream = FileOutputStream(file)
            bitmap.compress(android.graphics.Bitmap.CompressFormat.JPEG, 100, outputStream)
            outputStream.flush()
            outputStream.close()
            file.toUri()
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }
    private fun generateFileName(): String {
        return SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(System.currentTimeMillis())
    }
}