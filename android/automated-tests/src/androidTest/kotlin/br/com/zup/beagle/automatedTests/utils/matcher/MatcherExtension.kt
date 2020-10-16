package br.com.zup.beagle.automatedTests.utils.matcher

import android.support.test.espresso.AmbiguousViewMatcherException
import android.support.test.espresso.NoMatchingRootException
import android.support.test.espresso.NoMatchingViewException
import android.support.test.espresso.UiController
import android.support.test.espresso.ViewAction
import android.support.test.espresso.ViewInteraction
import android.view.View
import org.hamcrest.CoreMatchers.any
import org.hamcrest.Matcher


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
    }
}
