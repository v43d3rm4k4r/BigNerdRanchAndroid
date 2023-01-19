package com.bignardranch.beatbox

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Rule

@RunWith(AndroidJUnit4::class)
internal class MainActivityTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.bignardranch.beatbox", appContext.packageName)
    }

    @Test
    fun showsFirstFileName() {
        onView(withText(BUTTON_TEXT))
            .check(matches(isDisplayed()))
    }

    @Test
    fun playsSound() {
        onView(withText(BUTTON_TEXT))
            .perform(click())
    }

    private companion object {
        const val BUTTON_TEXT = "cjipie"
    }
}