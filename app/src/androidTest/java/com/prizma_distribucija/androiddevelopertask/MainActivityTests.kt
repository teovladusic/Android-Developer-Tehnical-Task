package com.prizma_distribucija.androiddevelopertask

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.prizma_distribucija.androiddevelopertask.di.launchFragmentInHiltContainer
import com.prizma_distribucija.androiddevelopertask.feature_feed.presentation.feed.FeedFragment
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock

@HiltAndroidTest
class MainActivityTests {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    //don't know how to test bottom menu navigation



}