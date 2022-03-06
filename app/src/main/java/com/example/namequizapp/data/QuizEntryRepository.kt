package com.example.namequizapp.data

import kotlinx.coroutines.flow.Flow

class QuizEntryRepository(private val quizEntryDao: QuizEntryDao) {
    fun getAllQuizEntries() : Flow<List<QuizEntryModel>> =
        quizEntryDao.getAll()

    fun deleteQuizEntry(quizEntryModel : QuizEntryModel) =
        quizEntryDao.delete(quizEntryModel)

    fun insertQuizEntry(quizEntryModel: QuizEntryModel) =
        quizEntryDao.insertQuizEntry(quizEntryModel)
}