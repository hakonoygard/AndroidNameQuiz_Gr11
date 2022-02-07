package com.example.namequizapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.namequizapp.databinding.ActivityDatabaseBinding

/**
 * Activity to display entries in memory.
 */
class DatabaseActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDatabaseBinding

    private var sorted : Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_database)
        binding = ActivityDatabaseBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        actionBar?.setDisplayHomeAsUpEnabled(true)
        renderLayout()
    }


    /**
     * Render layout and initialize listeners
     */
    private fun renderLayout() {
        binding.fab.setOnClickListener {
            startActivity(Intent(this, NewEntryActivity::class.java))
        }

        binding.ivSort.setOnClickListener {
            if(sorted) {
                (application as QuizApp).quizEntries.sortByDescending { it.name }
            } else {
                (application as QuizApp).quizEntries.sortBy { it.name }
            }

            sorted = !sorted
           populateRecyclerView()
        }

       populateRecyclerView()
    }

    /**
     * Populates / updates recycler view. Used upon initial creation and when
     * entries are added / deleted.
     */
    private fun populateRecyclerView(){
        val adapter = EntryAdapter((application as QuizApp).quizEntries) {
            (application as QuizApp).quizEntries.remove(it.tag)
            populateRecyclerView()
        }
        binding.rvEntries.adapter = adapter
        binding.rvEntries.layoutManager = LinearLayoutManager(this)
    }
}