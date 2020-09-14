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

package br.com.zup.beagle.android.components.page

import android.graphics.Color
import android.util.Log
import br.com.zup.beagle.android.context.Bind
import br.com.zup.beagle.android.utils.observeBindChanges
import br.com.zup.beagle.android.view.ViewFactory
import br.com.zup.beagle.android.view.custom.BeaglePageIndicatorView
import br.com.zup.beagle.android.widget.RootView
import br.com.zup.beagle.android.widget.WidgetView
import br.com.zup.beagle.annotation.RegisterWidget

@RegisterWidget
class PageIndicator(
    val selectedColor: String,
    val unselectedColor: String,
    var numberOfPages: Int? = null,
    var currentPage: Bind<Int>? = null
) : WidgetView(), PageIndicatorComponent {

    @Transient
    private val viewFactory: ViewFactory = ViewFactory()

    @Transient
    private lateinit var pageIndicator: BeaglePageIndicatorView

    override fun buildView(rootView: RootView) = viewFactory.makePageIndicator(rootView.getContext()).apply {
        pageIndicator = this
        setSelectedColor(Color.parseColor(selectedColor))
        setUnselectedColor(Color.parseColor(unselectedColor))
        numberOfPages?.let {
            setCount(it)
        }
        currentPage?.let {
            observeBindChanges(rootView, this, it) { position ->
                position?.let {
                    onItemUpdated(position)
                }
            }
        }
    }


    override fun setCount(pages: Int) {
        pageIndicator.setCount(pages)
    }

    override fun onItemUpdated(newIndex: Int) {
        pageIndicator.setCurrentIndex(newIndex)
    }

    override fun initPageView(pageIndicatorOutput: PageIndicatorOutput) {}
}