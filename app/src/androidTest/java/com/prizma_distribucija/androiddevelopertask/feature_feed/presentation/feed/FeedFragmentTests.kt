package com.prizma_distribucija.androiddevelopertask.feature_feed.presentation.feed

import android.content.Context
import android.widget.VideoView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDialog
import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.MediumTest
import com.prizma_distribucija.androiddevelopertask.R
import com.prizma_distribucija.androiddevelopertask.core.di.AppModule
import com.prizma_distribucija.androiddevelopertask.di.launchFragmentInHiltContainer
import com.prizma_distribucija.androiddevelopertask.fakes.DataStoreManagerFakeImpl
import com.prizma_distribucija.androiddevelopertask.fakes.FeedRepositoryFakeImpl
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.hamcrest.Matchers.not
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
@MediumTest
@HiltAndroidTest
class FeedFragmentTests {

    @get:Rule
    var hiltAndroidRule = HiltAndroidRule(this)

    @Before
    fun setUp() {
        hiltAndroidRule.inject()
    }


    @Test
    fun onViewCreated_shouldCreateUserNotLoggedInDialog() {
        lateinit var dialog: AlertDialog
        launchFragmentInHiltContainer<FeedFragment> {
            //should crash if dialog not initialized
            dialog = userNotLoggedInDialog
        }

        assert(true)
    }

    @Test
    fun onUserNotAuthenticated_shouldShowAlertDialog() {
        lateinit var dialog: AlertDialog

        assert(DataStoreManagerFakeImpl.dataStoreAuthToken.value == "")

        launchFragmentInHiltContainer<FeedFragment> {
            dialog = this.userNotLoggedInDialog
        }

        assert(dialog.isShowing)
    }

    @Test
    fun onLoginClick_shouldNavigateToLoginFragment() {
        lateinit var dialog: AlertDialog

        val navController = TestNavHostController(ApplicationProvider.getApplicationContext())

        assert(DataStoreManagerFakeImpl.dataStoreAuthToken.value == "")

        launchFragmentInHiltContainer<FeedFragment> {
            navController.setGraph(R.navigation.nav_graph)
            Navigation.setViewNavController(this.requireView(), navController)
            dialog = this.userNotLoggedInDialog
        }

        assert(dialog.isShowing)

        onView(withText("Login")).perform(click())

        assert(navController.currentDestination?.id == R.id.loginFragment)
    }

    @Test
    fun onLoginClick_shouldDismissDialog() {
        lateinit var dialog: AlertDialog

        val navController = TestNavHostController(ApplicationProvider.getApplicationContext())

        assert(DataStoreManagerFakeImpl.dataStoreAuthToken.value == "")

        launchFragmentInHiltContainer<FeedFragment> {
            navController.setGraph(R.navigation.nav_graph)
            Navigation.setViewNavController(this.requireView(), navController)
            dialog = this.userNotLoggedInDialog
        }

        assert(dialog.isShowing)

        onView(withText("Login")).perform(click())

        assert(dialog.isShowing == false)
    }

    @Test
    fun onGetFeedError_shouldShowErrorTextView() = runTest {
        FeedRepositoryFakeImpl.delayTime = 0
        DataStoreManagerFakeImpl.dataStoreAuthToken.emit("token")
        FeedRepositoryFakeImpl.isSuccessful = false

        launchFragmentInHiltContainer<FeedFragment> {
        }

        onView(withId(R.id.tv_get_feed_error)).check(matches(isDisplayed()))
        onView(withId(R.id.progress_bar_get_feed)).check(matches(not(isDisplayed())))
    }

    @Test
    fun onGetFeedLoading_shouldProgressBar() = runTest {
        DataStoreManagerFakeImpl.dataStoreAuthToken.emit("token")
        FeedRepositoryFakeImpl.isSuccessful = false
        FeedRepositoryFakeImpl.delayTime = 5000

        launchFragmentInHiltContainer<FeedFragment> {
        }

        onView(withId(R.id.progress_bar_get_feed)).check(matches(isDisplayed()))
    }
}