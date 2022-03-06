package com.example.namequizapp.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.example.namequizapp.BaseFragment
import com.example.namequizapp.R
import com.example.namequizapp.data.AppDatabase
import com.example.namequizapp.data.QuizEntryRepository
import com.example.namequizapp.data.QuizQuestion
import com.example.namequizapp.databinding.FragmentQuizBinding
import com.example.namequizapp.utils.Constants
import com.example.namequizapp.utils.ImageUtils.convertToBitmap
import com.example.namequizapp.viewmodels.QuizGameViewModel
import com.example.namequizapp.viewmodels.QuizGameViewModelFactory

class QuizFragment : BaseFragment<FragmentQuizBinding>() {

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentQuizBinding {
        return FragmentQuizBinding.inflate(inflater, container, false)
    }

    private val viewModel: QuizGameViewModel by activityViewModels {
        QuizGameViewModelFactory(
            QuizEntryRepository((AppDatabase.getDatabase(requireContext()) as AppDatabase).quizEntryDao())
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeObservers()
        initializeClickListeners()
        viewModel.fetchQuizEntries()
    }

    private fun initializeClickListeners() {
        binding.btnOption1.setOnClickListener(btnListener)
        binding.btnOption2.setOnClickListener(btnListener)
        binding.btnOption3.setOnClickListener(btnListener)
        binding.fab.setOnClickListener{
            viewModel.getNextQuizQuestion()
        }
    }

    private val btnListener= View.OnClickListener { view ->
        val answer = when (view.id) {
            R.id.btnOption1 -> binding.btnOption1.text
            R.id.btnOption2 -> binding.btnOption2.text
            R.id.btnOption3 -> binding.btnOption3.text
            else -> "NAN"
        }
        viewModel.checkIfCorrect(answer as String)
    }

    private fun initializeObservers() {
        viewModel.quizEntries.observe(viewLifecycleOwner){ viewModel.newGame() }

        viewModel.quizQuestion.observe(viewLifecycleOwner, quizQuestionObserver)

        viewModel.score.observe(viewLifecycleOwner) {
            binding.tvScore.text = it.toString()
        }

        viewModel.attempts.observe(viewLifecycleOwner) {
            binding.tvAttempts.text = it.toString()
        }

        viewModel.btnOneColor.observe(viewLifecycleOwner) {
            binding.btnOption1.setBackgroundColor(
                ContextCompat.getColor(requireContext(), resolveColor(it)))
        }

        viewModel.btnTwoColor.observe(viewLifecycleOwner) {
            binding.btnOption2.setBackgroundColor(
                ContextCompat.getColor(requireContext(), resolveColor(it)))
        }

        viewModel.btnThreeColor.observe(viewLifecycleOwner) {
            binding.btnOption3.setBackgroundColor(
                ContextCompat.getColor(requireContext(), resolveColor(it)))
        }

        viewModel.continueBtnVisibility.observe(viewLifecycleOwner){
            binding.fab.visibility = if(it) View.VISIBLE else View.GONE
        }

        viewModel.answerButtonsEnabled.observe(viewLifecycleOwner){
            binding.btnOption1.isEnabled = it
            binding.btnOption2.isEnabled = it
            binding.btnOption3.isEnabled = it
        }

    }

    private fun resolveColor(color : Constants.BUTTON_COLORS): Int {
        return when(color) {
            Constants.BUTTON_COLORS.RED -> R.color.red
            Constants.BUTTON_COLORS.GREEN -> R.color.green
            else -> R.color.gray
        }
    }

    private val quizQuestionObserver = Observer<QuizQuestion> { quizQuestion ->
        binding.btnOption1.text = quizQuestion.optionOne
        binding.btnOption2.text = quizQuestion.optionTwo
        binding.btnOption3.text = quizQuestion.optionThree
        binding.ivImage.setImageBitmap(quizQuestion.image.convertToBitmap())
    }
}