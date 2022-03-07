package com.example.namequizapp.viewmodels

import androidx.lifecycle.*
import com.example.namequizapp.data.QuizEntryModel
import com.example.namequizapp.data.QuizEntryRepository
import com.example.namequizapp.data.QuizQuestion
import com.example.namequizapp.utils.Constants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class QuizGameViewModel(private val repository: QuizEntryRepository) : ViewModel() {

    private var currentIndex = 0

    private val _attempts = MutableLiveData(0)
    val attempts: LiveData<Int>
        get() = _attempts

    private val _score = MutableLiveData(0)
    val score: LiveData<Int>
        get() = _score

    private val _quizEntries = MutableLiveData<List<QuizEntryModel>>()
    val quizEntries: LiveData<List<QuizEntryModel>>
        get() = _quizEntries


    private val _quizQuestion = MutableLiveData<QuizQuestion>()
    val quizQuestion : LiveData<QuizQuestion>
        get() = _quizQuestion

    private val _btnOneColor = MutableLiveData<Constants.BUTTON_COLORS>()
    val btnOneColor : LiveData<Constants.BUTTON_COLORS>
        get() = _btnOneColor


    private val _btnTwoColor = MutableLiveData<Constants.BUTTON_COLORS>()
    val btnTwoColor : LiveData<Constants.BUTTON_COLORS>
        get() = _btnTwoColor

    private val _btnThreeColor = MutableLiveData<Constants.BUTTON_COLORS>()
    val btnThreeColor : LiveData<Constants.BUTTON_COLORS>
        get() = _btnThreeColor

    private val _continueBtnVisibility = MutableLiveData(false)
    val continueBtnVisibility : LiveData<Boolean>
        get() = _continueBtnVisibility

    private val _answerButtonsEnabled = MutableLiveData(true)
    val answerButtonsEnabled : LiveData<Boolean>
        get() = _answerButtonsEnabled


    private lateinit var listOfNames: List<String>

    private var correct : QuizEntryModel? = null


    private fun getAllEntries() : Flow<List<QuizEntryModel>> =
        repository.getAllQuizEntries()


    fun fetchQuizEntries(){
        viewModelScope.launch {
            getAllEntries().collect {
                _quizEntries.value = it.also {
                    listOfNames = it.map { model -> model.name }
                }

            }
        }
    }



    fun getNextQuizQuestion() {
        _continueBtnVisibility.value = false
        _btnOneColor.value = Constants.BUTTON_COLORS.GRAY
        _btnTwoColor.value = Constants.BUTTON_COLORS.GRAY
        _btnThreeColor.value = Constants.BUTTON_COLORS.GRAY
        _answerButtonsEnabled.value = true
        quizEntries.value?.let {
            with(it[currentIndex]) {
                correct = this

                val shuffledNames = listOfNames.shuffled().toMutableList()
                shuffledNames.remove(this.name)

                val listOfOptions = listOf(this.name, shuffledNames[0], shuffledNames[1]).shuffled()

                _quizQuestion.value = QuizQuestion(
                    image = this.image,
                    optionOne = listOfOptions[0],
                    optionTwo= listOfOptions[1],
                    optionThree = listOfOptions[2],
                    correct = this
                )

                if (currentIndex == (quizEntries.value?.size?.minus(1))) currentIndex = 0 else currentIndex++
            }
        }
    }



    fun checkIfCorrect(answer: String) {
        _attempts.value = _attempts.value?.plus(1)
        if (answer == correct?.name ?: "Wrong")
            _score.value = _score.value?.plus(1)

        _quizQuestion.value?.let {
            _btnOneColor.value = updateButtonColor(answer, it.optionOne)
            _btnTwoColor.value = updateButtonColor(answer, it.optionTwo)
            _btnThreeColor.value = updateButtonColor(answer, it.optionThree)
        }

        _answerButtonsEnabled.value = false
        _continueBtnVisibility.value = true

    }

    private fun updateButtonColor(answer: String, value: String) : Constants.BUTTON_COLORS {

        if(value == answer){
            return if(answer == correct?.name ?: "Wrong") Constants.BUTTON_COLORS.GREEN
            else Constants.BUTTON_COLORS.RED
        }

        return if(value == correct?.name ?: "Wrong") Constants.BUTTON_COLORS.GREEN
        else Constants.BUTTON_COLORS.GRAY
    }

    fun newGame() {
        _attempts.value = 0
        _score.value = 0
        currentIndex = 0
        getNextQuizQuestion()
    }
}


class QuizGameViewModelFactory(
    private val repository: QuizEntryRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(QuizGameViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return QuizGameViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}