package com.project.tic_tac_toe

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import com.project.tic_tac_toe.util.*
import org.hamcrest.Matchers.allOf
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    private val robot = MainActivityRobot()

    @Test
    fun testMainActivityView() {
        RUN_UI_TEST(robot) {
            GIVEN { createMainActivity() }
            AND { useAppContext() }
        }
    }

    @Test
    fun testResetButton() {
        RUN_UI_TEST(robot) {
            GIVEN { createMainActivity() }
            AND { clickOnSpecificPlaceOnBoard(0) }
            AND { clickOnResetButton() }
        }
    }

    @Test
    fun testSimpleGameplayCirclesWon() {
        RUN_UI_TEST(robot) {
            GIVEN { createMainActivity() }
            AND { clickOnSpecificPlaceOnBoard(0) }
            AND { clickOnSpecificPlaceOnBoard(1) }
            AND { clickOnSpecificPlaceOnBoard(3) }
            AND { thenIShouldSeeTextWinningMessage() }
            AND { clickOnResetButton() }
        }
    }

    class MainActivityRobot : BaseRobot() {

        fun useAppContext() {
            val appContext = InstrumentationRegistry.getInstrumentation().targetContext
            Assert.assertEquals("com.project.tic_tac_toe", appContext.packageName)
        }

        fun clickOnResetButton() {
            val buttonReset = onView(
                allOf(
                    withId(R.id.button_reset), withText("Reset"),
                    childAtPosition(
                        childAtPosition(
                            withId(android.R.id.content),
                            0
                        ),
                        13
                    ),
                    isDisplayed()
                )
            )
            buttonReset.perform(click())
            Thread.sleep(2000)
        }

        fun clickOnSpecificPlaceOnBoard(position: Int) {
            val topLeft = onView(
                allOf(
                    withContentDescription("Tic-Tac-Toe Cell"),
                    childAtPosition(
                        childAtPosition(
                            withId(android.R.id.content),
                            0
                        ),
                        position
                    ),
                    isDisplayed()
                )
            )
            topLeft.perform(click())
            Thread.sleep(1500)
        }

        fun thenIShouldSeeTextWinningMessage() {
            Thread.sleep(1000)
            allOf(
                withIndex(
                withId(R.id.text_winning_message),
                0
                ),
                isDisplayed()
            )
            Thread.sleep(2000)
        }
    }
}