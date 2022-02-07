package com.example.namequizapp

import android.app.Application
import androidx.core.content.res.ResourcesCompat
import java.io.File

/**
 * Extending application class to add shared variable
 */
class QuizApp(
    var quizEntries : MutableList<QuizEntry> = mutableListOf()
) : Application()