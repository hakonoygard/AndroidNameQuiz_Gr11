package com.example.namequizapp.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface QuizEntryDao {
    @Query("SELECT * FROM quizentrymodel ORDER BY uid")
    fun getAll() : Flow<List<QuizEntryModel>>

    @Query("SELECT name FROM quizentrymodel")
    fun getNames() : Flow<List<String>>

    @Insert
    fun insertQuizEntry(quizEntryModel: QuizEntryModel)

    @Delete
    fun delete(quizEntryModel: QuizEntryModel)
}