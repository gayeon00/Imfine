package com.example.imfine.util

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint

object BitmapUtil {

    fun convertToGrayScale(bitmap: Bitmap): Bitmap {
        val width = bitmap.width
        val height = bitmap.height

        // Bitmap의 복사본 생성
        val grayscaleBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)

        // ColorMatrix 설정
        val matrix = ColorMatrix()
        matrix.setSaturation(0f) // 채도를 0으로 설정하여 흑백 이미지 생성

        // ColorMatrixFilter와 함께 Paint 객체 생성
        val paint = Paint()
        paint.colorFilter = ColorMatrixColorFilter(matrix)

        // Canvas를 이용하여 이미지를 그려 흑백 이미지 생성
        val canvas = Canvas(grayscaleBitmap)
        canvas.drawBitmap(bitmap, 0f, 0f, paint)

        return grayscaleBitmap
    }
}