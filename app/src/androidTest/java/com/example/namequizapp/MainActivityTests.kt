package com.example.namequizapp

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.namequizapp.view.DatabaseActivity
import com.example.namequizapp.view.MainActivity
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

    /**
     * Function to test navigation from main activity to database activity
     */
    @Test
    fun testDatabaseNavigation() {
        Intents.init()
        onView(withId(R.id.btnSeeEntries)).perform(click())
        intended(hasComponent(DatabaseActivity::class.java.name))
        Intents.release()
    }
}