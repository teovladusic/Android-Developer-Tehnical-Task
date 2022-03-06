package com.prizma_distribucija.androiddevelopertask.feature_login.presentation.sign_up

import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewAssertion
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.prizma_distribucija.androiddevelopertask.R
import com.prizma_distribucija.androiddevelopertask.di.launchFragmentInHiltContainer
import com.prizma_distribucija.androiddevelopertask.fakes.SignUpRepositoryFakeImpl
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.not
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

@ExperimentalCoroutinesApi
@HiltAndroidTest
class SignUpFragmentTests {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun onSignUpClick_passwordAndFullNameAreEmpty_shouldConsumeShowMessageEvent() {
        launchFragmentInHiltContainer<SignUpFragment> {
        }

        onView(withId(R.id.text_input_sign_up_email)).perform(
            clearText(), typeText("random@email.com"),
            closeSoftKeyboard()
        )

        onView(withId(R.id.btn_sign_up)).perform(ViewActions.click())

        onView(withId(com.google.android.material.R.id.snackbar_text))
            .check(matches(ViewMatchers.withText("Please fill out all fields.")))
    }

    @Test
    fun onSignUpLoading_showProgressBar() = runTest {
        launchFragmentInHiltContainer<SignUpFragment> {
        }

        onView(withId(R.id.text_input_sign_up_email)).perform(
            typeText("email"),
            closeSoftKeyboard()
        )
        onView(withId(R.id.text_input_sign_up_password)).perform(
            typeText("password"),
            closeSoftKeyboard()
        )
        onView(withId(R.id.text_input_sign_up_full_name)).perform(
            typeText("fullName"),
            closeSoftKeyboard()
        )

        SignUpRepositoryFakeImpl.delayTime = 1000

        onView(withId(R.id.btn_sign_up)).perform(click())

        onView(withId(R.id.progress_bar)).check(matches(isDisplayed()))
    }

    @Test
    fun onSignUpError_showSnackbarAndHideProgressBar() {
        launchFragmentInHiltContainer<SignUpFragment> {
        }

        onView(withId(R.id.text_input_sign_up_email)).perform(
            typeText("email"),
            closeSoftKeyboard()
        )
        onView(withId(R.id.text_input_sign_up_password)).perform(
            typeText("password"),
            closeSoftKeyboard()
        )
        onView(withId(R.id.text_input_sign_up_full_name)).perform(
            typeText("fullName"),
            closeSoftKeyboard()
        )

        SignUpRepositoryFakeImpl.delayTime = 0

        onView(withId(R.id.btn_sign_up)).perform(click())

        onView(withId(R.id.progress_bar)).check(matches(not(isDisplayed())))

        onView(withId(com.google.android.material.R.id.snackbar_text))
            .check(matches(ViewMatchers.withText("error message")))
    }

    @Test
    fun onRegisterSuccess_navigateToFeedFragment() = runTest {
        val navController = mock(NavController::class.java)

        launchFragmentInHiltContainer<SignUpFragment> {
            navController.setGraph(R.id.nav_graph)
            Navigation.setViewNavController(this.requireView(), navController)
        }

        onView(withId(R.id.text_input_sign_up_email)).perform(
            typeText("email"),
            closeSoftKeyboard()
        )
        onView(withId(R.id.text_input_sign_up_password)).perform(
            typeText("password"),
            closeSoftKeyboard()
        )
        onView(withId(R.id.text_input_sign_up_full_name)).perform(
            typeText("fullName"),
            closeSoftKeyboard()
        )

        SignUpRepositoryFakeImpl.delayTime = 0
        SignUpRepositoryFakeImpl.isSuccessful = true

        onView(withId(R.id.btn_sign_up)).perform(click())

        verify(navController).navigate(R.id.action_global_feedFragment)
    }
}