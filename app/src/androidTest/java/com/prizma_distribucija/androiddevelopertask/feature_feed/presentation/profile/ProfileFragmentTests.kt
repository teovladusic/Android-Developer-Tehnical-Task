package com.prizma_distribucija.androiddevelopertask.feature_feed.presentation.profile

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import com.prizma_distribucija.androiddevelopertask.R
import com.prizma_distribucija.androiddevelopertask.di.launchFragmentInHiltContainer
import com.prizma_distribucija.androiddevelopertask.fakes.ProfileRepositoryFakeImpl
import com.prizma_distribucija.androiddevelopertask.feature_feed.domain.model.Athlete
import com.prizma_distribucija.androiddevelopertask.feature_feed.domain.model.Plan
import com.prizma_distribucija.androiddevelopertask.feature_feed.domain.model.User
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.hamcrest.Matchers.not
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
@HiltAndroidTest
class ProfileFragmentTests {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun onGetUserSuccess_shouldPopulateUserUi() {
        ProfileRepositoryFakeImpl.isSuccessful = true
        ProfileRepositoryFakeImpl.delayTime = 0

        launchFragmentInHiltContainer<ProfileFragment> { }

        //check that views are correctly display and hidden
        onView(withId(R.id.constraint_layout)).check(matches(isDisplayed()))
        onView(withId(R.id.constraint_layout_stats)).check(matches(isDisplayed()))
        onView(withId(R.id.tv_athletes)).check(matches(isDisplayed()))
        onView(withId(R.id.progress_bar_get_profile)).check(matches(not(isDisplayed())))

        //check that ui is correctly populated
        val plan = Plan("type")
        val athletes = listOf(Athlete(1, "name", "avatar"))
        val user = User(
            id = 1,
            name = "name",
            avatar = "avatar",
            views = "views",
            videoPlays = "videoPlays",
            plan = plan,
            athletes = athletes
        )

        onView(withId(R.id.tv_name)).check(matches(withText(user.name)))
        onView(withId(R.id.tv_views_count)).check(matches(withText(user.views)))
        onView(withId(R.id.tv_video_plays_count)).check(matches(withText(user.videoPlays)))
        onView(withId(R.id.tv_plan_name)).check(matches(withText(user.plan.type)))

        onView(withId(R.id.tv_athlete_name)).check(matches(withText(athletes[0].name)))
    }

    @Test
    fun onGetUserLoading_shouldDisplayProgressBarAndHideOtherViews() = runTest {
        ProfileRepositoryFakeImpl.isSuccessful = false
        ProfileRepositoryFakeImpl.delayTime = 500

        launchFragmentInHiltContainer<ProfileFragment> { }

        onView(withId(R.id.constraint_layout)).check(matches(not(isDisplayed())))
        onView(withId(R.id.constraint_layout_stats)).check(matches(not(isDisplayed())))
        onView(withId(R.id.tv_athletes)).check(matches(not(isDisplayed())))
        onView(withId(R.id.progress_bar_get_profile)).check(matches(isDisplayed()))
    }

    @Test
    fun onGetUserError_shouldDisplaySnackbarAndCorrectlyDisplayUi() = runTest {
        ProfileRepositoryFakeImpl.isSuccessful = false
        ProfileRepositoryFakeImpl.delayTime = 0

        launchFragmentInHiltContainer<ProfileFragment> {
        }

        onView(withId(com.google.android.material.R.id.snackbar_text))
            .check(matches(withText("error message")))


        //check that views are correctly display and hidden
        onView(withId(R.id.constraint_layout)).check(matches(isDisplayed()))
        onView(withId(R.id.constraint_layout_stats)).check(matches(isDisplayed()))
        onView(withId(R.id.tv_athletes)).check(matches(isDisplayed()))
        onView(withId(R.id.progress_bar_get_profile)).check(matches(not(isDisplayed())))
    }


}