package com.example.tictactoe

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import com.example.tictactoe.util.AND
import com.example.tictactoe.util.BaseRobot
import com.example.tictactoe.util.GIVEN
import com.example.tictactoe.util.RUN_UI_TEST
import org.hamcrest.Matchers.allOf
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class MainActivityFragmentTest {

    private val robot = MainActivityRobot()

    @Test
    fun testMainActivityView() {
        RUN_UI_TEST(robot) {
            GIVEN { createMainActivity() }
            AND { useAppContext() }
            AND { mainActivityTest() }
        }
    }

    class MainActivityRobot: BaseRobot() {

        fun useAppContext() {
            val appContext = InstrumentationRegistry.getInstrumentation().targetContext
            Assert.assertEquals("com.example.tictactoe", appContext.packageName)
        }

        fun mainActivityTest() {
            val helloWorldText = onView(
                allOf(
                    withId(R.id.textViewMainActivity),
                    withText("Hello World!"),
                    isDisplayed()
                )
            )
            helloWorldText.perform(click())
        }
    }
}
