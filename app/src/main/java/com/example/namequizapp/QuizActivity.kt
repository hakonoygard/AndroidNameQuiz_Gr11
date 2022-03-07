package com.example.namequizapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import com.example.namequizapp.data.AppDatabase
import com.example.namequizapp.data.QuizEntryRepository
import com.example.namequizapp.data.QuizQuestion
import com.example.namequizapp.databinding.FragmentQuizBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.namequizapp.data.QuizEntryDao
import com.example.namequizapp.databinding.ActivityMainBinding
import com.example.namequizapp.databinding.ActivityQuizBinding
import com.example.namequizapp.utils.Constants
import com.example.namequizapp.utils.ImageUtils.convertToBitmap
import com.example.namequizapp.viewmodels.QuizGameViewModel
import com.example.namequizapp.viewmodels.QuizGameViewModelFactory

class QuizActivity : AppCompatActivity() {

    //:BaseFragment<ActivityQuizBinding>()

   /* override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): ActivityQuizBinding {
        return ActivityQuizBinding.inflate(inflater, container, false)
    }*/

    private lateinit var binding : ActivityQuizBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuizBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        initializeObservers()
        initializeClickListeners()
        viewModel.fetchQuizEntries()


    }


    private val viewModel: QuizGameViewModel by viewModels {
        QuizGameViewModelFactory(
            QuizEntryRepository((AppDatabase.getDatabase(applicationContext) as AppDatabase).quizEntryDao())
        )
    }





    /*  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
          super.onViewCreated(view, savedInstanceState)
          initializeObservers()
          initializeClickListeners()
          viewModel.fetchQuizEntries()
      }*/

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
        viewModel.quizEntries.observe(this){ viewModel.newGame() }

        viewModel.quizQuestion.observe(this, quizQuestionObserver)

        viewModel.score.observe(this) {
            binding.tvScore.text = it.toString()
        }

        viewModel.attempts.observe(this) {
            binding.tvAttempts.text = it.toString()
        }

        viewModel.btnOneColor.observe(this) {
            binding.btnOption1.setBackgroundColor(
                ContextCompat.getColor(applicationContext, resolveColor(it)))
        }

        viewModel.btnTwoColor.observe(this) {
            binding.btnOption2.setBackgroundColor(
                ContextCompat.getColor(applicationContext, resolveColor(it)))
        }

        viewModel.btnThreeColor.observe(this) {
            binding.btnOption3.setBackgroundColor(
                ContextCompat.getColor(applicationContext, resolveColor(it)))
        }

        viewModel.continueBtnVisibility.observe(this){
            binding.fab.visibility = if(it) View.VISIBLE else View.GONE
        }

        viewModel.answerButtonsEnabled.observe(this){
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