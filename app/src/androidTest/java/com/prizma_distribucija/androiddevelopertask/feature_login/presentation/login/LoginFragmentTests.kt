package com.prizma_distribucija.androiddevelopertask.feature_login.presentation.login

import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import com.prizma_distribucija.androiddevelopertask.R
import com.prizma_distribucija.androiddevelopertask.di.launchFragmentInHiltContainer
import com.prizma_distribucija.androiddevelopertask.fakes.LoginRepositoryFakeImpl
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.CoreMatchers.not
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

@ExperimentalCoroutinesApi
@HiltAndroidTest
class LoginFragmentTests {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun onRegisterClick_shouldNavigateToSignUpFragment() {
        val navController = mock(NavController::class.java)

        launchFragmentInHiltContainer<LoginFragment> {
            navController.setGraph(R.navigation.nav_graph)
            Navigation.setViewNavController(this.requireView(), navController)
        }

        onView(withText("Register")).perform(click())

        val action = LoginFragmentDirections.actionLoginFragmentToSignUpFragment()
        verify(navController).navigate(action)
    }

    @Test
    fun onLoginClick_dataIsNotValid_shouldShowSnackbar() {
        launchFragmentInHiltContainer<LoginFragment> {
        }

        onView(withId(R.id.text_input_email)).perform(
            clearText(), typeText("random@email.com"),
            closeSoftKeyboard()
        )

        onView(withId(R.id.btn_login)).perform(click())

        onView(withId(com.google.android.material.R.id.snackbar_text))
            .check(matches(withText("Please fill out all information.")))
    }

    @Test
    fun onLoginLoading_shouldDisplayProgressBar() = runTest {
        launchFragmentInHiltContainer<LoginFragment> {
        }

        onView(withId(R.id.text_input_email)).perform(
            clearText(), typeText("random@email.com"),
            closeSoftKeyboard()
        )

        onView(withId(R.id.text_input_password)).perform(
            clearText(), typeText("password"),
            closeSoftKeyboard()
        )

        LoginRepositoryFakeImpl.delayTime = 500
        LoginRepositoryFakeImpl.isSuccessful = false

        onView(withId(R.id.btn_login)).perform(click())

        onView(withId(R.id.progress_bar_login))
            .check(matches(isDisplayed()))
    }

    @Test
    fun onLoginError_shouldDisplaySnackbarAndHideProgressaBar() = runTest {
        launchFragmentInHiltContainer<LoginFragment> {
        }

        onView(withId(R.id.text_input_email)).perform(
            clearText(), typeText("random@email.com"),
            closeSoftKeyboard()
        )

        onView(withId(R.id.text_input_password)).perform(
            clearText(), typeText("password"),
            closeSoftKeyboard()
        )

        LoginRepositoryFakeImpl.isSuccessful = false

        onView(withId(R.id.btn_login)).perform(click())

        onView(withId(com.google.android.material.R.id.snackbar_text))
            .check(matches(withText("error message")))

        onView(withId(R.id.progress_bar_login))
            .check(matches(not(isDisplayed())))
    }

    @Test
    fun onLoginSuccess_shouldHideProgressBarAndNavigateToFeedFragment() {
        val navController = mock(NavController::class.java)
        launchFragmentInHiltContainer<LoginFragment> {
            navController.setGraph(R.id.nav_graph)
            Navigation.setViewNavController(this.requireView(), navController)
        }

        onView(withId(R.id.text_input_email)).perform(
            clearText(), typeText("random@email.com"),
            closeSoftKeyboard()
        )

        onView(withId(R.id.text_input_password)).perform(
            clearText(), typeText("password"),
            closeSoftKeyboard()
        )

        LoginRepositoryFakeImpl.isSuccessful = true

        onView(withId(R.id.btn_login)).perform(click())

        onView(withId(R.id.progress_bar_login))
            .check(matches(not(isDisplayed())))

        verify(navController).navigate(R.id.action_global_feedFragment)
    }

}
