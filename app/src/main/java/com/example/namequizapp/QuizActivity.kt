package com.example.namequizapp

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.namequizapp.databinding.ActivityQuizBinding

/**
 * Activity that runs the quiz.
 */
class QuizActivity : AppCompatActivity() {

    private lateinit var binding: ActivityQuizBinding

    private var score : Int = 0

    private var attempts : Int = 0

    private lateinit var shuffledEntries : MutableList<QuizEntry>

    private var names : MutableList<String> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)
        binding = ActivityQuizBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        actionBar?.setDisplayHomeAsUpEnabled(true)
        shuffledEntries = (application as QuizApp).quizEntries.shuffled().toMutableList()
        populateNamesList()
        quizRound()
    }

    /**
     * Retrieves names for all entries and stores them as possible answers to be shuffled
     */
    private fun populateNamesList(){
        shuffledEntries.forEach { names.add(it.name) }
    }


    /**
     *  Function that provides functionality for one round of quiz.
     */
    private fun quizRound() {
        val correct = shuffledEntries[0]
        shuffledEntries.remove(correct)
        val namesCopy = names.shuffled().toMutableList()
        namesCopy.remove(correct.name)

        val listOptions = listOf(correct.name, namesCopy[0], namesCopy[1]).shuffled()

        binding.ivImage.setImageBitmap(correct.image)
        binding.tvScore.text = getString(R.string.score, score)
        binding.tvAttempts.text = getString(R.string.attempts, attempts)
        binding.btnOption1.text = listOptions[0]
        binding.btnOption2.text = listOptions[1]
        binding.btnOption3.text = listOptions[2]

        binding.btnOption1.setOnClickListener {
            checkIfCorrect(binding.btnOption1.text as String, correct.name)
        }

        binding.btnOption2.setOnClickListener {
            checkIfCorrect(binding.btnOption2.text as String, correct.name)
        }

        binding.btnOption3.setOnClickListener {
            checkIfCorrect(binding.btnOption3.text as String, correct.name)
        }
    }

    /**
     * Function to react to provided answer.
     */
    private fun checkIfCorrect(answer : String, correct : String) {
        attempts++

        if(answer == correct) {
            score++
            Toast.makeText(applicationContext, "Correct!", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(applicationContext, getString(R.string.wrong_answer, correct), Toast.LENGTH_SHORT).show()
        }

        if(shuffledEntries.size > 0) quizRound() else quizEnd()
    }

    /**
     * Run when all images has been guessed on
     */
    private fun quizEnd(){
        binding.btnOption1.visibility = View.GONE
        binding.btnOption2.visibility = View.GONE
        binding.btnOption3.visibility = View.GONE
        binding.tvAttempts.visibility = View.GONE
        binding.ivImage.visibility = View.GONE
        binding.tvScore.visibility = View.GONE
        binding.tvCompletedMessage.text = getString(R.string.completed_message, score, attempts)
        binding.tvCompletedMessage.visibility = View.VISIBLE
    }
}