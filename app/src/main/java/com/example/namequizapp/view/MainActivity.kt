package com.example.namequizapp.view

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.coroutineScope
import com.example.namequizapp.R
import com.example.namequizapp.data.AppDatabase
import com.example.namequizapp.data.QuizEntryModel
import com.example.namequizapp.data.QuizEntryRepository
import com.example.namequizapp.databinding.ActivityMainBinding
import com.example.namequizapp.utils.Constants
import com.example.namequizapp.utils.ImageUtils.convertToString
import com.example.namequizapp.viewmodels.QuizEntryViewModel
import com.example.namequizapp.viewmodels.QuizEntryViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Main activity. Welcome screen and provides navigation to other
 * activities.
 */
class MainActivity : AppCompatActivity() {

    private val viewModel: QuizEntryViewModel by viewModels {
        QuizEntryViewModelFactory(
            QuizEntryRepository(
                (AppDatabase.getDatabase(applicationContext) as AppDatabase).quizEntryDao()
            )
        )
    }

    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        populateDatabaseOnFirstLaunch()
        setUpListeners()

        Log.d("TAG", QuizActivity::class.java.name)
    }

    private fun setUpListeners(){

        binding.btnStartQuiz.setOnClickListener {
            startActivity(Intent(applicationContext, QuizActivity::class.java))
        }
        binding.btnSeeEntries.setOnClickListener {
            startActivity(Intent(applicationContext, DatabaseActivity::class.java))
        }

        binding.btnAddEntry.setOnClickListener {
            startActivity(Intent(applicationContext, AddEntryActivity::class.java))
        }
    }


    private fun populateDatabaseOnFirstLaunch() {
        val sharedPref = this.getPreferences(Context.MODE_PRIVATE) ?: return

        if(sharedPref.getBoolean(Constants.FIRST_LAUNCH, true)) {

            lifecycle.coroutineScope.launch(Dispatchers.IO) {
                viewModel.insertQuizEntry(QuizEntryModel(
                    name = "Jonas",
                    image = BitmapFactory.decodeResource(resources, R.drawable.cat10).convertToString()
                ))
                viewModel.insertQuizEntry(QuizEntryModel(
                    name = "Henning",
                    image = BitmapFactory.decodeResource(resources, R.drawable.cat2).convertToString()
                ))
                viewModel.insertQuizEntry(QuizEntryModel(
                    name = "Finn",
                    image = BitmapFactory.decodeResource(resources, R.drawable.cat7).convertToString()
                ))
            }

            with(sharedPref.edit()){
                putBoolean(Constants.FIRST_LAUNCH, false)
                apply()
            }
        }
    }
}