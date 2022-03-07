package com.example.namequizapp

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasAction
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.namequizapp.adapters.EntryAdapterNew
import org.hamcrest.Matcher
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


/**
 * All tests are with Main activity as rule, as the application is following
 * single activity architecture
 */

@RunWith(
    AndroidJUnit4::class
)
class ExampleInstrumentedTest {


    @get:Rule
    var rule: ActivityScenarioRule<MainActivity> = ActivityScenarioRule(MainActivity::class.java)

    private lateinit var scenario: ActivityScenario<MainActivity>


    @Before
    fun setup() {
        scenario = rule.scenario
    }

    @Test
    fun testQuizActivityNavigation() {
        //Click on "Start Quiz" button to go to QuizActivity
        onView(withId(R.id.btnStartQuiz)).perform(click())
        //Check if intent with QuizActivity has been launched
        //onView(withId(R.id.ivImage)).check(matches(isDisplayed()))


        intended(hasAction("android.intent.category.LAUNCHER"))
    }

    @Test
    fun testAddEntryActivityNavigation(){
    }

    @Test
    fun testDeletingEntry() {
        onView(withId(R.id.entryOverviewFragment)).perform(click())

        var firstCount = 0
        var newCount = 0

        var recyclerView: RecyclerView? = null

        scenario.onActivity {
            recyclerView = it.findViewById<RecyclerView>(R.id.rvEntries)
        }
        recyclerView?.adapter?.let { adapter ->
            firstCount = adapter.itemCount

            if(firstCount > 0) {
                onView(withId(R.id.rvEntries)).perform(
                    RecyclerViewActions.actionOnItemAtPosition<EntryAdapterNew.EntryAdapterNewViewHolder>(
                        0,
                        CustomViewAction.clickChildViewWithId(R.id.btnSlett)
                    )
                )
            }

            Thread.sleep(100)

            newCount = adapter.itemCount
        }

        if (firstCount != 0) {
            assert(newCount < firstCount)
        } else {
            assertTrue(firstCount == newCount)
        }
    }
}

object CustomViewAction {
    fun clickChildViewWithId(id: Int): ViewAction {
        return object : ViewAction {
            override fun getConstraints(): Matcher<View>? {
                return null
            }

            override fun getDescription(): String {
                return "Click on a child view with specified id."
            }

            override fun perform(uiController: UiController?, view: View) {
                val v: View = view.findViewById(id)
                v.performClick()
            }
        }
    }
}