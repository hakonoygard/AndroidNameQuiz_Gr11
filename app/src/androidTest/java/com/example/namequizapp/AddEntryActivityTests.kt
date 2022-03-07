package com.example.namequizapp

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import android.net.Uri
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers.hasAction
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.namequizapp.view.AddEntryActivity
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.random.Random


@RunWith(
    AndroidJUnit4::class
)
class AddEntryActivityTests {

    @get:Rule
    var rule: ActivityScenarioRule<AddEntryActivity> =
        ActivityScenarioRule(AddEntryActivity::class.java)

    private lateinit var scenario: ActivityScenario<AddEntryActivity>

    @Before
    fun setup() {
        scenario = rule.scenario
    }

    /**
     * Function for testing adding a new entry in the database
     */
    @Test
    fun givenCorrectData_AddingEntry_ResultsInExtraEntry() {
        Intents.init()

        val testName = "Test" + Random.nextInt(0, 1000)
        val uri = Uri.parse("android.resource://com.example.namequizapp/drawable/cat10")
        val resultData = Intent()
        resultData.data = uri
        val result = Instrumentation.ActivityResult(Activity.RESULT_OK, resultData)
        intending(hasAction(Intent.ACTION_PICK)).respondWith(result)

        onView(withId(R.id.ivNewImage)).perform(click())

        onView(withId(R.id.inputTextName)).perform(
            typeText(testName),
            closeSoftKeyboard()
        )
        onView(withId(R.id.btnSubmit)).perform(click())
        onView(withId(R.id.rvEntries)).check(matches(hasItem(hasDescendant(withText(testName)))))

        Intents.release()
    }
}



/**
 * From https://developer.android.com/training/testing/espresso/recipes.html#asserting-data-item-not-in-adapter
 */
fun hasItem(matcher: Matcher<View?>): Matcher<View?>? {
    return object : BoundedMatcher<View?, RecyclerView>(RecyclerView::class.java) {
        override fun describeTo(description: Description) {
            description.appendText("has item: ")
            matcher.describeTo(description)
        }

        override fun matchesSafely(view: RecyclerView): Boolean {
            val adapter = view.adapter
            for (position in 0 until adapter!!.itemCount) {
                val type = adapter.getItemViewType(position)
                val holder = adapter.createViewHolder(view, type)
                adapter.onBindViewHolder(holder, position)
                if (matcher.matches(holder.itemView)) {
                    return true
                }
            }
            return false
        }
    }
}

