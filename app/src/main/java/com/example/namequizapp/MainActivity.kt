package com.example.namequizapp

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.namequizapp.databinding.ActivityMainBinding

/**
 * Main activity. Welcome screen and provides navigation to other
 * activities.
 */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        populateDefaultEntries()
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setListeners()
    }

    /**
     * Populates the list of entries with default entries, in case of none being present
     */
    private fun populateDefaultEntries() {
        if((application as QuizApp).quizEntries.size == 0){
            (application as QuizApp).quizEntries = mutableListOf(
                QuizEntry("Kenneth", BitmapFactory.decodeResource(resources, R.drawable.cat2)),
                QuizEntry("Henning", BitmapFactory.decodeResource(resources, R.drawable.cat10)),
                QuizEntry("Rosa", BitmapFactory.decodeResource(resources, R.drawable.cat7))
            )
        }
    }

    /**
     * Initializes listeners to enable navigation to other activities
     */
    private fun setListeners(){
        binding.btnQuizActivity.setOnClickListener {
            startActivity(Intent(this, QuizActivity::class.java))
        }
        binding.btnDatabaseActivity.setOnClickListener {
            startActivity(Intent(this, DatabaseActivity::class.java))
        }
        binding.btnNewEntryActivity.setOnClickListener {
            startActivity(Intent(this, NewEntryActivity::class.java))
        }
    }
}