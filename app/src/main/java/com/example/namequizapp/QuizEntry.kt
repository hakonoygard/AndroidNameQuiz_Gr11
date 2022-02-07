package com.example.namequizapp

import android.graphics.Bitmap
import java.io.Serializable

/**
 * Data class to represent an entry
 */
data class QuizEntry(
    val name: String,
    val image: Bitmap
) : Serializable
