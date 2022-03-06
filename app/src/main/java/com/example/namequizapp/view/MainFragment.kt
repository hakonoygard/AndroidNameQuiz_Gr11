package com.example.namequizapp.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.coroutineScope
import androidx.navigation.findNavController
import com.example.namequizapp.BaseFragment
import com.example.namequizapp.R
import com.example.namequizapp.data.AppDatabase
import com.example.namequizapp.data.QuizEntryRepository
import com.example.namequizapp.databinding.FragmentMainBinding
import com.example.namequizapp.databinding.FragmentQuizBinding
import com.example.namequizapp.viewmodels.QuizEntryViewModel
import com.example.namequizapp.viewmodels.QuizEntryViewModelFactory
import com.example.namequizapp.viewmodels.QuizGameViewModel
import com.example.namequizapp.viewmodels.QuizGameViewModelFactory
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect

/**
 *  Home screen. From this fragment, user can launch new quiz.
 */
class MainFragment : BaseFragment<FragmentMainBinding>() {

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentMainBinding {
        return FragmentMainBinding.inflate(inflater, container, false)
    }


    private val viewModel: QuizGameViewModel by activityViewModels {
        QuizGameViewModelFactory(
            QuizEntryRepository((AppDatabase.getDatabase(requireContext()) as AppDatabase).quizEntryDao())
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnQuizActivity.setOnClickListener {
            it.findNavController().navigate(R.id.action_mainFragment_to_quizFragment)
        }

        viewModel.quizEntries.observe(viewLifecycleOwner) {
            val quizReady = it.size >=3

            binding.btnQuizActivity.isEnabled = quizReady
            binding.tvError.visibility = if (quizReady) View.GONE else View.VISIBLE
        }

        viewModel.fetchQuizEntries()
    }
}