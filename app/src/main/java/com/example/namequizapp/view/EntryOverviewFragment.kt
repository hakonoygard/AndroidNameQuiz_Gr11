package com.example.namequizapp.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.coroutineScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.namequizapp.BaseFragment
import com.example.namequizapp.R
import com.example.namequizapp.adapters.EntryAdapterNew
import com.example.namequizapp.data.AppDatabase
import com.example.namequizapp.data.QuizEntryRepository
import com.example.namequizapp.databinding.FragmentEntriesOverviewBinding
import com.example.namequizapp.viewmodels.QuizEntryViewModel
import com.example.namequizapp.viewmodels.QuizEntryViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class EntryOverviewFragment : BaseFragment<FragmentEntriesOverviewBinding>() {


    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentEntriesOverviewBinding {
        return FragmentEntriesOverviewBinding.inflate(inflater, container, false)
    }

    private val viewModel: QuizEntryViewModel by activityViewModels {
        QuizEntryViewModelFactory(
            QuizEntryRepository((AppDatabase.getDatabase(requireContext()) as AppDatabase).quizEntryDao())
        )
    }

    private lateinit var recyclerView: RecyclerView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = binding.rvEntries
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
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
            it.findNavController().navigate(R.id.action_entryOverviewFragment_to_newEntryFragment)
        }
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