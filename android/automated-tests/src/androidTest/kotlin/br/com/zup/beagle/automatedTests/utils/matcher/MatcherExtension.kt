package br.com.zup.beagle.automatedTests.utils.matcher

import android.view.Gravity
import android.view.View
import android.widget.TextView
import androidx.test.espresso.AmbiguousViewMatcherException
import androidx.test.espresso.NoMatchingRootException
import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.ViewInteraction
import br.com.zup.beagle.widget.core.TextAlignment
import org.hamcrest.CoreMatchers.any
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher


class MatcherExtension {

    companion object {
        fun exists(interaction: ViewInteraction): Boolean {
            return try {
                interaction.perform(object : ViewAction {
                    override fun getConstraints(): Matcher<View> {
                        return any(View::class.java)
                    }

                    override fun getDescription(): String {
                        return "check for existence"
                    }

                    override fun perform(
                        uiController: UiController?,
                        view: View?
                    ) {
                    }
                })
                true
            } catch (ex: AmbiguousViewMatcherException) {
                true
            } catch (ex: NoMatchingViewException) {
                false
            } catch (ex: NoMatchingRootException) {
                false
            }
        }

        fun withTextAlignment(textAlignment: TextAlignment): Matcher<View?>? {
            return object : TypeSafeMatcher<View>() {
                override fun matchesSafely(item: View): Boolean {
                    if (item is TextView) {
                        return when (textAlignment) {
                            TextAlignment.CENTER -> {
                                item.gravity == Gravity.CENTER
                            }
                            TextAlignment.LEFT -> {
                                item.gravity == (Gravity.START + Gravity.TOP)
                            }
                            TextAlignment.RIGHT -> {
                                item.gravity == (Gravity.END + Gravity.TOP)
                            }
                        }
                    }
                    return false
                }

                override fun describeTo(description: Description) {
                    description.appendText("textAlignment is $textAlignment ")
                }
            }
        }
    }
}
