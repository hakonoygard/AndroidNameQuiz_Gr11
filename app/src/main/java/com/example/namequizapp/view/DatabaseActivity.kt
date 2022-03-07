package com.example.namequizapp.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.coroutineScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.namequizapp.adapters.EntryAdapter
import com.example.namequizapp.data.AppDatabase
import com.example.namequizapp.data.QuizEntryRepository
import com.example.namequizapp.databinding.ActivityDatabaseBinding
import com.example.namequizapp.viewmodels.QuizEntryViewModel
import com.example.namequizapp.viewmodels.QuizEntryViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class DatabaseActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDatabaseBinding
    private lateinit var recyclerView: RecyclerView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDatabaseBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        recyclerView = binding.rvEntries
        recyclerView.layoutManager = LinearLayoutManager(applicationContext)
        val entryAdapter = EntryAdapter {
            lifecycle.coroutineScope.launch(Dispatchers.IO) {
                viewModel.deleteQuizEntry(it)
            }
        }

        recyclerView.adapter = entryAdapter
        populateRecyclerView(entryAdapter)

        binding.ivSort.setOnClickListener {
            viewModel.sortAsc = !viewModel.sortAsc
            populateRecyclerView(entryAdapter)
        }

        binding.fabToNewEntry.setOnClickListener {
            startActivity(Intent(applicationContext, AddEntryActivity::class.java))
        }
    }

    private val viewModel: QuizEntryViewModel by viewModels {
        QuizEntryViewModelFactory(
            QuizEntryRepository((AppDatabase.getDatabase(applicationContext) as AppDatabase).quizEntryDao())
        )
    }

    private fun populateRecyclerView(entryAdapter: EntryAdapter) {
        lifecycle.coroutineScope.launch{
            viewModel.getAllEntries().collect { entries ->
                entryAdapter.submitList(
                    if (viewModel.sortAsc) entries.sortedBy { it.name }
                    else entries.sortedByDescending { it.name }
                )
            }
        }
    }
}