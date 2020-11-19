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

package br.com.zup.beagle.automatedTests.utils.action

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.matcher.ViewMatchers
import org.hamcrest.Matcher
import org.hamcrest.Matchers

class SmoothScrollAction(private val position: Int, val onScrollEnd: () -> Unit) : ViewAction {
    override fun getConstraints(): Matcher<View> {
        return Matchers.allOf(ViewMatchers.isAssignableFrom(RecyclerView::class.java), ViewMatchers.isDisplayed())
    }

    override fun getDescription(): String = ""

    override fun perform(uiController: UiController?, view: View?) {
        uiController?.loopMainThreadForAtLeast(100)
        val recyclerView = view as RecyclerView
        if (canScroll(recyclerView, position)) {
            recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    when (newState) {
                        RecyclerView.SCROLL_STATE_IDLE -> {
                            onScrollEnd.invoke()
                            recyclerView.removeOnScrollListener(this)
                        }
                    }
                }
            })
            recyclerView.smoothScrollToPosition(position)
        } else {
            onScrollEnd.invoke()
        }
    }

    //TODO: refatorar
    private fun canScroll(recyclerView: RecyclerView, position: Int): Boolean {
        val layoutManager = recyclerView.layoutManager as LinearLayoutManager
        return if (position == 0) {
            layoutManager.findFirstCompletelyVisibleItemPosition() > 0
        } else {
            if (position == layoutManager.itemCount - 1) {
                layoutManager.findLastCompletelyVisibleItemPosition() < layoutManager.itemCount - 1
            } else {
                layoutManager.findFirstCompletelyVisibleItemPosition() > position || layoutManager.findLastCompletelyVisibleItemPosition() < position
            }
        }
    }
}
