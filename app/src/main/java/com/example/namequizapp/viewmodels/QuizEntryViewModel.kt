package com.example.namequizapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.namequizapp.data.QuizEntryModel
import com.example.namequizapp.data.QuizEntryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*

class QuizEntryViewModel(private val repository: QuizEntryRepository) : ViewModel() {

    var sortAsc : Boolean = true

    fun getAllEntries() : Flow<List<QuizEntryModel>> = repository.getAllQuizEntries()

    fun deleteQuizEntry(quizEntryModel: QuizEntryModel) {
        repository.deleteQuizEntry(quizEntryModel)
    }

    fun insertQuizEntry(quizEntryModel: QuizEntryModel) {
        repository.insertQuizEntry(quizEntryModel)
    }
}



class QuizEntryViewModelFactory(
    private val repository: QuizEntryRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(QuizEntryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return QuizEntryViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}