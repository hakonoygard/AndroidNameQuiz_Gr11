package com.example.namequizapp

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.namequizapp.view.QuizActivity
import org.hamcrest.Matchers.allOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(
    AndroidJUnit4::class
)
class QuizActivityTests {

    @get:Rule
    var rule: ActivityScenarioRule<QuizActivity> = ActivityScenarioRule(QuizActivity::class.java)

    private lateinit var scenario: ActivityScenario<QuizActivity>

    @Before
    fun setup() {
        scenario = rule.scenario
    }

    /**
     * Function to test if correct answer gives increase in score counter
     */
    @Test
    fun givenCorrectAnswerProvided_ScoreIsIncremented() {
        var correctAnswer = ""

        scenario.onActivity {
            correctAnswer = it.viewModel.quizQuestion.value?.correct?.name ?: ""
        }

        onView(withText(correctAnswer)).perform(click())
        onView(allOf(withId(R.id.tvScore), withText("1"))).check(matches(isDisplayed()))
    }

    /**
     * Function to test that wrong answer does not increase score counter
     */
    @Test
    fun givenWrongAnswerProvided_ScoreIsIncremented() {
        var wrongAnswer = ""

        scenario.onActivity {
            val correctAnswer = it.viewModel.quizQuestion.value?.correct?.name ?: ""
            wrongAnswer = if (it.viewModel.quizQuestion.value?.optionOne == correctAnswer) {
                    it.viewModel.quizQuestion.value?.optionTwo ?: ""
            } else it.viewModel.quizQuestion.value?.optionOne ?: ""
        }

        onView(withText(wrongAnswer)).perform(click())
        onView(allOf(withId(R.id.tvScore), withText("0"))).check(matches(isDisplayed()))
    }

}