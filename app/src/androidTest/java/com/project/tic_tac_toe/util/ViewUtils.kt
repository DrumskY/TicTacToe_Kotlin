package com.project.tic_tac_toe.util

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.action.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.google.android.material.textfield.TextInputLayout
import org.hamcrest.*

fun childAtPosition(
    parentMatcher: Matcher<View?>, position: Int
): Matcher<View?> {
    return object : TypeSafeMatcher<View?>() {
        override fun describeTo(description: Description) {
            description.appendText("Child at position $position in parent ")
            parentMatcher.describeTo(description)
        }

        override fun matchesSafely(view: View?): Boolean {
            val parent = view?.parent
            return (parent is ViewGroup && parentMatcher.matches(parent)
                    && view == parent.getChildAt(position))
        }
    }
}

fun ViewInteraction.isDisplayed(): Boolean {
    return try {
        check(matches(ViewMatchers.isDisplayed()))
        true
    } catch (e: NoMatchingViewException) {
        false
    }
}

fun getRecyclerViewLastIndex(@IdRes recyclerViewId: Int): Int {
    var numberOfAdapterItems = 0
    onView(withId(recyclerViewId)).check(matches(object : TypeSafeMatcher<View?>() {
        override fun matchesSafely(view: View?): Boolean {
            val listView: RecyclerView = view as RecyclerView

            //here we assume the adapter has been fully loaded already
            numberOfAdapterItems = listView.adapter!!.itemCount
            return true
        }

        override fun describeTo(description: Description?) {}
    }))
    return numberOfAdapterItems - 1
}

fun hasTextInputLayoutErrorText(expectedErrorText: String): Matcher<View> = object : TypeSafeMatcher<View>() {

    override fun describeTo(description: Description?) {}

    override fun matchesSafely(item: View?): Boolean {
        if (item !is TextInputLayout) return false
        val error = item.error ?: return false
        val e = error.toString()
        return expectedErrorText == e
    }
}

fun clickOnViewChild(viewId: Int) = object : ViewAction {
    override fun getConstraints() = null

    override fun getDescription() = "Click on a child view with specified id."

    override fun perform(uiController: UiController, view: View) = ViewActions.click().perform(uiController, view.findViewById(viewId))
}

fun swipeToRight(): ViewAction {
    return GeneralSwipeAction(
        Swipe.FAST,
        GeneralLocation.TOP_LEFT,
        { view ->
            val coordinates = GeneralLocation.TOP_RIGHT.calculateCoordinates(view)
            coordinates[0] = 500f
            coordinates
        }, Press.FINGER
    )
}

fun clickWithoutConstraints() = object : ViewAction {
    override fun getConstraints(): Matcher<View> {
        return ViewMatchers.isEnabled()
    }

    override fun getDescription(): String {
        return "click without constraints"
    }

    override fun perform(uiController: UiController, view: View) {
        view.performClick()
    }
}

fun setTextInTextView(value: String): ViewAction {
    return object : ViewAction {
        override fun getConstraints(): Matcher<View> {
            return CoreMatchers.allOf(ViewMatchers.isDisplayed(), ViewMatchers.isAssignableFrom(TextView::class.java))
        }

        override fun perform(uiController: UiController, view: View) {
            (view as TextView).text = value
        }

        override fun getDescription(): String {
            return "replace text"
        }
    }
}

fun withDrawable(@DrawableRes id: Int, @ColorRes tint: Int? = null, tintMode: PorterDuff.Mode = PorterDuff.Mode.SRC_IN) =
    object : TypeSafeMatcher<View>() {
        override fun describeTo(description: Description) {
            description.appendText("ImageView with drawable same as drawable with id $id")
            tint?.let { description.appendText(", tint color id: $tint, mode: $tintMode") }
        }

        override fun matchesSafely(view: View): Boolean {
            val context = view.context
            val tintColor = tint?.toColor(context)
            val expectedBitmap = context.getDrawable(id)?.tinted(tintColor, tintMode)?.toBitmap()

            return view is ImageView && view.drawable?.toBitmap()?.sameAs(expectedBitmap) ?: false
        }
    }

private fun Int.toColor(context: Context) = ContextCompat.getColor(context, this)

private fun Drawable.tinted(@ColorInt tintColor: Int? = null, tintMode: PorterDuff.Mode = PorterDuff.Mode.SRC_IN) =
    apply {
        setTintList(tintColor?.toColorStateList())
        setTintMode(tintMode)
    }

private fun Int.toColorStateList() = ColorStateList.valueOf(this)

fun withIndex(matcher: Matcher<View?>, index: Int): Matcher<View?> = object : TypeSafeMatcher<View?>() {
    var currentIndex = 0
    override fun describeTo(description: Description) {
        description.appendText("with index: ")
        description.appendValue(index)
        matcher.describeTo(description)
    }

    override fun matchesSafely(item: View?): Boolean {
        return matcher.matches(item) && currentIndex++ == index
    }
}

fun customWithText(title: String) = object : BoundedMatcher<View, View>(View::class.java) {
    override fun describeTo(description: Description?) {
        description?.appendText("Searching for title with: $title")
    }

    override fun matchesSafely(item: View?): Boolean {
        val views = ArrayList<View>()
        item?.findViewsWithText(views, title, View.FIND_VIEWS_WITH_TEXT)

        return when (views.size) {
            1 -> true
            else -> false
        }
    }
}

fun <T> first(matcher: Matcher<T>): Matcher<T>? {
    return object : BaseMatcher<T>() {
        var isFirst = true
        override fun matches(item: Any): Boolean {
            if (isFirst && matcher.matches(item)) {
                isFirst = false
                return true
            }
            return false
        }

        override fun describeTo(description: Description) {
            description.appendText("should return first matching item")
        }
    }
}
