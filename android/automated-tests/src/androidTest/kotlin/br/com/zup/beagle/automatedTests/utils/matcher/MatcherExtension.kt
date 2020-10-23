/*
 * Copyright 2020 ZUP IT SERVICOS EM TECNOLOGIA E INOVACAO SA
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package br.com.zup.beagle.automatedTests.utils.matcher

import android.view.View
import androidx.test.espresso.AmbiguousViewMatcherException
import androidx.test.espresso.NoMatchingRootException
import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.ViewInteraction
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
