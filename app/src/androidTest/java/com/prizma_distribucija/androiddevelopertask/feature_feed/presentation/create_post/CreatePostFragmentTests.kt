package com.prizma_distribucija.androiddevelopertask.feature_feed.presentation.create_post

import androidx.appcompat.app.AlertDialog
import androidx.datastore.core.DataStore
import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers
import com.prizma_distribucija.androiddevelopertask.R
import com.prizma_distribucija.androiddevelopertask.di.launchFragmentInHiltContainer
import com.prizma_distribucija.androiddevelopertask.fakes.DataStoreManagerFakeImpl
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
@ExperimentalCoroutinesApi
class CreatePostFragmentTests {

    @get:Rule
    var hiltAndroidRule = HiltAndroidRule(this)

    @Before
    fun setUp() {
        hiltAndroidRule.inject()
    }

    @Test
    fun onViewCreated_shouldCreateUserNotLoggedInDialog() {
        lateinit var dialog: AlertDialog
        launchFragmentInHiltContainer<CreatePostFragment> {
            //should crash if dialog not initialized
            dialog = userNotLoggedInDialog
        }

        assert(true)
    }

    @Test
    fun onUserNotAuthenticated_shouldShowAlertDialog() = runTest {
        lateinit var dialog: AlertDialog

        DataStoreManagerFakeImpl.dataStoreAuthToken.emit("")

        assert(DataStoreManagerFakeImpl.dataStoreAuthToken.value == "")

        launchFragmentInHiltContainer<CreatePostFragment> {
            dialog = this.userNotLoggedInDialog
        }

        assert(dialog.isShowing)
    }

    @Test
    fun onUserAuthenticated_shouldHideAlertDialog() = runTest {
        lateinit var dialog: AlertDialog

        DataStoreManagerFakeImpl.dataStoreAuthToken.emit("token")

        assert(DataStoreManagerFakeImpl.dataStoreAuthToken.value == "token")

        launchFragmentInHiltContainer<CreatePostFragment> {
            dialog = this.userNotLoggedInDialog
        }

        assert(dialog.isShowing == false)
    }

    @Test
    fun onLoginClick_shouldNavigateToLoginFragment() = runTest {
        lateinit var dialog: AlertDialog

        val navController = TestNavHostController(ApplicationProvider.getApplicationContext())

        DataStoreManagerFakeImpl.dataStoreAuthToken.emit("")

        assert(DataStoreManagerFakeImpl.dataStoreAuthToken.value == "")

        launchFragmentInHiltContainer<CreatePostFragment> {
            navController.setGraph(R.navigation.nav_graph)
            Navigation.setViewNavController(this.requireView(), navController)
            dialog = this.userNotLoggedInDialog
        }

        assert(dialog.isShowing)

        Espresso.onView(ViewMatchers.withText("Login")).perform(ViewActions.click())

        assert(navController.currentDestination?.id == R.id.loginFragment)
    }

    @Test
    fun onLoginClick_shouldDismissDialog() = runTest {
        lateinit var dialog: AlertDialog

        val navController = TestNavHostController(ApplicationProvider.getApplicationContext())

        DataStoreManagerFakeImpl.dataStoreAuthToken.emit("")

        assert(DataStoreManagerFakeImpl.dataStoreAuthToken.value == "")

        launchFragmentInHiltContainer<CreatePostFragment> {
            navController.setGraph(R.navigation.nav_graph)
            Navigation.setViewNavController(this.requireView(), navController)
            dialog = this.userNotLoggedInDialog
        }

        assert(dialog.isShowing)

        Espresso.onView(ViewMatchers.withText("Login")).perform(ViewActions.click())

        assert(dialog.isShowing == false)
    }
}