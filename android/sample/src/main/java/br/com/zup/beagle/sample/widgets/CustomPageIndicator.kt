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

package br.com.zup.beagle.sample.widgets

import br.com.zup.beagle.annotation.RegisterWidget
import android.view.View
import br.com.zup.beagle.android.components.page.PageIndicatorComponent
import br.com.zup.beagle.android.components.page.PageIndicatorOutput
import br.com.zup.beagle.android.widget.RootView
import br.com.zup.beagle.sample.components.CustomPageIndicatorView

@RegisterWidget
data class CustomPageIndicator(
    val showContinue: Boolean,
    val showSkip: Boolean
) : PageIndicatorComponent {

    @Transient
    private lateinit var customPageIndicatorView: CustomPageIndicatorView
    @Transient
    private lateinit var output: PageIndicatorOutput

    override fun initPageView(pageIndicatorOutput: PageIndicatorOutput) {
        output = pageIndicatorOutput
    }

    override fun onItemUpdated(newIndex: Int) {
        customPageIndicatorView.setCurrentIndex(newIndex)
    }

    override fun setCount(pages: Int) {
        customPageIndicatorView.setCount(pages)
    }

    override fun buildView(rootView: RootView) = CustomPageIndicatorView(rootView.getContext()).apply {
        customPageIndicatorView = this
        setIndexChangedListener { index ->
            output.swapToPage(index)
            setContinueButtonVisibility(
                if (showContinue) {
                    View.VISIBLE
                } else {
                    View.INVISIBLE
                }
            )
            setBackButtonVisibility(
                if (showSkip) {
                    View.VISIBLE
                } else {
                    View.INVISIBLE
                }
            )
        }
    }
}
