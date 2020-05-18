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

package br.com.zup.beagle.widget.pager

import android.content.Context
import android.graphics.Color
import br.com.zup.beagle.view.BeaglePageIndicatorView
import br.com.zup.beagle.view.ViewFactory

data class PageIndicator(
    val selectedColor: String,
    val unselectedColor: String
) : PageIndicatorComponent {

    @Transient
    private var viewFactory: ViewFactory = ViewFactory()
    @Transient
    private lateinit var pageIndicator: BeaglePageIndicatorView

    internal constructor(
        selectedColor: String,
        unselectedColor: String,
        viewFactory: ViewFactory
    ) : this(selectedColor, unselectedColor) {
        this.viewFactory = viewFactory
    }

    override fun buildView(context: Context) = viewFactory.makePageIndicator(context).apply {
        pageIndicator = this
        setSelectedColor(Color.parseColor(selectedColor))
        setUnselectedColor(Color.parseColor(unselectedColor))
    }

    override fun setCount(pages: Int) {
        pageIndicator.setCount(pages)
    }

    override fun onItemUpdated(newIndex: Int) {
        pageIndicator.setCurrentIndex(newIndex)
    }

    override fun initPageView(pageIndicatorOutput: PageIndicatorOutput) {}
}