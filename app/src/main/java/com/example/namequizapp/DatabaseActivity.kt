package com.example.namequizapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.coroutineScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.namequizapp.adapters.EntryAdapterNew
import com.example.namequizapp.data.AppDatabase
import com.example.namequizapp.data.QuizEntryRepository
import com.example.namequizapp.databinding.ActivityDatabaseBinding
import com.example.namequizapp.databinding.FragmentEntriesOverviewBinding
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
        var view = binding.root
        setContentView(view)

        recyclerView = binding.rvEntries
        recyclerView.layoutManager = LinearLayoutManager(applicationContext)
        val entryAdapter = EntryAdapterNew {
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
            //it.findNavController().navigate(R.id.action_entryOverviewFragment_to_newEntryFragment)
            startActivity(Intent(applicationContext, AddEntryActivity::class.java))
        }
    }



    private val viewModel: QuizEntryViewModel by viewModels {
        QuizEntryViewModelFactory(
            QuizEntryRepository((AppDatabase.getDatabase(applicationContext) as AppDatabase).quizEntryDao())
        )
    }



    private fun populateRecyclerView(entryAdapterNew: EntryAdapterNew) {
        lifecycle.coroutineScope.launch{
            viewModel.getAllEntries().collect { entries ->
                entryAdapterNew.submitList(
                    if (viewModel.sortAsc) entries.sortedBy { it.name }
                    else entries.sortedByDescending { it.name }
                )
            }
        }
    }
}