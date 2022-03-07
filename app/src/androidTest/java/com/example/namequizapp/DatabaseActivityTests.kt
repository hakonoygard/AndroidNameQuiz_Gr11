package com.example.namequizapp

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.namequizapp.adapters.EntryAdapter
import com.example.namequizapp.view.DatabaseActivity
import org.hamcrest.Matcher
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(
    AndroidJUnit4::class
)
class DatabaseActivityTests {

    @get:Rule
    var rule: ActivityScenarioRule<DatabaseActivity> =
        ActivityScenarioRule(DatabaseActivity::class.java)

    private lateinit var scenario: ActivityScenario<DatabaseActivity>

    @Before
    fun setup() {
        scenario = rule.scenario
    }


    /**
     * Function for testing deleting of an entry in the database
     */
    @Test
    fun testDeletingEntry() {

        var firstCount = 0
        var newCount = 0

        var recyclerView: RecyclerView? = null

        scenario.onActivity {
            recyclerView = it.findViewById(R.id.rvEntries)
        }
        recyclerView?.adapter?.let { adapter ->
            firstCount = adapter.itemCount

            if (firstCount > 0) {
                onView(withId(R.id.rvEntries)).perform(
                    RecyclerViewActions.actionOnItemAtPosition<EntryAdapter.EntryAdapterNewViewHolder>(
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

